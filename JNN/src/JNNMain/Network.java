package JNNMain;


import java.io.Serializable;
import java.util.Random;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import JNNMain.exceptions.InputDoesNotMatchLayerException;


//Dies ist die Network Klasse, die repr�sentiert ein Neuronales Netz.
//Sie enth�lt alle Informationen �ber das Netz, wie die Layer und deren Gr��e,
//die Bias und Weight Werte

public class Network implements Serializable{
	
	//Lediglich die Versionsnumer der Klasse um den Java Compiler glücklich zu machen, muss keine Weitere Beachtung geschenkt bekommen
	private static final long serialVersionUID = 1L;
	
	
	private RealVector[] biases;   //alle Bias werte in dem Netzwerk, jeder Vektor beschreibt die Bias-Werte eines Layers
	private RealMatrix[] weights;  //die Weight werte einer Verbung zwischen Layern, jede Matrix beschreibt eine Verbingdung zwischen zwei Layern
	private int numberOfLayers;    //Anzahl der Layer im Netzwerk
	private int[] layerSizes;      //Die anzahl der Neuronen in enem Layer, der index bechreibt einen Layer
	
	//Der Konstuktor der Klasse, er erstellt ein Network mit den Infos aus einem Int-Array
	public Network(int[] layers) {
		Random r = new Random();
		
		//die Atribute werden belegt
		layerSizes = layers;
		numberOfLayers = layers.length;
		biases = new RealVector[layers.length-1]; //Länge -1, da der Input, Layer weder Bias noch Weights hat
		weights = new RealMatrix[layers.length-1];
		
		for (int i = 0; i < layers.length-1; i++) {
			
			
			//es wird ein double-Array erstellt mit zuf�lligen werten, um dann biases daraus zu erstellen
			double[] biasValues = new double[layers[i+1]];
			for (int j = 0; j < biasValues.length; j++) {
				biasValues[j] = r.nextGaussian();
			}
			biases[i] = new ArrayRealVector(biasValues);  								//biases wird aus dem Array erstellt
			
			//es wird ein zwei dimensinales double-Array erstellt mit zuf�lligen werten, um dann weights daraus zu erstellen
			double[][] weightValues =  new double[layers[i+1]][layers[i]];
			for (int j = 0; j < weightValues.length; j++) {
				for (int j2 = 0; j2 < weightValues[j].length; j2++) {
					weightValues[j][j2] = r.nextGaussian();
				}
			}
			weights[i] = MatrixUtils.createRealMatrix(weightValues);  				//weights wird aus den Werten erstellt
		}
		
	}
	
	//die Methode die den Output eines Netzwerks berechnet
	public RealVector calculateArray(double[] input) throws InputDoesNotMatchLayerException{
		
		if (input.length != layerSizes[0]) 
			throw new InputDoesNotMatchLayerException(input); 					//abfrage der Fehlerbedingung und werfen eines Fehlers
		
		UnivariateFunction s = (double x) -> (1.0/(1.0+ Math.exp(-x))); 		//s entspricht der Sigmoid-Funktion
		
		RealVector vinput = new ArrayRealVector(input);
		for (int i = 0; i < numberOfLayers-1; i++) 
				vinput = ((weights[i].operate(vinput)).add(biases[i])).map(s);
		return vinput;
	}
	
	
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getNumberOfLayers() {
		return numberOfLayers;
	}
	
	public int[] getLayerSizes() {
		return layerSizes;
	}

	public RealVector[] getBiases() {
		return biases;
	}

	public RealMatrix[] getWeights() {
		return weights;
	}

	public void setBiases(RealVector[] biases) {
		this.biases = biases;
	}

	public void setWeights(RealMatrix[] weights) {
		this.weights = weights;
	}
	
}