package SetupWizardForTryosCheckerCreation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * This is testRemoveComment.java 
 * It tests RemoveComments.java for its full functionality.
 * @author fshan
 *
 */
public class TestRemoveComment
{
	/**
	 * Main function to test RemoveComments.java
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException
	{
		BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\qalab\\workspace2\\CreateCheckers\\src\\SetupWizardForTryosCheckerCreation\\comment"));
		String line;
		boolean bActiveMultLineComment = false;
        boolean bContinueLine = false;
        boolean bEscapeNewline = false;
        int intLine = 0;
						
		try {
			while ((line = br.readLine()) != null) { 
				
				intLine++;
				System.out.print("At line " + intLine + ":    ");
				
				// Remove any comments that maybe in the source code
				// Use OBJECT CLASS to return multiple values.
				MultiReturnValues multiValues = new MultiReturnValues();
				multiValues = RemoveComments.RemoveCommentsFromLine(line, bActiveMultLineComment, bContinueLine, bEscapeNewline );
				line = multiValues.getStrLine();
				bActiveMultLineComment = multiValues.isActiveMultLineComment();
				bContinueLine = multiValues.isContinueLine();
				bEscapeNewline = multiValues.isEscapeNewline();
				System.out.println(line);
				
				// Continue the while loop if the line was just a comment.
				if(line.isEmpty() || line == null)
					continue;
			
			}
			
			br.close(); 
		} 
		
		catch (IOException e)
		{
			System.out.println("The tool had an error: " + e.getMessage());
		}
				
	}

}
