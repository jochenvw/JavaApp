package com.example.jvwapp;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.AppServiceMSICredentials;
import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.keyvault.models.SecretBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

	@Autowired
	TelemetryClient telemetryClient;

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) throws Exception {
		if (name.equals("johndoe")) {
			throw new Exception("Sure ....");
		}

		return new Greeting(counter.incrementAndGet(), String.format(template, name));
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
}