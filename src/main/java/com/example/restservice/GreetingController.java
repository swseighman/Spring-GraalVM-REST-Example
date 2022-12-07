package com.example.restservice;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}
}

	// @GetMapping("/iterate")
	// 	public static String countUppers(@RequestParam(value = "strings",
	// 			defaultValue = "Now is the Time for All good Men to Come to the aid of their party!") String sentence) {
	// 		int ITERATIONS = 1;
	// 		StringBuilder result = new StringBuilder("iterations:\n");
			
	// 		for (int iter = 0; iter < ITERATIONS; iter++) {
	// 			if (ITERATIONS != 1) {
	// 				result.append(String.format("-- iteration " + (iter + 1) + " --%n"));
	// 			}   
	// 			long total = 0, start = System.currentTimeMillis(), last = start;
	// 			for (int i = 1; i < 10_000_000; i++) {
	// 				total += sentence
	// 				.chars()
	// 				.filter(Character::isUpperCase)
	// 				.count();
	// 				if (i % 1_000_000 == 0) {
	// 					long now = System.currentTimeMillis();
	// 					result.append(String.format("\t%d (%d ms)%n", i / 1_000_000, now - last));
	// 					last = now;
	// 				}   
	// 			}   
	// 			result.append(String.format("total: %d (%d ms)%n", total, System.currentTimeMillis() - start));
	// 		}   
			
	// 		return result.toString();
	// 	}
