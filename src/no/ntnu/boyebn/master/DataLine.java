package no.ntnu.boyebn.master;

import org.eclipse.emf.ecore.EObject;

public class DataLine {
	
	String studentId;
	int exerciseNumber;
	DataCollector[] collectors;
	
	public DataLine(String studentId, int exerciseNumber, DataCollectorFactory.Collector[] collectors) {
		this.studentId = studentId;
		this.exerciseNumber = exerciseNumber;
		this.collectors = new DataCollector[collectors.length];
		
		for (int i = 0; i < collectors.length; i++) {
			this.collectors[i] = DataCollectorFactory.getCollector(collectors[i]);
		}
	}
	
	public void addEvent(EObject eObject) {
		for (DataCollector collector : collectors) {
			collector.addEvent(eObject);
		}
	}
	
	public void calculateResult() {
		for (DataCollector collector : collectors) {
			collector.calculateResult();
		}
	}
	
	public String[] getResults() {
		String[] results = new String[collectors.length+2];
		
		results[0] = studentId;
		results[1] = Integer.toString(exerciseNumber);
		
		for (int i = 0; i < collectors.length; i++) {
			results[i+2] = collectors[i].getResult();
		}
		
		return results;
	}
	
	public String[] getNames() {
		String[] names = new String[collectors.length+2];
		
		names[0] = "student_id";
		names[1] = "exercise_number";
		
		for (int i = 0; i < collectors.length; i++) {
			names[i+2] = collectors[i].getName();
		}
		
		return names;
	}
	
	@Override
	public String toString() {
		return String.join(",", getResults());
	}

}
