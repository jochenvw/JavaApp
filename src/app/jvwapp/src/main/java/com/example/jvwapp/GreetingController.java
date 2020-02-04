package com.example.jvwapp;

import java.io.File;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.AppServiceMSICredentials;
import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.keyvault.models.SecretBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

	TelemetryClient telemetryClient = new TelemetryClient();

	@Autowired
	ResourceLoader resourceLoader;

	@Value("${jvw.whatever}")
	private String greetingTemplate;

	@Value("${jvw.file}")
	private String filename;

	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) throws Exception {
		if (name.equals("johndoe")) {
			throw new Exception("Sure ....");
		}

		return new Greeting(counter.incrementAndGet(), String.format(greetingTemplate, name));
	}

	@GetMapping("/secret")
	public Greeting secret(@RequestParam(value = "name", defaultValue = "sample") String name) throws Exception {
		Instant start = Instant.now();

		// Environment variable is injected from AppSettings
		String keyVaultEndpoint = System.getenv("KEYVAULTURL");
		// MSI used for auth to the KeyVault - which has an access policy set
		// for the identity of the web app
		AppServiceMSICredentials credentials = new AppServiceMSICredentials(AzureEnvironment.AZURE);
		KeyVaultClient keyVaultClient = new KeyVaultClient(credentials);
		SecretBundle bundle = keyVaultClient.getSecret(keyVaultEndpoint,name);

		// Track dependency call with application insights - which should
		// show up in the application map
		Instant finish = Instant.now();
		int timeElapsed = (int) Duration.between(start, finish).toMillis();
		telemetryClient.trackDependency("KeyVault", "GetSecret",
		new com.microsoft.applicationinsights.telemetry.Duration(0, 0, 0, 0, timeElapsed), true);

		return new Greeting(counter.incrementAndGet(), String.format("Vault secret value is %s!", bundle.value()));
	}


	@GetMapping("/file")
	public Greeting file(@RequestParam(value = "name", defaultValue = "JohnDoe") String name) throws Exception {
		Resource resource = resourceLoader.getResource(filename);
		File file = resource.getFile();
		Boolean fileExists = file.exists();
		System.out.println("File Found : " + fileExists);

		String content = new String(Files.readAllBytes(file.toPath()));
		if(!fileExists) {
			throw new Exception("Unable to find file " + filename);
		}
		return new Greeting(counter.incrementAndGet(), String.format(content, name));
	}
}