package JNNMain;
import java.util.Random;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.linear.*;

import JNNMain.exceptions.InputDoesNotMatchLayerException;


//Dies ist die Network Klasse, die repräsentiert ein Neuronales Netz.
//Sie enthält alle Informationen über das Netz, wie die Layer und deren Größe,
//die Bias und Weight Werte

public class Network {
	
	private RealVector[] biases;
	private RealMatrix[] weights;
	private int numberOfLayers;
	private int[] layerSizes;
	
	Sigmoid sigmoidFunction;
	
	
	//Der Konstuktor der Klasser, er erstellt ein Netwokr aus einem Int-Array
	public Network(int[] layers) {
		
		sigmoidFunction = new Sigmoid();
		
		layerSizes = layers;
		numberOfLayers = layers.length;
		biases = new RealVector[layers.length-1];
		weights = new RealMatrix[layers.length-1];
		for (int i = 0; i < layers.length-1; i++) {
			Random r = new Random();
			
			//es wird ein double-Array erstellt mit zufälligen werten, um dann biases daraus zu erstellen
			double[] biasValues = new double[layers[i+1]];
			for (int j = 0; j < biasValues.length; j++) {
				biasValues[j] = 1;
			}
			biases[i] = new ArrayRealVector(biasValues);
			
			//es wird ein zwei dimensinales double-Array erstellt mit zufälligen werten, um dann biases daraus zu erstellen
			double[][] weightValues =  new double[layers[i+1]][layers[i]];
			for (int j = 0; j < weightValues.length; j++) {
				for (int j2 = 0; j2 < weightValues[j].length; j2++) {
					weightValues[j][j2] = 0.2;
				}
			}
			weights[i] = MatrixUtils.createRealMatrix(weightValues);
		}
		
		/*
		//biases und weights werden intialisiert mit den richtigen längen
		biases = new RealVector[layers.length-1];
		weights = new RealMatrix[layers.length-1];
		
		
		for (int i = 0; i < layers.length-1; i++) {
			Random r = new Random();
			
			//es wird ein double-Array erstellt mit zufälligen werten, um dann biases daraus zu erstellen
			double[] biasValues = new double[layers[i+1]];
			for (int j = 0; j < biasValues.length; j++) {
				biasValues[j] = r.nextDouble();
			}
			biases[i] = new ArrayRealVector(biasValues);
			
			//es wird ein zwei dimensinales double-Array erstellt mit zufälligen werten, um dann biases daraus zu erstellen
			double[][] weightValues =  new double[layers[i+1]][layers[i]];
			for (int j = 0; j < weightValues.length; j++) {
				for (int j2 = 0; j2 < weightValues[j].length; j2++) {
					weightValues[j][j2] = r.nextDouble();
				}
			}
			weights[i] = MatrixUtils.createRealMatrix(weightValues);
		}
		*/
	}
	
	
	public RealVector calculateArray(double[] a) throws InputDoesNotMatchLayerException{
		if (a.length != layerSizes[0]) {
			throw new InputDoesNotMatchLayerException(a);
		}
		RealVector va = new ArrayRealVector(a);
		for (int i = 0; i < numberOfLayers-1; i++) {
			va = weights[i].operate(va).add(biases[i]).map(sigmoidFunction);
		}
		return va;
	}
	
	
	class Sigmoid implements UnivariateFunction{
		@Override
		public double value (double x) {
			return (1.0/(1.0+ Math.exp(x)));
		}
	}
	

}
