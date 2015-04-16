import java.lang.Comparable;
import java.util.Arrays;

/**
 * Times - 
 * 9 -10
 * "" "" ""
 * 15- 16
 *  
 */ 


/**
 * Maintains a list of Fitness Class objects
 * The list is initialised in order of start time
 * The methods allow objects to be added and deleted from the list
 * In addition an array can be returned in order of average attendance
 */
public class FitnessProgram {

	private final int MAX_CLASSES = 7;
	private final int WEEKS = 5; // Attendance is monitored over 5 week period
	private final int EARLIEST_TIME = 9; // Earliest start time for any class

	/** Array instance variables */
	private FitnessClass [] aryFitness = new FitnessClass [MAX_CLASSES];
	private FitnessClass [] aryNonNull; // non null array, used for Arrays.sort

	/** Default Constructor */
	public FitnessProgram () { 

	}

	/** 
	 * Set method to handle line input from initial ClassIN.txt input file
	 * 
	 * @param line to be parsed
	 */
	public void setLine(String line) {

		/** Perform string parsing on line to extract individual components and store as string variables */
		String [] splitLine = line.split("[ ,.;:]+") ;

		/** Parse time into int */
		String sTime = splitLine[3].trim();	
		int time = Integer.parseInt(sTime);

		/** Instantiate new Fitness Class object and supply variables for current iteration of setLine() */
		FitnessClass fitness = new FitnessClass(splitLine[0], splitLine[1], splitLine[2], time);	

		/** Order each object by time */
		aryFitness[fitness.getTime() - EARLIEST_TIME] = fitness;
	}

	/** Set method to handle line input from initial attendance file
	 * Parsing of lines are handled in GUI as this method accepts an Array. 
	 * Each array is tested to match FitnessClass classID before being passed on
	 * @param classID 
	 * @param sAttendances String attendance array, to be converted to int
	 */
	public void setAttendances(String classID, int [] attendances) {

		for (int i = 0; i<MAX_CLASSES;i++) {
			try{				
				if(aryFitness[i].getCourseID().equals(classID)){					
					aryFitness[i].setAttendances(attendances);				
				}		
			}
			catch(NullPointerException e) {		}
		}
	}

	/**
	 * Creates array of FitnessClass objects and excludes all null entries
	 * Array is then sorted by compareTo() in ascending order.
	 */
	public void orderArray(){
		// Instantiate array to size of the sum of non-null entries
		aryNonNull = new FitnessClass[MAX_CLASSES - this.nullCounter()];

		int j = 0; // index for non-null array
		for(int i =0; i<MAX_CLASSES;i++){

			try{
				if (aryFitness[i] == null){
					// Do nothing
				}
				else{ // aryFitness[i] contains non-null, copy object to aryNonNull
					aryNonNull[j] = aryFitness[i];
					j++; // increment counter for non null array
				}
			} catch (NullPointerException e) {
				// Handle nulls but do nothing with them. 
			}
		}
		Arrays.sort(aryNonNull);
	}

	/** 
	 * Input validation for duplicate ID's is handled here, outwith addClass..
	 * Checks for duplicate class ID's for input validation
	 * @param id
	 */
	public boolean duplicateID(String id) {

		boolean duplicateID = false;

		for (int i = 0; i<MAX_CLASSES;i++) {			
			try {				
				if(aryFitness[i].getCourseID().equals(id)){			
					duplicateID = true;	
				}				
			} catch(NullPointerException e)	{	}
		}
		return duplicateID;
	}

	/**
	 * This method sets a new FitnessClass object and adds to array
	 * It checks for available positions in array by finding a null and doing all work in catch
	 * Try/Catch, in this case is effectively used as an If/else that only deals with null pointer exceptions
	 * @param id
	 * @param name
	 * @param tutor
	 */
	public void addClass(String classID, String className, String tutorName){ 

		// Set new class attendances to 0;
		int [] attendances = { 0, 0, 0, 0, 0};
		
		
		for (int i = 0; i<MAX_CLASSES;i++){
			try {
				aryFitness[i].getCourseID(); // call method to check for null value
				
			} catch(NullPointerException e) { // if null is found, do work in catch. 
				int time = i + EARLIEST_TIME; // time is position in array + 9
				FitnessClass fitness = new FitnessClass(classID, className, tutorName, time);
				fitness.setAttendances(attendances); // Send default attendances
				aryFitness[i] = fitness; // store new object in array
				
				return; // jump out of loop to prevent next null being given same FitnessClass object
			}
		}
		

	}

