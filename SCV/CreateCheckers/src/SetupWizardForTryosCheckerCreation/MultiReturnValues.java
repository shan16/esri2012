package SetupWizardForTryosCheckerCreation;

/**
 * This is the ThreeReturnValues class
 * It acts as the wrapper to return three values in a function/method
 * @author fshan
 *
 */
public class MultiReturnValues 
{
		
		// member variables
		private String strLine;
		private boolean bActiveMultLineComment;
		private boolean bContinueLine;
		private boolean bEscapeNewline;
		
		// Default constructor
		public MultiReturnValues()
		{
			this.strLine = "";
			this.bActiveMultLineComment = false;
			this.bContinueLine = false;
			this.bEscapeNewline = false;
		}
		// Constructor
		public MultiReturnValues(String first, boolean second, boolean third, boolean fourth)
		{	
			this.strLine = first;
			this.bActiveMultLineComment = second;
			this.bContinueLine = third;		
			this.bEscapeNewline = fourth;
		}
		
		// Four getters
		public String getStrLine()
		{
			return strLine;
		}
		
		public boolean isActiveMultLineComment()
		{
			return bActiveMultLineComment;
		}
		
		public boolean isContinueLine()
		{
			return bContinueLine;
		}
		
		public boolean isEscapeNewline()
		{
			return bEscapeNewline;
		}
		
		// Four setters
		public void setStrLine(String strLine)
		{
			this.strLine = strLine;
		}
		
		public void setBActiveMultLineComment(boolean bActiveMultLineComment)
		{
			this.bActiveMultLineComment = bActiveMultLineComment;
		}
		
		public void setBContinueLine(boolean bContinueLine)
		{
			this.bContinueLine = bContinueLine;
		}
		
		public void setBEscapeNewline(boolean bEscapeNewline)
		{
			this.bEscapeNewline = bEscapeNewline;
		}
}
