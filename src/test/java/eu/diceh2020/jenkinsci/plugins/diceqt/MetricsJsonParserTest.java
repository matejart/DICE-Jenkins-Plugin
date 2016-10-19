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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.json.JSONException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import hudson.FilePath;

public class MetricsJsonParserTest {

	@Test
	public void testParseFileNotExists() throws Exception {
		File localPath = new File("/tmp/file-not-exists-json");
		if (localPath.exists())
			throw new Exception(String.format(
					"Test implementation problem: '%s' should not exist.",
					localPath.getAbsolutePath()));
		FilePath pathNoFile = new FilePath(localPath);
		
		Dictionary<String, Number> results =
				MetricsJsonParser.parse(pathNoFile);
		
		assertNull(results);
	}
	
	@Test
	public void testParseEmpty() throws Exception {
		File tmpFile = File.createTempFile("diceci-", ".json");
		tmpFile.deleteOnExit();

		FilePath pathEmptyFile = new FilePath(tmpFile);
		
		Dictionary<String, Number> results =
				MetricsJsonParser.parse(pathEmptyFile);
		
		assertNotNull(results);
		assertEquals(0, results.size());
	}

	@Test
	public void testParseSingleEntry()
			throws IOException, InterruptedException {
		
		String fileContent = "{'latency': 123.55}";
		Hashtable<String, Number>  expected = new Hashtable<String, Number>();
		expected.put("latency", 123.55);
		
		FilePath pathSingleEntry = Utilities.createTemporaryFile(
				fileContent);

		Dictionary<String, Number> results =
				MetricsJsonParser.parse(pathSingleEntry);
		
		assertNotNull(results);
		assertEquals(1, results.size());
		assertEquals(expected, results);
	}
	
	@Test
	public void testParseMultipleEntries()
			throws IOException, InterruptedException {
		
		String fileContent = "{"
				+ "'latency': 123.55,"
				+ "'throughput': 0.0031,"
				+ "'time': 500"
				+ "}";
		
		Hashtable<String, Number>  expected = new Hashtable<String, Number>();
		expected.put("latency", 123.55);
		expected.put("throughput", 0.0031);
		expected.put("time", 500.0);
		
		FilePath pathMultipleEntries = Utilities.createTemporaryFile(
				fileContent);

		Dictionary<String, Number> results =
				MetricsJsonParser.parse(pathMultipleEntries);
		
		assertNotNull(results);
		assertEquals(expected.size(), results.size());
		assertEquals(expected, results);
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void testParseNonFlatEntres()
			throws IOException, InterruptedException {

		String fileContent = "{"
				+ "'latency': 123.55,"
				+ "'throughput': 0.0031,"
				+ "'time': 500,"
				+ "'bla': { 'subkey1': 200 }"
				+ "}";
		
		FilePath pathBadInput = Utilities.createTemporaryFile(
				fileContent);
		
		thrown.expect(JSONException.class);
		MetricsJsonParser.parse(pathBadInput);
	}
}
