package no.ntnu.boyebn.master;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.eclipse.emf.ecore.EObject;

import no.hal.learning.exercise.jdt.impl.JdtSourceEditEventImpl;

public class EditCenterOfMassCollector implements DataCollector {
	
	Map<Long, Integer> edits;
	double result;
	
	static long firstEdit = Long.MAX_VALUE;
	static long lastEdit = Long.MIN_VALUE;
	static Map<Long, Integer> globalEdits = new TreeMap<>();
	
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
			globalEdits.put(editEvent.getTimestamp(), editEvent.getSizeMeasure());
			
			firstEdit = Math.min(editEvent.getTimestamp(), firstEdit);
			lastEdit = Math.max(editEvent.getTimestamp(), lastEdit);
		}
	}

	@Override
	public String getName() {
		return "edit_center_of_mass";
	}
	
	@Override
	public void calculateResult() {
		
		int totalEditSize = globalEdits.values().stream().reduce(Integer::sum).get();
		double percent = 0.001;
		
		int firstDay = -1;
		int lastDay = -1;
		
		int totalDays = getDay(lastEdit);
		
		double[] globalDistribution = new double[totalDays+1];
		
		// initialize distribution
		for (int i = 0; i < totalDays+1; i++) {
			globalDistribution[i] = 0;
		}
		
		// create distribution
		for (Entry<Long, Integer> entry : globalEdits.entrySet()) {
			globalDistribution[getDay(entry.getKey())] += entry.getValue();
		}
		
		// find first day
		for (int i = 0; i < totalDays+1; i++) {
			if (globalDistribution[i] >= totalEditSize*percent) {
				firstDay = i;
				break;
			}
		}
		
		// find last day
		for (int i = totalDays; i >= 0; i--) {
			if (globalDistribution[i] >= totalEditSize*percent) {
				lastDay = i;
				break;
			}
		}
		
		Map<Long, Integer> newEdits = new TreeMap<>();
		
		for (Entry<Long, Integer> entry : edits.entrySet()) {
			int day = getDay(entry.getKey());
			if (day >= firstDay && day <= lastDay) {
				newEdits.put(entry.getKey(), entry.getValue());
			}
		}
		
		int newTotalDays = lastDay - firstDay + 1;

		final double[] val = {0.0, 0.0};
		
		for (Entry<Long, Integer> entry : newEdits.entrySet()) {
			val[0] += Math.abs(entry.getValue());
			val[1] += (getDay(entry.getKey())-firstDay)*entry.getValue();
		}

		if (val[0] > 0 && newTotalDays > 0) {
			result = val[1]/(val[0]*newTotalDays);
		} else {
			result = 1.0;
		}
	}

	@Override
	public String getResult() {
		return String.format("%.3f", result);
	}

}
