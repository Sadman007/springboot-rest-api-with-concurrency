package com.example.demo.services;

import org.springframework.stereotype.Service;

@Service
public interface PrimeNumberService {
	public Integer countPrimes(int n);
	public Integer countPrimesInParts(int n);
}
