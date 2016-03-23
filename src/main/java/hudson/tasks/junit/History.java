/*
 * The MIT License
 *
 * Copyright (c) 2004-2010, Sun Microsystems, Inc., Tom Huybrechts, Yahoo!, Inc., Seiji Sogabe
 *
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
 */
package hudson.tasks.junit;

import hudson.model.AbstractBuild;
import hudson.model.Run;
import jenkins.model.Jenkins;
import hudson.tasks.test.TestObject;
import hudson.tasks.test.TestResult;
import hudson.util.ChartUtil;
import hudson.util.ColorPalette;
import hudson.util.DataSetBuilder;
import hudson.util.Graph;
import hudson.util.ShiftedCategoryAxis;
import hudson.util.StackedAreaRenderer2;

import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedAreaRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.RectangleInsets;
import org.kohsuke.stapler.Stapler;

/**
 * History of {@link hudson.tasks.test.TestObject} over time.
 *
 * @since 1.320
 */
public class History {

	/**
	 * Object that contains all test result objects.
	 */
	private final TestObject testObject;

	/**
	 * Constructor for the History class.
	 * 
	 * @param testObject
	 *            Object that contains all test result objects.
	 */
	public History(TestObject testObject) {
		this.testObject = testObject;
	}

	/**
	 * Getter method for TestObject.
	 * 
	 * @return TestObject
	 */
	public TestObject getTestObject() {
		return testObject;
	}

	/**
	 * Returns true if build history exists, else false.
	 * 
	 * @return boolean
	 */
	public boolean historyAvailable() {
		if (testObject.getRun().getParent().getBuilds().size() > 1)
			return true;
		else
			return false;
	}

	/**
	 * Returns a list of all TestResult objects from an interval between two
	 * specified build numbers.
	 * 
	 * @param start
	 *            First build number to be included.
	 * @param end
	 *            Last build number to be included.
	 * @return A list (List<TestResult>) of all TestResult objects from an
	 *         interval between start and end.
	 */
	public List<TestResult> getList(int start, int end) {

		List<TestResult> list = new ArrayList<TestResult>();

		// ensure the provided last build number is not bigger than the number
		// of all available builds
		end = Math.min(end, testObject.getRun().getParent().getBuilds().size());

		// for each Run in testObject, save its TestResult in a list
		for (Run<?, ?> b : testObject.getRun().getParent().getBuilds().subList(start, end)) {
			if (b.isBuilding())
				continue;
			TestResult o = testObject.getResultInRun(b);
			if (o != null) {
				list.add(o);
			}
		}
		return list;
	}

	/**
	 * Returns a list of all {@link hudson.tasks.junit.TestResult} objects.
	 * 
	 * @return A list of all {@link hudson.tasks.junit.TestResult} objects.
	 */
	public List<TestResult> getList() {
		return getList(0, testObject.getRun().getParent().getBuilds().size());
	}

