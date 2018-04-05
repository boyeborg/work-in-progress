package no.ntnu.boyebn.master;

import org.eclipse.emf.ecore.EObject;

import no.hal.learning.exercise.jdt.impl.JdtLaunchEventImpl;

public class DebuggerUsedCollector implements DataCollector {
	
	int debuggerUsed;
	
	public DebuggerUsedCollector() {
		debuggerUsed = 0;
	}

	@Override
	public void addEvent(EObject eObject) {
		if (eObject instanceof JdtLaunchEventImpl) {
			if (((JdtLaunchEventImpl) eObject).getMode().equals("debug")) {
				debuggerUsed = 1;
			}
		}
	}

	@Override
	public String getName() {
		return "debugger_used";
	}
	
	@Override
	public void calculateResult() {}

	@Override
	public String getResult() {
		return Integer.toString(debuggerUsed);
	}

}
