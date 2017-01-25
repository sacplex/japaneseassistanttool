public class ProbablyStringMatchesAppJap
	{
	public native float stringMatch(String left, String right);
	
	public static void main(String [] args)
		{
		System.loadLibrary("ProbablyStringMatchesLibrary");
		ProbablyStringMatchesAppJap psm = new ProbablyStringMatchesAppJap();
		
		String remember = "By now you know my rule about my holidays and celebrations.";
		String real = "By now you know my rule about holidays and celebrations. Don't stop 'til Hawaii sees midnight! ";
		
		float matchValue = psm.stringMatch("そですね", "そですね");
		
		System.out.println("Match Value: " + matchValue);
		}
	}