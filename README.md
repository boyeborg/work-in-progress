# Work in progress

This system preprosseses codebase snapshot data collected from student exercises and outputs the result to a CSV-file.

## Export system

The export script (`Export.java`) exports the student-ID and the exercise number by default. Other data can be exported through pre-processing modules called *data collectors*, or *collectors* for short. To add a collector, simply add it to the static array `collectors` located at the top of the Export class in `Export.java`. The collector array should look some like this:

```java
static DataCollectorFactory.Collector[] collectors = {
	DataCollectorFactory.Collector.AfterSuccessWork,
	DataCollectorFactory.Collector.Completion,
	DataCollectorFactory.Collector.DebuggerUsed,
	DataCollectorFactory.Collector.EditCenterOfMass,
	DataCollectorFactory.Collector.TotalTime,
	DataCollectorFactory.Collector.WarningCount
};

```

Feel free to add or remove collectors as needed. For more information on how the collectors work, see below.

For each exercise-file, a `DataLine` object is created. This class creates new collectors (through the `DataCollectorFactory` class) to be used for that exercise, and is used to get a comma separated line for the CSV containing the data collected and the student ID and exercise number (a unique number representing the exercise).

Edit the static string `ASSIGNMENT_PATH` to point to a folder containing one folder for each student (the name of the folder will be the student ID). Each student folder might contain multiple exercises.


## Collectors

A collector is a java class that takes in each event within a exercise and produces an output based on the events. This output is exported to the CSV-file. For example, the `DebugRunsCollector` class receive each event of an exercise and counts each launch event (meaning that the student executed the code within the eclipse IDE) that was caused by the student using the debugging-tool.

All collectors implements the data collector interface (`DataCollector.java`), and must therefore contain the following methods:

<dl>
<dt>public String getName()</dt>
<dd>Returns the name used in the description header of the exported CSV file. For ease of use in other systems (e.g. pandas in python), this name shoud not contain spaces. Best practice is to replace spaces with underscore. For example the debug runs collector uses the name "num_debug_runs".</dd>

<dt>public void addEvent(EObject eObject)</dt>
<dd>This method is called with each event within an exercise. The eObject parameter is the current event. Note that the events might not (in fact, almost never does) arrive in chronological order. See the existing collectors for examples of how to implement this function.</dd>

<dt>public void calculateResult()</dt>
<dd>This method is called after each exercise has been looped through and the events has been added. It should calculate the result based on the events added through the addEvent method. If any global pre-calculation is needed (e.g. normalization), this can also be done here, since all the collectors complete this step before the actual result is fetched.</dd>

<dt>public String getResult()</dt>
<dd>The method should return the result of the collector. The reason why this has to be returned as a string is that it is written to the CSV-file, and since every collector might operate with different types (int, double, float ect.) it is easier to just return the string so the export script does not have to deal with converting all the different types to strings.</dd>
</dl>

## Future work

Note that the current collectors and export script treats each exercise as one entity, i.e. computes one value for the entire exercise. Values for each student might be desired, like number of exercise completed within one assignment, or update frequency change for a student between two or more exercises (as described in the paper *Programming Pluralism: Using Learning Analytics to Detect Patterns in the Learning of Computer Programming* by P. Blikstein, M. Worsley, C. Piech et al.).
