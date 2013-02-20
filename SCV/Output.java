package SetupWizardForTryosCheckerCreation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * This class is formating the output depending on which one is 
 * selected (Command line, Website, XML for Visual Studio Plug-in or Extend).
 * The checker just call outputdefect, and this class build the website if 
 * Website is selected, and/or creates the xml files if selected.
 * @author qalab
 *
 */
public class Output
{
	
	static String m_packagename = "";
	static ArrayList m_packageWithDefects = new ArrayList();
	static ArrayList<String> m_allPackagesWithDefects = new ArrayList<String>();
    static ArrayList m_listExtension = new ArrayList();
    static ArrayList<String> m_fileWithDefects = new ArrayList<String>();
    static String m_checkerExtension = "";
    static String m_checkerName = "";
    static String m_checkerSumary = "";
    static String m_checkerDescription = ""; 

 
    /***********************Output message************************/
    
    /**
     * Errors are displayed by the same function: OutputMsg
     * @param msg
     */
    public static void OutputErrorTool(String msg)
    {
		OutputMsg(msg);	
	}


    /**
     * Errors are displayed by the same function: OutputMsg
     * @param msg
     */
    public static void OutputErrorFiles(String msg)
    {
    	OutputMsg(msg);
    }
    
    
    /**
     * This function is used to display messages that are not related to defects
     * in the source code, but to let the user know about errors/information that
     * the tool encountered.
     * 
     * @param oMsg
     */
	public static void OutputMsg(Object oMsg)
	{
		try
		{
			String msg = oMsg.toString();
			if(Tryo.m_config.m_inputtype == "cmdline")
			{
				System.out.println(msg);
			}
			if(Tryo.m_config.m_inputtype == "UItryo")
			{
				TryoForm.m_MainForm.ValueForTextBox = msg;
			}
			
			//------------------What to substitute VS? -----------------
			if (Tryo.m_config.m_inputtype == "vs")
            {
                System.out.println(msg);
            }
			if (Tryo.m_config.m_inputtype == "extend")
            {
                System.out.println(msg);
            }
		}
		catch (Exception ex)
		{			
		}
	}


		
	/**********************Output defects***********************/	

	/**
	 * OutputDefect can be called with or without a specialmessage about the defect 
	 * it just found. If a checkers is finding different kind of defects, it is 
	 * important to have a special message explaining which one is relevant to
	 * this particular defect.
	 * 
	 * @param checkerExtension
	 * @param filePath
	 * @param line
	 * @param column
	 */
	public static void Outputdefect(String checkerExtension, String filePath, int line,
			int column)
	{
		OutputDefect(checkerExtension, filePath, line, column, "");
	}


