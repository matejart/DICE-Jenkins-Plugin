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
import java.util.Iterator;

import org.json.JSONObject;

import hudson.FilePath;

/**
 * Class for parsing metrics data.
 * 
 * @author matej.artac (at) xlab.si
 *
 */
public class MetricsJsonParser {
	
	/**
	 * Parse the file indicated by the filePath parameter from a JSON
	 * file. It expects a flat structure of key-value pairs with the
	 * values being a numerical value. 
	 *
	 * @param filePath Describes location of the results file to be
	 * parsed.
	 * @return A dictionary of metric names and their values. If the
	 * file doesn't exist, the return value is null.
	 * @throws InterruptedException Thrown during processing 
	 * remoting operations
	 * @throws IOException 
	 */
	public static Hashtable<String, Number> parse(FilePath filePath) 
			throws IOException, InterruptedException {

		if (!filePath.exists()) {
			return null;
		}

		String jsonSource = filePath.readToString();
		Hashtable<String, Number> retval = parse(jsonSource);

		return retval;
	}
	
	/***
	 * Parse the JSON source stored in the input string. The method
	 * expects a flat structure of key-value pairs with the values
	 * being a numerical value.
	 *
	 * @param jsonSource Contains a string representation of the JSON
	 * to be parsed.
	 * @return A dictionary of metric names and their values. If the
	 * file doesn't exist, the return value is null.
	 */
	public static Hashtable<String, Number> parse(String jsonSource)
	{
		Hashtable<String, Number> retval = 
				new Hashtable<String, Number>();
		
		if (jsonSource.trim().isEmpty()) {
			// return an empty dictionary
			return retval;
		}
		
		JSONObject json = new JSONObject(jsonSource);
		Iterator<String> k = json.keys();
		while (k.hasNext()) {
			String metric = k.next();
			Double value = json.getDouble(metric);
			
			retval.put(metric, value);
		}
		
		return retval;
	}
}
