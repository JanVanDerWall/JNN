package Test;

import java.util.List;

import JNNMain.Learner;
import JNNMain.LearningNetwork;
import JNNMain.Network;
import JNNMain.NetworkFileInOut;
import JNNMain.NetworkJsonParser;
import JNNMain.TrainDataSet;

public class Test2 {
	public static void main(String [] args) {
		String imagesFile = "TrainData/train-images-idx3-ubyte";
		String lablesFile = "TrainData/train-labels-idx1-ubyte";
		
		String test_imagesFile = "TrainData/t10k-images-idx3-ubyte";
		String test_lablesFile = "TrainData/t10k-labels-idx1-ubyte";
		
		List<double[]> inputs = MnistReader.getNetworkInput(imagesFile);
		List<double[]> outputs = MnistReader.getOptOutput(lablesFile);
		
		List<double[]> test_inputs = MnistReader.getNetworkInput(test_imagesFile);
		List<double[]> test_outputs = MnistReader.getOptOutput(test_lablesFile);
		
		TrainDataSet[] trainData = new TrainDataSet[60000];
		for (int i = 0; i < trainData.length; i++) {
			trainData[i] = new TrainDataSet(inputs.get(i), outputs.get(i));
		}
		
		TrainDataSet[] testData = new TrainDataSet[10000];
		for (int i = 0; i < testData.length; i++) {
			testData[i]=new TrainDataSet(test_inputs.get(i), test_outputs.get(i));
		}
		
		//erstellen eines Netzwerks aus einer JSON-Datei und training
		LearningNetwork net;
		try {
			net = NetworkJsonParser.initLearningNetworkFromJSON("./src/Test/testNet.json", trainData, testData);
			System.out.println(net.getLearner().evaluate(testData));
			net.train();
			NetworkFileInOut.writeToFile("testFile.net", net);
			Network net2 = NetworkFileInOut.readFromFile("testFile.net");
			Learner l = new Learner(net2, trainData, testData);
			System.out.println("Evaluierung von net2: " + l.evaluate(testData) + " von " + testData.length);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}	
}
