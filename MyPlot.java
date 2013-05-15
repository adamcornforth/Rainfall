import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.*; 

// JFreeChart plugin used for plotting 
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

/*
    Compile with:
    javac -classpath lib/jfreechart-1.0.14.jar;lib/jcommon-1.0.17.jar *.java
    jar cfm Driver.jar Manifest.MF *.class
    java -jar Driver.jar

    Or compile with:
    supplied compile.bat file
*/

public class MyPlot extends JFrame {
    static ChartFrame frame;
    static Rainfall rainfall;
    static boolean frameOpen = false;  
    static int frameWidth = 650, frameHeight = 450;  
    String[] monthData = null, yearData = null;

    public MyPlot(Rainfall rainfall) {
        this.rainfall = rainfall; 
    }

    public void setPlotMonth(int year, int month, int year2, int month2) { 
        if(year2 == year && month2 == month) {
            JOptionPane.showMessageDialog(frame, "Please note that you have plotted the current month and year against the same month and year. Because of this, only one plot is shown.", "", JOptionPane.PLAIN_MESSAGE);
        }
        JFreeChart chart;
        XYSeriesCollection xyDataset = new XYSeriesCollection();

        this.monthData = rainfall.returnMonthData(year, month); 
        XYSeries series1 = new XYSeries(rainfall.returnMonth(month) + " " + year);
        for(int i = 0; i < this.monthData.length; i++) {
            if(this.monthData[i] == null) 
                series1.add(i + 1, null);
            else    
                series1.add(i + 1, ((double)Math.round((Double.parseDouble(this.monthData[i])) * 100) / 100));
        }

        xyDataset.addSeries(series1);

        // if second (comparison) year selected...
        if(year2 > 0 && !(year2 == year && month2 == month)) {
            XYSeries series2; 
            this.monthData = rainfall.returnMonthData(year2, month2); 
            series2 = new XYSeries(rainfall.returnMonth(month2) + " " + year2);
            chart = ChartFactory.createXYLineChart(rainfall.returnMonth(month) + " " + year + " and " + rainfall.returnMonth(month2) + " " + year2 + " Daily Precipitation Data Comparison", "Day of Month", "Rainfall (in mm)",xyDataset,PlotOrientation.VERTICAL,true,false,false);

            for(int i = 0; i < this.monthData.length; i++) {
                if(this.monthData[i] == null)
                    series2.add(i + 1, null);
                else    
                    series2.add(i + 1, ((double)Math.round((Double.parseDouble(this.monthData[i])) * 100) / 100));
            }

            xyDataset.addSeries(series2);
        } else {
            chart = ChartFactory.createXYLineChart(rainfall.returnMonth(month) + " " + year + " Daily Precipitation Data", "Day of Month", "Rainfall (in mm)",xyDataset,PlotOrientation.VERTICAL,true,false,false);
        }

        this.initPlotFrame(chart, "Month Precipitation Data"); 
    }

    public void setPlotYear(int year, int year2) { 
        if(year2 == year) {
            JOptionPane.showMessageDialog(frame, "Please note that you have plotted the current year against the same year. Because of this, only one plot is shown.", "", JOptionPane.PLAIN_MESSAGE);
        }
        JFreeChart chart;
        XYSeriesCollection xyDataset = new XYSeriesCollection();

        this.yearData = rainfall.returnYearData(year); 
        XYSeries series1 = new XYSeries(year);
        for(int i = 0; i < this.yearData.length; i++) {
            if(this.yearData[i].equals("No Data"))
                series1.add(i + 1, null); 
            else
                series1.add(i + 1, ((double)Math.round((Double.parseDouble(this.yearData[i])) * 100) / 100));
        }

        xyDataset.addSeries(series1);

        // if second (comparison) year selected...
        if(year2 > 0 && year2 != year) {
            XYSeries series2; 
            this.yearData = rainfall.returnYearData(year2); 
            series2 = new XYSeries(year2);
            chart = ChartFactory.createXYLineChart(year + " and " + year2 + " Monthly Average Precipitation Data Comparison", "Month", "Rainfall (in mm)",xyDataset,PlotOrientation.VERTICAL,true,false,false);

            for(int i = 0; i < this.yearData.length; i++) {
                if(!this.yearData[i].equals("No Data"))
                    series2.add(i + 1, ((double)Math.round((Double.parseDouble(this.yearData[i])) * 100) / 100));
                else
                    series2.add(i + 1, null); 
            }
            xyDataset.addSeries(series2);
        } else {
            chart = ChartFactory.createXYLineChart(year + " Monthly Average Precipitation Data", "Month", "Rainfall (in mm)",xyDataset,PlotOrientation.VERTICAL,true,false,false);
        }

        this.initPlotFrame(chart, "Year Precipitation Data"); 
    }

    private void initPlotFrame(JFreeChart chart, String title) {
        chart.setBackgroundPaint(Color.white);  

        XYPlot plot = (XYPlot)chart.getPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.GREEN);
        plot.setRangeGridlinePaint(Color.orange);
        plot.setAxisOffset(new RectangleInsets(50, 0, 20, 5));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer)plot.getRenderer();       
        renderer.setBaseShapesVisible(true);
        renderer.setBaseShapesFilled (true);

        frame = new ChartFrame(title, chart);
        frame.setSize(frameWidth, frameHeight);
        frame.setVisible(true);

        frameOpen = true;
    }

    public void disposeFrame() {
        if(frameOpen) {
            frame.setVisible(false);
            frame.dispose();
            frameOpen = false;
        }
    }

} 