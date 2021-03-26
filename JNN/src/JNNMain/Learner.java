package JNNMain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.analysis.UnivariateFunction;

import org.apache.commons.math3.linear.*;

public class Learner {
	
	Network network;
	public TrainDataSet[] trainData;
	public TestDataSet[] testData;
	
	
	
	public Learner(Network n) {
		network = n;
		
	}
	
	public Network trainStochastikGradientDecent(double lerningRate, double epochs, int miniBatchSize) {
		int num_miniBatches = trainData.length/miniBatchSize;
		List<TrainDataSet> trainDataList = Arrays.asList(trainData);
		
		for (int i = 0; i < epochs; i++) {
			
			//Collections.shuffle(trainDataList);
			TrainDataSet[] randomTrainData = new TrainDataSet[trainData.length];
			trainDataList.toArray(randomTrainData);
			
			List<TrainDataSet[]> miniBatches = new ArrayList<>();
			for (int j = 0; j < num_miniBatches; j++) {
				TrainDataSet[] miniBatch = new TrainDataSet[miniBatchSize];
				for (int k = 0; k < miniBatch.length; k++) {
					miniBatch[k] = randomTrainData[(j*miniBatchSize)+k];
				}
				miniBatches.add(miniBatch);
			}
			
			for (TrainDataSet[] miniBatch : miniBatches) {
				trainGradientDecent(miniBatch, lerningRate, 1);
			}
			
			
			System.out.println("epoch " + i);
			System.out.println(evaluate(testData));
		}
		
			
		
		
		return network;
	}
	public Network trainGradientDecent(double lerningRate, int epochs) {	
		return trainGradientDecent(trainData, lerningRate, epochs);
	}
	public Network trainGradientDecent(TrainDataSet[] training, double lerningRate, int epochs) {	
		
		for (int epoch=0; epoch<epochs; epoch++) {
			//Erstelle die Arays mit denen dann der Gradient gespeichert wird, fülle die Arrays mit nullen
			RealVector[] biasOfset = new ArrayRealVector[network.getNumberOfLayers()-1];
			RealMatrix[] weightOfset = new RealMatrix[network.getNumberOfLayers()-1];
			for (int i = 0; i < weightOfset.length; i++) {
				biasOfset[i] = new ArrayRealVector(network.getLayerSizes()[i+1]);
				weightOfset[i] = new Array2DRowRealMatrix(network.getLayerSizes()[i+1], network.getLayerSizes()[i]); //fülle Matrix automatisch mit nullen
			}
			
			for (TrainDataSet data : training) {
				Gradient g = backprop(data);                               //Gradient g ist der Gradient Vector, enthät sowohl weights als auch bias
				for (int i = 0; i < biasOfset.length; i++) {			   //bias.Ofset.length hat den selben Wert wie weightOfset.length
					biasOfset[i] = biasOfset[i].add(g.bias_g[i]);		   //erstelle die Summe
					weightOfset[i] = weightOfset[i].add(g.weight_g[i]);
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
		
		RealVector[] biasOfset = new ArrayRealVector[network.getNumberOfLayers()-1];
		RealMatrix[] weightOfset = new RealMatrix[network.getNumberOfLayers()-1];
		for (int i = 0; i < weightOfset.length; i++) {
			biasOfset[i] = new ArrayRealVector(network.getLayerSizes()[i+1]); //Fülle Vector automatisch mit nullen
			weightOfset[i] = new Array2DRowRealMatrix(network.getLayerSizes()[i+1], network.getLayerSizes()[i]); //fülle Matrix automatisch mit nullen
		}
		
		UnivariateFunction s = (double x) -> (1.0/(1.0+ Math.exp(-x))); //s entspricht der Sigmoid-Funktion
		UnivariateFunction sp = (double x) -> (s.value(x)*(1-s.value(x))); //sp entspricht der Ableitung der Sigmoid-Funktion
		
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
		
		RealVector error;
		error = costDerivative(layerActivations[layerActivations.length-1], data.getOutputs()).ebeMultiply(layerActivationZ[layerActivationZ.length-1].map(sp));
		biasOfset[biasOfset.length-1]=error;
		weightOfset[weightOfset.length-1]= error.outerProduct(layerActivations[layerActivations.length-2]);
		
		for(int i = 2; i<network.getNumberOfLayers(); i++) {
			activation = layerActivationZ[layerActivationZ.length-i];
			RealVector sigPrimAct = activation.map(sp);
			RealMatrix currentWeights = network.getWeights()[(network.getWeights().length-i)+1];
			error = currentWeights.transpose().operate(error).ebeMultiply(sigPrimAct);
			biasOfset[biasOfset.length-i]=error;
			weightOfset[weightOfset.length-i]= error.outerProduct(layerActivations[(layerActivations.length-i)-1]);
		}
		
		Gradient gradient = new Gradient(biasOfset, weightOfset);
		
		return gradient;
	}
	
	//Methode die die anzahl der Richtig bewerteten Datenpunkte aus einem Gegebenen Testdatensatz errechnet
	public int evaluate(TestDataSet[] data) {
		int ammount = data.length;
		int ammountRight = 0;
		
		//eine Schleife geht über jeden Datenpukt im Datensatz (data)
		for (int i = 0; i < ammount; i++) {
			
			//Der Output des Netzwerks wird berrechnet
			RealVector result = network.calculateArray(data[i].getInputs().toArray());
			double[] arrResult = result.toArray();
			
			//Die größte Zahl des otputs wird bestimmt
			int largestNumber = 0;
			for (int j = 0; j < arrResult.length; j++) {
				if (arrResult[j]>arrResult[largestNumber]) {
					largestNumber=j;
				}
			}
			
			//Wenn das Ergebniss stimmt, wird amountReight um eins erhöt
			if (largestNumber == data[i].getOutput()) {
				ammountRight++;
				
			}
		}
		
		return ammountRight;
	}

	//Methode, die die Ableitung der Kostenfunktion darstellt
	private RealVector costDerivative(RealVector a, RealVector b) {
		return a.subtract(b);
	}

}

