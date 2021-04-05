package Test;

import JNNMain.Learner;
import JNNMain.Network;
import JNNMain.TestDataSet;
import JNNMain.TrainDataSet;
import Test.MnistReader;

import java.util.List;

import org.apache.commons.math3.linear.*;

public class TestMain {
	public static void main(String [] args) {
		
		/*
		
		int[] a = {2,1};
		
		Network n = new Network(a);
		System.out.println("Hallo");
		Learner l = new Learner(n, 0.1);
		l.trainGradientDecent();
		
		*/
		
		/*
		TrainDataSet[] trainData= new TrainDataSet[5];
		
		double[] inputs1 = {1.0, 0.0, 0.0};
		double[] outputs1 = {1.0, 0.0, 0.0, 0.0};
		double[] inputs2 = {0.0, 1.0, 0.0};
		double[] outputs2 = {0.0, 1.0, 0.0, 0.0};
		double[] inputs3 = {0.0, 0.0, 1.0};
		double[] outputs3 = {0.0, 0.0, 2.0, 0.0};
		double[] inputs4 = {0.0, 1.0, 1.0};
		double[] outputs4 = {0.0, 0.0, 0.0, 1.0};
		double[] inputs5 = {1.0, 1.0, 1.0};
		double[] outputs5 = {0.0, 0.0, 0.0, 1.0};
		
		
		
		trainData[0] = new TrainDataSet(new ArrayRealVector(inputs1), new ArrayRealVector(outputs1));
		trainData[1] = new TrainDataSet(new ArrayRealVector(inputs2), new ArrayRealVector(outputs2));
		trainData[2] = new TrainDataSet(new ArrayRealVector(inputs3), new ArrayRealVector(outputs3));
		trainData[3] = new TrainDataSet(new ArrayRealVector(inputs4), new ArrayRealVector(outputs4));
		trainData[4] = new TrainDataSet(new ArrayRealVector(inputs5), new ArrayRealVector(outputs5));
		
		
		
		int[] layers = {3,6,4};
		
		Network n = new Network(layers);
		
		Learner l = new Learner(n);
		l.trainGradientDecent(trainData, 1, 100000);
		
		double[] in = {1.0, 1.0, 0.0};
		
		System.out.println(n.calculateArray(in));
		*/
		
		
		String imagesFile = "TrainData/train-images-idx3-ubyte";
		String lablesFile = "TrainData/train-labels-idx1-ubyte";
		
		String test_imagesFile = "TrainData/t10k-images-idx3-ubyte";
		String test_lablesFile = "TrainData/t10k-labels-idx1-ubyte";
		
		List<double[]> inputs = MnistReader.getNetworkInput(imagesFile);
		List<double[]> outputs = MnistReader.getOptOutput(lablesFile);
		
		
		
		List<double[]> test_inputs = MnistReader.getNetworkInput(test_imagesFile);
		int[] lables = MnistReader.getLabels(test_lablesFile);
		
		TrainDataSet[] trainData = new TrainDataSet[60000];
		for (int i = 0; i < trainData.length; i++) {
			trainData[i] = new TrainDataSet(new ArrayRealVector(inputs.get(i)), new ArrayRealVector(outputs.get(i)));
		}
		
		TestDataSet[] testData = new TestDataSet[10000];
		for (int i = 0; i < testData.length; i++) {
			testData[i]=new TestDataSet(new ArrayRealVector(test_inputs.get(i)), lables[i]);
		}
		
		int[] layers = {784,30,10};
		Network net = new Network(layers);
		Learner l = new Learner(net);
		l.trainData = trainData;
		l.testData = testData;
		System.out.println(l.evaluate_with_TestDataSet(testData));
		l.trainStochastikGradientDecent(3, 30, 10);
		
		
		/*
		int[] layers = {2,3,2};
		Network net  = new Network(layers);
		double[] in = {0.5, 0.3};               
		System.out.println(net.calculateArray(in));
		
		double[] testIn = {0.5, 0.3};
		double[] testOut = {0.1, 0.1};
		
		double[] testIn2 = {0.1, 0.3};
		double[] testOut2 = {0.1, 0.4};
		Learner l = new Learner(net);
		Gradient g = l.backprop(new TrainDataSet(new ArrayRealVector(testIn), new ArrayRealVector(testOut)));
	
		TrainDataSet[] trainData= new TrainDataSet[4];
		trainData[0] = new TrainDataSet(new ArrayRealVector(testIn), new ArrayRealVector(testOut));
		trainData[1] = new TrainDataSet(new ArrayRealVector(testIn2), new ArrayRealVector(testOut2));
		trainData[2] = new TrainDataSet(new ArrayRealVector(testIn), new ArrayRealVector(testOut));
		trainData[3] = new TrainDataSet(new ArrayRealVector(testIn2), new ArrayRealVector(testOut2));
		//l.trainGradientDecent(trainData, 3, 1);
		//l.trainGradientDecent(trainData, 3, 1);
		l.trainData = trainData;
		l.trainStochastikGradientDecent(1, 2, 2);            
		System.out.println(net.calculateArray(in));
		
		System.out.println("Hallo");
		*/
	
	}
	
	
}
