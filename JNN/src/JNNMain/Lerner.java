package JNNMain;

import org.apache.commons.math3.linear.*;

public class Lerner {
	
	Network network;
	TrainDataSet[] trainData;
	
	double lerningRate;
	
	public Network trainGradientDecent() {
		return trainGradientDecent(trainData);
	}
	
	public Network trainGradientDecent(TrainDataSet[] training) {
		return null;
	}
	
	private Gradient backprop() {
		return null;
	}

}

class Gradient{
	final RealVector[] bias_g;
	final RealMatrix[] weight_g;
	public Gradient(RealVector[] b, RealMatrix[] w){
		bias_g = b;
		weight_g = w;
	}
}
