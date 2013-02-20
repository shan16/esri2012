package SetupWizardForTryosCheckerCreation;

/**
 * This is the test cases for RemoveComments.java
 * It tests the scenarios one by one.
 * @author fshan
 *
 */
public class Test1
{

	public static void main(String[] args)
	{
				
		testComment1();
		testComment2();
		testCPP();
		testBegin();
		testEnd();		
		
	}
	
	/**
	 * Test whether COMMENT_1 matches the regular expression.
	 */
	public static void testComment1(){
		
		String input = "//Comment_1 should match!\n";
		String output = "laji";
		output = RemoveComments.RemoveCommentsFromString(input);
		
		if(output.isEmpty())
		{
			System.out.println("PASS: COMMENT_1 matches!");
		}
		else
		{
			System.out.println("FAIL: COMMENT_1 does NOT match!");
		}
	}
	
	/**
	 * Test whether COMMENT_2 matches the regular expression.
	 */
	public static void testComment2(){
		
		String input = "/*Comment_2 should match!*/";
		String output = "laji";
		output = RemoveComments.RemoveCommentsFromString(input);
		
		if(output.isEmpty())
		{
			System.out.println("PASS: COMMENT_2 matches!");
		}
		else
		{
			System.out.println("FAIL: COMMENT_2 does NOT match!");
		}
	}
	
	/**
	 * Test whether COMMENT_CPP matches the regular expression.
	 */
	public static void testCPP(){
		
		String input = "//CPP should match!";
		String output = "laji";
		output = RemoveComments.RemoveCommentsFromLine(input, false, false, false).getStrLine();
		
		if(output.isEmpty())
		{
			System.out.println("PASS: COMMENT_CPP matches!");
		}
		else
		{
			System.out.println("FAIL: COMMENT_CPP does NOT match!");
		}
	}
	
	
	/**
	 * Test whether COMMENT_BEGIN matches the regular expression.
	 */
	public static void testBegin(){
		
		String input = "/* BEGIN should match!";
		String output = "laji";
		output = RemoveComments.RemoveCommentsFromLine(input, false, false, false).getStrLine();
		
		if(output.isEmpty())
		{
			System.out.println("PASS: COMMENT_BEGIN matches!");
		}
		else
		{
			System.out.println("FAIL: COMMENT_BEGIN does NOT match!");
		}
	}
	
	/**
	 * Test whether COMMENT_END matches the regular expression.
	 */
	public static void testEnd(){
		
		String input = "END should match!*/";
		String output = "laji";
		output = RemoveComments.RemoveCommentsFromLine(input, true, false, false).getStrLine();
		
		if(output.isEmpty())
		{
			System.out.println("PASS: COMMENT_END matches!");
		}
		else
		{
			System.out.println("FAIL: COMMENT_END does NOT match!");
		}
	}
	
	
	
}