	/**
	 * Graph of the duration of tests over time.
	 * 
	 * @return A JFreeChart-generated graph that's bound to UI. The graph
	 *         represents the duration of tests over time.
	 */
	public Graph getDurationGraph() {
		return new GraphImpl("Tests duration  (seconds)") {

			protected DataSetBuilder<String, ChartLabel> createDataSet() {
				DataSetBuilder<String, ChartLabel> data = new DataSetBuilder<String, ChartLabel>();

				List<TestResult> list;
				try {
					list = getList(Integer.parseInt(Stapler.getCurrentRequest().getParameter("start")),
							Integer.parseInt(Stapler.getCurrentRequest().getParameter("end")));
				} catch (NumberFormatException e) {
					list = getList();
				}

				for (hudson.tasks.test.TestResult o : list) {
					data.add(((double) o.getDuration()) / (1000), "", new ChartLabel(o) {
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
				return data;
			}

		};
	}

	/**
	 * Graph of the number of tests over time.
	 * 
	 * @return A JFreeChart-generated graph that's bound to UI. The graph
	 *         represents the number of tests.
	 */
	public Graph getCountGraph() {
		return new GraphImpl("Number of tests (#)") {
			protected DataSetBuilder<String, ChartLabel> createDataSet() {
				DataSetBuilder<String, ChartLabel> data = new DataSetBuilder<String, ChartLabel>();

				List<TestResult> list;
				try {
					list = getList(Integer.parseInt(Stapler.getCurrentRequest().getParameter("start")),
							Integer.parseInt(Stapler.getCurrentRequest().getParameter("end")));
				} catch (NumberFormatException e) {
					list = getList();
				}

				for (TestResult o : list) {
					data.add(o.getPassCount(), "2Passed", new ChartLabel(o));
					data.add(o.getFailCount(), "1Failed", new ChartLabel(o));
					data.add(o.getSkipCount(), "0Skipped", new ChartLabel(o));
				}
				return data;
			}
		};
	}

	/**
	 * Graph of build latencies over time
	 * 
	 * @return A JFreeChart-generated graph that's bound to UI. The graph
	 *         represents build latencies over time.
	 */
	public Graph getLatencyGraph() {

		return new GraphImpl("Test latency (miliseconds)") {

			protected DataSetBuilder<String, ChartLabel> createDataSet() {

				// data to be displayed on the graph
				DataSetBuilder<String, ChartLabel> data = new DataSetBuilder<String, ChartLabel>();

				// list of test results
				List<TestResult> list;
				try {
					// Stapler maps an HTTP request to a method call / JSP
					// invocation against a model object.
					list = getList(Integer.parseInt(Stapler.getCurrentRequest().getParameter("start")),
							Integer.parseInt(Stapler.getCurrentRequest().getParameter("end")));
				} catch (NumberFormatException e) {
					list = getList();
				}

				/*
				 * TEST: arbitrary data to fill the graph (x = build number, y =
				 * build duration (tests included))
				 */
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

				return data;
			}
		};
	}

	/**
	 * Private abstract class, extends Graph.
	 * 
	 */
	private abstract class GraphImpl extends Graph {

		/**
		 * Graph label, visible on the left side of the graph (chart).
		 */
		private final String yLabel;

		/**
		 * An extended GraphImpl constructor with default (super) values and an
		 * additional label parameter.
		 * 
		 * @param yLabel
		 *            A string describing the graph (chart).
		 * 
		 */
		protected GraphImpl(String yLabel) {
			super(-1, 600, 300); // cannot use timestamp, since ranges may
									// change
			this.yLabel = yLabel;
		}

		protected abstract DataSetBuilder<String, ChartLabel> createDataSet();

		/**
		 * Creates a graph of type JFreeChart.
		 * 
		 * @return A JFreeChart-generated graph that's bound to UI. The graph
		 *         represents build latencies over time.
		 */
		protected JFreeChart createGraph() {
			final CategoryDataset dataset = createDataSet().build();

			final JFreeChart chart = ChartFactory.createStackedAreaChart(null, // chart
																				// title
					null, // unused
					yLabel, // range axis label
					dataset, // data
					PlotOrientation.VERTICAL, // orientation
					false, // include legend
					true, // tooltips
					false // urls
			);

			chart.setBackgroundPaint(Color.white);

			// CategoryPlot: A general plotting class that uses data from a
			// CategoryDataset and renders each data item using a
			// CategoryItemRenderer.
			final CategoryPlot plot = chart.getCategoryPlot();

			// plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0,
			// 5.0));
			plot.setBackgroundPaint(Color.WHITE);
			plot.setOutlinePaint(null);
			plot.setForegroundAlpha(0.8f);
			// plot.setDomainGridlinesVisible(true);
			// plot.setDomainGridlinePaint(Color.white);
			plot.setRangeGridlinesVisible(true);
			plot.setRangeGridlinePaint(Color.black);

			CategoryAxis domainAxis = new ShiftedCategoryAxis(null);
			plot.setDomainAxis(domainAxis);
			domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
			domainAxis.setLowerMargin(0.0);
			domainAxis.setUpperMargin(0.0);
			domainAxis.setCategoryMargin(0.0);

			final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
			ChartUtil.adjustChebyshev(dataset, rangeAxis);
			rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
			rangeAxis.setAutoRange(true);

			/**
			 * A renderer that draws stacked area charts for a CategoryPlot.
			 * Overrides methods getItemPaint, generateURL, generateToolTip.
			 * 
			 */
			StackedAreaRenderer ar = new StackedAreaRenderer2() {
				@Override
				public Paint getItemPaint(int row, int column) {
					ChartLabel key = (ChartLabel) dataset.getColumnKey(column);
					if (key.getColor() != null)
						return key.getColor();
					return super.getItemPaint(row, column);
				}

				@Override
				public String generateURL(CategoryDataset dataset, int row, int column) {
					ChartLabel label = (ChartLabel) dataset.getColumnKey(column);
					return label.getUrl();
				}

				@Override
				public String generateToolTip(CategoryDataset dataset, int row, int column) {
					ChartLabel label = (ChartLabel) dataset.getColumnKey(column);
					return label.o.getRun().getDisplayName() + " : " + label.o.getDurationString();
				}
			};
			plot.setRenderer(ar);
			// Sets the paint used for a series and sends a RendererChangeEvent
			// to all registered listeners.
			// The 1st param is the series index.
			ar.setSeriesPaint(0, ColorPalette.YELLOW); // Skips.
			ar.setSeriesPaint(1, ColorPalette.RED); // Failures.
			ar.setSeriesPaint(2, ColorPalette.BLUE); // Total.

			// crop extra space around the graph
			plot.setInsets(new RectangleInsets(0, 0, 0, 5.0));

			return chart;
		}
	}

	class ChartLabel implements Comparable<ChartLabel> {
		TestResult o;
		String url;

		public ChartLabel(TestResult o) {
			this.o = o;
			this.url = null;
		}

		public String getUrl() {
			if (this.url == null)
				generateUrl();
			return url;
		}

		private void generateUrl() {
			Run<?, ?> build = o.getRun();
			String buildLink = build.getUrl();
			String actionUrl = o.getTestResultAction().getUrlName();
			this.url = Jenkins.getInstance().getRootUrl() + buildLink + actionUrl + o.getUrl();
		}

		public int compareTo(ChartLabel that) {
			return this.o.getRun().number - that.o.getRun().number;
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof ChartLabel)) {
				return false;
			}
			ChartLabel that = (ChartLabel) o;
			return this.o == that.o;
		}

		public Color getColor() {
			return null;
		}

		@Override
		public int hashCode() {
			return o.hashCode();
		}

		@Override
		public String toString() {
			Run<?, ?> run = o.getRun();
			String l = run.getDisplayName();
			String s = run instanceof AbstractBuild ? ((AbstractBuild) run).getBuiltOnStr() : null;
			if (s != null)
				l += ' ' + s;
			return l;
			// return o.getDisplayName() + " " + o.getOwner().getDisplayName();
		}

	}

	/**
	 * Returns the passed string as an integer value. On fail, returns passed
	 * default integer value.
	 * 
	 * @param s
	 *            Passed number as string.
	 * @param defaultValue
	 *            Passed default integer value.
	 * @return Converted string as integer value on success, default integer
	 *         value on fail.
	 */
	public static int asInt(String s, int defaultValue) {
		if (s == null)
			return defaultValue;
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
}
