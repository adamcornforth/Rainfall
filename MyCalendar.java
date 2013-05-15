// Java libraries
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

// jfreechart plugin
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

public class MyCalendar extends JFrame {
	static Rainfall rainfall;

	static JFrame calendarFrame;
	static Container contentFrame; 

	static DefaultTableModel tableModel; //Table model
	static JScrollPane calendarScroll; //The scrollframe
	static JPanel pnlCalendar, pnlYear, pnlControlsMonth, pnlControlsYear, pnlMonthView, pnlYearView, pnlTabs, pnlCalendarYear;

	static JLabel lblMonthTitle, lblChangeYear, lblChangeYear2, lblYearTitle, lblYearData;
	static JButton prevMonth, nextMonth, prevYear, nextYear, plotMonth, plotPrevMonth, plotNextMonth, plotAgainstMonth, plotYear, plotYearAgainst;
	static JTable calendarTable;
	static JComboBox cmbYear, cmbYear2, cmbYearAgainst, cmbMonthAgainst, cmbYearAgainst2;
	static JTabbedPane tabbedPane;

	static int maxYear, maxMonth, selectedYear, selectedMonth;
	private int noOfDays; 

	public MyCalendar (Rainfall rainfall, int maxYear, int maxMonth) {
		String[] months = rainfall.returnMonthsArray();
		this.maxYear = maxYear;
		this.maxMonth = maxMonth;
		this.rainfall = rainfall;  

		// Set selected year/month to max year/month
		selectedMonth = maxMonth; 
		selectedYear = maxYear;

		// Look and feel (set to native look and feel of OS)
		try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
		catch (ClassNotFoundException e) {}
		catch (InstantiationException e) {}
		catch (IllegalAccessException e) {}
		catch (UnsupportedLookAndFeelException e) {}

		// Set frame
		calendarFrame = new JFrame ("Rainfall Data"); 
		calendarFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit program when frame closed
		calendarFrame.setSize(830, 710); // Set size
		contentFrame = calendarFrame.getContentPane(); 
		contentFrame.setLayout(null); // Remove layout

		// Two panels for tabs
		pnlTabs = new JPanel(null);
		pnlMonthView = new JPanel(null);
		pnlYearView = new JPanel(null);

		// Tabs 
		tabbedPane = new JTabbedPane();		
		tabbedPane.addTab("Year (" + maxYear + ")", pnlYearView);
		tabbedPane.addTab("Month (" + rainfall.returnMonth(maxMonth) + " " + maxYear + ")", pnlMonthView); 

		// Set labels, buttons, tables, panels and comboboxes
		lblMonthTitle = new JLabel ("");
		lblChangeYear = new JLabel ("Change year:");
		lblChangeYear2 = new JLabel ("Change year:");
		lblYearTitle = new JLabel ("");
		lblYearData = new JLabel(""); 
		cmbYear = new JComboBox();
		cmbYear2 = new JComboBox();
		cmbYearAgainst = new JComboBox();
		cmbMonthAgainst = new JComboBox();
		cmbYearAgainst2 = new JComboBox();
		prevMonth = new JButton ("<html>&laquo; Prev Month</html>");
		nextMonth = new JButton ("<html>Next Month &raquo;</html>");
		plotMonth = new JButton ("Plot Month");
		plotNextMonth = new JButton ("Plot Against Next Month");
		plotPrevMonth = new JButton ("Plot Against Prev Month");
		plotAgainstMonth = new JButton ("Plot Against: ");
		plotYear = new JButton ("Plot Year");
		plotYearAgainst = new JButton ("Plot Against: ");
		prevYear = new JButton ("<html>&laquo; Prev Year</html>");
		nextYear = new JButton ("<html>Next Year &raquo;</html>");
		tableModel = new DefaultTableModel(){public boolean isCellEditable(int rowIndex, int mColIndex){return false;}};
		calendarTable = new JTable(tableModel);
		calendarScroll = new JScrollPane(calendarTable);
		pnlTabs = new JPanel(new BorderLayout()); // panel for top tabs
		pnlCalendar = new JPanel(null);
		pnlYear = new JPanel(null);
		pnlControlsMonth = new JPanel(null);
		pnlControlsYear = new JPanel(null);
		pnlCalendarYear = new JPanel(null);

		// Set borders
		pnlCalendar.setBorder(BorderFactory.createTitledBorder("Month Data"));
		pnlYear.setBorder(BorderFactory.createTitledBorder("Year Data"));
		pnlControlsMonth.setBorder(BorderFactory.createTitledBorder("Month Plot Controls"));
		pnlControlsYear.setBorder(BorderFactory.createTitledBorder("Year Plot Controls"));

		// Set action listeners
		prevMonth.addActionListener(new prevMonth_Listener());
		nextMonth.addActionListener(new nextMonth_Listener());
		nextYear.addActionListener(new nextYear_Listener());
		prevYear.addActionListener(new prevYear_Listener());
		cmbYear.addActionListener(new cmbYear_Listener());
		cmbYear2.addActionListener(new cmbYear2_Listener());
		plotMonth.addActionListener(new plotMonth_Listener());
		plotPrevMonth.addActionListener(new plotPrevMonth_Listener());
		plotNextMonth.addActionListener(new plotNextMonth_Listener());
		plotAgainstMonth.addActionListener(new plotAgainstMonth_Listener());
		plotYear.addActionListener(new plotYear_Listener());
		plotYearAgainst.addActionListener(new plotYearAgainst_Listener());
		
		// Add tab pane to tab panel
		pnlTabs.add(tabbedPane); 

		// Add panels to Month View
		pnlMonthView.add(pnlCalendar);
		pnlMonthView.add(pnlControlsMonth);

		// Add Panels to Year View
		pnlYearView.add(pnlCalendarYear); // no content in pnlCalendarYear yet
		pnlYearView.add(pnlYear); 
		pnlYearView.add(pnlControlsYear); 

		// Add tab panel to frame
		contentFrame.add(pnlTabs);

		// Add content to calendar panel
		pnlCalendar.add(lblMonthTitle);
		pnlCalendar.add(lblChangeYear);
		pnlCalendar.add(cmbYear);
		pnlCalendar.add(prevMonth);
		pnlCalendar.add(nextMonth);
		pnlCalendar.add(calendarScroll);

		// Add content to year panel
		pnlYear.add(lblYearTitle); 
		pnlYear.add(prevYear); 
		pnlYear.add(nextYear); 
		pnlYear.add(lblChangeYear2);
		pnlYear.add(cmbYear2);
		pnlYear.add(lblYearTitle); 
		pnlYear.add(lblYearData);

		// Add content to month controls panel
		pnlControlsMonth.add(plotMonth);
		pnlControlsMonth.add(plotPrevMonth);
		pnlControlsMonth.add(plotNextMonth);
		pnlControlsMonth.add(plotAgainstMonth);
		pnlControlsMonth.add(cmbYearAgainst);
		pnlControlsMonth.add(cmbMonthAgainst);

		// Add content to year controls panel
		pnlControlsYear.add(plotYear); 
		pnlControlsYear.add(plotYearAgainst); 
		pnlControlsYear.add(cmbYearAgainst2);

		// Set bounds on Month View, Year View and Tabbed Pane
		pnlTabs.setBounds(15, 15, 793, 650); 
		tabbedPane.setBounds(8, 6, 780, 250);
		pnlMonthView.setBounds(0, 35, 780, 650); 
		pnlYearView.setBounds(0, 35, 780, 100); 

		// Set bounds on Calendar panel
		pnlCalendar.setBounds(3, 3, 780, 542);
		lblMonthTitle.setBounds(400-(lblMonthTitle.getPreferredSize().width)/2, 50, 500, 25);
		lblChangeYear.setBounds(608, 15, 85, 20);
		cmbYear.setBounds(679, 15, 90, 20);
		prevMonth.setBounds(10, 42, 120, 35);
		nextMonth.setBounds(651, 42, 120, 35);
		calendarScroll.setBounds(10, 85, 760, 447);

		// Set bounds on year panel
		pnlYear.setBounds(3, 3, 780, 542);
		lblYearTitle.setBounds(400-(lblYearTitle.getPreferredSize().width)/2, 50, 500, 25); 
		lblChangeYear2.setBounds(608, 15, 85, 20); 
		cmbYear2.setBounds(679, 15, 90, 20);
		prevYear.setBounds(10, 42, 120, 35);
		nextYear.setBounds(651, 42, 120, 35); 
		lblYearData.setBounds(10, 85, 760, 447); 

		// Set bounds on month controls panel
		pnlControlsMonth.setBounds(3, 548, 780, 65);
		plotMonth.setBounds(10, 20, 90, 35);
		plotPrevMonth.setBounds(112, 20, 150, 35);
		plotNextMonth.setBounds(264, 20, 150, 35);
		plotAgainstMonth.setBounds(425, 20, 100, 35);
		cmbMonthAgainst.setBounds(535, 27, 120, 20); 
		cmbYearAgainst.setBounds(665, 27, 90, 20); 

		// Set bounds on year conrols panel
		pnlControlsYear.setBounds(3, 548, 780, 65);
		plotYear.setBounds(10, 20, 80, 35);
		plotYearAgainst.setBounds(92, 20, 100, 35);
		cmbYearAgainst2.setBounds(198, 27, 90, 20); 
		
		
		// Add weeks headers
		String[] weeks = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"}; //All weeks
		for (int i=0; i < 7; i++)
			tableModel.addColumn(weeks[i]);
		
		calendarTable.getParent().setBackground(calendarTable.getBackground()); //Set background

		//No resize/reorder, cell selection, and row/col count
		calendarTable.setRowHeight(70);
		tableModel.setColumnCount(7); tableModel.setRowCount(6);
		calendarTable.setColumnSelectionAllowed(false);
		calendarTable.setRowSelectionAllowed(false);
		calendarTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		calendarTable.getTableHeader().setResizingAllowed(false);
		calendarTable.getTableHeader().setReorderingAllowed(false);
		
		//Populate combo boxes
		for (int i = maxYear-rainfall.getNoOfYears(); i <= maxYear; i++){
			cmbYear.addItem(String.valueOf(i));
			cmbYear2.addItem(String.valueOf(i));
			cmbYearAgainst.addItem(String.valueOf(i)); 
			cmbYearAgainst2.addItem(String.valueOf(i)); 
		}

		// populate months combo box
		for(int i = 0; i < 11; i++)
			cmbMonthAgainst.addItem(months[i]);
		
		loadCalendar(maxYear, maxMonth); // Load calendar

		// Show Frame
		calendarFrame.setResizable(false);
		calendarFrame.setVisible(true);
	}

