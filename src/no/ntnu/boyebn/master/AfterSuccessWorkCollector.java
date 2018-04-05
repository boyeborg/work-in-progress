package no.ntnu.boyebn.master;

import java.util.NoSuchElementException;
import java.util.TreeMap;

import org.eclipse.emf.ecore.EObject;

import no.hal.learning.exercise.jdt.impl.JdtSourceEditEventImpl;
import no.hal.learning.exercise.junit.impl.JunitTestEventImpl;

public class AfterSuccessWorkCollector implements DataCollector {
	
	boolean isCompleted;
	long completedTimestamp;
	TreeMap<Long, Integer> edits;
	
	public AfterSuccessWorkCollector() {
		isCompleted = false;
		completedTimestamp = Long.MAX_VALUE;
		edits = new TreeMap<>();
	}

	@Override
	public void addEvent(EObject eObject) {
		if (eObject instanceof JdtSourceEditEventImpl) {
			JdtSourceEditEventImpl editEvent = (JdtSourceEditEventImpl) eObject;
			edits.put(editEvent.getTimestamp(), Math.abs(editEvent.getSizeMeasure()));
		} else if (eObject instanceof JunitTestEventImpl) {
			JunitTestEventImpl testEvent = (JunitTestEventImpl) eObject;
			if (testEvent.getCompletion() == 1.0) {
				isCompleted = true;
				completedTimestamp = Math.min(completedTimestamp, testEvent.getTimestamp());
			}
		}
	}

	@Override
	public String getName() {
		return "work_after_completion";
	}

	@Override
	public String getResult() {
		if (!isCompleted) {
			return "0";
		}
		
		Long startKey = edits.higherKey(completedTimestamp);
		
		if (startKey == null) {
			return "0";
		}
		
		//edits.lowerEntry(completedTimestamp).getValue()
		
		try {
			return Integer.toString(edits.tailMap(startKey, true).values().stream().reduce(Integer::sum).get());			
		} catch (NoSuchElementException e) {
			return "0";
		}
	}

}
