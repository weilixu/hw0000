package analyzer.graphs;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import analyzer.htmlparser.BuildingAreaParser;
import analyzer.listeners.GraphGenerationListener;

import org.jfree.chart.ChartPanel;

import de.erichseifert.gral.ui.InteractivePanel;

public class GraphGenerator {

    private final double CONFIDENCE_INTERVAL = 0.95;
    private final String unit = "kWh";
    private String isSized = "NO";

    /*
     * Set the analyzer and folder
     */
    private final File resultFolder;
    private final AnalyzeResult resultAnalyzer;
    
    
    private double[] histoData;

    /*
     * set the parameters required for this graph generation
     */
    private int numSimulation;// number of simulations
    private int year; // start year
    private int numGraph;// number of graphs
    private int numMonths;// number of plotting keys

    private List<ChartPanel> timeCharts; // time series graphs
    private List<ChartPanel> histoCharts; // histogram graphs
    private List<InteractivePanel> boxCharts;

    public GraphGenerator(File rf, int num, String y) {
	// take in variables
	resultFolder = rf;
	
	numSimulation = num;

	// set-up the resultAnalyzer model
	resultAnalyzer = new AnalyzeResult(resultFolder.getAbsolutePath()
		+ "\\", "", isSized);
	try {
	    year = Integer.parseInt(y);
	    resultAnalyzer.setStartYear(year);
	} catch (NumberFormatException e) {
	    // do nothing
	}

	// initialize the two graph data
	timeCharts = new ArrayList<ChartPanel>();
	histoCharts = new ArrayList<ChartPanel>();
	boxCharts = new ArrayList<InteractivePanel>();

	// initialize the GUI listener
    }
    /**
     * get the number of graphs
     * 
     * @return
     */
    public int numberOfGraphs() {
	return numGraph;
    }

    /**
     * After simulation, set the results file before generate the charts
     */
    public void setResults() {
	resultAnalyzer.setHeader();
	resultAnalyzer.setData(numSimulation);
	// get the number of graphs and number of keys from the data
	numGraph = resultAnalyzer.getVariableLength();
	numMonths = resultAnalyzer.getKeysLength();
    }

    /**
     * get whether this simulation has sizing day simulated or not
     * 
     * @param s
     */
    public void setSized(String s) {
	isSized = s;
	resultAnalyzer.setSized(isSized);
    }
    
    public double[] getResults(){
	return histoData;
    }
    
    public ArrayList<Integer> getMissingList(){
	return resultAnalyzer.getMissing();
    }
    
    public List<InteractivePanel> getBoxandWhiskerCharts(){
	for(int i=0; i<numGraph; i++){
	    Double[][] boxData = new Double[numMonths][numSimulation];
	    String[] axisTick = new String[numMonths];
	    for(int j=0; j<numMonths; j++){
		String currentKey = resultAnalyzer.getKey(j);
		axisTick[j] = currentKey;
		double[] temp = resultAnalyzer.getData(currentKey, i);
		for(int k=0; k<temp.length; k++){
		    boxData[j][k] = temp[k];
		}
	    }
	    String title = resultAnalyzer.getVariable(i);
	    BoxPlotGraph graph = new BoxPlotGraph(title,numMonths, year);
	    graph.addRows(axisTick, boxData);
	    boxCharts.add(graph.getChart());
	    
	}
	return boxCharts;
    }

    /**
     * generate time series charts
     */
    public List<ChartPanel> getTimeSeriesCharts() {
	String startMonth = resultAnalyzer.getKey(0);
	
	for (int i = 0; i < numGraph; i++) {
	    double[] averages = new double[numMonths];
	    double[] lowerCI = new double[numMonths];
	    double[] upperCI = new double[numMonths];
	    for (int j = 0; j < numMonths; j++) {

		String currentKey = resultAnalyzer.getKey(j);
		double[] temp = resultAnalyzer.getData(currentKey, i);

		GenerateStatistics stat = new GenerateStatistics(temp);
		averages[j] = stat.getMean();

		double[] ci = stat.getCI(CONFIDENCE_INTERVAL);
		lowerCI[j] = ci[0];
		upperCI[j] = ci[1];

	    }

	    String currentVariable = resultAnalyzer.getVariable(i);
	    String title = "Mean " + currentVariable + " with "
		    + (CONFIDENCE_INTERVAL * 100) + "% CI";

	    PlotGraph plotGraph = new PlotGraph(title, averages, lowerCI,
		    upperCI, startMonth, numMonths, year);

	    ChartPanel chart = plotGraph.getChart();
	    timeCharts.add(chart);
	}
	return timeCharts;
    }

    /**
     * generates histogram charts
     */
    public List<ChartPanel> getHistogramCharts() {
	for (int i = 0; i < numGraph; i++) {
	    histoData = resultAnalyzer.getHistogramData(i);

	    String currentVariable = resultAnalyzer.getVariable(i);
	    String title = "Distribution of " + currentVariable;

	    PlotHistogram histogramGraph = new PlotHistogram(title, unit,histoData);
	    histoCharts.add(histogramGraph.createPanel());
	}
	return histoCharts;
    }

    /**
     * Clear the time series charts data and histogram charts data
     */
    public void clearCharts() {
	Iterator<ChartPanel> timeIterator = timeCharts.iterator();
	while (timeIterator.hasNext()) {
	    timeIterator.remove();
	}
	Iterator<ChartPanel> histoIterator = histoCharts.iterator();
	while (histoIterator.hasNext()) {
	    histoIterator.remove();
	}
    }
}
