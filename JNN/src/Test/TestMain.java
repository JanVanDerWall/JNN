package Test;

import JNNMain.*;

import java.io.IOException;
import java.util.List;



public class TestMain {
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
		LearningNetwork net;
		try {
			net = NetworkJsonParser.initLearningNetworkFromJSON("D:\\JNN\\JNN\\src\\Test\\testNet.json", trainData, testData);
			net.train();
			System.out.println(net.getLearner().evaluate(testData));
			System.out.println("er");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
		/*
		
		Network net2;
		try {
			net2 = NetworkFileWriter.readFromFile("test.net");
			Learner l2 = new Learner(net2, trainData, testData);
			System.out.println(l2.evaluate(testData));
			NetworkFileWriter.serialize("test.ser", net2);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	*/
	}	
}
