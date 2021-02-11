package JNNMain;

import org.apache.commons.math3.linear.*;

public class Lerner {
	
	Network network;
	TrainDataSet[] trainData;
	
	double lerningRate;
	
	public Lerner(Network n, double r) {
		network = n;
		lerningRate = r;
		
	}
	
	public Network trainGradientDecent() {
		return trainGradientDecent(trainData);
	}
	
	public Network trainGradientDecent(TrainDataSet[] training) {
		
		//Erstelle die Arays mit denen dann der Gradient gespeichert wird, fülle die Arrays mit nullen
		RealVector[] biasOfset = new ArrayRealVector[network.getNumberOfLayers()-1];
		RealMatrix[] weightOfset = new RealMatrix[network.getNumberOfLayers()-1];
		for (int i = 0; i < weightOfset.length; i++) {
			biasOfset[i] = new ArrayRealVector(network.getLayerSizes()[i+1]); //Fülle Vector automatisch mit nullen
			weightOfset[i] = new Array2DRowRealMatrix(network.getLayerSizes()[i+1], network.getLayerSizes()[i]); //fülle Matrix automatisch mit nullen
		}
		
		for (TrainDataSet data : training) {
			Gradient g = backprop(data);
			for (int i = 0; i < biasOfset.length; i++) {
				biasOfset[i] = biasOfset[i].add(g.bias_g[i]);
				weightOfset[i] = weightOfset[i].add(g.weight_g[i]);
			}
		}
		
		for(int i = 0; i<network.getNumberOfLayers()-1; i++) {
			
		}
		
		return null;
	}
	
	private Gradient backprop(TrainDataSet data) {
		return null;
	}

}

class Gradient{
	public RealVector[] bias_g;
	public RealMatrix[] weight_g;
	public Gradient(RealVector[] b, RealMatrix[] w){
		bias_g = b;
		weight_g = w;
	}
}
