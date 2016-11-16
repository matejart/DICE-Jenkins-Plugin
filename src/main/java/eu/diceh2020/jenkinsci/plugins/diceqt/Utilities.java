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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import hudson.FilePath;

public class Utilities {

	public static FilePath createTemporaryFile(String fileContent)
			throws IOException {
		File tmpFile = File.createTempFile("diceci-", ".json");
		tmpFile.deleteOnExit();

		FileOutputStream fout = new FileOutputStream(tmpFile);
		OutputStreamWriter stream = new OutputStreamWriter(
				fout, Charset.forName("UTF-8"));

		stream.write(fileContent);

		stream.close();
		fout.close();

		FilePath retval = new FilePath(tmpFile);

		return retval;
	}

	public static Hashtable<String, Number> clone(Hashtable<String, Number> input) {
		Hashtable<String, Number> retval = new Hashtable<String, Number>();
		Enumeration<String> en = input.keys();
		while (en.hasMoreElements()) {
			String key = en.nextElement();
			retval.put(key, input.get(key));
		}
		return retval;
	}

	public static ArrayList<String> clone(ArrayList<String> input) {
		ArrayList<String> retval = new ArrayList<String>(input);
		return retval;
	}
}
