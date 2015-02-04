package analyzer.graphs;

import java.awt.Dimension;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JPanel;

import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class PlotHistogram{
	private static int numBins;
	private static String title;
	private ChartPanel chart;

	public PlotHistogram(String t, double[] data, int n) {
		numBins = n;
		title = t;
		//JPanel jpanel = createDemoPanel(data);
		//jpanel.setPreferredSize(new Dimension(500, 270));
		//setContentPane(jpanel);
	}
	
//	public JPanel getHistogram() {
//		return jpanel;
//	}
	
	private IntervalXYDataset createDataset(double[] data) {
		HistogramDataset histogramdataset = new HistogramDataset();
		histogramdataset.addSeries("simulation results", data, numBins);
		return histogramdataset;
	}
	

	private JFreeChart createChart(
			IntervalXYDataset intervalxydataset) {
		JFreeChart jfreechart = ChartFactory.createHistogram(
				title, null, null,
				intervalxydataset, PlotOrientation.VERTICAL,
				true, true, false);
		XYPlot xyplot = (XYPlot) jfreechart.getPlot();
		xyplot.setForegroundAlpha(0.85F);
		XYBarRenderer xybarrenderer = (XYBarRenderer) xyplot
				.getRenderer();
		xybarrenderer.setDrawBarOutline(false);
		return jfreechart;
	}
	
	public ChartPanel createPanel(double[] data) {
		JFreeChart jfreechart = createChart(createDataset(data));
		return new ChartPanel(jfreechart);
	}
//
//	public static void main(String args[]) throws IOException {
//		String source = "C:/Users/Weili/Desktop/New folder/Results/";
//		String idfName = "";
//		AnalyzeResult analyzeResult = new AnalyzeResult(source, idfName);
//		// plot all graphs
//		int numSimulation = 9;
//		analyzeResult.setHeader();
//		analyzeResult.setData(numSimulation);
//		int numVars = analyzeResult.getVariableLength();
//
//		for (int i = 0; i < numVars; i++) {
//			double[] data = analyzeResult.getHistogramData(i);
//			System.out.println(Arrays.toString(data));
//			String currentVariable = analyzeResult.getVariable(i);
//			String t = "Distribution of " + currentVariable;
//			PlotHistogram histogramdemo1 = new PlotHistogram(t,
//					data, 10);
//			histogramdemo1.pack();
//			RefineryUtilities.centerFrameOnScreen(histogramdemo1);
//			histogramdemo1.setVisible(true);
//		}
//
//	}
}
