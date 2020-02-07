package com.example.jvwapp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
	@GetMapping("/health")
	public ResponseEntity greeting() throws Exception {
		return ResponseEntity.ok("All good here!");
	}
}