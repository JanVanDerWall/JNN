package JNNMain;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import JNNMain.exceptions.LearningTypeNotSupported;

public class NetworkJsonParser {
	
	public static LearningNetwork initLearningNetworkFromJSON(String location, TrainDataSet[] trainData) throws FileNotFoundException, LearningTypeNotSupported, IOException, ParseException {
		
		LearningNetwork net = getLearningNetworkFromJson(location);
		
		if (net.getEval()==true) {
			throw new NullPointerException("if \"evaluationFreq\" is provided \"testData\" must be provided");
		}
		
		net.learnerInit(trainData);
		
		return net;
		
	}
	
	public static LearningNetwork initLearningNetworkFromJSON(String location, TrainDataSet[] trainData, TrainDataSet[] testData) 
			throws FileNotFoundException, LearningTypeNotSupported, IOException, ParseException {
		
		LearningNetwork net = getLearningNetworkFromJson(location);
		
		if (net.getEval()==false) {
			System.out.println("Warning: evaluationFreq is not provided, continueing without evaluation");
		}
		
		net.learnerInit(trainData, testData);
		
		return net;
		
	}
	
	private static LearningNetwork getLearningNetworkFromJson(String location) 
			throws LearningTypeNotSupported, FileNotFoundException, IOException, ParseException {
		
		Object obj = new JSONParser().parse(new FileReader(location));
		JSONObject jsonObj = (JSONObject)obj;
		
		int[] layers = getLayersJson(jsonObj);
		
		String learningTypeString = (String)jsonObj.get("learningType");
		if(learningTypeString == null) {
			throw new NullPointerException("The parameter \"learningType\" is not provided");
		}
		LearningType learningType;
		switch (learningTypeString) {
		case "GradientDescent":
			learningType = LearningType.GradientDescent;
			break;
		
		case "StochasticGradientDescent":
			learningType = LearningType.StochasticGradientDescnet;
			break;
		default:
			throw new LearningTypeNotSupported(learningTypeString);
		}
		
		
		Integer evaluationFreq;
		Long evaluationFreqLong = (Long)jsonObj.get("evaluationFreq");
		if(evaluationFreqLong == null) {
			evaluationFreq=null;
		}else {
			evaluationFreq = evaluationFreqLong.intValue();
		}
		
		Long epochsLong = (Long)jsonObj.get("epochs");
		if(epochsLong == null) {
			throw new NullPointerException("The parameter \"epochs\" is not provided");
		}
		int epochs = epochsLong.intValue();
		Integer miniBatchSize;
		if (learningType == LearningType.StochasticGradientDescnet) {
			Long miniBatchSizeLong = ((Long)jsonObj.get("miniBatchSize"));
			if(miniBatchSizeLong == null) {
				throw new NullPointerException("The parameter \"miniBatchSize\" is not provided");
			}
			miniBatchSize = miniBatchSizeLong.intValue();
		} else {
			miniBatchSize = null;
		}
		Long learningRateLong = (Long)jsonObj.get("learningRate");
		if (learningRateLong == null) {
			throw new NullPointerException("\"learningRate\" must be provided");
		}
		int learningRate = learningRateLong.intValue();
		
		return new LearningNetwork(layers, learningType, learningRate, epochs, miniBatchSize, evaluationFreq);
	}
	
	public static int[] getLayersJson(JSONObject jsonObj) throws FileNotFoundException, 
	IOException, ParseException, LearningTypeNotSupported {
		
		Long numOfLayersLong = ((Long)jsonObj.get("numOfLayers"));
		if(numOfLayersLong == null) {
			throw new NullPointerException("The parameter \"numOfLayers\" is not provided");
		}
		
		int numOfLayers = numOfLayersLong.intValue();
		
		JSONArray jsonNeurons = (JSONArray) jsonObj.get("numOfNeurons");
		if(jsonNeurons == null) {
			throw new NullPointerException("The parameter \"numOfNeurons\" is not provided");
		}
		
		int[] layers = new int[numOfLayers];
		if (numOfLayers != jsonNeurons.size()) {
			throw new IndexOutOfBoundsException("Array elements: " + jsonNeurons.size() + 
					" numOfLayers: " + numOfLayers);
		}
		for (int i = 0; i < numOfLayers; i++) {
			layers[i] = ((Long)jsonNeurons.get(i)).intValue();
		}
		return layers;
		
	}

}
