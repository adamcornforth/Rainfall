public class Rainfall {
	// data
	private String[][][] data; 
	private String[] years; 

	// month identifiers
	final private String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};  

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
		this.contentLength = (fileLength - 3); 
		this.noOfYears = (this.contentLength / 12); 
		if(this.contentLength % 12 != 0) this.noOfYears++; // add a year if remainder (ie. for if an incomplete year is at the end)

		// prepare for storage
		this.data = new String[this.noOfYears][12][31]; 
		this.years = new String[this.noOfYears]; 

		/*
			Data is read from file from bottom to top, and stored with the most recent year (closest to bottom of file)
			at index 0 of [YEAR], and the most recent month at index 0 [12], the same with days. See below: 
			String[][][] data = new String[YEARS][MONTHS][DAYS];
		*/ 
		for (int i = 0; i < this.noOfYears; i++) {
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
		Returns month at index m, with lowest index being the most recent month 
	*/
	public String returnMonth(int m) {
		if(m <= 0 || m > 12) return "This month does not exist!"; 
		return this.months[m - 1]; 
	}

	/*
		Returns true if data exists at this.data[year][month-1][day-1]
		Method abstracts out the need for users to account for zero-indexing
		of the months and days
	*/
	public Boolean dataExistsAtDate(int year, int month, int day) {
		if(this.data[year][month - 1][day - 1] == null) return false; 
		return true; 
	}

	/*
		Returns string of data if data exists at this.data[year][month-1][day-1]
		Method abstracts out the need for users to account for zero-indexing
		of the months and days
	*/
	public String retreiveFromDate(int year, int month, int day) {
		if(month <= 0 || day <= 0) return "Error #1. Please do not use zero-values for the month and days"; 
		if(this.data[year][month - 1][day - 1] == null) return null; 

		return  String.format("%02d", day) + "/" + String.format("%02d", month) + "/" + this.years[year] + " = " + data[year][month - 1][day - 1]; 
	}

	/*
		Returns average of month data if data exists at this.data[year][month-1]
		Method abstracts out the need for users to account for zero-indexing
		of the month
	*/
	public float returnYearAverage(int year) {
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
		return (total/count); 
	}

	/*
		Returns wettest month of given year
	*/
	public String returnMonthWettest(int year) {
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
		return (this.months[maxMonthIndex]); 
	}

	/*
		Returns driest month of given year
	*/
	public String returnMonthDriest(int year) {
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
		return (this.months[minMonthIndex]); 
	}

	/*
		Returns average of month data if data exists at this.data[year][month-1]
		Method abstracts out the need for users to account for zero-indexing
		of the month
	*/
	public float returnMonthAverage(int year, int month) {
		if(month <= 0 || month > 12) return 0; 
		int count = 0; float total = 0;  
		for (int d = 0; d < 31; d++) {
			if(this.data[year][month - 1][d] != null) {
				count++; 
				total += Float.parseFloat(this.data[year][month - 1][d]); 
			}	
		}
		return (total/count); 
	}

}