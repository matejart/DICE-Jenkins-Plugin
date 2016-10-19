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

import java.util.Enumeration;
import java.util.Hashtable;

import hudson.model.Action;

public class DiceAction implements Action {
	private Hashtable<String, Number> metrics = null;

	public DiceAction(Hashtable<String, Number> metrics) {
		this.metrics = this.clone(metrics);
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
	
	public Hashtable<String, Number> getMetrics() {
		return this.clone(this.metrics);
	}
	
	public void setMetrics(Hashtable<String, Number> metrics) {
		this.metrics = this.clone(metrics);
	}
	
	private Hashtable<String, Number> clone(Hashtable<String, Number> input) {
		Hashtable<String, Number> retval = new Hashtable<String, Number>();
		Enumeration<String> en = input.keys();
		while (en.hasMoreElements()) {
			String key = en.nextElement();
			retval.put(key, input.get(key));
		}
		return retval;
	}

}
