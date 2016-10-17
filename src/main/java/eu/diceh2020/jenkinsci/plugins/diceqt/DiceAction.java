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

import hudson.model.Action;

public class DiceAction implements Action {
	private double latency = 0.0;

	public DiceAction(double latency) {
		this.latency = latency;
	}
	
	@Override
	public String getIconFileName() {
		return "clipboard.png";
	}

	@Override
	public String getDisplayName() {
		return "DICE report";
	}

	@Override
	public String getUrlName() {
		return "diceReport";
	}
	
	public double getLatency() {
		return this.latency;
	}
	
	public void setLatency(double latency) {
		this.latency = latency;
	}

}
