package SetupWizardForTryosCheckerCreation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RemoveComment class removes the C++ and Java style comments from a file.
 * 
 * @author qalab
 *
 */
public class RemoveComments 
{
	
	// For String:
	// The comment pattern of "//comment1" or "/*comment2*/"
	private static Pattern COMMENT_1 = Pattern.compile("//[\\d\\D]*?(\r\n?|\n)", Pattern.MULTILINE);
	private static Pattern COMMENT_2 = Pattern.compile("\\/\\*[\\d\\D]*?\\*\\/", Pattern.MULTILINE);
	
	private static Pattern CONTLINE = Pattern.compile("\\[\\s](\r\n?|\n)", Pattern.MULTILINE);
	

	// For Line:
	// The pattern of "end of line"
	private static Pattern CONTLINE_SINGLE = Pattern.compile("\\$", Pattern.MULTILINE);
	// The pattern of "//comment"
	private static Pattern COMMENT_CPP = Pattern.compile("//.*", Pattern.MULTILINE);
	// The pattern of "/*comment*/"
	private static Pattern COMMENT_CCOMPLETE = Pattern.compile("\\/\\*[\\d\\D]*?\\*\\/", Pattern.MULTILINE);
	// The pattern of "/*comment"
	private static Pattern COMMENT_CBEGIN = Pattern.compile("\\/\\*[\\d\\D]*", Pattern.MULTILINE);
	// The pattern of "comment*/"
	private static Pattern COMMENT_CEND = Pattern.compile("[\\d\\D]*?\\*\\/", Pattern.MULTILINE);
	// The pattern of "int x = 0 // comment"
	private static Pattern COMMENT_AFTER = Pattern.compile("[\\d\\D]*? \\/[\\*|\\/][\\d\\D]*", Pattern.MULTILINE);
	/**
	 * Removes comments from string.
	 * @param strText
	 * @return strText, string with comments removed.
	 */
	public static String RemoveCommentsFromString(String strText)
	{	
		if(strText == null || strText.isEmpty())
		{
			return strText;
		}
		
		Matcher matchComment1 = COMMENT_1.matcher(strText);
		Matcher matchComment2 = COMMENT_2.matcher(strText);

		if(matchComment1.matches() || matchComment2.matches())
		{
			Matcher matchContline = CONTLINE.matcher(strText);
			
			if(matchContline.matches())
			{			
				strText = matchContline.replaceAll("");
			}
			
			if(matchComment1.matches())
			{
				return matchComment1.replaceAll("");	
			}
			else
			{
				return matchComment2.replaceAll("");
			}
		}

		return strText;
		
	}
	
	
	/**
	 * Removes comments from a single line.
	 * @param strLine
	 * @param bActiveMultLineComment, multiple line status
	 * @param bContinueLine, continue line status
	 * @return strLine, string with comments removed
	 */
	public static MultiReturnValues RemoveCommentsFromLine(String strLine, boolean bActiveMultLineComment, boolean bContinueLine, boolean bEscapeNewline)
	{
		MultiReturnValues multiValues = new MultiReturnValues(strLine, bActiveMultLineComment, bContinueLine, bEscapeNewline);

		if(bEscapeNewline == true)
		{
			if(strLine.lastIndexOf("\\") == strLine.length() - 1)
			{
				multiValues.setBEscapeNewline(true);
			}
			else
				multiValues.setBEscapeNewline(false);
			
			multiValues.setStrLine("");
			return multiValues;
		}
		
		if(strLine == null || strLine.isEmpty())
		{
			if(multiValues.isContinueLine() == true)
			{
				 multiValues.setBContinueLine(false);
			}
			
			return multiValues;	
		}
		
		if(multiValues.isContinueLine() == true)
		{
			Matcher mContLineSingle = CONTLINE_SINGLE.matcher(strLine);
			
			if(!mContLineSingle.matches())
			{
				multiValues.setBContinueLine(false);
			}
			
			multiValues.setStrLine("");
			return multiValues;
		}
		
		if(multiValues.isActiveMultLineComment())
		{
			Matcher mCommentCEnd = COMMENT_CEND.matcher(strLine);
			
			if(mCommentCEnd.matches())
			{
				multiValues.setStrLine(strLine.substring(strLine.indexOf("*/") + 2));
				multiValues.setBActiveMultLineComment(false);
				
				if(multiValues.getStrLine() == null || multiValues.getStrLine().isEmpty() || StringExtension.isNullOrWhiteSpace(strLine))
				{
					multiValues.setStrLine("");
					return multiValues;
				}				
			}
			else
			{
				multiValues.setStrLine("");
				return multiValues;
			}
		}
		
		Matcher mCommentCPP = COMMENT_CPP.matcher(multiValues.getStrLine());
		if(mCommentCPP.matches())
		{
			/* The case of // #define FOO()\
            					(1 + 2)
				Escape sequence \\n in C and C++
			 */
			if(strLine.lastIndexOf("\\") == strLine.length() - 1)
			{
				multiValues.setBEscapeNewline(true);
			}
				
			Matcher mContLineSingle = CONTLINE_SINGLE.matcher(multiValues.getStrLine());
			if(mContLineSingle.matches())
			{
				multiValues.setBContinueLine(true);
			}
			multiValues.setStrLine(mCommentCPP.replaceAll(""));
			
		}
		
		if(multiValues.getStrLine() == null || multiValues.getStrLine().isEmpty() || StringExtension.isNullOrWhiteSpace(multiValues.getStrLine()))
		{
			multiValues.setStrLine("");
			return multiValues;
		}
		
		Matcher mCommentCComplete = COMMENT_CCOMPLETE.matcher(multiValues.getStrLine());
		if(mCommentCComplete.matches())
		{
			multiValues.setStrLine(mCommentCComplete.replaceAll(""));
		}
		
		if(multiValues.getStrLine() == null || multiValues.getStrLine().isEmpty() || StringExtension.isNullOrWhiteSpace(multiValues.getStrLine()))
		{
			multiValues.setStrLine("");
			return multiValues;
		}
		
		Matcher mCommentCBegin = COMMENT_CBEGIN.matcher(multiValues.getStrLine());
		if(mCommentCBegin.matches())
		{
			multiValues.setStrLine(mCommentCBegin.replaceAll(""));
			multiValues.setBActiveMultLineComment(true);
		}
		
		if(multiValues.getStrLine() == null || multiValues.getStrLine().isEmpty() || StringExtension.isNullOrWhiteSpace(multiValues.getStrLine()))
		{
			multiValues.setStrLine("");
			return multiValues;
		}
		
		
		strLine = multiValues.getStrLine();
		Matcher mCommentAfter = COMMENT_AFTER.matcher(strLine);
		if(mCommentAfter.matches())
		{
//			System.out.print("COMMENT_AFTER Matches at Last! ");
			multiValues.setStrLine(strLine.substring(0, strLine.indexOf("/") - 1));
		}

		return multiValues;	
	}
	
	
}





