package no.ntnu.boyebn.master;

import org.eclipse.emf.ecore.EObject;

import no.hal.learning.exercise.jdt.impl.JdtSourceEditEventImpl;

public class WarningCountCollector implements DataCollector {
	
	long latestEditTimestamp;
	int warningCount;
	
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
	public String getResult() {
		if (warningCount > 0) {
			return Integer.toString(warningCount);
		}
		
		return "0";
	}

}
