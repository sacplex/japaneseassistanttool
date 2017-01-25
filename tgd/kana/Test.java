public class DLLTest
	{
	static
		{
		System.loadLibrary("ProbablyStringMatchesLibrary");
		}
		
	public native float stringMatch(String left, String right);
	
	public static void main(String [] args)
		{
		new DLLTest().stringMatch("abc", "abc");
		}
	}