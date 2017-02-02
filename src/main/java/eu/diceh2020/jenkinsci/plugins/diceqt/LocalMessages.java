package eu.diceh2020.jenkinsci.plugins.diceqt;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Helper class for localisation messages.
 * @author matej.artac@xlab.si
 *
 */
public final class LocalMessages {
	public static final String ARCHIVER_DESCRIPTOR_DISPLAY_NAME =
			"DiceQTResultArchiver.Descriptor.DisplayName";
	public static final String BUILD_RESULT_DISPLAY_NAME =
			"DiceQTBuildResult.DisplayName";
	public static final String BUILD_ACTION_DISPLAY_NAME =
			"DiceQTBuildAction.DisplayName";
	public static final String PROJECT_ACTION_DISPLAY_NAME =
			"DiceQTProjectAction.DisplayName";
	
	private static final ResourceBundle resources =
			ResourceBundle.getBundle(
					"eu.diceh2020.jenkinsci.plugins.diceqt.Messages");
	
	public static String getMessage(String messageString) {
		return resources.getString(messageString);
	}
}
