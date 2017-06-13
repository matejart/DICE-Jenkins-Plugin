package eu.diceh2020.jenkinsci.plugins.diceqt.workflow;

import javax.inject.Inject;

import org.jenkinsci.plugins.workflow.steps.AbstractSynchronousNonBlockingStepExecution;
import org.jenkinsci.plugins.workflow.steps.StepContextParameter;

import eu.diceh2020.jenkinsci.plugins.diceqt.DiceQTResultArchiver;
import hudson.FilePath;
import hudson.model.AbstractBuild;
import hudson.model.Run;
import hudson.model.TaskListener;

public class DiceQTResultStepExecution extends AbstractSynchronousNonBlockingStepExecution<Void> {

	@StepContextParameter
	private transient TaskListener listener;

	@StepContextParameter
	private transient FilePath ws;

	@StepContextParameter
	private transient Run<?, ?> build;

	@Inject
	private transient String pathToResults;

	@Override
	protected Void run() throws Exception {

		DiceQTResultArchiver.archiveResults(build, ws,
				(AbstractBuild<?, ?>)build, listener, pathToResults);

		return null;
	}

}
