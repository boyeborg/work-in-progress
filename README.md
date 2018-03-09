# Work in progress

This system preprosseses codebase snapshot data collected from student exercises and outputs the result to a CSV-file.

## Export system

The export script (`Export.java`) exports the student-ID and the exercise number by default. Other data can be exported through pre-processing modules called *data collectors*, or *collectors* for short. To add a module, simply add it to the static array `collectors` located at the top of the Export class in `Export.java`. The collector array should look some like this:

```java
static DataCollector[] collectors = {
	new TotalTimeCollector(10 * 60 * 1000), // 10 minutes
	new CompletionCollector(),
	new DebugRunsCollector(),
	new AfterSuccessWorkCollector(10 * 60 * 1000) // 10 minutes
	};
```

Feel free to add or remove collectors as needed. For more information on how the collectors work, see below.

## Collector

A collector is a java class that takes in each event within a exercise and produces an output based on the events. This output is exported to the CSV-file. For example the debug runs collector receive each event of an exercise and counts each launch event (meaning that the student executed the code within the eclipse IDE) that was caused by the student using the debug-tool. Every collector is executed independently for each exercise.

All collectors implements the data collector interface (`DataCollector.java`), and must therefore contain the following methods:

<dl>
<dt>public String getName()</dt>
<dd>Returns the name used in the description header of the exported CSV file. For ease of use in other systems (e.g. pandas in python), this name shoud not contain spaces. Best practice is to replace spaces with underscore. For example the debug runs collector uses the name "num_debug_runs".</dd>

<dt>public void addEvent(EObject eObject)</dt>
<dd>This method is called with each event within an exercise. The eObject parameter is the current event. Note that the events might not (in fact, almost never does) arrive in chronological order. See the existing collectors for examples of how to write this function.</dd>

<dt>public String getName()</dt>
<dd>The method should calculate and return the result of the collector based on the events added through the addEvent method. The reason why this has to be returned as a string is that it is written to the CSV-file, and since every collector might operate with different types (int, double, float ect.) it is easier to just return the string so the export script does not have to deal with converting all the different types to strings.</dd>

<dt>public void reset()</dt>
<dd>This method is called before the export script starts to iterate over a new exercise to ensure that the collector is fresh and clean (i.e. does not contain data from previous exercises).</dd>
</dl>

## Future work

Note that the current collectors and export script treats each exercise as one entity, i.e. computes one value for the entire exercise. Values for each student might be desired, like number of exercise completed within one assignment or update frequency change for a student between two or more exercises (as described in the paper *Programming Pluralism: Using Learning Analytics to Detect Patterns in the Learning of Computer Programming* by P. Blikstein, M. Worsley, C. Piech et al.).
 