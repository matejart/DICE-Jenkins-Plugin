package eu.diceh2020.jenkinsci.plugins.diceqt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import hudson.FilePath;

public class Utilities {

	public static FilePath createTemporaryFile(String fileContent)
			throws IOException {
		File tmpFile = File.createTempFile("diceci-", ".json");
		tmpFile.deleteOnExit();
		
		FileOutputStream fout = new FileOutputStream(tmpFile);
		OutputStreamWriter stream = new OutputStreamWriter(fout);
		
		stream.write(fileContent);
		
		stream.close();
		fout.close();
		
		FilePath retval = new FilePath(tmpFile);
		
		return retval;
	}

}
