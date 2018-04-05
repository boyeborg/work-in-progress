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
	int result;
	
	static double L2norm = 0;
	
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
	public void calculateResult() {
		if (!isCompleted) {
			result = 0;
			return;
		}
		
		Long startKey = edits.higherKey(completedTimestamp);
		
		if (startKey == null) {
			result = 0;
			return;
		}
		
		try {
			result = edits.tailMap(startKey, true).values().stream().reduce(Integer::sum).get();
			L2norm += Math.pow(result, 2);
		} catch (NoSuchElementException e) {
			result = 0;
		}
	}

	@Override
	public String getResult() {
		double normalizedResult = result/Math.sqrt(L2norm);
		
		return String.format("%.3f", normalizedResult);
	}

}
