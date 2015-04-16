import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.*;
import java.io.*;

/**
 * Defines a GUI that displays details of a FitnessProgram object
 * and contains buttons enabling access to the required functionality.
 */
public class SportsCentreGUI extends JFrame implements ActionListener {

	/** GUI JButtons */
	private JButton closeButton, attendanceButton;
	private JButton addButton, deleteButton;

	/** GUI JTextFields */
	private JTextField idIn, classIn, tutorIn;

	/** Display of class timetable */
	private JTextArea display;

	/** Instantiate report frame */
	private ReportFrame report;// = new ReportFrame();	

	/** Names of input text files */
	private final String classesInFile = "ClassesIn.txt";
	private final String classesOutFile = "ClassesOut.txt";
	private final String attendancesFile = "AttendancesIn.txt";

	/** Constants */
	private final int MAX_CLASSES = 7;
	private final int WEEKS = 5;

	/** Instantiate new FitnessProgram for entire class to use.*/
	private FitnessProgram program = new FitnessProgram();

	/**
	 * Constructor for AssEx3GUI class
	 */
	public SportsCentreGUI() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Boyd-Orr Sports Centre");
		setSize(700, 300);
		display = new JTextArea();
		display.setEditable(false);

		display.setFont(new Font("Courier", Font.PLAIN, 14));
		add(display, BorderLayout.CENTER);
		layoutTop();
		layoutBottom();
		
