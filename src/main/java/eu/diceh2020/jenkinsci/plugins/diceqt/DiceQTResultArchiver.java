package eu.diceh2020.jenkinsci.plugins.diceqt;

import java.io.IOException;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.tasks.junit.*;
import jenkins.tasks.SimpleBuildStep;

import eu.diceh2020.jenkinsci.plugins.diceqt.DiceAction;

public class DiceQTResultArchiver extends Recorder implements SimpleBuildStep {
	
	/**
	 * Configuration parameter containing the path to the results file
	 * (e.g., a JSON file).
	 */
	private final String pathToResults;
	
	/**
	 * Getter used in the {@code config.jelly}
	 */
	public String getPathToResults() {
		return this.pathToResults;
	}
	
	@DataBoundConstructor
	public DiceQTResultArchiver(String pathToResults) {
		this.pathToResults = pathToResults;
	}

	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;
	}
	
	@Override
	public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener)
			throws InterruptedException, IOException {
		
		listener.getLogger().println("Nekaj dela.");
		
		double latency = 55.4;
		
		DiceAction action = run.getAction(DiceAction.class);
		boolean appending;
		if (action == null) {
			action = new DiceAction(latency);
			appending = false;
		} else {
			action.setLatency(latency);
			appending = true;
		}
		
		listener.getLogger().println(String.format("Performance metric latency: %f",
				latency));

		if (appending) {
			run.save();
		} else {
			run.addAction(action);
		}
	}

	@Override
	public DescriptorImpl getDescriptor() {
		return (DescriptorImpl) super.getDescriptor();
	}
	
	@Extension
	public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {

		@SuppressWarnings("rawtypes")
		public boolean isApplicable(Class<? extends AbstractProject> jobType) {
			return true;
		}

		public String getDisplayName() {
			return "DICE's Quality check";
		}
		
	}
}
