package no.ntnu.boyebn.master;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import no.hal.learning.exercise.jdt.impl.JdtSourceEditEventImpl;

public class TotalTimeCollector implements DataCollector {
	
	double maxPauseTimeInMillis;
	List<Double> timestamps;
	
	public TotalTimeCollector(double maxPauseTimeInMillis) {
		this.maxPauseTimeInMillis = maxPauseTimeInMillis;
		timestamps = new ArrayList<>();
	}
	
	@Override
	public String getName() {
		return "total_time";
	}
	
	@Override
	public void addEvent(EObject eObject) {
		if (eObject instanceof JdtSourceEditEventImpl) {
			double currTime = ((JdtSourceEditEventImpl) eObject).getTimestamp();
			timestamps.add(currTime);
		}
	}
	
	@Override
	public String getResult() {
		Collections.sort(timestamps);
		
		double prevTime = -1;
		double totalTime = 0;
		
		for (double time : timestamps) {
			if (prevTime != -1) {
				totalTime += Math.min(time - prevTime, maxPauseTimeInMillis);
			}
			
			prevTime = time;
		}
		
		// Convert to seconds
		return Long.toString(Math.round(totalTime/(1000.0)));
	}
	
}
