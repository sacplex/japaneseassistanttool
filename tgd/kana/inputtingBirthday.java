import java.util.Scanner;

public class inputtingBirthday
	{
	public static void main (String [] args)
		{
		Scanner intoTheComputer = new Scanner(System.in);
		String birthday = intoTheComputer.nextLine();
		System.out.println("My birthday is " + birthday);
		}
	}