	public static void OutputDefect(String checkerExt, String filePath,
			int line, int column, String specialmessage)
	{	
		try
		{
			// Obtain a list of files that have defects.
			if(new File(filePath).exists() && !m_fileWithDefects.contains(filePath))
			{
				m_fileWithDefects.add(filePath);
			}
			
			filePath = filePath.toLowerCase();
			
			// Count the number of defects.
            Tryo.m_intnbDefects++;
            
            boolean found = false;
            
            // Find the right checker information using its extension.
            for(int j = 0; j < (Tryo.m_arallcheckers.length / 8); j++)
            {
            	if (Tryo.m_arallcheckers[j, 0] == checkerExt)
                {
                    m_checkerExtension = Tryo.m_arallcheckers[j][0];
                    m_checkerName = Tryo.m_arallcheckers[j][1];
                    m_checkerSumary = Tryo.m_arallcheckers[j][2];
                    m_checkerDescription = Tryo.m_arallcheckers[j][3];
                    found = true;
                    break;
                }
            }
            
            if(found == false)
            {
            	return;
            }
            
            // If the input is command line and the output is not defined (not XML or Website). 
            // This could be used to integrate the tool in a separate UI (like it is integrated in Visual Studio).
            if ((Tryo.m_config.m_inputtype == "cmdline") &&
            	(!Tryo.m_config.m_aloutputtype.contains("XML")) && 
            	(!Tryo.m_config.m_aloutputtype.contains("Website")))
            {
                //Just write in the command line.
                System.out.println(checkerExt);
            }
            
            // If the output is Website (can be input type UI or Cmd Line).
            if (((Tryo.m_config.m_inputtype == "UItryo") && (Tryo.m_config.m_aloutputtype.contains("Website"))) ||
            	 ((Tryo.m_config.m_inputtype == "cmdline") && (Tryo.m_config.m_aloutputtype.contains("Website"))))
            {
                //This function is building the website.
                printDefectInWebsite(m_checkerExtension, m_checkerName, m_checkerSumary,
                		m_checkerDescription, specialmessage, filePath, line, column);
            }
            
            // If it is the Visual Studio plug-in. 
            if (Tryo.m_config.m_inputtype == "vs")
            {
                System.out.println("<File>" + filePath + "</File>");
                System.out.println("\n");
                System.out.println("<Line>" + m_checkerName + "</Line>");
                System.out.println("\n");
                System.out.println("<LineNumber>" + line + "</LineNumber>");
                System.out.println("\n");
                System.out.println("<columnNumber>" + column + "</columnNumber>");
                System.out.println("\n");
                System.out.println("<SpecialMessage>" + m_checkerSumary + "     " + specialmessage + "</SpecialMessage>");
                System.out.println("\n");
            }
            
            // If the ouput type is XML (the output for extend is XML)
            if ((Tryo.m_config.m_inputtype == "extend") ||
            	((Tryo.m_config.m_inputtype == "UItryo") && (Tryo.m_config.m_aloutputtype.contains("XML"))) ||
            	((Tryo.m_config.m_inputtype == "cmdline") && (Tryo.m_config.m_aloutputtype.contains("XML"))))
            {
            	File file;
            	// Format the XML files to follow the extend requirement (see Coverity's help).
            	if (Tryo.m_config.m_inputtype == "extend")
                {
            		file = new File(Tryo.m_config.m_folderResult + "\\ESRI_" + m_checkerExtension + ".errors.xml");
            		
            		// This text is added only once to the file
            		if(!file.exists())
            		{
            			// Create a file to write to (CreateText instead of AppendText).
            			file.createNewFile();
            			FileWriter fw = new FileWriter(file.getName());
            			BufferedWriter bw = new BufferedWriter(fw);
            			
            			bw.write("<error>\n");
            			bw.write("<checker>ESRI_" + m_checkerExtension + "</checker>\n");
            			
            			if (Tryo.m_config.m_aloutputtype.contains("CPP"))
            			{
            				bw.write("<file>" + filePath.toLowerCase().replace("\\", "/") + "</file>\n");
            			}
            			else if(Tryo.m_config.m_aloutputtype.contains("CS"))
            			{
            				File fileTemp = new File(filePath);
            				bw.write("<file>" + fileTemp.getAbsolutePath() + "</file>\n");
            			}
            			else if(Tryo.m_config.m_aloutputtype.contains("JAVA"))
            			{
            				// Be careful it is case sensitive
            				File fileTemp = new File(filePath);
            				String[] files = fileTemp.list();
            				for(int i = 0; i < files.length; i++)
            				{
            					if(files[i].toLowerCase() == fileTemp.getAbsolutePath().toLowerCase())
            					{
            						bw.write("<file>" + file.replace(Tryo.m_config.m_alSelectedFilesSource[0].toString().toLowerCase(), "").replace("\\", "/") + "</file>\n");
            						break;
            					}
            				}
            			}
            			bw.write("<function>unknown</function>\n");
            			bw.write("<score>100</score>\n");
            			bw.write("<num>" + Tryo.m_intnbDefects + "</num>\n");
            			bw.write("<event>\n");
            			bw.write("<tag>defect</tag>\n");
            			bw.write("<description>" + m_checkerSumary + "     " + specialmessage + "</description>\n");
            			bw.write("<line>" + line + "</line>\n");
            			bw.write("</event>\n");
            			bw.write("<extra>" + specialmessage.replace("<", "").replace(">", "") + "</extra>\n");
            			bw.write("<subcategory>none</subcategory>\n");
            			bw.write("</error>\n");           			
            		}
            		
            		else
            		{
            			//if the file already exist just Append the text to it
            			file.setWritable(true);
            			FileWriter fw = new FileWriter(file.getName());
            			BufferedWriter bw = new BufferedWriter(fw);
            			
            			bw.write("<error>\n");
                        bw.write("<checker>ESRI_" + m_checkerExtension + "</checker>\n");
                        
                        if (Tryo.m_config.m_aloutputtype.contains("CPP"))
            			{
            				bw.write("<file>" + filePath.toLowerCase().replace("\\", "/") + "</file>\n");
            			}
                        else if(Tryo.m_config.m_aloutputtype.contains("CS"))
            			{
            				File fileTemp = new File(filePath);
            				bw.write("<file>" + fileTemp.getAbsolutePath() + "</file>\n");
            			}
                        else if(Tryo.m_config.m_aloutputtype.contains("JAVA"))
            			{
            				// Be careful it is case sensitive
            				File fileTemp = new File(filePath);
            				String[] files = fileTemp.list();
            				for(int i = 0; i < files.length; i++)
            				{
            					if(files[i].toLowerCase() == fileTemp.getAbsolutePath().toLowerCase())
            					{
            						bw.write("<file>" + file.replace(Tryo.m_config.m_alSelectedFilesSource[0].toString().toLowerCase(), "").replace("\\", "/") + "</file>\n");
            						break;
            					}
            				}
            			}
            			bw.write("<function>unknown</function>\n");
            			bw.write("<score>100</score>\n");
            			bw.write("<num>" + Tryo.m_intnbDefects + "</num>\n");
            			bw.write("<event>\n");
            			bw.write("<tag>defect</tag>\n");
            			bw.write("<description>" + m_checkerSumary + "     " + specialmessage + "</description>\n");
            			bw.write("<line>" + line + "</line>\n");
            			bw.write("</event>\n");
            			bw.write("<extra>" + specialmessage.replace("<", "").replace(">", "") + "</extra>\n");
            			bw.write("<subcategory>none</subcategory>\n");
            			bw.write("</error>\n");     
            		}
                }
            	
            	//input type is not extend, but just XML
            	else
            	{
            		file = new File(Tryo.m_config.m_folderResult + "\\SCV.xml");
            		if(!file.exists())
            		{
            			//Create a file to write to (CreateText instead of AppendText).
            			file.createNewFile();
            			FileWriter fw = new FileWriter(file.getName());
            			BufferedWriter bw = new BufferedWriter(fw);
            			
            			bw.write("<error>\n");
                        bw.write("<checker>" + m_checkerExtension + "</checker>\n");
                        bw.write("<file>" + filePath.toLowerCase().replace("\\", "/") + "</file>\n");
                        bw.write("<num>" + Tryo.m_intnbDefects + "</num>\n");
                        bw.write("<description>" + m_checkerSumary + "     " + specialmessage + "</description>\n");
                        bw.write("<line>" + line + "</line>\n");
                        bw.write("<column>" + column + "</column>\n");
                        bw.write("</error>\n");
            		}
            		
            		else
            		{
            			//if the file already exist just Append the text to it
            			file.setWritable(true);
            			FileWriter fw = new FileWriter(file.getName());
            			BufferedWriter bw = new BufferedWriter(fw);
            			
            			bw.write("<error>\n");
                        bw.write("<checker>" + m_checkerExtension + "</checker>\n");
                        bw.write("<file>" + filePath.toLowerCase().replace("\\", "/") + "</file>\n");
                        bw.write("<num>" + Tryo.m_intnbDefects + "</num>\n");
                        bw.write("<description>" + m_checkerSumary + "     " + specialmessage + "</description>\n");
                        bw.write("<line>" + line + "</line>\n");
                        bw.write("<column>" + column + "</column>\n");
                        bw.write("</error>\n");
            		}
            	}	
            }
		}
		catch (Exception ex)
		{
			Output.OutputErrorTool("Error output2: " + ex);
		}
	}


