package no.ntnu.boyebn.master;

import org.eclipse.emf.ecore.EObject;

import no.hal.learning.exercise.jdt.impl.JdtSourceEditEventImpl;

public class WarningCountCollector implements DataCollector {
	
	long latestEditTimestamp;
	int warningCount;
	
	static double L2norm = 0;
	
	public WarningCountCollector() {
		latestEditTimestamp = 0;
		warningCount = 0;
	}

	@Override
	public void addEvent(EObject eObject) {
		if (eObject instanceof JdtSourceEditEventImpl) {
			JdtSourceEditEventImpl editEvent = (JdtSourceEditEventImpl) eObject;
			if (editEvent.getTimestamp() > latestEditTimestamp) {
				latestEditTimestamp = editEvent.getTimestamp();
				warningCount = editEvent.getWarningCount();
			}
		}
	}

	@Override
	public String getName() {
		return "warnings";
	}
	
	@Override
	public void calculateResult() {
		L2norm += Math.pow(warningCount, 2);
	}

	@Override
	public String getResult() {
		double normalizedResult = Math.abs(warningCount)/Math.sqrt(L2norm);
		
		return String.format("%.3f", normalizedResult);
	}

}
