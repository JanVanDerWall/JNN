package JNNMain;
import java.util.Random;

import org.apache.commons.math3.linear.*;


//Dies ist die Network Klasse, die repräsentiert ein Neuronales Netz.
//Sie enthält alle Informationen über das Netz, wie die Layer und deren Größe,
//die Bias und Weight Werte

public class Network {
	
	RealVector[] biases;
	RealMatrix[] weights;
	int numberOfLayers;
	int[] layerSizes;
	
	public Network(int[] layers) {
		
		biases = new RealVector[layers.length-1];
		weights = new RealMatrix[layers.length-1];
		
		for (int i = 0; i < layers.length-1; i++) {
			Random r = new Random();
			double[] biasValues = new double[layers[i+1]];
			for (int j = 0; j < biasValues.length; j++) {
				biasValues[j] = r.nextDouble();
			}
			biases[i] = new ArrayRealVector(biasValues);
			double[][] weightValues =  new double[layers[i+1]][layers[i]];
			for (int j = 0; j < weightValues.length; j++) {
				for (int j2 = 0; j2 < weightValues[j].length; j2++) {
					weightValues[j][j2] = r.nextDouble();
				}
			}
			weights[i] = new Array2DRowRealMatrix(weightValues);
		}
		
	}
	
	
	public int[] calculate() {
		return null;
	}
	
	private double sigmoid (double input) {
		return (1.0/(1.0+ Math.exp(input)));
	}

}
