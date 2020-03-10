package com.example.jvwapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	@Value("${azure.keyvault.uri}")
	private static String keyVaultUri;

	@Value("${azure.application-insights.instrumentation-key}")
	private static String appInsightsKey;

	@Value("${my.special.secret}")
	private static String kvSecret;

	public static void main(String[] args) {
		System.out.println("App started - with follwing config:");
		System.out.println("KV URI" + keyVaultUri);
		System.out.println("App insights key" + appInsightsKey);
		System.out.println("KeyVaultSecret" + kvSecret);

		SpringApplication.run(DemoApplication.class, args);
	}
}
