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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;

import hudson.FilePath;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.*;
import hudson.model.TaskListener;
import hudson.util.RunList;
import junit.framework.TestCase;
import eu.diceh2020.jenkinsci.plugins.diceqt.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DiceQTResultArchiverTest {

	private Launcher launcher = mock(Launcher.class);
	private PrintStream logger = mock(PrintStream.class);
	private BuildListener listener = mock(BuildListener.class);
	private FreeStyleBuild build = mock(FreeStyleBuild.class);
	private FreeStyleProject job = mock(FreeStyleProject.class);
	private Run<?, ?> run = mock(Run.class);
	private List<FreeStyleBuild> buildList = new ArrayList<FreeStyleBuild>();

	@Before
	public void setUp() throws Exception {
		when(listener.getLogger()).thenReturn(logger);
		when(job.getBuilds()).thenReturn(RunList.fromRuns(buildList));
		when(build.getParent()).thenReturn(job);
	}
	
	@Test
	public void testPerformSingleMetric() throws Exception {
		
		String fileContent = "{'latency': 123.55}";
		Hashtable<String, Number>  expected = new Hashtable<String, Number>();
		expected.put("latency", 123.55);
		
		FilePath resultsFilePath = Utilities.createTemporaryFile(
				fileContent);
		FilePath workspace = resultsFilePath.getParent();
		
		DiceQTResultArchiver archiver = this.getArchiver(
				new DiceQTResultArchiver(resultsFilePath.getName()));
		
		archiver.perform((Run<?, ?>) run, workspace, launcher, listener);
		
		ArgumentCaptor<DiceQTResultBuildAction> actionArgument = ArgumentCaptor.forClass(
				DiceQTResultBuildAction.class);
		verify(run).addAction(actionArgument.capture());
		assertEquals(expected, actionArgument.getValue().getMetrics());
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testPerformNoResults() throws Exception {
		FilePath nonExistentFilePath = new FilePath(new File(
				"/tmp/non-existent.ne.json"));
		String metricsFileName = nonExistentFilePath.getName();
		if (nonExistentFilePath.exists())
			throw new Exception("A problem with test: "
					+ "the non-existing file should not exist");
		
		FilePath workspace = nonExistentFilePath.getParent();
		
		DiceQTResultArchiver archiver = this.getArchiver(
				new DiceQTResultArchiver(metricsFileName));
		
		thrown.expect(FileNotFoundException.class);
		thrown.expectMessage("metrics file");
		thrown.expectMessage(metricsFileName);
		archiver.perform((Run<?, ?>) run, workspace, launcher, listener);
	}
	
	@Test
	public void testPerformEmptyResults() throws Exception {
		String fileContent = "";
		Hashtable<String, Number>  expected = new Hashtable<String, Number>();
		
		FilePath resultsFilePath = Utilities.createTemporaryFile(
				fileContent);
		FilePath workspace = resultsFilePath.getParent();
		
		DiceQTResultArchiver archiver = this.getArchiver(
				new DiceQTResultArchiver(resultsFilePath.getName()));
		
		archiver.perform((Run<?, ?>) run, workspace, launcher, listener);
		
		ArgumentCaptor<DiceQTResultBuildAction> actionArgument = ArgumentCaptor.forClass(
				DiceQTResultBuildAction.class);
		verify(run).addAction(actionArgument.capture());
		assertEquals(expected, actionArgument.getValue().getMetrics());
	}
	
	private DiceQTResultArchiver getArchiver(DiceQTResultArchiver archiver)
			throws Exception {
		
		DiceQTResultArchiver spy = spy(archiver);
		doReturn(null).when(spy).getBuild(any(Run.class));
		
		return spy;
	}
}
