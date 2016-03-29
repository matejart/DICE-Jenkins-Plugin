## Synopsis

The DICE Jenkins plugin allows the user to publish and review results of a third-party testing tool.

## Motivation

The emergence of a specific third-party testing tool for Java projects created a need for a Jenkins plugin that would allow publishing and reviewing of testing results in a custom format.

## Basic Contents:

* `doc`: A folder containing javadoc files.
* `src`: A folder containing the classes and other files necessary for the plugin to work.
* `DICE plugin user manual.pdf`: A manual for this plugin.
* `pom.xml`: The project build file.
* `README.md`: This file.

## Code Example

An example of a method in class History.java that creates a graph of build latencies from existing result reports on the Jenkins host:


```java
public Graph getLatencyGraph() {

	...

	for (hudson.tasks.test.TestResult o : list) {
	    data.add(((double) o.getDuration()) * 1000, "", new ChartLabel(o) {
		@Override
		public Color getColor() {
		    if (o.getFailCount() > 0)
			return ColorPalette.RED;
		    else if (o.getSkipCount() > 0)
			return ColorPalette.YELLOW;
		    else
			return ColorPalette.BLUE;
		}
	    });
	}

	...

}
```
An example of the corresponding front-end piece of code responsible for displaying the above-built graph in HTML format:

```javascript
function setLatency() {
	document.getElementById("graph").src = "latencyGraph/png?${rangeParameters}"
	document.getElementById("graph").lazyMap = "latencyGraph/map?${rangeParameters}"
	document.getElementById("graph").alt = "[Latency graph]";
	document.getElementById("latency-link").style.display = "none";
}
```
```HTML
<div align="center">
       	show
  	<a id="latency-link" href="#" onclick='javascript:setLatency()'> latency </a>
</div>
```
