package cyptography;

import java.util.Random;

/**
 * This RSA implementation doesn't use OAEP, so is not secure 
 * enough to be implemented in real life (Duh).
 * @author Mazen
 */
public class RSA {
	private double n;			//modulus AKA max 	
	private double e;			//encryption key AKA public key	
	private double d;			//decryption key AKA private key
	private double totient; 	//totient : phi(n)
	
	//Constant used for eratosthenes' sieve to generate primes list 
	private static final double DEFAULT_MAX = 20;
	
	/**
	 * In our implementation (more precisely because of the DEFAULT_MAX value used in 
	 * Eratosthenes Sieve) the keys can't be bigger than DEFAULT_MAX.
	 */
	public RSA() {
		/* Generate 2 prime numbers (In a real implementation they 
		 * need to be 512 to 1024 digits, and not 3/4 like ours =p)*/
		double[] primesList = new EratosthenesSieve(DEFAULT_MAX).getPrimes();
		double len = primesList.length,
			// Randomly generate two distinct indexes
			rand0 = new Random().nextInt((int) len),	
			rand1;
		do {
			rand1 = new Random().nextInt((int) len);
		} while (rand0 == rand1);	// keep generating until distinct indexes
		// Get two randomly chosen primes
		double p = primesList[(int) rand0],			
			q = primesList[(int) rand1];
		System.err.println("Two chosen primes are : (p="+p+",q="+q+")");
		
		/* Calculate the modulus */
		n = p*q;
		System.err.println("Modulus is : n="+n);
		
		/* Calculate Euler's Totient of n using : phi(m*n)=phi(m)*phi(n)*d/phi(d) with d=gcd(m,n) 
		 * eventually : phi(p)=(p-1) & phi(n)=phi(p*q)=phi(p)*phi(q)=(p-1)*(q-1) & gcd(p,q)=1 */
		totient = (p-1)*(q-1);
		System.err.println("Totient : phi(n)="+totient);
		
		/* Generate a Public key 'e' which is a prime number between [3,phi(n)] */
		double[] primesList1 = new EratosthenesSieve(totient).getPrimes();	//new primes list in [2,phi(n)]
		double len1 = primesList1.length;
		do {
			double rand = new Random().nextInt((int)len1-1)+1;	//get any index >0 because primesList1[0] = 2
			e = primesList1[(int) rand];
		} while (gcd(e,totient)!=1);	//public key and totient have to be coprime
		System.err.println("Public key : e="+e);
		
		/* Generate the Private key 'd', such as : e*d = 1 mod phi(n) */
		d = modInverse(e, totient);
		System.err.println("Private key : d="+d);
	}
	
	/**
	 * Pseudo code available at : 
	 * https://en.wikipedia.org/wiki/Extended_Euclidean_algorithm#Modular_integers
	 * Computes : a*t = 1 mod n 
	 * Furthermore, in our case :
	 * @param a - public key : e
	 * @param n - totient of n : phi(n)
	 * @return t - private key : d
	 */
	private static double modInverse(double a, double n) {
		double t, nt, r, nr, q, tmp;
        if (n < 0) 
        	n = -n;
        if (a < 0) 
        	a = n - (-a % n);
        t = 0;  
        nt = 1; 
        r = n;  
        nr = a % n;
        while (nr != 0) {
          q = r/nr;
          tmp = nt;  
          nt = t - q*nt;  
          t = tmp;
          
          tmp = nr;  
          nr = r - q*nr;  
          r = tmp;
        }
        if (r > 1) 
        	return -666;  /* No inverse */
        if (t < 0) 
        	t += n;
        return t;
	}
	
	//Source : https://rosettacode.org/wiki/Greatest_common_divisor#Recursive_5
	public static double gcd(double a, double b){
		if(a == 0) 
			return b;
		if(b == 0) 
			return a;
		if(a > b) 
			return gcd(b, a % b);
		return gcd(a, b % a);
	}
	
	/**
	 * Source : http://doctrina.org/How-RSA-Works-With-Examples.html#rsa-function-evaluation
	 * Computes : F(m,k) = m^k mod n
	 * Encrypting : F(msge,e) = msge^e mod n = cphr
	 * Decrypting : F(cphr,d) = cphr^d mod n = msge
	 * @param m - the message as integer
	 * @param k - the public/private key
	 * return the cipher/plaintext as integer
	 */
	public double evaluate(double m, double k) {
		return Math.pow(m, k)%n;
	}
	
	//should be used when converting texts to ints
	@SuppressWarnings("unused")
	private double wrapAround(double integer) {
		// TODO
		return 0;
	}

	public double getPublicKey() {
		return e;
	}

	public double getPrivateKey() {
		return d;
	}

	public double getModulus() {
		return n;
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		RSA rsa = new RSA();
	}
}
