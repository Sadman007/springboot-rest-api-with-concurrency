package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.services.PrimeNumberService;

@Service
public class PrimeNumberServiceImpl implements PrimeNumberService {
	private Logger logger = LoggerFactory.getLogger(PrimeNumberServiceImpl.class);
	private ExecutorService executorService = Executors.newFixedThreadPool(100);
	
	// This method is left unused intentionally.
	private List<Integer> sieve(int n) {
		Boolean[] compositeness = new Boolean[n + 1];
		List<Integer> primes = new ArrayList<>();
		for (int i = 0; i <= n; i++) {
			compositeness[i] = false;
		}
		for (int i = 2; i * i <= n; i++) {
			if (compositeness[i] == false) {
				for (int j = i * i; j <= n; j += i) {
					compositeness[j] = true;
				}
			}
		}
		if(n >= 2) {
			primes.add(2);
		}
		for (int i = 3; i <= n; i += 2) {
			if (compositeness[i] == false) {
				primes.add(i);
			}
		}
		return primes;
	}
	
	private int bruteForcePrimeCount(int st, int en) {
		int primeCount = 0;
		if (en < st) {
			return primeCount;
		}
		for (int i = st; i <= en; i++) {
			boolean isPrime = true;
			for (int j = 2; j * j <= i; j++) {
				if (i % j == 0) {
					isPrime = false;
					break;
				}
			}
			if (isPrime == true) {
				primeCount++;
			}
		}
		return primeCount;
	}
	
	@Override
	public Integer countPrimes(int n) {
		logger.info(Thread.currentThread().getName());
		if (n < 2) {
			return 0;
		}
		return bruteForcePrimeCount(2, n);
	}

	@Override
	public Integer countPrimesInParts(int n) {
		List<CompletableFuture<Integer>> primeFutures = new ArrayList<CompletableFuture<Integer>>();
		if (n < 2) {
			return 0;
		}
		int st = 2;
		int gap = Math.max(1, n / 10);
		int en = st;
		
		while(st <= n && st <= en) {
			final int fst = st;
			final int fen = en;
			logger.info("range to calculate: " + fst + " --- " + fen);
			primeFutures.add(CompletableFuture
					.supplyAsync(() -> bruteForcePrimeCount(fst, fen), executorService));
			st = en + 1;
			en = Math.min(en + gap, n);
		}
		
		CompletableFuture<Void> allFutures = CompletableFuture.allOf(
				primeFutures.toArray(new CompletableFuture[primeFutures.size()])
		);
		
		CompletableFuture<List<Integer>> allContentsFuture = allFutures.thenApply(v -> {
			   return primeFutures.stream()
			           .map(primeFuture -> primeFuture.join())
			           .collect(Collectors.toList());
			});
		
		int totalPrimes = 0;
		List<Integer> futureResults = null;
		try {
			futureResults = allContentsFuture.get();
			for (Integer primeCountFromFuture : futureResults) {
				totalPrimes += primeCountFromFuture;
			}
			
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return totalPrimes;
	}

}
