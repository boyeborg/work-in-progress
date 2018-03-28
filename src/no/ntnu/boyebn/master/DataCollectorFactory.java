package no.ntnu.boyebn.master;

public class DataCollectorFactory {
	
	public static enum Collector {
		AfterSuccessWork,
		Completion,
		DebugRuns,
		EditCenterOfMass,
		TotalTime
	}
	
	public static DataCollector getCollector(Collector collector) {
		switch (collector) {
		case AfterSuccessWork:
			return new AfterSuccessWorkCollector(10 * 60 * 1000); // 10 minutes
		case Completion:
			return new CompletionCollector();
		case DebugRuns:
			return new DebugRunsCollector();
		case EditCenterOfMass:
			return new EditCenterOfMassCollector();
		case TotalTime:
			return new TotalTimeCollector(10 * 60 * 1000); // 10 minutes
		default:
			return null;
		}
	}
}
