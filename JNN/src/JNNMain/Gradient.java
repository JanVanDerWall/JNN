package JNNMain;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class Gradient{
	public RealVector[] bias_g;
	public RealMatrix[] weight_g;
	public Gradient(RealVector[] b, RealMatrix[] w){
		bias_g = b;
		weight_g = w;
	}
	public RealVector[] getBias_g() {
		return bias_g;
	}
	public RealMatrix[] getWeight_g() {
		return weight_g;
	}
	
	
	
}
