package eu.diceh2020.jenkinsci.plugins.diceqt;

/*-
 * #%L
 * DICE Jenkins plug-in
 * %%
 * Copyright (C) 2016 XLAB d.o.o.
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import java.util.ArrayList;
import java.util.Hashtable;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * This class represents the report of the project's performance.
 * It contains history of the performance metrics through the
 * builds.  
 * @author matej.artac@xlab.si
 *
 */
public class MetricsHistory {
	/**
	 * Stores numerical data from the builds. Column index
	 * of the data point is equivalent to the build number
	 * offset from the startBuild. Each row represents the
	 * data history of the respective metric.  
	 */
	private INDArray history;
	/**
	 * Build number of the first column in the history.
	 */
	private int startBuild;
	/**
	 * Build number of the last column in the history.
	 */
	private int endBuild;
	/**
	 * A list of build numbers. This is useful because we can
	 * store a succession of build numbers without gaps in the
	 * history for any of the deleted builds.
	 */
	private final ArrayList<Integer> buildNumbers = new ArrayList<Integer>();
	/**
	 * A list of metric names. Each metric name in the list
	 * corresponds to a row in the history.
	 */
	private ArrayList<String> metrics;
	
	
	public MetricsHistory(int startBuild) {
		this.history = null;
		this.startBuild = startBuild;
		this.endBuild = -1;
		this.metrics = new ArrayList<String>();
	}
	
	/**
	 * Gets the list of all the names of the metrics in the
	 * history. 
	 * @return An ArrayList of Strings representing the names
	 * of the metrics encountered in the project builds
	 */
	public ArrayList<String> getMetrics() {
		return Utilities.clone(this.metrics);
	}

	/**
	 * Gets the build number of the first data point the history.
	 */
	public int getStartBuild() {
		return startBuild;
	}

	/**
	 * Build number of the last data point in the history.
	 */
	public int getEndBuild() {
		return endBuild;
	}
	
	/**
	 * Obtains the number of points in the history.
	 * @return The number of points in the history, also
	 *  equivalent to the number of builds the history
	 *  representation covers.
	 */
	public int getLength() {
		if (this.endBuild < this.startBuild)
			return 0;
		else
			return this.startBuild - this.endBuild + 1;
	}
	
	/**
	 * Returns the current list of build numbers. This is useful
	 * because there may be gaps in the builds (e.g., because a user
	 * has deleted a build), but we don't store this gap explicitly
	 * in the history.
	 * @return a collection of integers representing the build
	 * numbers in the history of the builds.
	 */
	public ArrayList<Integer> getBuildNumbers() {
		return new ArrayList<Integer>(this.buildNumbers);
	}
	
	/**
	 * Appends a metric vector to the end of the series.
	 * @param metrics The dictionary containing the build metrics
	 * to be appended.
	 * @param buildNumber A build number (or unique ID) assigned
	 * to the build, which produced this metrics dictionary.
	 * @return The new end build (normally the same as the supplied
	 * buildNumber).
	 */
	public int appendMetrics(Hashtable<String, Number> metrics,
			int buildNumber) {
		int newEndBuild = Math.max(this.endBuild, buildNumber);
		this.buildNumbers.add(buildNumber);
				
		for (String name : metrics.keySet()) {
			int row = this.metrics.indexOf(name);
			if (row < 0)
			{
				this.metrics.add(name);
			}
		}
		
		INDArray newCol = Nd4j.zeros(this.metrics.size(), 1);
		int[] index = new int[] {0, 0};
		for (String name : this.metrics) {
			double value = 0.0;
			if (metrics.containsKey(name))
			{
				value = metrics.get(name).doubleValue();
			}
			newCol.putScalar(index, value);
			index[0]++;
		}
		
		if (this.history == null) {
			this.history = newCol;
		} else {
			// extend for any new metrics
			int newMetricsCount = 
					this.metrics.size() - this.history.rows();
			if (newMetricsCount > 0)
			{
				this.history = Nd4j.append(this.history,
						newMetricsCount, 0.0, 0);
			}
			this.history = Nd4j.append(this.history,
					1, 0.0, 1);
			this.history.putColumn(this.history.columns() - 1,
					newCol);
		}
		
		this.endBuild = newEndBuild;
		return newEndBuild;
	}
	
	/**
	 * Returns the time series of the metric. The series is sorted from
	 * the oldest to the newest.
	 * @param metricName The name of the metric to retrieve the history of.
	 * @return A collection of values representing the measured metrics
	 * in each respective build.
	 */
	public ArrayList<Number> getHistory(String metricName) {
		ArrayList<Number> retval = new ArrayList<Number>();
		
		int row = this.metrics.indexOf(metricName);
		if (row >= 0) {
			int index[] = new int[]{ row, 0 };
			for (; index[1] <= this.history.columns() - 1; index[1]++) {
				retval.add(this.history.getDouble(index[0], index[1]));
			}
		}
		return retval;
	}
}
