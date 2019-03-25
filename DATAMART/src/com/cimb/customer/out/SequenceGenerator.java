package com.cimb.customer.out;

import java.util.Random;

/**
 * Encapsulates algorithms and state for generating deterministic sequences.
 * The sequence of numbers generated will always follow the same pattern,
 * regardless of the time, place, or platform.
 */
public class SequenceGenerator {
	private static long globalSeqNum = System.currentTimeMillis() * 1000;
	private final Random random;
	private int uniqueInt;

	/**
	 * Constructs a new sequence generator with a known seed.
	 */
	public SequenceGenerator() {
		random = new Random(3141592653589793238L); // a known constant
		uniqueInt = 01;
	}
	
	/**
	 * Returns a globally unique long integer.
	 */
	public static long nextGloballyUniqueLong() {
		return globalSeqNum++;
	}
	
	/**
	 * Returns a unique 7-digit integer.
	 */
	public int nextUniqueInt() {
		return uniqueInt++;
	}

	/**
	 * Returns a pseudo-random integer between 0 and n-1.
	 * @see Random#nextInt(int)
	 */
	public int nextInt(int n) {
		return random.nextInt(n);
	}
	
	/**
	 * Returns a pseudo-random real number following a gaussian distribution.
	 * @see Random#nextGaussian()
	 */
	public double nextGaussian() {
		return random.nextGaussian();
	}
}