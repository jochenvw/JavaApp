package com.example.jvwapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.microsoft.applicationinsights.TelemetryClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

	TelemetryClient telemetryClient = new TelemetryClient();

	@Value("${jvw.whatever}")
	private String greetingTemplate;

	@Value("${jvw.file}")
	private String filename;

	@Value("${jvw.keyvaultsecret}")
	private String keyVaultSecret;

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
		return new Greeting(counter.incrementAndGet(), String.format("Vault secret value is %s!", keyVaultSecret));
	}

	@GetMapping("/file")
	public Greeting file(@RequestParam(value = "name", defaultValue = "JohnDoe") String name) throws Exception {
		InputStream resource = new ClassPathResource(filename).getInputStream();

		File tempFile = new File("tmp.txt");
		copyInputStreamToFile(resource, tempFile);
		if(tempFile.exists()) {
			System.out.println("File exists !");
		}

		resource = new ClassPathResource(filename).getInputStream();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource))) {
			String content = reader.lines().collect(Collectors.joining("\n"));
			return new Greeting(counter.incrementAndGet(), String.format(content, name));
		}
	}

	// InputStream -> File
	private static void copyInputStreamToFile(InputStream inputStream, File file) throws IOException {

		try (FileOutputStream outputStream = new FileOutputStream(file)) {

			int read;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
		}

	}
}