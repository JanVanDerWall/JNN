package JNNMain;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import JNNMain.exceptions.LearningTypeNotSupported;

public class NetworkJsonParser {
	
	public static Network getNetworkFromJson(String location) throws FileNotFoundException, 
	IOException, ParseException, LearningTypeNotSupported {
		Network net = null;
		
		Object obj = new JSONParser().parse(new FileReader(location));
		
		JSONObject jsonObj = (JSONObject)obj;
		
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
		
		String learningTypeString = (String)jsonObj.get("learningType");
		if(learningTypeString == null) {
			throw new NullPointerException("The parameter \"learningType\" is not provided");
		}
		
		LearningType learningType;
		if (learningTypeString.equals("")) {
			net = new Network(layers);
		}else {
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
			
			net = new LearningNetwork(layers, learningType);
		}
		
		return net;
		
	}

}
