import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class foo {

	public static void main(String arg[]) throws FileNotFoundException{
		
		String text = new Scanner(new File ("C:\\Users\\qalab\\workspace2\\abc\\src\\sample")).useDelimiter("\\A").next();
		System.out.println("-------" + text);
//		String out = text.replace('"', '\"').replace("\\", "\\\\").replace(System.getProperty("line.separator"), "\"" + System.getProperty("line.separator") + "\"");
		String out = text.replace('"', '\"').replace("\\", "\\\\").replace(System.getProperty("line.separator"), "");
		System.out.println(out);
		if(out.contains(System.getProperty("line.separator"))){
			System.out.println("---HEHE---");
		}
		
		String slash = "// caonima";
		Pattern p = Pattern.compile("//.*");
		Matcher m = p.matcher(slash);
		if(m.matches()){
			System.out.println("niduile!");
		}
	}
}
