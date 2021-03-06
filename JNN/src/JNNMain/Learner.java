package JNNMain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.analysis.UnivariateFunction;

import org.apache.commons.math3.linear.*;

public class Learner {
	
	Network network;
	private TrainDataSet[] trainData;
	private TrainDataSet[] testData;
	
	
	
	public Learner(Network n, TrainDataSet[] trainData, TrainDataSet[] testData) {
		network = n;
		this.trainData = trainData;
		this.testData = testData;
		
	}
	
	public Learner(Network n, TrainDataSet[] trainData) {
		network = n;
		this.trainData = trainData;
		
	}
	
	//Stochastic Gradient Descent mit der möglichkeit die Performance des Netzwerks zu evaluieren
	public Network trainStochastikGradientDescent_ev(double learningRate, int epochs, int miniBatchSize, int evaluationFreq) {
		
		//Wiederholung nach der Enzahl der Epochen , 1 Wiedeholung = eine Epoche
		for (int i = 0; i < epochs; i++) {
			trainStochastikGradientDescent(learningRate, 1, miniBatchSize); //Durchführen des Stochastic Gradientd Descent mit einer Epoche
			
			//Evaluieren des Netzwerks, bestimmt durch evaluationFreq
			if (i%evaluationFreq==0) {
				System.out.println("epoch " + i);
				System.out.println(evaluate(testData));
			}
		}
		
		return network;
		
	}
	
	//Identische Funktionsweise zu trainStochastikGradientDescent_ev,  es wird jedoch der einfache Gradient Descent verwendet
	public Network trainGradientDecent_ev(double learningRate, int epochs, int evaluationFreq){
		for (int i = 0; i < epochs; i++) {
			trainGradientDecent(learningRate, epochs);
			if (i%evaluationFreq==0) {
				System.out.println("epoch " + i);
				System.out.println(evaluate(testData));
			}
		}
		
		return network;
	}
	
	
	//Training mit einem effizienteren Algorithmus
	public Network trainStochastikGradientDescent(double lerningRate, int epochs, int miniBatchSize) {
		int num_miniBatches = trainData.length/miniBatchSize;			//bestimmen der größe eines Subsets der Trainigsdaten
		List<TrainDataSet> trainDataList = Arrays.asList(trainData);	//Die Liste wird verwendet um die .schuffle Methode zu benutzen
		
		//Wiederholung nach der Enzahl der Epochen
		for (int i = 0; i < epochs; i++) {
			
			//erstellen eines Zufällig gemischten Arrays
			Collections.shuffle(trainDataList);
			TrainDataSet[] randomTrainData = new TrainDataSet[trainData.length];
			trainDataList.toArray(randomTrainData);
			
			List<TrainDataSet[]> miniBatches = new ArrayList<>(); //Liste mit den "Mini"-Batches
			//"miniBatches" wird mit Subsets der Trainigsdaten von der Größe miniBatchSize befüllt
			for (int j = 0; j < num_miniBatches; j++) {
				TrainDataSet[] miniBatch = new TrainDataSet[miniBatchSize];
				for (int k = 0; k < miniBatch.length; k++) {
					miniBatch[k] = randomTrainData[(j*miniBatchSize)+k];
				}
				miniBatches.add(miniBatch);
			}
			
			//Gradient Decent wird mit jeder MiniBatch ausgeführt, dabei wird nur eine Epoche verwendet, 
			//da die Epoche verwendet
			for (TrainDataSet[] miniBatch : miniBatches) {
				trainGradientDecent(miniBatch, lerningRate, 1);
			}
			
		}
		
		return network;
	}
	
	//Training mit Gradientdescent auf Basis der Daten im Learner-Objekt
	public Network trainGradientDecent(double lerningRate, int epochs) {	
		return trainGradientDecent(trainData, lerningRate, epochs);
	}
	
