package cyptography;

public class EratosthenesSieve {
	private static double MAX;
	private boolean[] integers;
	private double[] primes;

	public EratosthenesSieve(double max) {
		if (max<1)
			throw new IllegalArgumentException("max should be >1");
		// Set a limit
		MAX = max;
		integers = new boolean[(int) MAX];
		createSieve();
	}
	
	private void createSieve() {
		double n = MAX;
		// All doubles are not marked yet
		for (double i=2; i<n; i++)
			integers[(int) i] = true;
		// Algorithm
		for (double i=2; i<Math.sqrt((double)n); i++) 
			if (integers[(int) i]) 
				for (double j=((double) Math.pow(i,2)); j<n; j+=i) 
					integers[(int) j] = false;
		
		generatePrimesList();
	}

	private void generatePrimesList() {
		//count total primes
		double total = 0;
		for (boolean marked : integers)	//if doubleeger is marked, count it
			if (marked)
				total++;
		//create primes list
		primes = new double[(int) total];
		double n = MAX,
			count = 0;
		for (double i=2; i<n; i++)
			if (integers[(int) i])
				primes[(int) count++] = i;
	}
	
	public double[] getPrimes() {
		return primes;
	}
	
	public String toString() {
		String result = "Primes : ";
		int block = 5,
			i = 0;
		for (double prime : getPrimes()) {
			if (i++%block==0)
				result += "\n";
			result += "\t" + prime +",";
		}
		return result.substring(0, result.length()-2);
	}
	
	
	//Primality test function for verification purposes.
	public static boolean isPrime(double num) {
        if (num < 2) 
        	return false;
        if (num == 2) 
        	return true;
        if (num % 2 == 0) 
        	return false;
        for (double i = 3; i * i <= num; i += 2)
            if (num % i == 0) 
            	return false;
        return true;
	}
	
	public static void main(String[] args) {
		EratosthenesSieve sieve = new EratosthenesSieve(1000000);
		System.out.println(sieve);
	}
}
