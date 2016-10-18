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
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

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

import static org.mockito.Mockito.*;

public class DiceQTResultArchiverTest extends TestCase {

	private Launcher launcher = mock(Launcher.class);
	private PrintStream logger = mock(PrintStream.class);
	private BuildListener listener = mock(BuildListener.class);
	private FreeStyleBuild build = mock(FreeStyleBuild.class);
	private FreeStyleProject job = mock(FreeStyleProject.class);
	private Run<?, ?> run = mock(Run.class);
	private List<FreeStyleBuild> buildList = new ArrayList<FreeStyleBuild>();

	public void setUp() throws Exception {
		when(listener.getLogger()).thenReturn(logger);
		when(job.getBuilds()).thenReturn(RunList.fromRuns(buildList));
		when(build.getParent()).thenReturn(job);
	}
	
	public void testPerform() throws Exception {
		DiceQTResultArchiver archiver = this.getArchiver(
				new DiceQTResultArchiver(""));
		File tmpFile = File.createTempFile("unittest", "dice");
		tmpFile.deleteOnExit();
		FilePath workspace = new FilePath(tmpFile);
		
		archiver.perform((Run<?, ?>) run, workspace, launcher, listener);
		
		ArgumentCaptor<DiceAction> actionArgument = ArgumentCaptor.forClass(
				DiceAction.class);
		verify(run).addAction(actionArgument.capture());
		assertEquals(55.4, actionArgument.getValue().getLatency());
	}
	
	private DiceQTResultArchiver getArchiver(DiceQTResultArchiver archiver)
			throws Exception {
		
		DiceQTResultArchiver spy = spy(archiver);
		
		return spy;
	}
}