	/**
	 * Function print the defect in a webPage. But first make sure it is not a 
	 * new Checker/Assembly. If it is, it needs to write the html header.
	 * @param checkerExt
	 * @param checkerName
	 * @param checkerSumary
	 * @param checkerDescription
	 * @param specialmessage
	 * @param filePath
	 * @param line
	 * @param column
	 */
	private static void printDefectInWebsite(String checkerExt,
			String checkerName, String checkerSumary,
			String checkerDescription, String specialmessage,
			String filePath, int line, int column)
	{
		// The checker are done 1 by 1. So the logic behind writing defects for a new
		// checker is in:. Here we just care about being a new Package (so new webpage).
		isNewPackage(filePath, checkerExt, checkerName, checkerSumary, checkerDescription);
		
		try
		{
			// The WebPage name is package-Checkerextention
			File file = new File(Tryo.m_config.m_folderResult + "\\" + m_packagename + "-" + checkerExt + ".htm");
			FileWriter fw = new FileWriter(file.getName(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			String error = "<br><hr><font color='navy'><b>In the file: " + filePath + "</b></font><br><br>";
			error += "<font color='red'> Defect found ";
			
			if(line > 0)
			{
				error += "at the line " + line + " (column " + column + "): <br>";
				// Called a function to print the line of code that has the defects
				String tempLineOfCode = printLineOfCode(filePath, line);
				if ((tempLineOfCode != null) && (column <= tempLineOfCode.length()) && (column > 2))
				{
					error += tempLineOfCode.substring(0, column - 2);
					// Use the column index to make the defect appear bold.
					error += "<b>";
					for (int columnnumb = column - 1; columnnumb < tempLineOfCode.Length; columnnumb++)
					{
						if (tempLineOfCode.charAt(columnnumb) == ' ')
                        {
                            error += "</b>";
                            error += tempLineOfCode.substring(columnnumb);
                            break;
                        }
						error += tempLineOfCode.charAt(columnnumb);
					}
				}
				else
				{
					error += tempLineOfCode;
				}
			}
			error += "<br></font><font color='navy'>";
			if (specialmessage != "")
            {
                error += "<br>" + specialmessage;
            }
			error += "<br>" + checkerSumary + "</font><br>";
			bw.write(error);
			bw.flush();
			bw.close();
			
		}
		catch(Exception ex)
		{
			Output.OutputErrorFiles("Failed to open: " + Tryo.m_config.m_folderResult + "\\" + m_packagename + "-" + checkerExt + ".htm because of error output3: " + ex.getMessage());
		}
	}

	
	/**
	 * 
	 * @param path
	 * @param checkerExt
	 * @param checkerName
	 * @param checkerSumary
	 * @param checkerDescription
	 */
	private static void isNewPackage(String path, String checkerExt, String checkerName, String checkerSumary, String checkerDescription)
	{
		try
		{
			String newAssemblyName;
            String[] decomposePath;
            String delimStr = "\\";
            char[] delimit = delimStr.toCharArray();
            // Try to get the Package from the path (subsubfolder of ArcGIS)
            decomposePath = path.split(delimStr);
            int numb = 0;
            
            while ((numb < decomposePath.length) && (decomposePath[numb].toLowerCase() != "arcgis"))
            {
            	numb++;
            }
            
            if ((numb != decomposePath.length) && (decomposePath[numb].toLowerCase() == "arcgis"))
            {
            	// Found the package (subsubfolder of ArcGIS)
                if (numb + 2 < decomposePath.length)
                {
                    newAssemblyName = decomposePath[numb + 2];
                }
                else
                {
                    // if Arcgis only has one or zero subfolder
                    if (numb + 1 < decomposePath.length)
                        newAssemblyName = decomposePath[numb + 1];
                    else
                        newAssemblyName = decomposePath[numb];
                }
            }
            
            else
            {
                // Didn't have ArcGIS part of the path. So can't deduct the package
                if (decomposePath.length >= 2)
                    newAssemblyName = decomposePath[1];
                else
                {
                    newAssemblyName = decomposePath[0];
                }
            }
            
            // We have the package name, let's see if it is a new one or not.
            if (!m_packagename.equals(newAssemblyName))
            {
            	// If it is a new one, we need first to close the old html page
            	if (!m_packagename.equals(""))
            	{
            		// Don't close the previous html page if it is the first one (previous packagename is null)
            		try
            		{
            			File file = new File(Tryo.m_config.m_folderResult + "\\" + m_packagename + "-" + checkerExt + ".htm");
            			FileWriter fw = new FileWriter(file.getName(), true);
            			BufferedWriter bw = new BufferedWriter(fw);
            			String output = "<br><br><a href='" + m_packagename + "-index.htm'> Go to index of the Assembly : " + m_packagename + "</a>";
            			output += "<br><a href='index-" + checkerExt + ".htm'> Go to index '" + checkerName + "' issues. </a>";
                        output += "<br><a href='index.htm'>Return to the index of all errors </a>";
                        output += "</body>";
                        output += "</html>";
                        bw.write(output);
                        bw.flush();
                        bw.close();
            		}
            		catch (Exception ex)
            		{
            			Output.OutputErrorFiles("Failed to open: " + Tryo.m_config.m_folderResult + "\\" + m_packagename + "-" + checkerExt + ".htm because of error output4: " + ex.getMessage());
            		}
            	}
            	
            	// Change the current package name to be the new one, and add the new package to list of packages having defects
            	m_packagename = newAssemblyName;
                if (!m_packageWithDefects.contains(m_packagename))
                    m_packageWithDefects.add(m_packagename);
                if (!m_allPackagesWithDefects.contains(m_packagename))
                    m_allPackagesWithDefects.add(m_packagename);
            	
                // Create a html header for the new package
                try
                {
                	File file = new File(Tryo.m_config.m_folderResult + "\\" + m_packagename + "-" + checkerExt + ".htm");
                	file.createNewFile();
        			FileWriter fw = new FileWriter(file.getName());
        			BufferedWriter bw = new BufferedWriter(fw);
        			
        			String output = "<html>";
                    output += "<head>";
                    output += "<title>" + checkerName + "</title>";
                    output += "<meta http-equiv='Content-Type' content='text/html; charset=iso-8859-1'>";
                    output += "</head>";
                    output += "<body bgcolor='#FFFFFF' text='#000000'>";
                    output += "<br><div align='center'><font color='navy' size='+3'><b>" + checkerName + " Assembly : " + m_packagename + "</b></font></div><font color='navy'><br>";
                    output += "If you have any comment about this Source Code Validation report, please send an email to <a href='MAILTO:scv@esri.com'>scv@esri.com</a>.<br><hr><br>";
                    output += checkerDescription + "</font><br>";
                    bw.write(output);
                    bw.flush();
                    bw.close();
                }
                catch (Exception ex)
                {
                	Output.OutputErrorFiles("Failed to open: " + Tryo.m_config.m_folderResult + "\\" + m_packagename + "-" + checkerExt + ".htm because of error output5: " + ex.getMessage());
                }	
            }      
		}
		catch (Exception ex)
        {
            Output.OutputErrorTool("Error output6: " + ex);
        }
	}

	
	
	/**********************Website index***********************/
	
	/**
	 * This function is called once after each checker runs by the main function Tryo.cs.
	 * It will create a page summarizing the packages that have defects for that particular checker.
	 */
	public static void WebSiteSumurizeChecker()
	{
		if (Tryo.m_config.m_aloutputtype.contains("Website"))
		{
			try
			{
				// If there is any defect for this checker
				if (m_packagename != "")
				{
					// First thing it does is closing the last html page it created (done otherwise by isNewPackage).
					if (!m_listExtension.contains(m_checkerExtension))
                        m_listExtension.add(m_checkerExtension);
					
					File file = new File(Tryo.m_config.m_folderResult + "\\" + m_packagename + "-" + m_checkerExtension + ".htm");
        			FileWriter fw = new FileWriter(file.getName(), true);
        			BufferedWriter bw = new BufferedWriter(fw);
        			String output = "<br><br><a href='" + m_packagename + "-index.htm'> Go to index of the Assembly : " + m_packagename + "</a>";
        			output += "<br><a href='index-" + m_checkerExtension + ".htm'> Go to index '" + m_checkerName + "' issues. </a>";
                    output += "<br><a href='index.htm'>Return to the index of all errors </a>";
                    output += "</body>";
                    output += "</html>";
                    bw.write(output);
                    bw.flush();
                    bw.close();
				}
			}
			catch(Exception ex)
			{
				Output.OutputErrorFiles("Failed to open: " + Tryo.m_config.m_folderResult + "\\" + m_packagename + "-" + m_checkerExtension + ".htm because of error output8: " + ex.getMessage());
			}
			
			try
			{
				// If there is any defect for this checker
				if (m_packagename != "")
				{
					// Now it will create an html page specific for that checker that will list all the packages that have that defect
					File file = new File(Tryo.m_config.m_folderResult + "\\index-" + m_checkerExtension + ".htm");
        			FileWriter fw = new FileWriter(file.getName(), true);
        			BufferedWriter bw = new BufferedWriter(fw);
        			
        			String output = "<html>";
        			output += "<head>";
                    output += "<title>" + m_checkerName + "</title>";
                    output += "<meta http-equiv='Content-Type' content='text/html; charset=iso-8859-1'>";
                    output += "</head>";
                    output += "<body bgcolor='#FFFFFF' text='#000000'>";
                    output += "<br><div align='center'><font size='+3' color='navy'><b>" + m_checkerName + " Assembly : " + m_packagename + "</b></font></div><font color='navy'><br>";
                    output += "If you have any comment about this Source Code Validation report, please send an email to <a href='MAILTO:scv@esri.com'>scv@esri.com</a>.<br><hr><br>";
                    output += m_checkerDescription + "<br>";
                    output += "<br><hr><br>";
                    output += "List of the Assemblies whith that kind of defect : <br>";
                    Collections.sort(m_packageWithDefects);
                    
                    //Go through all the packages one by one
                    for(int i = 0; i < m_packageWithDefects.size(); i++)
                    {
                    	output += "<br><a href='" + m_packageWithDefects.get(i) + "-" + m_checkerExtension + ".htm'>" + m_packageWithDefects.get(i) + "</a>";
                    }
                    output += "<br><br></font><hr><br><b>Summary</b><br><br>Number of assemblies with error(s) : " + m_packageWithDefects.size();
                    output += "<br><br><a href='index.htm'>Return to the index of all errors </a>";
                    output += "</body>";
                    output += "</html>";
                    bw.write(output);
                    bw.flush();
                    bw.close();
                    m_packageWithDefects.clear();
                    m_packagename = "";
				}
			}
			catch(Exception ex)
			{
				Output.OutputErrorFiles("Failed to open: " + Tryo.m_config.m_folderResult + "\\" + m_packagename + "-" + m_checkerExtension + ".htm because of error output9: " + ex.getMessage());
			}
		}	
	}
	
	
	/**
	 * This function is called once when all the checkers are done running, and the 
	 * website is ready to be finished (or the xml to be launch, if running from the UI).
	 */
	public static void WebSiteIndex()
	{
		if(Tryo.m_config.m_aloutputtype.contains("Website"))
		{
			try
			{
				// Getting the extension, name and description for all the checkers that have defects.
				String[][] archeckerdescription = new String[m_listExtension.size()][3];
				int index = 0;
				Collections.sort(m_listExtension);
				
				for (int j = 0; j < (Tryo.m_arallcheckers.length / 8); j++)
                {
                    if (m_listExtension.contains(Tryo.m_arallcheckers[j][0]))
                    {
                        archeckerdescription[index][0] = Tryo.m_arallcheckers[j][0];
                        archeckerdescription[index][1] = Tryo.m_arallcheckers[j][1];
                        archeckerdescription[index][2] = Tryo.m_arallcheckers[j][2];
                        index++;
                    }
                }
				
				// Create the main Index page
				File file = new File(Tryo.m_config.m_folderResult + "\\index.htm");
    			FileWriter fw = new FileWriter(file.getName(), true);
    			BufferedWriter bw = new BufferedWriter(fw);
    			
    			String output = "<html>";
                output += "<head>";
                output += "<title>Index</title>";
                output += "<meta http-equiv='Content-Type' content='text/html; charset=iso-8859-1'>";
                output += "</head>";
                output += "<body bgcolor='#FFFFFF' text='#000000'>";
                output += "<br><div align='center'><font size='+3' color='navy'><b>List of the assemblies/checkers having defects</b></font></div><br><font color='navy'>";
                output += "If you have any comment about this Source Code Validation report, please send an email to <a href='MAILTO:scv@esri.com'>scv@esri.com</a>.<br><br><hr><br>";
                Collections.sort(m_allPackagesWithDefects);
                
                if (m_allPackagesWithDefects.size() == 0)
                {
                    output += "No defect found";
                }
                else
                {
                	// look through the arraylist of packages having defects to list them with a link to the page of that particular assembly.
                	output += "<font color='navy'>List of the assemblies having defects :<br>";
                	for(int i = 0; i < m_allPackagesWithDefects.size(); i++)
                	{
                		output += "<br><a href='" + m_allPackagesWithDefects.get(i) + "-index.htm'> Go to the main page of the assembly '" + m_allPackagesWithDefects.get(i) + "'. </a>";
                	}
                	
                	// For each checker that have defects, write the description of it, and a link to it's main page (already created by WebSiteSumurizeChecker).
                    output += "<br><br><hr><br>List of the checkers that found defects :<br>";
                    Collections.sort(m_listExtension);
                    for(int i = 0; i < m_listExtension.size(); i++)
                    {
                    	for(int ext = 0; ext < m_listExtension.size(); ext++)
                    	{
                    		if(archeckerdescription[ext][0] == m_listExtension.get(i))
                    		{
                    			output += "<br><br>" + archeckerdescription[ext][2];
                                output += "<br><a href='index-" + m_listExtension.get(i) + ".htm'> Go to the index page of the checker '" + archeckerdescription[ext][1] + "'. </a>";
                                break;
                    		}
                    	}
                    }	
                }
                output += "</font></body>";
                output += "</html>";
                bw.write(output);
                bw.flush();
                bw.close();
                
                // Now it will create a webpage per assembly that have defects.
                ArrayList checkersforassembly = new ArrayList();
                ArrayList<String> pathfiles = new ArrayList<String>();
                
                //To get the list of assembly with defect, it is easy : m_allpackageswithdefects. But we need to know which assembly has which type of defect. Harder!!!
                //To do that, we will use the name of all the webpages we have already created. They are called assemblyname-checker.htm
                
                // -----------------------------WARNING: NOT sure about the following for loop----------------------
                for(int i = 0; i < Tryo.m_config.m_folderResult.size(); i++)
                {
                	if(Tryo.m_config.m_folderResult[i].endsWith(".htm"))
                		pathfiles.add(Tryo.m_config.m_folderResult[i]);
                }
                Collections.sort(pathfiles);
                
                //We will look through all the assemblies that have defects.
                for(int i = 0; i < m_allPackagesWithDefects.size(); i++)
                {
                	// We will find the list of htm file name that assembly appear in. That will give us the list of checkers that found defects for that assembly.
                	for(int j = 0; j < pathfiles.size(); j++)
                	{
                		if((pathfiles.get(j)).contains((CharSequence) m_allPackagesWithDefects.get(i)))
                		{
                			String checkertemp = pathfiles.get(j).substring(pathfiles.get(j).indexOf((String)m_allPackagesWithDefects.get(i)) + m_allPackagesWithDefects.get(i).length() + 1, 
                					pathfiles.get(j).indexOf(".htm") - (pathfiles.get(j).indexOf(m_allPackagesWithDefects.get(i)) + m_allPackagesWithDefects.get(i).length() + 1));
                			if (!checkersforassembly.contains(checkertemp))
                                checkersforassembly.add(checkertemp);
                		}
                	}
                	// Now we have the list of checker that found defects for that particular assembly.
                    Collections.sort(checkersforassembly);
                    
                    // Create an htm page for that particular assembly
                    File file2 = new File(Tryo.m_config.m_folderResult + "\\" + m_allPackagesWithDefects.get(i) + "-index.htm");
        			FileWriter fw2 = new FileWriter(file2.getName(), true);
        			BufferedWriter bw2 = new BufferedWriter(fw2);
                	
        			String output2 = "<html>";
                    output2 += "<head>";
                    output2 += "<title>Index</title>";
                    output2 += "<meta http-equiv='Content-Type' content='text/html; charset=iso-8859-1'>";
                    output2 += "</head>";
                    output2 += "<body bgcolor='#FFFFFF' text='#000000'>";
                    output2 += "<br><div align='center'><font size='+3' color='navy'><b>List of the checkers that found issues in the assembly : " + m_allPackagesWithDefects.get(i) + 
                    		" </b></font></div><br><font color='navy'> If you have any comment about this Source Code Validation report, please send an email";
                    output2 += "to <a href='MAILTO:scv@esri.com'>scv@esri.com</a>.<br><hr><br>";
                    
                    Collections.sort(m_listExtension);
                    // For each checker that found defects for that particular assembly, we're getting its name and description from the mail arrayList, and display a link in the htm page.
                    for(int j = 0; j < checkersforassembly.size(); j++)
                    {
                    	for (int ext = 0; ext < m_listExtension.size(); ext++)
                        {
                            if (archeckerdescription[ext][0] == checkersforassembly.get(j))
                            {
                                output2 += "<br><br>" + archeckerdescription[ext][2];
                                output2 += "<br><a href='" + m_allPackagesWithDefects.get(i) + "-" + checkersforassembly.get(j) + ".htm'> Go to the page of the issues found by the checker '" +
                                		archeckerdescription[ext][1] + "' for the assembly " + m_allPackagesWithDefects.get(i) + ". </a>";
                                break;
                            }
                        }
                    }
                    
                    // Close the htm page for that assembly
                    output += "<br><br><a href='index.htm'>Return to the index of all errors </a>";
                    output2 += "</font></body>";
                    output2 += "</html>";
                    bw2.write(output2);
                    bw2.flush();
                    bw2.close();
                    checkersforassembly.clear();		
                }
                m_allPackagesWithDefects.clear();
                m_listExtension.clear();
                
                //Try to load the webpage in the default internet browser
                if ((Tryo.m_config.m_inputtype == "UItryo") && (File.Exists(Tryo.m_config.m_folderResult + "\\index.htm")))
                {
                    Process.Start(Tryo.m_config.m_folderResult + "\\index.htm");
                }
               
			}
			catch(Exception ex)
			{
				Output.OutputErrorFiles("Failed to open: " + Tryo.m_config.m_folderResult + "\\index.htm because of error output10: " + ex.getMessage());
			}
		}
		
		else
		{
			try
			{
				//If it is XML file, open it in the default viewer
                if ((Tryo.m_config.m_inputtype == "UItryo") && (File.Exists(Tryo.m_config.m_folderResult + "\\SCV.xml")))
                {
                    Process.Start(Tryo.m_config.m_folderResult + "\\SCV.xml");
                }
			}
			catch(Exception ex)
			{
				Output.OutputErrorFiles("Failed to open: " + Tryo.m_config.m_folderResult + "\\SCV.xml because of error output11: " + ex.getMessage());
			}
		}
	}
	
	
	
	/**
	 * This function creates a file that list all the files that have defects except the .cpp, .h, .csproj files. The goal of this file is to force the Prevent (Coverity) database to include theses files.
     * Otherwise the defects found in files like .idl will not be shown on the prevent website even if the defects was entered in the database (because the file it will refer too, will not be there).
	 */
	public static void CreateFileCoverityLinkage()
    {
    	try
    	{
    		if (m_fileWithDefects.size() == 0)
                return;
    		
    		File fileTemp = new File(Tryo.m_config.m_folderResult);
    		if (Tryo.m_config.m_aloutputtype.contains("CPP"))
    		{
    			String output = "";
    			Collections.sort(m_fileWithDefects);
    			for(int i = 0; i < m_fileWithDefects.size(); i++)
    			{
    				//Do not list the files that are already in Prevent (.cpp, .h, .csproj)
                    if ((!m_fileWithDefects.get(i).endsWith(".csproj")) && (!m_fileWithDefects.get(i).endsWith(".cpp")) && (!m_fileWithDefects.get(i).endsWith(".h")))
                        output += "# line 1 \"" + m_fileWithDefects.get(i).replace("\\", "\\\\") + "\"\n";
    			}
    			File file2 = new File(fileTemp.getAbsolutePath() + "\\LinkageCoverity.cpp");
    			FileWriter fw2 = new FileWriter(file2.getName());
    			BufferedWriter bw2 = new BufferedWriter(fw2);
    			bw2.write(output);    			
    		}
    		
    		else if(Tryo.m_config.m_aloutputtype.contains("CS"))
    		{
    			//Making sure first that the file isn't already there. So first collect all the path already included
                //First get the intermediate directory from the path (replace the \output\ by \emit\)
                String intermediate = fileTemp.getAbsolutePath().replace("\\cs\\output", "\\cs\\emit\\");
                File file = new File(intermediate);
                file.mkdir();
                File[] files = file.listFiles();
                String allRead = "";
                
                for(int i = 0; i < files.length; i++)
                {
                	String currentLine = "";
        			BufferedReader br = new BufferedReader(new FileReader(files[i].getAbsolutePath()));
        			while((currentLine = br.readLine()) != null)
        			{
        				allRead += currentLine;
        			}
        			br.close();
                }
                
                for(int i = 0; i < m_fileWithDefects.size(); i++)
                {
                	// Do not list the files that are already in Prevent (.cs)
                	if(!m_fileWithDefects.get(i).endsWith(".cs"))
                	{
                		if(new File(m_fileWithDefects.get(i)).exists())
                		{
                			File fileTemp2 = new File(m_fileWithDefects.get(i));
                			if(allRead.contains(fileTemp2.getAbsolutePath()))
                			{
                				String folder = Integer.toString((int) (100 + Math.random()*900));
                				String random1 = Integer.toString((int)(100000000 + Math.random()*900000000)) + 
                						Integer.toString((int)(100000000 + Math.random()*900000000)) + 
                						Integer.toString((int)(100000000 + Math.random()*900000000)) + 
                						Integer.toString((int)(10 + Math.random()*90));
                				String random2 = Integer.toString((int)(100000000 + Math.random()*900000000)) + 
                						Integer.toString((int)(100000000 + Math.random()*900000000)) + 
                						Integer.toString((int)(100000000 + Math.random()*900000000)) + 
                						Integer.toString((int)(10000 + Math.random()*90000));
                				String subfolder = intermediate + folder + "\\" + random1;
                				File dir = new File(subfolder);
                				dir.mkdir();
                				
                				File file2 = new File(subfolder + "\\incl");
                    			FileWriter fw2 = new FileWriter(file2.getName());
                    			BufferedWriter bw2 = new BufferedWriter(fw2);
                    			bw2.write(fileTemp2.getAbsolutePath() + "|");
                    			
                    			File file3 = new File(subfolder + "\\md5");
                    			FileWriter fw3 = new FileWriter(file3.getName());
                    			BufferedWriter bw3 = new BufferedWriter(fw3);
                    			bw3.write(random2);
                    			
                    			InputStream inStream = null;
                    			OutputStream outStream = null;
                    			try
                    			{                    				 
                    	    	    File afile =new File(m_fileWithDefects.get(i));
                    	    	    File bfile =new File(subfolder + "\\source");
                    	 
                    	    	    inStream = new FileInputStream(afile);
                    	    	    outStream = new FileOutputStream(bfile);
                    	 
                    	    	    byte[] buffer = new byte[1024];
                    	 
                    	    	    int length;
                    	    	    //copy the file content in bytes 
                    	    	    while ((length = inStream.read(buffer)) > 0){
                    	 
                    	    	    	outStream.write(buffer, 0, length);
                    	 
                    	    	    }
                    	 
                    	    	    inStream.close();
                    	    	    outStream.close();
                    	 
                    	    	    System.out.println("File is copied successful!");
                    	 
                    	    	}
                    			catch(IOException e)
                    			{
                    	    		e.printStackTrace();
                    	    	}
                    			bw2.flush();
                    			bw3.flush();
                    			bw2.close();
                    			bw3.close();
                			}
                		}
                	}
                }       
    		}
    	}
    	catch(Exception ex)
    	{
    		Output.OutputErrorFiles("Error output12: " + ex.getMessage());
    	}
    }
	


	/**
	 * Function that will open the file, at retrieve the line of code corresponding to the number.
	 * @param filePath
	 * @param lineDefect
	 * @return
	 */
	private static String printLineOfCode(String filePath, int lineDefect)
	{
		try
		{
			int lineRead = 1;
			// Create and open a BufferedReader.
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line = br.readLine();
			// Loop through all the lines.
			while (lineRead != lineDefect)
			{
				line = br.readLine();
				lineRead++;
			}
			// Close BufferedReader.
			br.close();
			
			return line;
		}
		catch(Exception ex)
		{
			Output.OutputErrorTool("Error output7: " + ex.getMessage());
            return "";
		}
	}
	
	
	/**
	 * This entire function just print the help when somebody ask for it on the command line. 
	 * It is long, because it needs to list all the checkers/category.
	 * 
	 * @param allitems
	 */
	public static void OutputHelpCommandLine(String[][] allitems)
	{
		try
		{
			ArrayList arraylextension = new ArrayList();
            ArrayList arraylcategory = new ArrayList();
            ArrayList arraylseverity = new ArrayList();
            ArrayList arraylpercat = new ArrayList();
            ArrayList arraylpersev = new ArrayList();
            boolean first = true;
            OutputMsg("\n");
            String strArgs = "Tryo [input:{PATH}] [output:{PATH}] [outputtype:{XML,WebSite,CS,CPP,JAVA}] [fromconfig] [saveconfig] [allcheckers]";
            
            // Write all the checkers ids
            for (int i = 0; i < (allitems.length / 6); i++)
            {
                arraylextension.add(allitems[i][0]);
            }
            Collections.sort(arraylextension);
            for(int i = 0; i < arraylextension.size(); i++)
            {
            	strArgs += " [-" + arraylextension.get(i) + "]";
            }
            
            // Write all the categories
            strArgs += " [cat:{";
            for (int j = 0; j < (allitems.length / 6); j++)
            {
                if (!arraylcategory.contains(allitems[j][4]))
                    arraylcategory.add(allitems[j][4]);
            }
            Collections.sort(arraylcategory);
            for(int i = 0; i < arraylcategory.size(); i++)
            {
            	if (!first)
                    strArgs += ",";
                first = false;
                strArgs += arraylcategory.get(i);
            }
            first = true;
            strArgs += "}]";
            
            // Write all the severities
            strArgs += " [severity:{";
            for (int k = 0; k < (allitems.length / 6); k++)
            {
                if (!arraylseverity.contains(allitems[k][5]))
                    arraylseverity.add(allitems[k][5]);
            }
            Collections.sort(arraylseverity);
            for(int i = 0; i < arraylseverity.size(); i++)
            {
            	if (!first)
                    strArgs += ",";
                first = false;
                strArgs += arraylseverity.get(i);
            }
            strArgs += "}]";
            OutputMsg(strArgs);
            OutputMsg("\n");
            OutputMsg("input:\t -Path to the directory or file to be checked.");
            OutputMsg("output:\t -Path to the directory to write the output files (will overwrite).");
            OutputMsg("outputtype:\t -Type of files to be create (XML and/or WebSite).");
            OutputMsg("fromconfig\t -Load options from config file (AppData\\ESRI\\Tryo\\Config.xml).");
            OutputMsg("saveconfig\t -Save options to config file (AppData\\ESRI\\Tryo\\Config.xml).");
            OutputMsg("allcheckers\t -Run all the checkers");
            OutputMsg("\n");
            
            // Write for all the checkers id
            for(int i = 0; i < arraylextension.size(); i++)
            {
            	for(int l = 0; l < (allitems.length / 6); l++)
            	{
            		if ((arraylextension.get(i).toString()).compareTo(allitems[l][0]) == 0)
                    {
                        OutputMsg(" -" + allitems[l][0]);
                        OutputMsg(" --" + allitems[l][0]);
                        OutputMsg(" /" + allitems[l][0] + "\t -Run the checker: " + allitems[l][1] + " : " + allitems[l][2]);
                        break;
                    }
            	}
            }
            
            OutputMsg("\n");
            
            // Write for all the categories
            Boolean firstincat = true;
            String strpercat = "";
            
            for(int i = 0; i < arraylcategory.size(); i++)
            {
            	firstincat = true;
                strpercat = "cat:" + arraylcategory.get(i).toString() + "\t -Run all checkers in the category " + category2 + " (";
                for (int m = 0; m < (allitems.length / 6); m++)
                {
                    if ((arraylcategory.get(i).toString()).compareTo(allitems[m][4]) == 0)
                    {
                        arraylpercat.add(allitems[m][0]);
                    }
                }
                Collections.sort(arraylpercat);
                for(int j = 0; j < arraylpercat.size(); j++)
                {
                	if (!firstincat)
                    {
                        strpercat += ", ";
                    }
                    strpercat += arraylpercat.get(j).toString();
                    firstincat = false;
                }
                strpercat += ")";
                OutputMsg(strpercat);
                arraylpercat.clear();              
            }
            OutputMsg("\n");
            
            // Write for all the severities
            Boolean firstinsev = true;
            String strpersev = "";
            
            for(int i = 0; i < arraylseverity.size(); i++)
            {
            	firstinsev = true;
                strpersev = "severity:" + arraylseverity.get(i).toString() + "\t Run all checkers with severity level " + severity2 + " (";
                for (int n = 0; n < (allitems.length / 6); n++)
                {
                    if ((arraylseverity.get(i).toString()).compareTo(allitems[n][5]) == 0)
                    {
                        arraylpersev.add(allitems[n][0]);
                    }
                }
                Collections.sort(arraylpersev);
                for(int j = 0; j < arraylpersev.size(); j++)
                {
                	if (!firstinsev)
                    {
                        strpersev += ", ";
                    }
                    strpersev += arraylpersev.get(j).toString();
                    firstinsev = false;
                }
                strpersev += ")";
                OutputMsg(strpersev);
                arraylpersev.clear();                
            }
            
            OutputMsg("\n");
            OutputMsg("-h");
            OutputMsg("/h");
            OutputMsg("-?");
            OutputMsg("/?");
            OutputMsg("--help\t -Display help");
            OutputMsg("\n");
            OutputMsg("Example:");
            OutputMsg("-To run using the UI");
            OutputMsg(" Tryo.exe");
            OutputMsg("-To run using the config file (already run once, first run easier with the UI)");
            OutputMsg(" Tryo.exe fromconfig");
            OutputMsg("-To run using allcheckers in specific folders:");
            OutputMsg(" Tryo.exe input:C:\\ArcGIS output:C:\\OutputTryo outputtype:WebSite allcheckers");
            OutputMsg("-To run using the config file but only the checkers from the Category:Category1");
            OutputMsg(" Tryo.exe fromconfig cat:Category1");
            
            arraylextension = null;
            arraylcategory = null;
            arraylseverity = null;            
		}
		catch(Exception ex)
		{
			Output.OutputErrorTool("Error output13:" + ex);
		}
	}
}
