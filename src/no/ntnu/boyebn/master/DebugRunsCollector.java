package no.ntnu.boyebn.master;

import org.eclipse.emf.ecore.EObject;

import no.hal.learning.exercise.jdt.impl.JdtLaunchEventImpl;

public class DebugRunsCollector implements DataCollector {
	
	int numDebugRuns;
	
	static double L2norm = 0;
	
	public DebugRunsCollector() {
		numDebugRuns = 0;
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
	public void calculateResult() {
		L2norm += Math.pow(numDebugRuns, 2);
	}
	
	@Override
	public String getResult() {
		double normalizedResult = numDebugRuns/Math.sqrt(L2norm);
		
		return String.format("%.3f", normalizedResult);
	}

}
