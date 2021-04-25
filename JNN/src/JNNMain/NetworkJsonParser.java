package JNNMain;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import JNNMain.exceptions.LearningTypeNotSupported;

/*
 * Diese Klasse übernimmt das erstellen von Netzwerken aus JSON Dateien
 */
public class NetworkJsonParser {
	
	//erstellen eines Netzwerks ohne testData
	public static LearningNetwork initLearningNetworkFromJSON(String location, TrainDataSet[] trainData) 
			throws FileNotFoundException, LearningTypeNotSupported, IOException, ParseException {
		
		LearningNetwork net = getLearningNetworkFromJson(location);
		
		if (net.getEval()==true) {
			throw new NullPointerException("if \"evaluationFreq\" is provided \"testData\" must be provided");
		}
		
		net.learnerInit(trainData);
		
		return net;
		
	}
	//erstellen eines Netzwerks mit testData
	public static LearningNetwork initLearningNetworkFromJSON(String location, TrainDataSet[] trainData, TrainDataSet[] testData) 
			throws FileNotFoundException, LearningTypeNotSupported, IOException, ParseException {
		
		LearningNetwork net = getLearningNetworkFromJson(location);
		
		if (net.getEval()==false) {
			System.out.println("Warning: evaluationFreq is not provided, continueing without evaluation");
		}
		
		net.learnerInit(trainData, testData);
		
		return net;
		
	}
	
	//erstellen des Learning Networks aus einer JSON-Datei
	private static LearningNetwork getLearningNetworkFromJson(String fileLocation) 
			throws LearningTypeNotSupported, FileNotFoundException, IOException, ParseException {
		
		JSONObject jsonObj = (JSONObject) new JSONParser().parse(new FileReader(fileLocation)); //das JSON-Objekt wird erstellt
		
		int[] layers = getLayersJson(jsonObj); //das Array mit den Layern des Netzwerks wird gezogen,  auf null wird an anderer Stelle kontrolliert
		
		/*
		 * Es werden die Parameter für das Trainieren eines Netzwerks aus dem JSON-Objekt gezogen
		 * für jeden Parameter werden Kontrollen durchgeführt, ob dieser in der JSON-Datei korrekt
		 * angegeben ist. Es werden Null-Pointer Exceptions geworfen, sollte dies nicht der Fall sein
		 * Wenn ein Parameter nicht korrekt oder falsch geschrieben in der JSON-Datei angegeben ist,
		 * gibt der JSON-Reader "null" zurrück. Es kann also kontrolliert werden, ob die Entsprechenden
		 * Java-Objekte "null" sind
		 */
		
		//learning Type gibt an, welche Algorithmus verwendet werden soll
		String learningTypeString = (String)jsonObj.get("learningType");
		if(learningTypeString == null) { //kontrolle auf null
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
		
		Integer evaluationFreq; //Integer Klasse ist notwendig, um auf "null" zu kontrollieren
		//Wenn .get einen Zahlenwert zurück gibt, ist dieser vom Typ Long, er wird allerdings als "Object" zurückgegeben
		//daher kann man die Rückgabe von .get() nur zu Long casten
		Long evaluationFreqLong = (Long)jsonObj.get("evaluationFreq"); 
		if(evaluationFreqLong == null) {
			evaluationFreq=null;
		}else {
			evaluationFreq = evaluationFreqLong.intValue();
		}
		
		// der selbe Prozess wird für alle anderen notwendigen Parameter des Netzwerks durchgeführt
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
		
		Number learningRateNumber = (Number)jsonObj.get("learningRate");
		if (learningRateNumber == null) {
			throw new NullPointerException("\"learningRate\" must be provided");
		}
		double learningRate = learningRateNumber.doubleValue();
		
		//das Learning Network wird erstellt und zurückgegeben
		return new LearningNetwork(layers, learningType, learningRate, epochs, miniBatchSize, evaluationFreq); 
	}
	
	//Aus lesbarkeitsgründen des obrigen Codes, habe ich das ist eine eigene Methode gefasst
	public static int[] getLayersJson(JSONObject jsonObj) throws FileNotFoundException, 
	IOException, ParseException, LearningTypeNotSupported {
		// Code folgt den Selben Mustern wie der obrige Code
		
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
		//number of Layers und die Länge des Arrays müssen gleich sein
		if (numOfLayers != jsonNeurons.size()) {
			throw new IndexOutOfBoundsException("Array elements: " + jsonNeurons.size() + 
					" numOfLayers: " + numOfLayers);
		}
		//das JSON-Array wird in das int Array geschrieben
		for (int i = 0; i < numOfLayers; i++) {
			layers[i] = ((Long)jsonNeurons.get(i)).intValue();
		}
		return layers;
		
	}

}
