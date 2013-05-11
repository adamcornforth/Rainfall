import java.io.*;
import java.util.Scanner; 
import java.lang.*;

public class Driver {

	public static void main(String[] args) throws IOException {
		// read file contents to string array, a line per index   
		Loader loader = new Loader("2RainfallDataLanc.txt"); 
		Rainfall rainfall = new Rainfall(loader.getFileContents(), loader.getFileLines());
		MyCalendar cal = new MyCalendar(rainfall, 2012, 0);  
	}
}