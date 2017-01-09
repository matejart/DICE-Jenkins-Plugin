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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

public class MetricsHistoryTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testEmpty() {
		MetricsHistory history = new MetricsHistory(0);
		assertEquals(0, history.getStartBuild());
		assertEquals(-1, history.getEndBuild());
		assertEquals(0, history.getLength());
		assertTrue(history.getMetrics().isEmpty());
	}

	/**
	 * Test the process of building the test history. Use
	 * a staggered/segmented approach where builds have
	 * different sets of metrics.
	 */
	@Test
	public void testBuildHistorySegmented() {
		MetricsHistory history = new MetricsHistory(0);
		ArrayList<Integer> expectedBuildNumbers = new ArrayList<Integer>();
		
		// start with one metric
		Hashtable<String, Number> metrics0 = new Hashtable<String, Number>();
		metrics0.put("throughput", 22.4);
		TreeSet<String> expectedMetrics = new TreeSet<String>();
		expectedMetrics.add("throughput");
		Hashtable<String, ArrayList<Number>> expectedHistory =
				new Hashtable<String, ArrayList<Number>>();
		expectedHistory.put("throughput", new ArrayList<Number>());
		expectedHistory.get("throughput").add(22.4); // row 0, col 0
		expectedBuildNumbers.add(1);
		
		int newEndBuild = history.appendMetrics(metrics0, 1);
		ArrayList<Integer> buildNumbers = history.getBuildNumbers();
		assertEquals(1, newEndBuild);
		assertEquals(1, history.getEndBuild());
		assertEquals(expectedMetrics,
				new TreeSet<String>(history.getMetrics()));
		assertEqualsDelta(expectedHistory.get("throughput"),
				history.getHistory("throughput"));
		assertEquals(expectedBuildNumbers, buildNumbers);
		
		// the next build produces a different metric
		Hashtable<String, Number> metrics1 = new Hashtable<String, Number>();
		metrics1.put("latency", 341.1);
		expectedHistory.get("throughput").add(0.0); // row 0, col 1
		expectedMetrics.add("latency");
		expectedHistory.put("latency", new ArrayList<Number>());
		expectedHistory.get("latency").add(0.0);    // row 1, col 0
		expectedHistory.get("latency").add(341.1);  // row 1, col 1
		expectedBuildNumbers.add(2);
		newEndBuild = history.appendMetrics(metrics1, 2);
		buildNumbers = history.getBuildNumbers();
		assertEquals(2, newEndBuild);
		assertEquals(2, history.getEndBuild());
		assertEquals(expectedMetrics,
				new TreeSet<String>(history.getMetrics()));
		assertEqualsDelta(expectedHistory.get("throughput"),
				history.getHistory("throughput"));
		assertEqualsDelta(expectedHistory.get("latency"),
				history.getHistory("latency"));
		assertEquals(expectedBuildNumbers, buildNumbers);

		// then we have partially overlapping metrics
		Hashtable<String, Number> metrics2 = new Hashtable<String, Number>();
		metrics2.put("throughput", 31.5);
		metrics2.put("duration", 290.2);
		expectedMetrics.add("duration");
		expectedHistory.get("throughput").add(31.5);  // row 0, col 2
		expectedHistory.get("latency").add(0.0);      // row 1, col 2
		expectedHistory.put("duration", new ArrayList<Number>());
		expectedHistory.get("duration").add(0.0);     // row 2, col 0
		expectedHistory.get("duration").add(0.0);     // row 2, col 1
		expectedHistory.get("duration").add(290.2);    // row 2, col 2
		expectedBuildNumbers.add(3);
		newEndBuild = history.appendMetrics(metrics2, 3);
		buildNumbers = history.getBuildNumbers();
		assertEquals(3, newEndBuild);
		assertEquals(3, history.getEndBuild());
		assertEquals(expectedMetrics,
				new TreeSet<String>(history.getMetrics()));
		assertEqualsDelta(expectedHistory.get("throughput"),
				history.getHistory("throughput"));
		assertEqualsDelta(expectedHistory.get("latency"),
				history.getHistory("latency"));
		assertEqualsDelta(expectedHistory.get("duration"),
				history.getHistory("duration"));
		assertEquals(expectedBuildNumbers, buildNumbers);
	}
	
	/**
	 * Test the process of building the test history. Use
	 * a uniform approach where builds have the same sets of
	 * metrics.
	 */
	@Test
	public void testBuildHistoryUniform() {
		MetricsHistory history = new MetricsHistory(0);
		ArrayList<Integer> expectedBuildNumbers = new ArrayList<Integer>();
		
		// job 1 parameters
		Hashtable<String, Number> metrics0 = new Hashtable<String, Number>();
		metrics0.put("throughput", 22.4);
		metrics0.put("latency", 299.01);
		metrics0.put("duration", 5.24);
		// job 1 expected values
		TreeSet<String> expectedMetrics = new TreeSet<String>();
		expectedMetrics.add("throughput");
		expectedMetrics.add("latency");
		expectedMetrics.add("duration");
		Hashtable<String, ArrayList<Number>> expectedHistory =
				new Hashtable<String, ArrayList<Number>>();
		expectedHistory.put("throughput", new ArrayList<Number>());
		expectedHistory.get("throughput").add(22.4);  // row 0, col 0
		expectedHistory.put("latency", new ArrayList<Number>());
		expectedHistory.get("latency").add(299.01);   // row 1, col 0
		expectedHistory.put("duration", new ArrayList<Number>());
		expectedHistory.get("duration").add(5.24);    // row 2, col 0
		expectedBuildNumbers.add(1);
		// job 1 mock-execute
		int newEndBuild = history.appendMetrics(metrics0, 1);
		ArrayList<Integer> buildNumbers = history.getBuildNumbers();
		// job 1 test
		assertEquals(1, newEndBuild);
		assertEquals(1, history.getEndBuild());
		assertEquals(expectedBuildNumbers, buildNumbers);
		assertEquals(expectedMetrics,
				new TreeSet<String>(history.getMetrics()));
		assertEqualsDelta(expectedHistory.get("throughput"),
				history.getHistory("throughput"));
		
		// job 2 parameters
		Hashtable<String, Number> metrics1 = new Hashtable<String, Number>();
		metrics1.put("throughput", 33.92);
		metrics1.put("latency", 341.1);
		metrics1.put("duration", 6.42);
		// job 2 expected values
		expectedHistory.get("throughput").add(33.92); // row 0, col 1
		expectedHistory.get("latency").add(341.1);    // row 1, col 1
		expectedHistory.get("duration").add(6.42);    // row 2, col 1
		expectedBuildNumbers.add(2);
		// job 2 mock-execute
		newEndBuild = history.appendMetrics(metrics1, 2);
		buildNumbers = history.getBuildNumbers();
		// job 2 test
		assertEquals(2, newEndBuild);
		assertEquals(2, history.getEndBuild());
		assertEquals(expectedBuildNumbers, buildNumbers);
		assertEquals(expectedMetrics,
				new TreeSet<String>(history.getMetrics()));
		assertEqualsDelta(expectedHistory.get("throughput"),
				history.getHistory("throughput"));
		assertEqualsDelta(expectedHistory.get("latency"),
				history.getHistory("latency"));

		// job 3 parameters
		Hashtable<String, Number> metrics2 = new Hashtable<String, Number>();
		metrics2.put("throughput", 31.5);
		metrics2.put("latency", 400.05);
		metrics2.put("duration", 290.2);
		// job 3 expected values
		expectedHistory.get("throughput").add(31.5);  // row 0, col 2
		expectedHistory.get("latency").add(400.05);   // row 1, col 2
		expectedHistory.get("duration").add(290.2);   // row 2, col 2
		expectedBuildNumbers.add(3);
		// job 3 mock-execute
		newEndBuild = history.appendMetrics(metrics2, 3);
		buildNumbers = history.getBuildNumbers();
		// job 3 test
		assertEquals(3, newEndBuild);
		assertEquals(3, history.getEndBuild());
		assertEquals(expectedBuildNumbers, buildNumbers);
		assertEquals(expectedMetrics,
				new TreeSet<String>(history.getMetrics()));
		assertEqualsDelta(expectedHistory.get("throughput"),
				history.getHistory("throughput"));
		assertEqualsDelta(expectedHistory.get("latency"),
				history.getHistory("latency"));
		assertEqualsDelta(expectedHistory.get("duration"),
				history.getHistory("duration"));
	}
	
	/**
	 * Test retrieval of build history where build numbers
	 * have gaps.
	 */
	@Test
	public void testBuildHistoryGaps() {
		// input of the builds
		TreeSet<String> metricsNames = new TreeSet<String>();
		metricsNames.add("throughput");
		metricsNames.add("duration");
		metricsNames.add("latency");
		ArrayList<Hashtable<String, Number>> inputJobs =
				new ArrayList<Hashtable<String, Number>>();
		Hashtable<String, Number> metrics = new Hashtable<String, Number>();
		metrics.put("BUILD_NUMBER", 3);
		metrics.put("throughput", 22.4);
		metrics.put("latency", 299.01);
		metrics.put("duration", 5.24);
		inputJobs.add(metrics);
		metrics = new Hashtable<String, Number>();
		metrics.put("BUILD_NUMBER", 4);
		metrics.put("throughput", 33.92);
		metrics.put("latency", 341.1);
		metrics.put("duration", 6.42);
		inputJobs.add(metrics);
		metrics = new Hashtable<String, Number>();
		metrics.put("BUILD_NUMBER", 5);
		metrics.put("throughput", 31.5);
		metrics.put("latency", 400.05);
		metrics.put("duration", 290.2);
		inputJobs.add(metrics);
		metrics = new Hashtable<String, Number>();
		metrics.put("BUILD_NUMBER", 15);
		metrics.put("throughput", 19.9);
		metrics.put("latency", 180.05);
		metrics.put("duration", 140.2);
		inputJobs.add(metrics);
		metrics = new Hashtable<String, Number>();
		metrics.put("BUILD_NUMBER", 20);
		metrics.put("throughput", 32.6);
		metrics.put("latency", 348.26);
		metrics.put("duration", 211.13);
		inputJobs.add(metrics);
		
		// expected values
		ArrayList<Integer> expectedBuildNumbers = new ArrayList<Integer>();
		expectedBuildNumbers.add(3);
		expectedBuildNumbers.add(4);
		expectedBuildNumbers.add(5);
		expectedBuildNumbers.add(15);
		expectedBuildNumbers.add(20);
		Hashtable<String, ArrayList<Number>> expectedHistory =
				new Hashtable<String, ArrayList<Number>>();
		expectedHistory.put("throughput", new ArrayList<Number>());
		expectedHistory.put("latency", new ArrayList<Number>());
		expectedHistory.put("duration", new ArrayList<Number>());
		expectedHistory.get("throughput").add(22.4);
		expectedHistory.get("latency").add(299.01);
		expectedHistory.get("duration").add(5.24);
		expectedHistory.get("throughput").add(33.92);
		expectedHistory.get("latency").add(341.1);
		expectedHistory.get("duration").add(6.42);
		expectedHistory.get("throughput").add(31.5);
		expectedHistory.get("latency").add(400.05);
		expectedHistory.get("duration").add(290.2);
		expectedHistory.get("throughput").add(19.9);
		expectedHistory.get("latency").add(180.05);
		expectedHistory.get("duration").add(140.2);
		expectedHistory.get("throughput").add(32.6);
		expectedHistory.get("latency").add(348.26);
		expectedHistory.get("duration").add(211.13);
		
		// mock-run builds
		MetricsHistory history = new MetricsHistory(0);
		for (Hashtable<String, Number> job: inputJobs) {
			int buildNumber = job.get("BUILD_NUMBER").intValue();
			job.remove("BUILD_NUMBER");
			int newEndBuild = history.appendMetrics(job, buildNumber);
			assertEquals(buildNumber, newEndBuild);
			assertEquals(buildNumber, history.getEndBuild());
		}
		
		// check the results
		ArrayList<Integer> buildNumbers = history.getBuildNumbers();
		assertEquals(expectedBuildNumbers, buildNumbers);
		assertEquals(metricsNames,
				new TreeSet<String>(history.getMetrics()));
		for (String metricName : metricsNames) {
			assertEqualsDelta(expectedHistory.get(metricName),
					history.getHistory(metricName));
		}
	}

	public static void assertEqualsDelta(List<Number> expected,
			List<Number> actual) {
		
		assertEqualsDelta(expected, actual, 1e-6f);
	}
	
	public static void assertEqualsDelta(List<Number> expected,
			List<Number> actual, float delta) {
		
		assertEquals("Array lengths differ", expected.size(),
				actual.size());
		ArrayList<Float> diffs = new ArrayList<Float>();
		boolean equals = true;
		for (int i = 0; i < expected.size(); i++) {
			float diff = Math.abs(actual.get(i).floatValue() - 
					expected.get(i).floatValue());
			equals = equals && (diff <= delta);
			diffs.add(new Float(diff));
		}
		
		assertTrue(String.format(
				"Arrays differ.\nExpected: %s\nActual:   %s",
				expected.toString(), actual.toString()),
			equals);
	}
}
