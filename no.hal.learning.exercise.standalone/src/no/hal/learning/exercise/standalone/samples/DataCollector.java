package no.hal.learning.exercise.standalone.samples;

import org.eclipse.emf.ecore.EObject;

public interface DataCollector {
	
	public void addEvent(EObject eObject);
	public String getName();
	public String getResult();
	public void reset();

}
