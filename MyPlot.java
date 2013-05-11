import javax.swing.JFrame;
import java.awt.Color;

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

    public MyPlot(Rainfall rainfall) {
        this.rainfall = rainfall; 
    }

    public void setPlot(int year, int month, int year2, int month2) { 
        String[] monthData = null;
        JFreeChart chart;
        XYSeriesCollection xyDataset = new XYSeriesCollection();

        monthData = rainfall.returnMonthData(year, month); 
        XYSeries series1 = new XYSeries(rainfall.returnMonth(month) + " " + year);
        for(int i = 0; i < monthData.length; i++)
            series1.add(i + 1, ((double)Math.round((Double.parseDouble(monthData[i])) * 100) / 100));


        xyDataset.addSeries(series1);

        // if second (comparison) year selected...
        if(year2 > 0) {
            XYSeries series2; 
            monthData = rainfall.returnMonthData(year2, month2); 
            if((month2 > month && year2 < year) || (year2 == year && month > month2)) {
                series2 = new XYSeries("(Previous Month) " + rainfall.returnMonth(month2) + " " + year2);
                chart = ChartFactory.createXYLineChart(rainfall.returnMonth(month) + " " + year + " and " + rainfall.returnMonth(month2) + " " + year2 + " (Previous Month) Daily Precipitation Data Comparison", "Day of Month", "Rainfall (in mm)",xyDataset,PlotOrientation.VERTICAL,true,false,false);
            } else {
                series2 = new XYSeries("(Next Month) " + rainfall.returnMonth(month2) + " " + year2);
                chart = ChartFactory.createXYLineChart(rainfall.returnMonth(month) + " " + year + " and " + rainfall.returnMonth(month2) + " " + year2 + " (Next Month) Daily Precipitation Data Comparison", "Day of Month", "Rainfall (in mm)",xyDataset,PlotOrientation.VERTICAL,true,false,false);
            }

            for(int i = 0; i < monthData.length; i++)
                series2.add(i + 1, ((double)Math.round((Double.parseDouble(monthData[i])) * 100) / 100));
            xyDataset.addSeries(series2);
        } else {
            chart = ChartFactory.createXYLineChart(rainfall.returnMonth(month) + " " + year + " Daily Precipitation Data", "Day of Month", "Rainfall (in mm)",xyDataset,PlotOrientation.VERTICAL,true,false,false);
        }

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

        frame = new ChartFrame("Precipitation Data", chart);
        frame.setSize(650, 450);
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