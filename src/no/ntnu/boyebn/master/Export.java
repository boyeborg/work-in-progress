package no.ntnu.boyebn.master;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;


import no.hal.learning.exercise.ExercisePackage;
import no.hal.learning.exercise.jdt.JdtPackage;
import no.hal.learning.exercise.junit.JunitPackage;
import no.hal.learning.exercise.util.ExerciseResourceFactoryImpl;
import no.hal.learning.exercise.workbench.WorkbenchPackage;
import no.hal.learning.exercise.workspace.WorkspacePackage;

public class Export {
	
	static String EXERCISE_PATH = "data/Øving 5";
	static DataCollector[] collectors = {
			new TotalTimeCollector(10 * 60 * 1000), // 10 minutes
			new CompletionCollector(),
			new DebugRunsCollector(),
			new AfterSuccessWorkCollector(10 * 60 * 1000) // 10 minutes
			};

	public static void main(String[] args) {
		
		// Create a resource set for reading XML files
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(ExercisePackage.eNS_URI, ExercisePackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(JdtPackage.eNS_URI, JdtPackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(JunitPackage.eNS_URI, JunitPackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(WorkspacePackage.eNS_URI, WorkspacePackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(WorkbenchPackage.eNS_URI, WorkbenchPackage.eINSTANCE);
		
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ex", new ExerciseResourceFactoryImpl());
		
		// Create a list to hold the name of the exercises (used to convert the names to numbers)
		List<String> nameOfExercises = new ArrayList<>();
		
		// Create a list to store all the lines that are going to be output to the CSV file 
		List<String> lines = new ArrayList<>();
		
		// Add the header to the CSV file
		String header = "student_id,exercise_number";
		for (DataCollector dataCollector : collectors) {
			header += String.format(",%s", dataCollector.getName());
		}
		lines.add(header);
		
		// Loop through the student folders within the exercise folder
		for (File studentDirectory : new File(EXERCISE_PATH).listFiles(File::isDirectory)) {
			
			// Store the student ID
			String studentId = studentDirectory.getName();
			
			// Find all exercise XML files
			File[] studentExercises = studentDirectory.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File file, String name) {
					return name.endsWith(".ex");
				}
			});
			
			// Loop through all exercise files
			for (File studentExercise : studentExercises) {
				
				// Reset all of the data collectors
				for (DataCollector dataCollector : collectors) {
					dataCollector.reset();
				}
				
				if (!nameOfExercises.contains(studentExercise.getName())) {
					nameOfExercises.add(studentExercise.getName());
				}
				
				// Get the number representing the exercise
				int nameIndex = nameOfExercises.indexOf(studentExercise.getName());
				
				// Get the resource containing the events of the XML file
				Resource resource = resourceSet.getResource(URI.createURI(studentExercise.getAbsolutePath()), true);
				
				// Get the content (events) within the file
				TreeIterator<EObject> it = resource.getAllContents();
				
				// Loop through all events within the file
				while (it.hasNext()) {
					// Get the event
					EObject eObject = it.next();
					
					// Feed the event to all the data collectors
					for (DataCollector dataCollector : collectors) {
						dataCollector.addEvent(eObject);
					}
				}
				
				// Create the new line for the CSV
				String line = String.format("%s,%d", studentId, nameIndex);
				
				// Add the results of all the event to the line
				for (DataCollector dataCollector : collectors) {
					line += String.format(",%s", dataCollector.getResult());
				}
				
				// Add the line to the list
				lines.add(line);
			}
		}
		
		Path csvFile = Paths.get("data/exportedData.csv");
		try {
			Files.write(csvFile, lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
