package eu.diceh2020.jenkinsci.plugins.diceqt.workflow;

import javax.annotation.CheckForNull;

import org.jenkinsci.plugins.workflow.steps.AbstractStepDescriptorImpl;
import org.jenkinsci.plugins.workflow.steps.AbstractStepImpl;
import org.kohsuke.stapler.DataBoundConstructor;

import eu.diceh2020.jenkinsci.plugins.diceqt.LocalMessages;
import hudson.Extension;

/**
 * This class implements the step that can be used in the {@code Jenkinsfile}.
 * For example, use it in a post step of the pipeline:
 * <pre>
 * pipeline {
 *     agent any
 *     stages {
 *         # ...
 *     }
 *     post {
 *         success {
 *             DICEQualityCheck(pathToResults: "output/result.json")
 *         }
 *     }
 * }
 * </pre>
 * @author matej.artac@xlab.si
 *
 */
public class DiceQTResultStep extends AbstractStepImpl {

	/**
	 * Configuration parameter containing the path to the results file
	 * (e.g., a JSON file).
	 */
	private final String pathToResults;

	/**
	 * Constructor of the class.
	 * @param pathToResults Path to the results file to be read and processed
	 *                      by the workflow.
	 */
	@DataBoundConstructor
	public DiceQTResultStep(@CheckForNull String pathToResults) {
		this.pathToResults = pathToResults;
	}

	@CheckForNull
	public String getPathToResults() {
		return this.pathToResults;
	}

	@Extension
	public static final class DescriptorImpl extends AbstractStepDescriptorImpl {

		public DescriptorImpl() {
			super(DiceQTResultStepExecution.class);
		}

		@Override
		public String getFunctionName() {
			return "DICEQualityCheck";
		}

		@Override
		public String getDisplayName() {
			return LocalMessages.getMessage(
					LocalMessages.ARCHIVER_DESCRIPTOR_DISPLAY_NAME);
		}

	}
}
