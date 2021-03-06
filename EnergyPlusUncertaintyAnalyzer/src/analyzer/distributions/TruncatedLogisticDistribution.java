package analyzer.distributions;

import org.apache.commons.math3.distribution.LogisticDistribution;

/**
 * This class performs truncated logistic distribution
 * 
 * This class only takes mean, sigma, lower truncation and higher
 * truncations to initialize
 * 
 * @author weilix
 *
 */
public class TruncatedLogisticDistribution extends LogisticDistribution implements TruncatedDistribution{
    
    private double lower;
    private double higher;
    
    public TruncatedLogisticDistribution(double mu, double s, double lower, double higher) {
	super(mu, s);
	this.lower = lower;
	this.higher = higher;
    }
    
    @Override
    public double truncatedSample() {
	double rnd = sample();
	while (rnd < lower || rnd > higher) {
	    rnd = sample();
	}
	return rnd;
    }
    
    @Override
    public double[] truncatedSample(int num){
	double[] samples = new double[num];
	for(int i=0; i<num; i++){
	    samples[i]=truncatedSample();
	}
	return samples;
    }

}