		// Read in initial file input
		initLadiesDay();
		initAttendances();
		// Call these methods to initially populate both GUI's
		updateDisplay();
		displayReport();

	}

	/**
	 * Creates the FitnessProgram list ordered by start time
	 * using data from the file ClassesIn.txt
	 */
	public void initLadiesDay() {

		try{
			String line = "";
			FileReader reader = new FileReader(classesInFile);
			Scanner in = new Scanner(reader);

			while(in.hasNextLine()) {
				// Passes entire line to FitnessProgram where it will be parsed.
				line = in.nextLine() + "\n";	
				program.setLine(line);
			}
			reader.close();
			in.close();
		}
		catch(IOException e) {
			System.err.println("File not found");
		}

	}

	/**
	 * Initialises the attendances using data
	 * from the file AttendancesIn.txt
	 */
	public void initAttendances() {

		try{
			String line = "";
			FileReader reader = new FileReader(attendancesFile);
			Scanner in = new Scanner(reader);
			int [] attendances = new int[WEEKS];	

			while(in.hasNextLine()) {

				line = in.nextLine() + "\n";
				// Separate classID from line before using split
				String classID = line.substring(0,3);
				line = line.substring(3).trim();

				// Split each attendance value.
				String [] splitLine = line.split("[ ,.;:]+") ; //

				// Instantiate new attendance array
				attendances = new int[WEEKS];	
				for (int i=0; i<WEEKS; i++) {

					//Convert from string to int
					attendances[i] = Integer.parseInt(splitLine[i]);

				}
				// Pass array to FitnessProgram
				program.setAttendances(classID, attendances);
			}
			reader.close();
			in.close();
		}
		catch(IOException e) {
			System.err.println("File not found");
		}
	}

	/**
	 * Builds JTextArea display.
	 * Shares an index 'i' with get methods in FitnessProgram
	 * Passes boolean to get methods to indicate that method is being called by updateDisplay() and not ReportFrame.
	 */
	public void updateDisplay() {
		
		// Time headers are hard coded, as they never change
		String [] displayHeaders = {"9-10","10-11","11-12","12-13","13-14","14-15","15-16"};
		
		// Clears field everytime method is called, i.e. updates display.
		display.setText(" ");

		// Set initial time headers		
		for(int i = 0; i<MAX_CLASSES; i++) {
			display.append(String.format("  %-10s"
					, displayHeaders[i]));
		}
		display.append("\n");

		// Populate with class names from array
		for(int i = 0; i<MAX_CLASSES; i++) { 
			display.append(String.format("  %-10s", program.getClassName(i, true)));
		}
		display.append("\n");

		// Populate with tutor names from array
		for(int i = 0; i<MAX_CLASSES; i++) { 
			display.append(String.format("  %-10s", program.getTutorName(i, true)));						
		}

	}

	/**
	 * adds buttons to top of GUI
	 */
	public void layoutTop() {
		JPanel top = new JPanel();
		closeButton = new JButton("Save and Exit");
		closeButton.addActionListener(this);
		top.add(closeButton);
		attendanceButton = new JButton("View Attendances");
		attendanceButton.addActionListener(this);
		top.add(attendanceButton);
		add(top, BorderLayout.NORTH);
	}

	/**
	 * adds labels, text fields and buttons to bottom of GUI
	 */
	public void layoutBottom() {
		// instantiate panel for bottom of display
		JPanel bottom = new JPanel(new GridLayout(3, 3));

		// add upper label, text field and button
		JLabel idLabel = new JLabel("Enter Class Id");
		bottom.add(idLabel);
		idIn = new JTextField();
		bottom.add(idIn);
		JPanel panel1 = new JPanel();
		addButton = new JButton("Add");
		addButton.addActionListener(this);
		panel1.add(addButton);
		bottom.add(panel1);

		// add middle label, text field and button
		JLabel nmeLabel = new JLabel("Enter Class Name");
		bottom.add(nmeLabel);
		classIn = new JTextField();
		bottom.add(classIn);
		JPanel panel2 = new JPanel();
		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(this);
		panel2.add(deleteButton);
		bottom.add(panel2);

		// add lower label text field and button
		JLabel tutLabel = new JLabel("Enter Tutor Name");
		bottom.add(tutLabel);
		tutorIn = new JTextField();
		bottom.add(tutorIn);

		add(bottom, BorderLayout.SOUTH);
	}

	/**
	 * Processes adding a class
	 * Conditional statement checks for empty values and additionally checks if there are available slots via...
	 * ... FitnessProgram.sFull() method
	 */
	public void processAdding() {

		if(idIn.getText().isEmpty() || classIn.getText().isEmpty() || tutorIn.getText().isEmpty()){
			JOptionPane.showMessageDialog
			(null, "Fields cannot be empty", "Warning", JOptionPane.ERROR_MESSAGE);
		}
		else if(program.nullCounter() == 0) { // if null counter returns 0, there are no available positions in array
			JOptionPane.showMessageDialog
			(null, "No time slots are available. Delete one to make room.", "Warning", JOptionPane.ERROR_MESSAGE);			
		}
		else if(program.duplicateID(idIn.getText())){	//duplicateID tests array for repeated ID's
			JOptionPane.showMessageDialog
			(null, "The ID you have entered already belongs to another class. Try another.", "Warning", JOptionPane.ERROR_MESSAGE);			
			idIn.setText("");
		}
		else { // if everything passes, send textField values to FitnessProgram
			program.addClass(idIn.getText().toLowerCase(), classIn.getText().toLowerCase(), tutorIn.getText().toLowerCase());
			idIn.setText("");
			classIn.setText("");
			tutorIn.setText("");
		}

	}

	/**
	 * Processes deleting a class
	 * Empty fields are dealt with in this method however, non matches are handled by FitnessProgram.deleteClass
	 */
	public void processDeletion() {

		// If any field is empty, display warning message.
		if(idIn.getText().isEmpty() || classIn.getText().isEmpty() || tutorIn.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Fields cannot be empty. Please Enter a value", "Warning", JOptionPane.ERROR_MESSAGE);
		}	
		// If FitnessProgram.deleteClass method returns false, no match has been found, display warning message
		else if(!program.deleteClass(idIn.getText(), classIn.getText(), tutorIn.getText())) {
			JOptionPane.showMessageDialog(null, "No match was found", "Warning", JOptionPane.ERROR_MESSAGE);
		}
		// else all validation has passed, run deleteClass.
		else
		{
			program.deleteClass(idIn.getText().toLowerCase(), classIn.getText().toLowerCase(), tutorIn.getText().toLowerCase());
			idIn.setText("");
			classIn.setText("");
			tutorIn.setText("");
		}
	}
	
	/**
	 * Instantiates a new window and displays the attendance report
	 */
	public void displayReport() {
	
	report = new ReportFrame();	
	report.buildReport(program);

	}

	/**
	 * Writes lines to file representing class name, 
	 * tutor and start time and then exits from the program
	 */
	public void processSaveAndClose() { 

		// fileReport is built in ReportFrame Class
		String fileReport = report.buildReport(program);
		try{
			PrintWriter save = new PrintWriter(classesOutFile);
			//Print to file, using fileReport string as parameter
			save.println(fileReport);
			save.close();
		}

		catch(IOException e){
			System.err.println("File Not Found");
		}
	}
	/**
	 * 
	 * Process button clicks.
	 * @param ae the ActionEvent
	 */
	public void actionPerformed(ActionEvent ae) {

		if(ae.getSource() == addButton) {

			processAdding();
			// update display and report everytime button is clicked
			updateDisplay();
			displayReport();

		}
		else if (ae.getSource() == deleteButton) {

			processDeletion();
			// update display and report everytime button is clicked
			updateDisplay();
			displayReport();
		}
		else if(ae.getSource() == attendanceButton) {
		
			report.setVisible(true);
			displayReport();

		}
		else if(ae.getSource() == closeButton){

			processSaveAndClose();
			System.exit(0);

		}

	}

}
