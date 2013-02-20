package SetupWizardForTryosCheckerCreation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import IWizardForTryosCheckerCreation.TryosCheckerForm;

/**
 * This is the example of a checker class. It could help generate a dll file
 * for Tryo to load up when Tryo starts.
 * 
 * @author qalab
 *
 */
public class ClassExample 
{
	TryosCheckerForm TryosCheckerForm = new TryosCheckerForm();
	
	/**
	 * Extension of the checker, the extension needs to be unique and is only for internal use.
	 * 
	 * @return The ID extension of the checker.
	 */

	// ---------------------WARNING: NEED TO FIX Override-----------------------
	public String getID()
	{	
		return TryosCheckerForm.getParameters().get("Extension");		
	}
	
	
	/**
	 * The name of the checker that will be displayed to a user.
	 * 
	 * @return The name of the checker.
	 */
	public String getName()
	{		
		return TryosCheckerForm.getParameters().get("Name");		
	}
	
	/**
	 * A small single line or less description of what the checker does.
	 * 
	 * @return A brief summary of the checker.
	 */
	public String getSummary()
	{		
		return TryosCheckerForm.getParameters().get("Summary");		
	}
	
	
	/**
	 * A complete detailed description of what the checker does.
	 * 
	 * @return Complete description of the checker.
	 */
	public String getDescription()
	{		
		return TryosCheckerForm.getParameters().get("Description");		
	}
	
	
	/**
	 * Category of the checker which is used to group like checkers together.
	 * 
	 * @return The category group that the checker belongs to.
	 */
	public String getCategory()
	{		
		return TryosCheckerForm.getParameters().get("Category");		
	}
	
	
	/**
	 * Severity of the checker's importance to be fixed, 0=Low 1=Medium 2=High
	 * 
	 * @return The severity of the checker.
	 */
	public int getSeverity()
	{		
		return Integer.valueOf(TryosCheckerForm.getParameters().get("Severity"));		
	}
	
	
	/**
	 * Set true if the tool should be published for tryo.exe.
	 * If not it will only be accessible with the correct unlisted extension on the command line.
	 * 
	 * @return True if the checker is to be published, else false.
	 */
	public boolean getPublished()
	{	
		return true;		
	}
	
	
	/**
	 * Set true if the tool should only run on server.
	 * 
	 * @return True if the checker should be ran on the server, else false.
	 */
	public boolean getRunOnlyOnServer()
	{		
		return false;		
	}
	
	
	/**
	 * Method called when the user select the checker. 
	 * Input is a ArrayList of paths that are files (can be any type of file ico, bmp ...)
	 * 
	 * @param arlFilePaths, An array list of file paths to search for defects.
	 * 
	 * @return True if ran without an exception, else false.
	 */
	public boolean run(ArrayList<String> arlFilePaths)
	{		
		// START OF THE CODE TO BE EDITED
	    // In this example the checker finds all the specified string "Bug" in the list of files that are given to him.
		String strToFind = "Bug";
		
		// Loop through all the file paths that were given
		for(int i = 0; i < arlFilePaths.size(); i++)
		{			
			try
			{				
				String filePath = (String) arlFilePaths.get(i);
				
				// Read the entire text file into a String. 
				String strText = new Scanner(new File (filePath)).useDelimiter("\\A").next();
				
				// If defect was found in file then remove any comments that maybe in the source code, 
		        // else if the defect was not found continue onto the next file.
				if(!strText.contains(strToFind))
				{					
					strText = RemoveComments.RemoveCommentsFromString(strText);					
				}
				else
					continue;
				
				// Continue if invalid source code or value was just in the comments
				if(strText.isEmpty() || strText == null || !strText.contains(strToFind))
					continue;
				
				BufferedReader br = new BufferedReader(new FileReader(filePath));
				String line;
				boolean bActiveMultLineComment = false;
		        boolean bContinueLine = false;
		        boolean bEscapeNewline = false;
		        int intLine = 0;
								
				while ((line = br.readLine()) != null)
				{ 					
					intLine++;
					int intColumn = 1;
					
					// Remove any comments that maybe in the source code
					// Use OBJECT CLASS to return multiple values.
					MultiReturnValues multiValues = new MultiReturnValues();
					multiValues = RemoveComments.RemoveCommentsFromLine(line, bActiveMultLineComment, bContinueLine, bEscapeNewline );
					line = multiValues.getStrLine();
					bActiveMultLineComment = multiValues.isActiveMultLineComment();
					bContinueLine = multiValues.isContinueLine();
					bEscapeNewline = multiValues.isEscapeNewline();
					
					// Continue the while loop if the line was just a comment.
					if(line.isEmpty() || line == null)
						continue;
					
					// As long as the string "Bug" is found, output the defect
					while(line.indexOf(strToFind) >= 0)
					{						
						// ----------------------WARNING: Output class needed-----------------
//						Output.Outputdefect(getID(), filePath, intLine, intColumn + line.indexOf(strToFind));
						intColumn = intColumn + line.indexOf(strToFind) + strToFind.length();
						line = line.substring(line.indexOf(strToFind) + strToFind.length());						
					}
				}
				
				br.close(); 
				
//				Output.OutputMsg("This message will be displayed either in Command line, UI or Visual Studio output window, depending on where this checker runs.");								
			}
			catch(Exception ex)
			{			
//				Output.OutputErrorTool("The tool had an error: " + ex.getMessage());
				return false;
			}			
		}	
		// END OF THE CODE TO BE EDITED
		return true;		
	}
	
}
