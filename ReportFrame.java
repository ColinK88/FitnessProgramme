import java.awt.*;

import javax.swing.*;

/**
 * Class to define window in which attendance report is displayed.
 */
public class ReportFrame extends JFrame {

	private JTextArea reportDisplay;
	
	private final int MAX_CLASSES = 7;

	public ReportFrame(){
	
	/** Set intial attributes of ReportFrame */
	this.setSize(900, 400);
	this.setResizable(false);	
	this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	this.setLocation(300, 300);
	this.setTitle("Attendance Report");

	/** Instaniate JTextArea object */
	reportDisplay = new JTextArea(); 
	reportDisplay.setEditable(false);
	
	reportDisplay.setFont(new Font("Courier", Font.PLAIN, 14));
	
	this.add(reportDisplay);

	}
	/**
	 * Builds display. Calls get methods in FitnessProgram.
	 * This method shares an index 'i' with the get methods
	 * It passes 'false' indicating that the method is being called from this class.
	 * @param program - current program object
	 * @return fileReport formatted string, to be written to text file in GUI
	 */
	public String buildReport(FitnessProgram program){

		// Headers and various string componenets for report.
		String id = "ID";
		String Class = "Class";
		String tutor = "Tutor";
		String attendancesHeader = "Attendances";
		String avgAttendance = "Average Attendance";
		String overall = "The overall average attendance is: ";

		reportDisplay.setText("");

		reportDisplay.append(String.format("   "
				+ "%-10s %-20s %-20s %-20s %-20s %n", id, Class, tutor, attendancesHeader, avgAttendance));

		// Call sort method to change array order to ascending average attendance.
		program.orderArray();
		
		// Build header.
		for(int i = 0; i<10; i++) { // 10 is arbitrary and simply fills the display with  a line of '=' characters.
			reportDisplay.append("===========");
		}
		reportDisplay.append("\n");

		// loop through for amount of non-null entries in array
		for(int i = 0; i<MAX_CLASSES - program.nullCounter(); i++){
			
			// get attendances
			int [] attendances = program.getAttendances(i, false);

			// Convert into one contiguous string before adding to display
			String sAtt = "" +attendances[0] +" " +attendances[1] +" " 
					+attendances[2] +" " +attendances[3] +" " +attendances[4];

			// Build display...
				reportDisplay.append(String.format("   %-10s %-20s %-20s %-25s %-20.2f %n", 
						program.getCourseID(i, false), program.getClassName(i, false), 
						program.getTutorName(i, false), sAtt, program.getAverage(i, false)));
			
		}

		// Display overall average
		reportDisplay.append(String.format("%n%s %.2f" ,overall, program.getOverallAverage()));
		
		// Create string variable of reportDisplay
		String fileReport = reportDisplay.getText().toString();
		
		return fileReport; // This string is used by GUI to write report to .txt file.
		
	}
}
