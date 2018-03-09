package no.ntnu.boyebn.master;

import org.eclipse.emf.ecore.EObject;

import no.hal.learning.exercise.junit.impl.JunitTestProposalImpl;

public class CompletionCollector implements DataCollector {
	
	double completion = 0.0;

	@Override
	public void addEvent(EObject eObject) {
		if (eObject instanceof JunitTestProposalImpl) {
			completion = Math.max(((JunitTestProposalImpl) eObject).getCompletion(), completion);
		}
	}

	@Override
	public String getName() {
		return "completion";
	}

	@Override
	public String getResult() {
		return Double.toString(completion);
	}

	@Override
	public void reset() {
		completion = 0.0;
	}

}
