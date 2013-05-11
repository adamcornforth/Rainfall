// Java Libraries
import java.util.*;

public class Rainfall {
	// data
	private String[][][] data; 
	private String[] years; 

	// month identifiers
	final private String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};  

	// general rainfall file data
	private int noOfYears;
	private int contentLength; 

	/*
		Create the data structure that stores all the data from the single array.
		Stored as: [Year] x [Month] x [Day] in a 3D Array. 

		Done by finding out how many years are in the file then looping through every year 
		(and it's associated months) and adding the days from each month into the array, 
		in reverse order of month and reverse order of year so as to sort out the fact 
		that the rainfall data is stored with the newest data first. 
	*/
	public Rainfall(String[] fileStringArray, int fileLength) {
		// find out how many years are in file, deducting 3 due to the header 
		this.contentLength = (fileLength - 4); 
		this.noOfYears = (this.contentLength / 12); 
		// if(this.contentLength % 12 != 0) this.noOfYears++; // add a year if remainder (ie. for if an incomplete year is at the end)

		// prepare for storage
		this.data = new String[this.noOfYears + 1][12][31]; 
		this.years = new String[this.noOfYears + 1]; 
 
		/*
			Data is read from file from bottom to top, and stored with the most recent year (closest to bottom of file)
			at index 0 of [YEAR], and the most recent month at index 0 [12], the same with days. See below: 
			String[][][] data = new String[YEARS][MONTHS][DAYS];
		*/ 
		for (int i = 0; i <= this.noOfYears; i++) {
			String[] parts; 

			// loops months backwards so that we can store them in the correct order
			for (int m = 11; m >= 0; m--) { 
				// split current month at every space 
				parts = fileStringArray[(fileLength - 1) - ((i * 12) + m)].split("\\s+"); 

				this.years[i] = parts[0]; 

				// split data into day columns in data array
				for (int d = 0; d < 31 ; d++) {
					if(!("").equals(parts[d + 2]) && !parts[d + 2].equals("-99.99"))
						this.data[i][11 - m][d] = parts[d + 2]; 
					else 
						this.data[i][11 - m][d] = null; 
				}
			}
		}
	}

	/* 
		Returns number of years recorded in the text file
	*/
	public int getNoOfYears() {
		return this.noOfYears; 
	}

	/* 
		Returns year at index y, with lowest index being the most recent years 
		recorded in the text file
	*/
	public String returnYear(int y) {
		if(y < 0 || y > this.noOfYears) return "No Data for Year"; 
		return this.years[y]; 
	}

	/* 
		Returns the index (key) of the year denoted by 'y'
	*/
	public int returnIndexOfYear(int y) {
		for (int i = 0; i <= this.noOfYears; i++) {
			if(years[i].equals(String.valueOf(y)))
				return i;
		}
		return -1;
	}

	/* 
		Returns month at index m, with lowest index being the most recent month 
	*/
	public String returnMonth(int m) {
		if(m < 0 || m > 12) return "This month does not exist!"; 
		return this.months[m]; 
	}

	/*
		Returns true if data exists at this.data[year][month-1][day-1]
		Method abstracts out the need for users to account for zero-indexing
		of the months and days
	*/
	public Boolean dataExistsAtDate(int year, int month, int day) {
		year = this.returnIndexOfYear(year); 
		if(this.data[year][month - 1][day - 1] == null) return false; 
		return true; 
	}

	/*
		Returns string of data if data exists at this.data[year][month-1][day-1]
		Method abstracts out the need for users to account for zero-indexing
		of the months and days
	*/
	public String retreiveFromDate(int year, int month, int day) {
		year = this.returnIndexOfYear(year); 
		if(month < 0 || day < 0) return "Error #1. Please do not use zero-values for the month and days"; 
		if(this.data[year][month][day - 1] == null) return "No Data"; 

		return  data[year][month][day - 1] + "mm"; 
	}

	/*
		Returns the change from the previous month of the given year, month and day
	*/
	public String retreivePrevChangeFromDate(int year, int month, int day) {
		int prevYear, prevMonth;
		year = this.returnIndexOfYear(year); 
		if(year < 0 || year > this.noOfYears) return "No Data"; 
		if(month < 0 || day < 0) return "Error #1. Please do not use zero-values for the days"; 
		if(this.data[year][month][day - 1] == null) return "No Data"; 
		if (month == 0) { // Decrement one year
			prevMonth = 11; prevYear = year + 1; 
		} else { // Decrement one month
			prevMonth = month - 1; prevYear = year; 
		}
		if(prevYear < 0 || prevYear > this.noOfYears) return "No Previous Year"; 
		if(this.data[prevYear][prevMonth][day - 1] == null) return "No Previous Mo. Data"; 
		double result = ((double)(Math.round((Double.parseDouble(data[year][month][day - 1]) - Double.parseDouble(data[prevYear][prevMonth][day - 1]))*100))/100);
		if(result < 0.001 && result > -0.001) return "<span style='color: blue'>No Prev Mo. Change</span>";
		return (result >= 0 ? "<span style='color: red'>+" + result + "mm</span> Prev Mo." : "<span style='color: green'>" + result + "mm</span> Prev Mo."); 
	}

	/*
		Returns the change from the next month of the given year, month and day
	*/
	public String retreiveNextChangeFromDate(int year, int month, int day) {
		int nextYear, nextMonth;
		year = this.returnIndexOfYear(year); 
		if(year < 0 || year > this.noOfYears) return "No Data"; 
		if(month < 0 || day < 0) return "Error #1. Please do not use zero-values for the days"; 
		if(this.data[year][month][day - 1] == null) return "No Data"; 
		if (month == 11) { // Increment one year
			nextMonth = 0; nextYear = year - 1; 
		} else { // Increment one month
			nextMonth = month + 1; nextYear = year; 
		}
		if(nextYear < 0 || nextYear > this.noOfYears) return "No Next Year"; 
		if(this.data[nextYear][nextMonth][day - 1] == null) return "No Next Mo. Data"; 
		double result = ((double)(Math.round((Double.parseDouble(data[year][month][day - 1]) - Double.parseDouble(data[nextYear][nextMonth][day - 1]))*100))/100);
		if(result < 0.001 && result > -0.001) return "<span style='color: blue'>No Next Mo. Change</span>";
		return (result >= 0 ? "<span style='color: red'>+" + result + "mm</span> Next Mo." : "<span style='color: green'>" + result + "mm</span> Next Mo."); 
	}

	/*
		Returns average of month data if data exists at this.data[year][month-1]
		Method abstracts out the need for users to account for zero-indexing
		of the month
	*/
	public double returnYearAverage(int year) {
		year = this.returnIndexOfYear(year); 
		if(year < 0 || year > this.noOfYears) return 0; 
		int count = 0; float total = 0;  
		for(int m = 0; m < 12; m++) {
			for (int d = 0; d < 31; d++) {
				if(this.data[year][m][d] != null) {
					count++; 
					total += Float.parseFloat(this.data[year][m][d]); 
				}	
			}
		}
		return ((double)Math.round((total/count) * 100) / 100);  
	}

	/*
		Returns wettest month of given year
	*/
	public String returnMonthWettest(int year) {
		year = this.returnIndexOfYear(year); 
		if(year < 0 || year > this.noOfYears) return "None"; 
		float total = 0;  
		float maxMonthTotal = 0; 
		int maxMonthIndex = 0; 
		int count = 0; 
		for(int m = 0; m < 12; m++) {
			for (int d = 0; d < 31; d++) {
				if(this.data[year][m][d] != null) {
					total += Float.parseFloat(this.data[year][m][d]); 
					count++;
				}	
			}
			total = total/count; 
			if(total > maxMonthTotal) {
				maxMonthTotal = total;
				maxMonthIndex = m; 
			} 
			total = 0; 
			count = 0;
		}
		return (this.months[maxMonthIndex]) + " ("+((double)Math.round(maxMonthTotal * 100) / 100)+"mm)";  
	}
 
	/*
		Returns driest month of given year
	*/
	public String returnMonthDriest(int year) {
		year = this.returnIndexOfYear(year); 
		if(year < 0 || year > this.noOfYears) return "None"; 
		float total = 0;  
		float minMonthTotal = 0; 
		int minMonthIndex = 0; 
		int count = 0; 
		for(int m = 0; m < 12; m++) {
			for (int d = 0; d < 31; d++) {
				if(this.data[year][m][d] != null) {
					total += Float.parseFloat(this.data[year][m][d]); 
					count++;
				}	
			}
			if(m == 0) minMonthTotal = total/count; 
			total = total/count; 
			if(total < minMonthTotal) {
				minMonthTotal = total;
				minMonthIndex = m; 
			} 
			total = 0; 
			count = 0;
		}
		return (this.months[minMonthIndex]) + " ("+((double)Math.round(minMonthTotal * 100) / 100)+"mm)"; 
	}

	/*
		Returns average of month data if data exists at this.data[year][month]
	*/
	public String returnMonthAverage(int year, int month) {
		year = this.returnIndexOfYear(year); 
		if(month < 0 || month > 12) return "No Data"; 
		int count = 0; float total = 0;  
		for (int d = 0; d < 31; d++) {
			if(this.data[year][month][d] != null) {
				count++; 
				total += Float.parseFloat(this.data[year][month][d]); 
			}	
		}
		if (count == 0) return "No Data"; 
		return String.valueOf(((double)Math.round(total/count * 100) / 100)) + "mm";  
	}

	/*
		Returns wettest day in month data if data exists at this.data[year][month]
	*/
	public String returnMonthWettestDay(int year, int month) {
		year = this.returnIndexOfYear(year); 
		if(month < 0 || month > 12) return "No Data"; 
		float wettestDay;
		if(this.data[year][month][0] != null) {
			wettestDay = Float.parseFloat(this.data[year][month][0]); 
		} else {
			return "No Data";
		}		int count = 0; float total = 0;  
		for (int d = 0; d < 31; d++) {
			if(this.data[year][month][d] != null) {
				count++; 
				wettestDay = (Float.parseFloat(this.data[year][month][d]) > wettestDay) ? Float.parseFloat(this.data[year][month][d]) : wettestDay; 
			}	
		}
		if (count == 0) return "No Data"; 
		return String.valueOf(((double)Math.round(wettestDay * 100) / 100)) + "mm";   
	}

	/*
		Returns driest day in month data if data exists at this.data[year][month]
	*/
	public String returnMonthDriestDay(int year, int month) {
		year = this.returnIndexOfYear(year); 
		if(month < 0 || month > 12) return "No Data"; 
		float driestDay;
		if(this.data[year][month][0] != null) {
			driestDay = Float.parseFloat(this.data[year][month][0]); 
		} else {
			return "No Data";
		}
		int count = 0; float total = 0;  
		for (int d = 0; d < 31; d++) { 
			if(this.data[year][month][d] != null) {
				count++; 
				driestDay = (Float.parseFloat(this.data[year][month][d]) < driestDay) ? Float.parseFloat(this.data[year][month][d]) : driestDay; 
			}	
		}
		if (count == 0) return "No Data"; 
		return String.valueOf(((double)Math.round(driestDay * 100) / 100)) + "mm";  
	}

	/* 
		Returns the month array 
	*/
	public String[] returnMonthsArray() {
		return this.months; 
	}

	/* 
		Returns month data, as an array
	*/
	public String[] returnMonthData(int year, int month) {
		GregorianCalendar gregCalendar = new GregorianCalendar(year, month, 1);
		String[] monthArray; 
		monthArray = new String[31];
		year = this.returnIndexOfYear(year); 
		if(month < 0 || month > 12) return null; 
		for (int d = 0; d < 31; d++) {
			if(this.data[year][month][d] != null) 
				monthArray[d] = this.data[year][month][d];
			else
				monthArray[d] = "0";
		}

		return monthArray;
	}

}