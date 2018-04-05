package no.ntnu.boyebn.master;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import no.hal.learning.exercise.jdt.impl.JdtSourceEditEventImpl;

public class TotalTimeCollector implements DataCollector {
	
	static double L2norm = 0;
	double maxPauseTimeInMillis;
	List<Double> timestamps;
	long result;
	
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
	public void calculateResult() {
		Collections.sort(timestamps);
		
		double prevTime = -1;
		double totalTime = 0;
		
		for (double time : timestamps) {
			if (prevTime != -1) {
				totalTime += Math.min(time - prevTime, maxPauseTimeInMillis);
			}
			
			prevTime = time;
		}
		
		result = Math.round(totalTime/(1000.0));
		
		L2norm += Math.pow(result, 2);
	}
	
	@Override
	public String getResult() {
		
		double normalizedResult = result/Math.sqrt(L2norm);
		
		return String.format("%.3f", normalizedResult);
	}
	
	
	
}
