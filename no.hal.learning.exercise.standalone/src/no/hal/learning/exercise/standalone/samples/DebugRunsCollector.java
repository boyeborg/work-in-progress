package no.hal.learning.exercise.standalone.samples;

import org.eclipse.emf.ecore.EObject;

import no.hal.learning.exercise.jdt.impl.JdtLaunchEventImpl;

public class DebugRunsCollector implements DataCollector {
	
	int numDebugRuns;
	
	public DebugRunsCollector() {
		reset();
	}

	@Override
	public void addEvent(EObject eObject) {
		if (eObject instanceof JdtLaunchEventImpl) {
			if (((JdtLaunchEventImpl) eObject).getMode().equals("debug")) {
				numDebugRuns++;
			}
		}
	}

	@Override
	public String getName() {
		return "num_debug_runs";
	}

	@Override
	public String getResult() {
		return Integer.toString(numDebugRuns);
	}

	@Override
	public void reset() {
		numDebugRuns = 0;
	}

}
