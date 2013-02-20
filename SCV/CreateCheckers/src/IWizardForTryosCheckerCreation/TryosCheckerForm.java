package IWizardForTryosCheckerCreation;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextPane;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This is Form1.java class
 * Form1.java is the GUI for adding a new checker in SCV.
 * @author fshan
 *
 */
public class TryosCheckerForm {

	private JFrame form1;
	private static JTextField txtExtension;
	private JTextField txtName;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField txtCategory;
	private JTextField txtSummary;
	private static JComboBox cbCategory = new JComboBox();	
	private static JComboBox comboSeverity = new JComboBox();
	private static JLabel labelExtensionErr = new JLabel("");
	private static JLabel labelNameErr = new JLabel("");
	private static JLabel labelCategoryErr = new JLabel("");
	private static JLabel labelSummaryErr = new JLabel("");
	private static JLabel labelDescriptionErr = new JLabel("");
	
	private ArrayList m_arlExt;
    private Map<String, String> parameters; 
    
	
	// The regular expression of characters: / ? : $ \ * < > | # % "
	private static final Pattern p = Pattern.compile( "(([\\w]|[\\s])*([[\\[\\]\\(\\)/\\?:\\&\\\\*<>|#%]|[\"]]+)([\\w]|[\\s])*)*" );
	
	
	// Getter to access parameters from other classes.
	public Map<String, String> getParameters()
	{
		return this.parameters;	
	}
	
	
	
