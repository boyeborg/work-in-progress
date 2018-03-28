package no.ntnu.boyebn.master;

import java.util.Map;
import java.util.TreeMap;

import org.eclipse.emf.ecore.EObject;

import no.hal.learning.exercise.jdt.impl.JdtSourceEditEventImpl;

public class EditCenterOfMassCollector implements DataCollector {
	
	Map<Long, Integer> edits;
	
	public EditCenterOfMassCollector() {
		edits = new TreeMap<>();
	}

	@Override
	public void addEvent(EObject eObject) {
		if (eObject instanceof JdtSourceEditEventImpl) {
			JdtSourceEditEventImpl editEvent = (JdtSourceEditEventImpl) eObject;
			edits.put(editEvent.getTimestamp(), editEvent.getSizeMeasure());
		}
	}

	@Override
	public String getName() {
		return "edit_center_of_mass";
	}

	@Override
	public String getResult() {
		
		final double res[] = {0, 0};
		
		edits.forEach((k, v) -> {
			res[0] += k*v;
			res[1] += v;
		});
		
		return Long.toString(Math.round(res[0]/res[1]));
	}

}