	//Training mit Gradientsescent auf Basis von übergebenen Daten
	public Network trainGradientDecent(TrainDataSet[] training, double lerningRate, int epochs) {	
		
		for (int epoch=0; epoch<epochs; epoch++) {
			//Erstelle die Arays mit denen dann der Gradient gespeichert wird, fülle die Arrays mit nullen
			RealVector[] biasOfset = new ArrayRealVector[network.getNumberOfLayers()-1];
			RealMatrix[] weightOfset = new RealMatrix[network.getNumberOfLayers()-1];
			
			for (int i = 0; i < weightOfset.length; i++) {
				biasOfset[i] = new ArrayRealVector(network.getLayerSizes()[i+1]); //automatisch mit Nullen gefüllt
				weightOfset[i] = new Array2DRowRealMatrix(network.getLayerSizes()[i+1], network.getLayerSizes()[i]); //fülle Matrix automatisch mit nullen
			}
			
			for (TrainDataSet data : training) {
				Gradient g = backprop(data);                               //Gradient g ist der Gradient Vector, enthät sowohl weights als auch bias
				for (int i = 0; i < biasOfset.length; i++) {			   //bias.Ofset.length hat den selben Wert wie weightOfset.length
					biasOfset[i] = biasOfset[i].add(g.bias_g[i]);		   //erstelle die Summe die in der Gleichung vorkommt. Diese steht im korrespondierenden
					weightOfset[i] = weightOfset[i].add(g.weight_g[i]);    //Kapitel der Dokumentation
				}
			}
			
			RealMatrix[] netWeights = network.getWeights();               //Weitere Referenzen auf die Netwerk Parameter, lediglich aus Gründen
			RealVector[] netBiases = network.getBiases();					//der esthetik des späteren Codes
			for(int i = 0; i<network.getNumberOfLayers()-1; i++) {
				netWeights[i] = netWeights[i].subtract(weightOfset[i].scalarMultiply((lerningRate/training.length)));  //der Tweite Teil der Updategleichung wird ausgeführt
				netBiases[i] = netBiases[i].subtract(biasOfset[i].mapMultiply((lerningRate/training.length)));         //führ Weigts und fürs Biases. (Die Gleichung steht in der Dokumentation)
			}
			
		}
		
		return network;
	}
	
	
	public Gradient backprop(TrainDataSet data) {
		
		RealVector[] biasOfset = new ArrayRealVector[network.getNumberOfLayers()-1]; //Länge -1, da der Input, Layer weder Bias noch Weights hat
		RealMatrix[] weightOfset = new RealMatrix[network.getNumberOfLayers()-1];
		for (int i = 0; i < weightOfset.length; i++) {
			biasOfset[i] = new ArrayRealVector(network.getLayerSizes()[i+1]); //Fülle Vector automatisch mit nullen
			weightOfset[i] = new Array2DRowRealMatrix(network.getLayerSizes()[i+1], network.getLayerSizes()[i]); //fülle Matrix automatisch mit nullen
		}
		
		UnivariateFunction s = (double x) -> (1.0/(1.0+ Math.exp(-x))); //s entspricht der Sigmoid-Funktion
		UnivariateFunction sp = (double x) -> (s.value(x)*(1-s.value(x))); //sp entspricht der Ableitung der Sigmoid-Funktion
		
		RealVector activation = data.getInputs(); //der erste Input
		RealVector[] layerActivations = new RealVector[network.getNumberOfLayers()];    //Die Outputs bzw. Inputs der Layer
		layerActivations[0]=activation;													//die Inputs zum ersten Layer sind gegeben
		RealVector[] layerActivationZ = new RealVector[network.getNumberOfLayers()-1]; //Die Aktivations (Outputs) der Layer ohne die Sigmoid funktion
		
		//der Ouput des Netzwerks wird berechnet, dabei werden alle Daten gespeichert, die wichtig sind
		//entspricht zu großen teilen der Berechnung des Outputs eines Netzwerks
		for (int i = 0; i < network.getNumberOfLayers()-1; i++) {
			RealVector activationZ = network.getWeights()[i].operate(activation).
					add(network.getBiases()[i]); //wie das Berechnen des Netzwerkoutputs, ohne Sigmoid
			layerActivationZ[i]= activationZ; //Zwischenwerte werden gespeichert
			activation = activationZ.map(s);	//berechnen des outputs mit Sigmoid
			layerActivations[i+1] = activation; //+1, da die Inputs Ebenfalls abgespeichert werden
		}
		
		RealVector error;
		
		//Error und Ableitungen werden für den letzten Layer berechnet
		error = layerActivations[layerActivations.length-1].subtract(data.getOutputs()).ebeMultiply(layerActivationZ[layerActivationZ.length-1].map(sp)); //Gleichung nr. 1 wird ausgeführt
		biasOfset[biasOfset.length-1]=error; //Gleichung nr.3 wird ausgeführt
		weightOfset[weightOfset.length-1]= error.outerProduct(layerActivations[layerActivations.length-2]); //Gleichung nr. 4 wird ausgeführt
		
		//der Error und die Ableitungen werden für die Anderen Layer berechnet
		for(int i = 2; i<network.getNumberOfLayers(); i++) {
			//in den Folgenden 4 Zeilen wird Gleichung Nr. 2 Ausgeführt
			activation = layerActivationZ[layerActivationZ.length-i];
			RealVector sigPrimAct = activation.map(sp);
			RealMatrix currentWeights = network.getWeights()[(network.getWeights().length-i)+1];
			error = currentWeights.transpose().operate(error).ebeMultiply(sigPrimAct);
			
			biasOfset[biasOfset.length-i]=error; //Gleichung nr.3 wird ausgeführt
			weightOfset[weightOfset.length-i]= error.outerProduct(layerActivations[(layerActivations.length-i)-1]); //Gleichung nr. 4 wird ausgeführt
		}
		
		Gradient gradient = new Gradient(biasOfset, weightOfset); //biasOfset und weightOfset werden in einem Gradienten zusammengefasst um sie zurück zu geben
		
		return gradient;
	}
	
	
	//Evaluiert wie gut das Netzwerk ist, gibt die Anzahl der richtig erkannten Bilder zurück
	public int evaluate(TrainDataSet[] data) {
		
		int ammountRight = 0;
		
		for (int i = 0; i < data.length; i++) {
			
			//Der Output des Netzwerks wird berrechnet
			double[] result = network.calculateArray(data[i].getInputs().toArray()).toArray();
			
			//Die größte Zahl des otputs wird bestimmt
			int largestNumber = 0;
			for (int j = 0; j < result.length; j++) {
				if (result[j]>result[largestNumber]) {
					largestNumber=j;
				}
			}
			
			//Die größte Zahl des Optimalen outputs wird bestimmt
			double[] optOutputs = data[i].getOutputs().toArray();
			int optLargestNumber = 0;
			for (int j = 0; j < optOutputs.length; j++) {
				if (optOutputs[j]>optOutputs[optLargestNumber]) {
					optLargestNumber=j;
				}
			}
			
			//wenn die beiden größten Zahlen übereinstimmen, lag das Netzwerk richtig
			if(largestNumber == optLargestNumber) {
				ammountRight++;
			}
			
		}
		return ammountRight;
	}

}

//Diese Klasse repräsentiert den Gradienten der Kostenfunktion, ist im Prizip lediglich ein Tupel
class Gradient{
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

