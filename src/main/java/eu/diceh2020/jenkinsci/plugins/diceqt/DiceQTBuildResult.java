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

import java.io.IOException;
import java.util.Hashtable;

import hudson.model.*;

/**
 * Implementation of a model that represents a single build's
 * results of the quality testing.
 * 
 * @author matej.artac@xlab.si
 *
 */
public class DiceQTBuildResult implements ModelObject {
	
	/**
	 * The {@link DiceQTResultBuildAction} that this report belongs to.
	 */
	private transient DiceQTResultBuildAction buildAction;

	DiceQTBuildResult(final DiceQTResultBuildAction buildAction, 
			TaskListener listener) throws IOException {
		this.buildAction = buildAction;
	}
	
	@Override
	public String getDisplayName() {
		return LocalMessages.getMessage(
				LocalMessages.BUILD_RESULT_DISPLAY_NAME);
	}

	public Run<?, ?> getBuild() {
		return this.buildAction.getBuild();
	}
	
	public Hashtable<String, Number> getMetrics() {
		return this.buildAction.getMetrics();
	}
}
