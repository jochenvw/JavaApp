package com.example.jvwapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {
	@Value("${azure.keyvault.uri}")
	private String keyVaultUri;

	@Value("${azure.application-insights.instrumentation-key}")
	private String appInsightsKey;

	@Value("${my.special.secret}")
	private String kvSecret;

	@GetMapping("/info")
	public ResponseEntity greeting() throws Exception {
		String response = "";
		response += "App started - with follwing config:" + "\r\n";
		response += "KV URI" + keyVaultUri + "\r\n";
		response += "App insights key" + appInsightsKey + "\r\n";
		response += "KeyVaultSecret" + kvSecret + "\r\n";
		return ResponseEntity.ok(response);
	}
}