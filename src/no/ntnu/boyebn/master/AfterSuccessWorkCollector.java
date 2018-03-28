package no.ntnu.boyebn.master;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;

import no.hal.learning.exercise.jdt.impl.JdtSourceEditEventImpl;
import no.hal.learning.exercise.junit.impl.JunitTestEventImpl;

public class AfterSuccessWorkCollector implements DataCollector {
	
	double maxPauseTimeInMillis;
	boolean isCompleted;
	double completedTimestamp;
	List<Double> sourceEditTimestamps;
	
	public AfterSuccessWorkCollector(double maxPauseTimeInMillis) {
		this.maxPauseTimeInMillis = maxPauseTimeInMillis;
		isCompleted = false;
		sourceEditTimestamps = new ArrayList<>();
		completedTimestamp = Double.MAX_VALUE;
	}

	@Override
	public void addEvent(EObject eObject) {
		if (eObject instanceof JdtSourceEditEventImpl) {
			double currTime = ((JdtSourceEditEventImpl) eObject).getTimestamp();
			sourceEditTimestamps.add(currTime);
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
		
		List<Double> editsAfterCompletion = sourceEditTimestamps.stream()
				.filter(timestamp -> (timestamp > completedTimestamp))
				.sorted()
				.collect(Collectors.toList());
		
		
		double prevTime = -1;
		double totalTime = 0;
		
		for (double time : editsAfterCompletion) {
			if (prevTime != -1) {
				totalTime += Math.min(time - prevTime, maxPauseTimeInMillis);
			}
			
			prevTime = time;
		}
		
		return Long.toString(Math.round(totalTime/(1000.0)));
	}

}
