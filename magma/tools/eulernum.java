import java.math.BigInteger;


public class eulernum {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int b = 100;
		
		BigInteger prod = BigInteger.valueOf(1);
		BigInteger bang = BigInteger.valueOf(b);
		
		for(int i = b; i>0;i--){
			prod = prod.multiply(bang);
			bang = bang.subtract(BigInteger.valueOf(1));
			
		}
		int lol = 0;
		String lolol = prod.toString(); 
		for(int i=0;i<prod.toString().length();i++){
			lol = lol+Integer.parseInt(lolol.substring(i, i+1));
		}
		System.out.println(lol);
		
		
		
	}

	
	
	
	public static BigInteger sumnum(long max){
		
		int sum = 0;
		
		for(int i=0;i<(max+1);i++){
			sum=sum+i;
		}
		
		return BigInteger.valueOf(sum);
	}
	
	static boolean prime(long N){
		boolean isPrime = true;
        if (N < 2) isPrime = false;

        // try all possible factors i of N
        // if if N has a factor that it has one less than or equal to sqrt(N),
        // so for efficiency we only need to check i <= sqrt(N) or equivalently i*i <= N
        for (long i = 2; i*i <= N; i++) {

            // if i divides evenly into N, N is prime, so break out of loop
            if (N % i == 0) {
                isPrime = false;
                break;
            }
        }
        return isPrime;
	}
}

