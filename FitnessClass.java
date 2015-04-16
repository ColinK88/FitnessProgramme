/** Defines an object representing a single fitness class
 */
public class FitnessClass implements Comparable<FitnessClass> {

	/** Instance Variables */
	private String classID;
	private String className;
	private String tutorName;
	private int startTime;
	private int [] attendances;
	private double averageAttendance;

	public FitnessClass(String id, String classN, String tutor, int time) {

		this.classID = id;
		this.className = classN;
		this.tutorName = tutor;
		this.startTime = time;

	}

	public void setAttendances(int [] attend) {		
		attendances = attend;
	}

	/** modified compareTo method, orders FitnessClasses in order of average attendance*/
	public int compareTo(FitnessClass other) { // compareTo is part of comparable interface

		if(this.getAverage() < other.getAverage()){   		
			return 1;  		
		}
		else if(this.getAverage() > other.getAverage()){    		
			return -1;  		
		}
		else if(this.getAverage() > other.getAverage()){   		
			return 0;   		
		}
		return 0; 

	}

	/** Accessor methods used by FitnessProgram */
	public String getCourseID() {return classID; }
	public String getClassName() {return className; }
	public String getTutorName() {return tutorName; }  
	public int getTime() {return startTime; }
	public int [] getAttendances() { return attendances; }

	/** @return average attendance */
	public double getAverage() {
	
		double sum = 0;
		for (int i = 0; i<attendances.length;i++){

			sum += attendances[i];
		}

		averageAttendance = (sum/attendances.length);

		return averageAttendance;
	}

}
