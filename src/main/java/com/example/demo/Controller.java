package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.ExecutionException;

import com.example.demo.services.PrimeNumberService;

@RestController
public class Controller {
	
	@Autowired
	private PrimeNumberService primeNumberService;

	@GetMapping(value = "primes-count-async", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Integer> getNumberPropertiesCountAsync (@RequestParam Integer n) throws InterruptedException, ExecutionException {
		Map<String, Integer> response = new HashMap<String, Integer>();
		long st = System.currentTimeMillis();
		response.put("prime_count", primeNumberService.countPrimesInParts(n));
		long en = System.currentTimeMillis();
		response.put("elapsed_time", Integer.valueOf((int) (en - st)));
		return response;
	}
	
	@GetMapping(value = "primes-count", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Integer> getNumberPropertiesCount (@RequestParam Integer n) throws InterruptedException, ExecutionException {
		Map<String, Integer> response = new HashMap<String, Integer>();
		long st = System.currentTimeMillis();
		response.put("prime_count", primeNumberService.countPrimes(n));
		long en = System.currentTimeMillis();
		response.put("elapsed_time", Integer.valueOf((int) (en - st)));
		return response;
	}
	
	@GetMapping(value = "healthcheck", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> healthCheck() {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("status", 200);
		response.put("message", "Health is OK!");
		response.put("available_cores", Runtime.getRuntime().availableProcessors());
		return response;
	}
}
