package com.example.jvwapp;

import java.util.concurrent.atomic.AtomicLong;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.AppServiceMSICredentials;
import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.keyvault.models.SecretBundle;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GreetingController {

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
		String keyVaultEndpoint = System.getenv("KEYVAULTURL");
		AppServiceMSICredentials credentials = new AppServiceMSICredentials(AzureEnvironment.AZURE);
		KeyVaultClient keyVaultClient = new KeyVaultClient(credentials);
		SecretBundle bundle = keyVaultClient.getSecret(keyVaultEndpoint,name);

		return new Greeting(counter.incrementAndGet(), String.format("Vault secret value is %s!", bundle.value()));
	}
}