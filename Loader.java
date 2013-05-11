import java.io.*;
import java.util.Scanner; 
import java.lang.*;

public class Loader {
	private String[] fileContents; 
	private int fileLines; 

	/* 
		Reads from a file (given a filename) and saves line-by-line to String array this.fileContents
		also saves line number count
	*/ 
	public Loader(String file) throws IOException {
		File textFile = new File(file);
		this.fileLines = countFileLines(file); 
		String[] contentsArray = new String[this.fileLines]; 
		StringBuilder contents = new StringBuilder(); 
		try {
			// buffering, 1 line at a time
			BufferedReader input = new BufferedReader(new FileReader(textFile)); 
			try {
				String line = null; 
				int count = 0; 
				/* Readline:
				*  returns the content of a line MINUS the newline.
		        *  returns null only for the END of the stream.
		        *  returns an empty String if two newlines appear in a row.
				*/
				while ((line = input.readLine()) != null) {
					contentsArray[count] = line; 
					count++; 
				}
			}
			finally {
				input.close(); 
			}
		}
		catch(IOException ex) {
			ex.printStackTrace(); 
		}

		this.fileContents = contentsArray; 
	} 

	/* 
		Return file contents array, line by line
	*/ 
	public String[] getFileContents() {
		return this.fileContents; 
	}

	/* 
		Return file line count
	*/ 
	public int getFileLines() {
		return this.fileLines; 
	}

	/* 
		Return the number of lines in a file, given a filename
	*/ 
	private static int countFileLines(String filename) throws IOException {
	    InputStream stream = new BufferedInputStream(new FileInputStream(filename));
	    try {
	    	// init counter variables
	        int readC = 0; int i = 0; 
	        // initiliase byte (character) container 
	        byte[] bytes = new byte[1024];

	        // used for returning '1' as final result if set to false, as in 
	        // when there's characters in the text file but no carriage returns / newlines 
	        boolean noChar = true;

	        // loop through each line of file
	        while ((readC = stream.read(bytes)) != -1) {
	            noChar = false;
	            // loop through characters, checking for newline character - if so, increment count
	            for (int j = 0; j < readC; ++j) {
	                if (bytes[j] == '\n') {
	                    ++i;
	                }
	            }
	        }

	        // return '1' if i == 0 and noChar = false (Assume 1 line if there's characters 
	        // but no newline character)... else just return 'i', the number of lines
	        return (i == 0 && !noChar) ? 1 : i;
	    } finally {
	        stream.close();
	    }
	}
}