	/**
	 * Constructor of form1, the TryoCheckerForm
	 * @param arExt, extension array, read from external dll file
	 * @param arCat, category array, read from external dll file
	 */
	public TryosCheckerForm(ArrayList arExt, ArrayList arCat)
    {
      m_arlExt = arExt;
      
      if (arCat.size() > 0)
      {
    	  // arCat is not sorted.
    	  for(int i = 0; i < arCat.size(); i++)
    	  {
    		  cbCategory.addItem(arCat.get(i).toString());
    	  }

      }
	
    }
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					TryosCheckerForm window = new TryosCheckerForm();
					window.form1.setVisible(true);
				} 
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	

	/**
	 * Create the application.
	 */
	public TryosCheckerForm()
	{
		initialize();
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		initFrame();
		
		JLabel label1 = initLabel1();
		
		initTxtExtension(label1);
		
		JLabel label7 = initLabel7();
		
		initTxtName(label7);
		
		initLabel4();
		
		JRadioButton rNew = initRNew();
		
		JRadioButton rExisting = initRExisting();
		
		initTxtCategory();
				
		initCbCategory();
		
		JLabel label3 = initLabel3();
			
		initComboSeverity(label3);
		
		JLabel label6 = initLabel6();
		
		initTxtSummary(label6);
		
		JLabel label5 = initLabel5();
			
		JButton btnCancel = initBtnCancel();
		
		final JButton btnOk = initBtnOk();
		
		final JTextPane txtDescription = initTxtDescription(label5);
		
		initLabelExtensionErr();
				
		initLabelNameErr();
		
		initLabelCategoryErr();
		
		initLabelSummaryErr();
		
		initLabelDescriptionErr(txtDescription);
			
		cancel(btnCancel);
		
		addMouseListenerRExisting(rExisting);
		
		addMouseListenerRNew(rNew);
			
		validate(btnOk, txtDescription);			
	}



	/**
	 * Wrapper of all validation methods.
	 * @param btnOk
	 * @param txtDescription
	 */
	private void validate(final JButton btnOk, final JTextPane txtDescription)
	{
		// Validate input in txtExtension JTextField
		txtExtension_Validating();
		
		// Validate input in txtName JTextField
		txtName_Validating();
		
		// Validate input in txtCategory JTextField
		txtCategory_Validating();
		
		// Validate input in txtSummary JTextField
		txtSumary_Validating();
		
		// Validate input in txtDescription JTextPane
		txtDescription_Validating(txtDescription);
	
		// Validate all fields and Generate the parameters dictionary when btnOK is clicked
		validateAll(btnOk, txtDescription);
	}



	/**
	 * Validate all fields and Generate the parameters dictionary when btnOK is clicked
	 * @param btnOk
	 * @param txtDescription
	 */
	private void validateAll(final JButton btnOk, final JTextPane txtDescription)
	{
		btnOk.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{		
				if((txtExtension.getText().length() >= 5) && (txtExtension.getText().indexOf(" ") < 0) &&
						(txtName.getText().length() >= 10) &&
						(!txtCategory.isEnabled() || ((txtCategory.getText().length() >= 5) && txtCategory.getText().indexOf(" ") < 0)) &&
						(txtSummary.getText().length() >= 10) &&
						(txtDescription.getText().length() >= 30))
				{	
					parameters = new HashMap<String, String>();
					parameters.put("Extension", txtExtension.getText());
					parameters.put("itemname", txtExtension.getText());
					parameters.put("Name", txtName.getText());
					parameters.put("Summary", replaceString(txtSummary.getText()));
					parameters.put("Description", replaceString(txtDescription.getText()));
					
					if(cbCategory.isEnabled())
					{
						parameters.put("Category", cbCategory.getSelectedItem().toString());	
					}
					else
					{
						parameters.put("Category", txtCategory.getText());	
					}
					
					parameters.put("Severity", Integer.toString(comboSeverity.getSelectedIndex()));
					
					Iterator<String> iterator = parameters.keySet().iterator();   
				    
					while (iterator.hasNext())
					{   
					   String key = iterator.next().toString();   
					   String value = parameters.get(key).toString();   
					    
					   System.out.println(key + ": " + value);   
					}  

					form1.dispose();		
				}
				
				else
				{
					txtExtension_Validating();
			        txtName_Validating();
			        txtCategory_Validating();
			        txtSumary_Validating();
			        txtDescription_Validating(txtDescription);
			        
					JOptionPane.showMessageDialog(btnOk, "You need to change your parameters as indicated by the red point(s).");		
				}	
			}
		});
	}



	/**
	 * Terminate and close the window when btnCancel is clicked.
	 * @param btnCancel
	 */
	private void cancel(JButton btnCancel)
	{
		btnCancel.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				form1.dispose();
			}
		});
	}



	/**
	 * Validate input in txtExtension JTextField
	 */
	private void txtExtension_Validating()
	{
		txtExtension.addFocusListener(new FocusAdapter()
		{
   			@Override
   			public void focusLost(FocusEvent e)
   			{
   				String input = txtExtension.getText();
   				
   				Matcher m = p.matcher(input);
   				
/*   				for(int i = 0; i < m_arlExt.size(); i++)
   				{
   					if(input.equalsIgnoreCase(m_arlExt.get(i).toString()))
   					{
   						labelExtensionErr.setVisible(true);
   						labelExtensionErr.setToolTipText("Extensions need to be unique");
   					}
   				}
*/   				if(input.indexOf(" ") >= 0)
   				{
   					labelExtensionErr.setVisible(true);
   					labelExtensionErr.setToolTipText("Extensions can't have spaces");
   				}
   				else if(input.length() < 5)
  				{
   					labelExtensionErr.setVisible(true);
   					labelExtensionErr.setToolTipText("Extensions must be at least 5 characters!");
   				}
   				else if(m.matches())
   				{	
					labelExtensionErr.setVisible(true);
					labelExtensionErr.setToolTipText("The Name cannot contain any of the following charactors: / ? : && \\ * \" < > | # %");					
   				}
   				else
   				{
   					labelExtensionErr.setVisible(false);
   					labelExtensionErr.setToolTipText("");	
   				}					
   			}		   				
   		});
	}


	/**
	 * Validate input in txtName JTextField
	 */
	private void txtName_Validating()
	{
		txtName.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent e)
			{				
				String input = txtName.getText();
				
				Matcher m = p.matcher(input);
				
				if(input.length() < 10)
				{					
					labelNameErr.setVisible(true);
					labelNameErr.setToolTipText("Must be at least 10 characters!");					
				}
				else if(m.matches())
				{					
						labelNameErr.setVisible(true);
						labelNameErr.setToolTipText("The Name cannot contain any of the following charactors: / ? : && \\ * \" < > | # %");					
				}
				else
				{					
					labelNameErr.setVisible(false);
					labelNameErr.setToolTipText("");					
				}			
			}		
		});
	}
	
	
	/**
	 * Validate input in txtCategory JTextField
	 */
	private void txtCategory_Validating()
	{
		txtCategory.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent e)
			{		
				if(txtCategory.isEnabled())
				{			
					String input = txtCategory.getText();
					
					Matcher m = p.matcher(input);
					
					if(input.indexOf(" ") >= 0)
					{
						labelCategoryErr.setVisible(true);
						labelCategoryErr.setToolTipText("Categories can't have spaces.");						
					}
					else if(input.length() < 5)
					{						
						labelCategoryErr.setVisible(true);
						labelCategoryErr.setToolTipText("Categories must have at least 5 characters!");						
					}
					else if(m.matches())
					{						
						labelCategoryErr.setVisible(true);
						labelCategoryErr.setToolTipText("The Category cannot contain any of the following charactors: / ? : && \\ * \" < > | # %");						
					}
					
					else{						
						labelCategoryErr.setVisible(false);
						labelCategoryErr.setToolTipText("");						
					}					
				}				
			}			
		});
	}
	
	
	/**
	 * Validate input in txtSummary JTextField
	 */
	private void txtSumary_Validating()
	{
		txtSummary.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent e)
			{				
				String input = txtSummary.getText();
				
				if(input.length() < 10)
				{	
					labelSummaryErr.setVisible(true);
					labelSummaryErr.setToolTipText("Must have at least 10 characters!");					
				}
				else if(input.length() > 130)
				{					
					labelSummaryErr.setVisible(true);
					labelSummaryErr.setToolTipText("The summary discription should be shorter!"); // --------------> Shorter than 130 characters?					
				}
				else
				{					
					labelSummaryErr.setVisible(false);
					labelSummaryErr.setToolTipText("");					
				}				
			}			
		});
	}
	
	
	/**
	 * Validate input in txtDescription JTextPane
	 * @param txtDescription
	 */
	private void txtDescription_Validating(final JTextPane txtDescription)
	{
		txtDescription.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent e)
			{				
				String input = txtDescription.getText();
				
				if(input.length() < 30)
				{					
					labelDescriptionErr.setVisible(true);
					labelDescriptionErr.setToolTipText("Must be at least 30 characters!");					
				}
				else
				{				
					labelDescriptionErr.setVisible(false);
					labelDescriptionErr.setToolTipText("");					
				}			
			}		
		});
	}
	
	

	/**
	 * Add mouse listener for rNew radio button
	 * @param rNew
	 */
	private void addMouseListenerRNew(JRadioButton rNew)
	{
		// If the radio button rNew is checked then disables the cbCategory JCombobox and then enables and validates the txtCategory JTextField.
		rNew.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{				
				cbCategory.setEnabled(false);
				txtCategory.setEnabled(true);				
			}			
		});
	}



	/**
	 * Add mouse listener for rExisting radio button
	 * @param rExisting
	 */
	private void addMouseListenerRExisting(JRadioButton rExisting)
	{
		// If the radio button rExisting is checked then enables the cbCategory JCombobox and then disables and validates the txtCategory JTextField.
		rExisting.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{				
				cbCategory.setEnabled(true);
				txtCategory.setEnabled(false);				
			}
		});
	}


	/**
	 * initialize TryosCheckerForm
	 */
	private void initFrame()
	{
		form1 = new JFrame();
		form1.getContentPane().setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 11));
		form1.getContentPane().setBackground(SystemColor.control);
		form1.setTitle("Parameters for a new SCV checker");
		form1.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\qalab\\workspace2\\CreateCheckers\\__TemplateIcon_1_48x48x32.png"));
		form1.setBounds(100, 100, 650, 291);
		form1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		form1.getContentPane().setLayout(null);
	}


	/**
	 * Initialize label1 for extension(ID)
	 * @return
	 */
	private JLabel initLabel1()
	{
		JLabel label1 = new JLabel("ID");
		label1.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 11));
		label1.setBounds(12, 9, 18, 13);
		form1.getContentPane().add(label1);
		return label1;
	}

	
	/**
	 * initialize txtExtension JTextfield for extension(ID)
	 * @param label1
	 */
	private void initTxtExtension(JLabel label1)
	{
		txtExtension = new JTextField();
		txtExtension.setToolTipText("Extension of the checker, the extension needs to be unique and is only for internal use.");
		label1.setLabelFor(txtExtension);
		txtExtension.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 11));
		txtExtension.setBounds(12, 25, 248, 20);
		form1.getContentPane().add(txtExtension);
		txtExtension.setColumns(10);
	}
	
	
	/**
	 * initialize label7 for Name
	 * @return
	 */
	private JLabel initLabel7()
	{
		JLabel label7 = new JLabel("Name");
		label7.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 11));
		label7.setBounds(12, 59, 35, 13);
		form1.getContentPane().add(label7);
		return label7;
	}
	
	
	/**
	 * initialize txtName JTextField for Name
	 * @param label7
	 */
	private void initTxtName(JLabel label7)
	{
		txtName = new JTextField();
		txtName.setToolTipText("The name of the checker that will be displayed to a user.");
		label7.setLabelFor(txtName);
		txtName.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 11));
		txtName.setBounds(12, 75, 248, 20);
		form1.getContentPane().add(txtName);
		txtName.setColumns(10);
	}
	
	
	/**
	 * initialize label4 for Category
	 */
	private void initLabel4()
	{
		JLabel label4 = new JLabel("Category");
		label4.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 11));
		label4.setBounds(12, 111, 49, 13);
		form1.getContentPane().add(label4);
	}
	
	
	/**
	 * initialize rNew JRadioButton for Category
	 * @return
	 */
	private JRadioButton initRNew()
	{
		JRadioButton rNew = new JRadioButton("New");
		buttonGroup.add(rNew);
		rNew.setBackground(SystemColor.control);
		rNew.setSelected(true);
		rNew.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 11));
		rNew.setToolTipText("Create new category.");
		rNew.setBounds(15, 128, 47, 17);
		form1.getContentPane().add(rNew);
		return rNew;
	}
	
	
	/**
	 * initialize rExisting JRadioButton for Category
	 * @return
	 */
	private JRadioButton initRExisting()
	{
		JRadioButton rExisting = new JRadioButton("Existing");
		buttonGroup.add(rExisting);
		rExisting.setBackground(SystemColor.control);
		rExisting.setToolTipText("Use existing category.");
		rExisting.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 11));
		rExisting.setBounds(15, 153, 61, 17);
		form1.getContentPane().add(rExisting);
		return rExisting;
	}
	
	
	/**
	 * initialize txtCategory JTextField for Category
	 */
	private void initTxtCategory()
	{
		txtCategory = new JTextField();
		txtCategory.setToolTipText("Create new category which will be used to group like checkers together.");
		txtCategory.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 11));
		txtCategory.setBounds(82, 127, 178, 20);
		form1.getContentPane().add(txtCategory);
		txtCategory.setColumns(10);
	}
	
	
	/**
	 * initialize cbCategory JComboBox for Category
	 */
	private void initCbCategory()
	{
		cbCategory.setBackground(SystemColor.window);
		cbCategory.setEnabled(false);
		
		// -----------WARNING------------The items should be read from the ArrayList arCat.
		
		cbCategory.setModel(new DefaultComboBoxModel(new String[] {"CheckSource", "ESRISxS", "FixSolution", "I18NCheckers", "IDLCheckers", "Localization", "Performance", "WIN64"}));
		cbCategory.setToolTipText("Existing categories which is used to group like checkers together.");
		cbCategory.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 11));
		cbCategory.setBounds(82, 153, 178, 21);
		form1.getContentPane().add(cbCategory);
	}
	
	/**
	 * initialize label3 for Severity
	 * @return
	 */
	private JLabel initLabel3()
	{
		JLabel label3 = new JLabel("Severity");
		label3.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 11));
		label3.setBounds(12, 185, 45, 13);
		form1.getContentPane().add(label3);
		return label3;
	}
	
	
	/**
	 * initialize comboSeverity JComboBox for Severity
	 * @param label3
	 */
	private void initComboSeverity(JLabel label3)
	{
		label3.setLabelFor(comboSeverity);
		comboSeverity.setBackground(new Color(255, 255, 255));
		comboSeverity.setModel(new DefaultComboBoxModel(new String[] {"Low", "Medium", "High"}));
		comboSeverity.setToolTipText("Severity of the checker's importance to be fixed.");
		comboSeverity.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 11));
		comboSeverity.setBounds(15, 201, 128, 21);
		form1.getContentPane().add(comboSeverity);
	}
	
	
	/**
	 * initialize label6 for Summary
	 * @return
	 */
	private JLabel initLabel6()
	{
		JLabel label6 = new JLabel("Summary");
		label6.setToolTipText("");
		label6.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 11));
		label6.setBackground(SystemColor.control);
		label6.setBounds(294, 9, 50, 13);
		form1.getContentPane().add(label6);
		return label6;
	}
	
	
	/**
	 * initialize txtSummary JTextField for Summary
	 * @param label6
	 */
	private void initTxtSummary(JLabel label6)
	{
		txtSummary = new JTextField();
		txtSummary.setToolTipText("A small single line or less description of what the checker does.");
		label6.setLabelFor(txtSummary);
		txtSummary.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 11));
		txtSummary.setBounds(294, 25, 324, 20);
		form1.getContentPane().add(txtSummary);
		txtSummary.setColumns(10);
	}
	
	
	/**
	 * initialize label5 for Description
	 * @return
	 */
	private JLabel initLabel5()
	{
		JLabel label5 = new JLabel("Description");
		label5.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 11));
		label5.setBackground(SystemColor.control);
		label5.setBounds(294, 59, 60, 13);
		form1.getContentPane().add(label5);
		return label5;
	}
	
	
	/**
	 * initialize txtDescription JTextPanel for Description
	 * @param label5
	 * @return
	 */
	private JTextPane initTxtDescription(JLabel label5)
	{
		final JTextPane txtDescription = new JTextPane();
		label5.setLabelFor(txtDescription);
		txtDescription.setToolTipText("A complete detailed description of what the checker does.");
		txtDescription.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 11));
		txtDescription.setBounds(294, 75, 324, 146);
		form1.getContentPane().add(txtDescription);
		return txtDescription;
	}
	
	
	/**
	 * initialize btnCancel JButton for Cancel
	 * @return
	 */
	private JButton initBtnCancel()
	{
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 11));
		btnCancel.setBackground(SystemColor.control);
		btnCancel.setToolTipText("Cancels creating the \"Create a new SCV checker\" item.");
		btnCancel.setBounds(450, 227, 75, 25);
		form1.getContentPane().add(btnCancel);
		return btnCancel;
	}
	
	
	/**
	 * initialize btnOK JButton for OK
	 * @return
	 */
	private JButton initBtnOk()
	{
		final JButton btnOk = new JButton("OK");
		btnOk.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 11));
		btnOk.setBackground(SystemColor.control);
		btnOk.setToolTipText("Ok to createthe \"Create a new SCV checker\" item.");
		btnOk.setBounds(531, 227, 77, 25);
		form1.getContentPane().add(btnOk);
		return btnOk;
	}
	
	
	/**
	 * initialize labelExtensionErr for Extension error
	 */
	private void initLabelExtensionErr()
	{
		labelExtensionErr.setLabelFor(txtExtension);
		labelExtensionErr.setVisible(false);
		labelExtensionErr.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 11));
		labelExtensionErr.setHorizontalAlignment(SwingConstants.CENTER);
		labelExtensionErr.setIcon(new ImageIcon("C:\\Users\\qalab\\workspace2\\CreateCheckers\\error_icon2_15x15.png"));
		labelExtensionErr.setBounds(261, 25, 20, 25);
		form1.getContentPane().add(labelExtensionErr);
	}
	
	
	/**
	 * initialize labelNameErr for Name error
	 */
	private void initLabelNameErr()
	{
		labelNameErr.setLabelFor(txtName);
		labelNameErr.setVisible(false);
		labelNameErr.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 11));
		labelNameErr.setHorizontalAlignment(SwingConstants.CENTER);
		labelNameErr.setIcon(new ImageIcon("C:\\Users\\qalab\\workspace2\\CreateCheckers\\error_icon2_15x15.png"));
		labelNameErr.setBounds(261, 75, 20, 25);
		form1.getContentPane().add(labelNameErr);
	}
	
	
	/**
	 * initialize labelCategoryErr for Category error
	 */
	private void initLabelCategoryErr()
	{
		labelCategoryErr.setLabelFor(txtCategory);
		labelCategoryErr.setVisible(false);
		labelCategoryErr.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 11));
		labelCategoryErr.setIcon(new ImageIcon("C:\\Users\\qalab\\workspace2\\CreateCheckers\\error_icon2_15x15.png"));
		labelCategoryErr.setHorizontalAlignment(SwingConstants.CENTER);
		labelCategoryErr.setBounds(261, 128, 20, 25);
		form1.getContentPane().add(labelCategoryErr);
	}
	
	
	/**
	 * initialize labelSummaryErr for Summary error
	 */
	private void initLabelSummaryErr()
	{
		labelSummaryErr.setLabelFor(txtSummary);
		labelSummaryErr.setVisible(false);
		labelSummaryErr.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 11));
		labelSummaryErr.setIcon(new ImageIcon("C:\\Users\\qalab\\workspace2\\CreateCheckers\\error_icon2_15x15.png"));
		labelSummaryErr.setHorizontalAlignment(SwingConstants.CENTER);
		labelSummaryErr.setBounds(618, 25, 20, 25);
		form1.getContentPane().add(labelSummaryErr);
	}
	
	
	/**
	 * initialize labelDescriptionErr for Description error
	 * @param txtDescription
	 */
	private void initLabelDescriptionErr(final JTextPane txtDescription)
	{
		labelDescriptionErr.setLabelFor(txtDescription);
		labelDescriptionErr.setVisible(false);
		labelDescriptionErr.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 11));
		labelDescriptionErr.setIcon(new ImageIcon("C:\\Users\\qalab\\workspace2\\CreateCheckers\\error_icon2_15x15.png"));
		labelDescriptionErr.setHorizontalAlignment(SwingConstants.CENTER);
		labelDescriptionErr.setBounds(618, 129, 20, 25);
		form1.getContentPane().add(labelDescriptionErr);
	}
	
	
	/**
	 * Replace the characters in String to regular expression.
	 * @param text
	 * @return
	 */
	protected String replaceString(String text)
	{	
		return text.replace('"', '\"').replace("\\", "\\\\").replace(System.getProperty("line.separator"), "");		
	}
	
	
}
