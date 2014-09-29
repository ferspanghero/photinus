package edu.uci.ics.sdcl.firefly.util;

public class LeastCommonDenominator {

	public static long gcd(long a, long b)
	{
	    while (b > 0)
	    {
	        long temp = b;
	        b = a % b; // % is remainder
	        a = temp;
	    }
	    return a;
	}

	public static long gcd(Object[] input)
	{
	    long result = ((Integer)input[0]).longValue();
	    for(int i = 1; i < input.length; i++) result = gcd(result, ((Integer)input[i]).longValue());
	    return result;
	}
	
	public static long lcm(long a, long b)
	{
	    return a * (b / gcd(a, b));
	}

	public static long lcm(Object[] input)
	{
		long result = ((Integer)input[0]).longValue();
	    for(int i = 1; i < input.length; i++) result = lcm(result, ((Integer)input[i]).longValue());
	    return result;
	}
	
}
