import java.io.*;
import java.util.Scanner; 
import java.lang.*;

public class Driver {

	public static void main(String[] args) throws IOException {
		int fileLength; 
		Loader loader = new Loader("2RainfallDataLanc.txt"); 

		// read file contents to string array, a line per cell   
		Rainfall rainfall = new Rainfall(loader.getFileContents(), loader.getFileLines());

		// iterate years 
		for(int y = 0; y < rainfall.getNoOfYears(); y++) {
			System.out.print(rainfall.returnYear(y));
			System.out.print(" Averages <average " + ((double)Math.round(rainfall.returnYearAverage(y) * 100) / 100) + ">");
			System.out.print(" : <wettest: " + rainfall.returnMonthWettest(y) + ">");
			System.out.print(" : <driest: " + rainfall.returnMonthDriest(y) + ">");
			System.out.print("\n----------------------------------------------------------------------- \n"); 
			int col = 0; 
			for (int m = 1; m <= 12; m++) {
				col++;
				System.out.print(rainfall.returnMonth(m) + ": "); 
				System.out.print(((double)Math.round(rainfall.returnMonthAverage(y, m) * 100) / 100) + "\t"); 
				if(col % 4 == 0) System.out.print("\n"); 
			}
			System.out.print("\n\n"); 
		}

		// MyCalendar cal = new MyCalendar(2012, 0, 01);  
	}
}