	/**
	 * Deals with deletion functionality as well as ensuring valid input
	 * @param id 
	 * @param name
	 * @param tutor
	 * @return match - boolean for input validation. returns false if any parameter value doesn't match entry in array
	 */
	public boolean deleteClass(String id, String name, String tutor){ 

		boolean match = true;
		for (int i = 0; i<MAX_CLASSES;i++) {
			try{

				// match will only pass if courseID matches value stored in FitnessClass array.
				match = (			
						id.equals(aryFitness[i].getCourseID()));
	
				// If match is found, delete specified Fitness Class and jump out of loop
				if(match) {
					aryFitness[i] = null;
					i=MAX_CLASSES;
				}
				// If no match is found initially, do continue on to next iteration in loop
				else if(!match){										
				}
				// If match is still false at end of loop, return false(no match has been found)
				else if (i==MAX_CLASSES && !match){					
					return match;					
				}				
			}
			catch(NullPointerException e) {		}
		}
		return match; // default return value is true and assumes match was found

	}

	/** 
	 * Keeps track of null values in array
	 * Used to determine size of aryNonNull[] which is used for array sorting
	 * @return sum of null FitnessClass objects in array
	 */
	public int nullCounter(){
		int nullCount = 0;

		for (int i = 0; i<MAX_CLASSES;i++) {			
			try{
				aryFitness[i].getCourseID();
			} catch(NullPointerException e) {
				nullCount++;
				System.out.println(nullCount);
			}
		}	

		return nullCount;
	}

	/**
	 *  - Accessor for GUI JTextArea Display 
	 * - Catches null values in array with Try/Catch and handles by setting String to 'Available'
	 * - Uses an index to loop through FitnessClass array.
	 * @param i index from GUI
	 * @param display - boolean to determine which GUI is calling accessor method.
	 * @return className
	 */
	public String getClassName(int i, boolean display) { //Shares index with SportsCentreGUI.updateDisplay()

		String className = "";
		try {
			// if boolean passedis true, method is being called by SportsCentreGUI, call AryFitness(which contains nulls)
			if(display){
				className = aryFitness[i].getClassName();
			}
			// else method has been called by ReportFrame, call non null array. 
			else{
				className = aryNonNull[i].getClassName();	
			}
		}
		catch (NullPointerException e) { // if this is found to be null, pass the string as "available"
			className = "Available"; 		
		}
		
		return className;
	}

	/** 
	 *  Same as above Accessor
	 *  @return Tutor Name
	 */
	public String getTutorName(int i, boolean display) { //Shares index with SportsCentreGUI.updateDisplay()

		String tutor = "";
		try {		
			if(display){// if boolean passedis true, method is being called by SportsCentreGUI, call AryFitness(which contains nulls)
				tutor = aryFitness[i].getTutorName();
			}
			else{ // else method has been called by ReportFrame, call non null array. 
				tutor = aryNonNull[i].getTutorName();
			}
		}
		catch (NullPointerException e) { // if this is null, pass tutor as " "
			tutor = " ";
		}
	
		return tutor;
	}

	/** 
	 *  Same as above Accessor
	 *  @return Course ID
	 */
	public String getCourseID(int i, boolean display) { //Shares index with SportsCentreGUI.updateDisplay()

		String courseID = "";
		try {
			
			if(display){// if boolean passedis true, method is being called by SportsCentreGUI, call AryFitness(which contains nulls)
				courseID = aryFitness[i].getCourseID(); 
			}
			else{ // else method has been called by ReportFrame, call non null array. 
				courseID = aryNonNull[i].getCourseID();
			}
		}
		catch (NullPointerException e) {
			courseID = " ";
		}

		return courseID;
	}

	/** 
	 *  Same as above Accessor
	 *  @return attendance array
	 */
	public int []getAttendances(int i, boolean display){
		int [] attendance = new int[WEEKS];
		try {		
			if(display){ // if boolean passedis true, method is being called by SportsCentreGUI, call AryFitness(which contains nulls)
				attendance = aryFitness[i].getAttendances();
			}
			else{ // else method has been called by ReportFrame, call non null array. 
				attendance = aryNonNull[i].getAttendances();
			}

		} catch (NullPointerException e) {	}
		return attendance;
	}

	/** 
	 *  Same as above Accessor
	 *  @return Average
	 */
	public double getAverage(int i, boolean display){

		double average = 0.0;
		try{			
			if(display){ // if boolean passed is true, method is being called by SportsCentreGUI, call AryFitness(which contains nulls)
				average = aryFitness[i].getAverage(); 

			}
			else{ // else method has been called by ReportFrame, call non null array. 
				average = aryNonNull[i].getAverage();	
			}

		} catch (NullPointerException e) {	}
		return average;

	}

	/** 
	 *  Finds the overall attendance of all non-null fitness class objects
	 *  @return Overall Average
	 */
	public double getOverallAverage(){

		double overallAverage = 0.0;
		double sum = 0.0;
		for(int i = 0;i<MAX_CLASSES;i++){
			// Method isnt concerned with null pointers at this point as this doesn't affect sum variable
			try{
				sum += aryFitness[i].getAverage();
			}catch(NullPointerException e){

			}
		}

		// Find average, subtract non-null values
		// Could also just use aryNonNull[] and skip the subtraction.
		overallAverage = sum /(aryFitness.length - this.nullCounter()); 

		return overallAverage;
	}




}


