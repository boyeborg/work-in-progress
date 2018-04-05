package no.ntnu.boyebn.master;

public class DataCollectorFactory {
	
	public static enum Collector {
		AfterSuccessWork,
		Completion,
		DebugRuns,
		EditCenterOfMass,
		TotalTime,
		DebuggerUsed,
		WarningCount
	}
	
	public static DataCollector getCollector(Collector collector) {
		switch (collector) {
		case AfterSuccessWork:
			return new AfterSuccessWorkCollector();
		case Completion:
			return new CompletionCollector();
		case DebugRuns:
			return new DebugRunsCollector();
		case EditCenterOfMass:
			return new EditCenterOfMassCollector();
		case TotalTime:
			return new TotalTimeCollector(10 * 60 * 1000); // 10 minutes
		case DebuggerUsed:
			return new DebuggerUsedCollector();
		case WarningCount:
			return new WarningCountCollector();
		default:
			return null;
		}
	}
}
