import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.text.SimpleDateFormat;

public class MyCalendar extends JFrame {
  	public MyCalendar(int year, int month, int day) {
	    // Instantiate Greg Cal with given arguments 
    	Calendar cal1 = new GregorianCalendar(year, month, day);

	    // Output given date (Format the output with leading zeros for days and month)
	    SimpleDateFormat date_format = new SimpleDateFormat("yyyy/MM/dd");
	    System.out.println(date_format.format(cal1.getTime()));
  }
}