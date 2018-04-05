package no.ntnu.boyebn.master;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.eclipse.emf.ecore.EObject;

import no.hal.learning.exercise.jdt.impl.JdtSourceEditEventImpl;

public class EditCenterOfMassCollector implements DataCollector {
	
	Map<Long, Integer> edits;
	
	static long firstEdit = Long.MAX_VALUE;
	static long lastEdit = Long.MIN_VALUE;
	
	public EditCenterOfMassCollector() {
		edits = new TreeMap<>();
	}
	
	private int getDay(long timestamp) {
		return (int) TimeUnit.DAYS.convert(timestamp - firstEdit, TimeUnit.MILLISECONDS);
	}

	@Override
	public void addEvent(EObject eObject) {
		if (eObject instanceof JdtSourceEditEventImpl) {
			JdtSourceEditEventImpl editEvent = (JdtSourceEditEventImpl) eObject;
			edits.put(editEvent.getTimestamp(), editEvent.getSizeMeasure());
			
			firstEdit = Math.min(editEvent.getTimestamp(), firstEdit);
			lastEdit = Math.max(editEvent.getTimestamp(), lastEdit);
		}
	}

	@Override
	public String getName() {
		return "edit_center_of_mass";
	}

	@Override
	public String getResult() {
		
		int totalDays = getDay(lastEdit);
		final double[] val = {0.0, 0.0};
		
		edits.forEach((timestamp, size) -> {
			val[0] += Math.abs(size);
			val[1] += getDay(timestamp)*size;
		});
		
		try {
			Double result = val[1]/(val[0]*totalDays);
			if (result.equals(Double.NaN)) {
				return "1.0";
			}
			return Double.toString(result);
		} catch (ArithmeticException e) {
			return "1.0";
		}
		
	}

}
