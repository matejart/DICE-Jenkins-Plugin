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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.TreeSet;

import antlr.collections.List;
import hudson.model.*;
import hudson.util.RunList;

/***
 * This class implements the view summarizing the whole project.
 * It charts the performances across the history of application's
 * builds.
 *
 * @author matej.artac@xlab.si
 *
 */
public class DiceQTResultProjectAction implements Action {

	private final AbstractProject<?, ?> project;

	DiceQTResultProjectAction(final AbstractProject<?, ?> project) {
		this.project = project;
	}
	
	@Override
	public String getIconFileName() {
		return "graph.gif";
	}

	@Override
	public String getDisplayName() {
		return "DICE graph";
	}

	@Override
	public String getUrlName() {
		return "dice-dashboard";
	}

	/**
	* Method necessary to get the side-panel included in the Jelly file
	* @return this {@link AbstractProject}
	*/
	public AbstractProject<?, ?> getProject() {
		return this.project;
	}
	
	/**
	 * Extracts a merged list of metric names used in all
	 * builds.
	 * @return
	 */
	public ArrayList<String> getMetricNames() {
		return getCurrentBuildHistory().getMetrics();
	}
	
	/**
	 * Get a time series for the the given metric.
	 * @param metricName The name of the metric to query
	 * the history of.
	 * @return A sequence of metric values.
	 */
	public ArrayList<Number> getMetricHistory(String metricName) {
		return getCurrentBuildHistory().getHistory(metricName);
	}
	
	private MetricsHistory getCurrentBuildHistory () {
		final MetricsHistory history = new MetricsHistory(0);
		
		RunList<?> buildList = this.project.getBuilds();
		for (Run<?, ?> build : buildList) {
			DiceQTResultBuildAction action = build.getAction(
					DiceQTResultBuildAction.class);
			if (action == null)
				continue;
			
			Hashtable<String, Number> metrics =
					action.getDiceQTResult().getMetrics();
			
			history.appendMetrics(metrics);
		}
		
		return history;
	}
}