	public static void loadCalendar(int year, int month){
		selectedMonth = month; selectedYear = year; 
		String[] months = rainfall.returnMonthsArray(); 
		String yearData; 
		int startOfMonth, noOfDays;
			 
		// Update Buttons
		prevMonth.setEnabled(true); nextMonth.setEnabled(true); prevYear.setEnabled(true); nextYear.setEnabled(true);

		if (month == 0 && year <= maxYear-rainfall.getNoOfYears()){prevMonth.setEnabled(false);} // At first Year
		if (year == maxYear-rainfall.getNoOfYears()) {prevYear.setEnabled(false);} // At first Year
		if (month == 11 && year >= maxYear){nextMonth.setEnabled(false);} // At last year
		if (year == maxYear) {nextYear.setEnabled(false);} // At last Year

		// Update tab titles
		tabbedPane.setTitleAt(0, "Year (" + year + ")");
		tabbedPane.setTitleAt(1, "Month (" + rainfall.returnMonth(month) + " " + year + ")");

		// Load the year data label
		lblYearTitle.setText("<html><p align='center'><strong>Year " + String.valueOf(year) + " Rainfall Data (in mm)</strong> <br /> Year Average: " + rainfall.returnYearAverage(year) + "mm </p></html>"); 
		yearData = "<html>";
		yearData += "<p align='center'  style='width: 590px;'><br /> <br />Wettest Month: <strong>" + rainfall.returnMonthWettest(year)+ "</strong> <br />Driest Month: <strong>" + rainfall.returnMonthDriest(year)+ "</strong> <br /><br /><br />";
		for (int m = 0; m < 12; m++) // loop months
			yearData += "<strong><u>"+ rainfall.returnMonth(m) +"</u></strong> <br /> Min: <strong>" + rainfall.returnMonthDriestDay(year, m) + "</strong>, Max: <strong>" + rainfall.returnMonthWettestDay(year, m) + "</strong>,  Average: <strong>" + rainfall.returnMonthAverage(year, m) + "</strong><br />"; 
		yearData +="<br /> <br /><br /></p></html>";  
		lblYearData.setText(yearData); 

		// Load the month label 
		lblMonthTitle.setText("<html><p align='center'><strong>" + months[month] + " " + year + " Rainfall Data (in mm)</strong> <br />Average: " + rainfall.returnMonthAverage(year, month) + "" + "</p></html>"); 
		lblMonthTitle.setBounds(400-(lblMonthTitle.getPreferredSize().width)/2, 50, 500, 25);
		lblYearTitle.setBounds(400-(lblMonthTitle.getPreferredSize().width)/2, 50, 500, 25);

		// Select this year in combo boxes
		cmbYear.setSelectedItem(String.valueOf(year));  
		cmbYear2.setSelectedItem(String.valueOf(year)); 
		
		// Clear table
		for (int i=0; i<6; i++)
			for (int j=0; j<7; j++)
				tableModel.setValueAt(null, i, j);
		
		// Get start of month (year, month, 1), meaning (year, month, 1st day of month) so can use DAY_OF_WEEK constant to access first day of week value for a given year/month
		GregorianCalendar gregCalendar = new GregorianCalendar(year, month, 1);
		startOfMonth = (gregCalendar.get(GregorianCalendar.DAY_OF_WEEK) - 3 == -2) ? 5 : gregCalendar.get(GregorianCalendar.DAY_OF_WEEK) - 3; // due to using -3 to hack a monday start of the week, have to change -2 (sunday) to 5 to avoid out of bounds errors

		// Draw calendar items, using rainfall object to retreive data for set year/month/day (variables year/month/i). Loops through months's days using GregorianCalendar's DAY_OF_MONTH constant
		for (int i=1; i<=gregCalendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH); i++)
			tableModel.setValueAt("<html><p align='center' style='width: 77px;'><strong>" + i + "</strong>. " + rainfall.retreiveFromDate(year, month, i) + "<br /><em style='font-size: 8px;'>" + rainfall.retreivePrevChangeFromDate(year, month, i) + "</em><br /><em style='font-size: 8px;'>" + rainfall.retreiveNextChangeFromDate(year, month, i) + "</em></p></html>", new Integer((i+startOfMonth) / 7), (i+startOfMonth) % 7);

