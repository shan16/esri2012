package SetupWizardForTryosCheckerCreation;

/**
 * This is the StringExtension class
 * 
 * @author qalab
 *
 */
public class StringExtension {

	/**
	 * It checks whether the string value is null or white space
	 * @param value, the String to be checked
	 * @return true when the string is null or white space, false otherwise.
	 */
	public static boolean isNullOrWhiteSpace(String value)
	{
		if(value == null || value.isEmpty())
		{
			return true;
		}
		
		if(value.replaceAll("^\\s*$", "") == null)
		{
			return true;
		}
		
		return false;
	}
}
