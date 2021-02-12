package JNNMain;

import org.apache.commons.math3.analysis.UnivariateFunction;
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
		
		RealMatrix[] netWeights = network.getWeights();
		RealVector[] netBiases = network.getBiases();
		for(int i = 0; i<network.getNumberOfLayers()-1; i++) {
			netWeights[i] = netWeights[i].subtract(weightOfset[i].scalarMultiply((lerningRate/training.length)));
			netBiases[i] = netBiases[i].subtract(biasOfset[i].mapMultiply((lerningRate/training.length)));
		}
		
		return network;
	}
	
	private Gradient backprop(TrainDataSet data) {
		
		RealVector[] biasOfset = new ArrayRealVector[network.getNumberOfLayers()-1];
		RealMatrix[] weightOfset = new RealMatrix[network.getNumberOfLayers()-1];
		for (int i = 0; i < weightOfset.length; i++) {
			biasOfset[i] = new ArrayRealVector(network.getLayerSizes()[i+1]); //Fülle Vector automatisch mit nullen
			weightOfset[i] = new Array2DRowRealMatrix(network.getLayerSizes()[i+1], network.getLayerSizes()[i]); //fülle Matrix automatisch mit nullen
		}
		
		UnivariateFunction s = (double x) -> (1.0/(1.0+ Math.exp(-x))); //s entspricht der Sigmoid-Funktion
		
		RealVector activation = data.getInputs();
		RealVector[] layerActivations = new RealVector[network.getNumberOfLayers()];
		layerActivations[0]=activation;
		RealVector[] layerActivationZ = new RealVector[network.getNumberOfLayers()-1]; //Die Aktivations der Layer ohne die Sigmoid funktion
		
		for (int i = 0; i < network.getNumberOfLayers()-1; i++) {
			RealVector activationZ = network.getWeights()[i].operate(activation).add(network.getBiases()[i]);
			layerActivationZ[i]= activationZ;
			activation = activationZ.map(s);
			layerActivations[i+1] = activation;
		}
		
		RealVector error = costDerivative(layerActivations[layerActivations.length-1], data.getOutputs()); //befehl noch nicht beendet
		
		return null;
	}

	private RealVector costDerivative(RealVector a, RealVector b) {
		return a.subtract(b);
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