		// Apply extended renderer to grey out cells with null values (ie. not set by the above loop)
		calendarTable.setDefaultRenderer(calendarTable.getColumnClass(0), new calendarTableRenderer()); 
	}

	static class calendarTableRenderer extends DefaultTableCellRenderer{
		public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
			JLabel renderedLabel = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (value == null) // Not a day of the month, colour grey
				renderedLabel.setBackground(new Color(240, 240, 240));
			else // just colour white
				renderedLabel.setBackground(new Color(255, 255, 255));
		    return renderedLabel;
		}
	} 

	static class prevMonth_Listener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			if (selectedMonth == 0) // Decrement one year
				loadCalendar(--selectedYear, 11);
			else // Decrement one month
				loadCalendar(selectedYear, --selectedMonth);
		}
	}

	static class nextMonth_Listener implements ActionListener {                       
		public void actionPerformed (ActionEvent e) {
			if (selectedMonth == 11) //Increment one year
				loadCalendar(++selectedYear, 0);
			else // Increment one month
				loadCalendar(selectedYear, ++selectedMonth);
		}
	}

	static class prevYear_Listener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			loadCalendar(--selectedYear, selectedMonth);
		}
	}
	
	static class nextYear_Listener implements ActionListener {                       
		public void actionPerformed (ActionEvent e) {
			loadCalendar(++selectedYear, selectedMonth);
		}
	}

	static class cmbYear_Listener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			if (cmbYear.getSelectedItem() != null)
				loadCalendar(Integer.parseInt(cmbYear.getSelectedItem().toString()), selectedMonth);
		}
	}

	static class cmbYear2_Listener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			if (cmbYear2.getSelectedItem() != null)
				loadCalendar(Integer.parseInt(cmbYear2.getSelectedItem().toString()), selectedMonth);
		}
	}

	static class plotMonth_Listener implements ActionListener {
		static MyPlot frmPlot = new MyPlot(rainfall);
		public void actionPerformed (ActionEvent e) {
			this.frmPlot.disposeFrame();
			frmPlot.setPlotMonth(selectedYear, selectedMonth, 0, 0);
		}
	}

	static class plotPrevMonth_Listener implements ActionListener {
		static MyPlot frmPlot = new MyPlot(rainfall);
		public void actionPerformed (ActionEvent e) {
			this.frmPlot.disposeFrame();
			if (selectedMonth == 0) // Decrement one year
				frmPlot.setPlotMonth(selectedYear, selectedMonth, selectedYear - 1, 11);
			else // Decrement one month
				frmPlot.setPlotMonth(selectedYear, selectedMonth, selectedYear, selectedMonth - 1);
		}
	}

	static class plotNextMonth_Listener implements ActionListener {
		static MyPlot frmPlot = new MyPlot(rainfall);
		public void actionPerformed (ActionEvent e) {
			this.frmPlot.disposeFrame();
			if (selectedMonth == 11) // Decrement one year
				frmPlot.setPlotMonth(selectedYear, selectedMonth, selectedYear + 1, 0);
			else // Decrement one month
				frmPlot.setPlotMonth(selectedYear, selectedMonth, selectedYear, selectedMonth + 1);
		}
	}

	static class plotAgainstMonth_Listener implements ActionListener {
		static MyPlot frmPlot = new MyPlot(rainfall);
		public void actionPerformed (ActionEvent e) {
			this.frmPlot.disposeFrame();
			frmPlot.setPlotMonth(selectedYear, selectedMonth, Integer.parseInt(cmbYearAgainst.getSelectedItem().toString()), rainfall.returnIndexOfMonth(cmbMonthAgainst.getSelectedItem().toString()));
		}
	}

	static class plotYear_Listener implements ActionListener {
		static MyPlot frmPlot = new MyPlot(rainfall);
		public void actionPerformed (ActionEvent e) {
			this.frmPlot.disposeFrame();
			frmPlot.setPlotYear(selectedYear, 0);
		}
	}

	static class plotYearAgainst_Listener implements ActionListener {
		static MyPlot frmPlot = new MyPlot(rainfall);
		public void actionPerformed (ActionEvent e) {
			this.frmPlot.disposeFrame();
			frmPlot.setPlotYear(selectedYear, Integer.parseInt(cmbYearAgainst2.getSelectedItem().toString()));
		}
	}

} 
