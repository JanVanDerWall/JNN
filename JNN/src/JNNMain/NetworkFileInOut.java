package JNNMain;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class NetworkFileInOut {
	
	//Serialisiert das Netzwerk in eine Datei
	public static void serialize (String destination, Network net) throws FileNotFoundException, IOException {
		try (FileOutputStream fos = new FileOutputStream(destination);
				ObjectOutputStream oos = new ObjectOutputStream(fos)){
			oos.writeObject(net);
		}
	}
	
	//Deserialisiert ein Network und gibt es als Java-Objekt zurück
	public static Network deserialize(String location) throws IOException, ClassNotFoundException {
		try (FileInputStream fis = new FileInputStream (location);
			    ObjectInputStream ois = new ObjectInputStream (fis)) {
			  Network net = (Network) ois.readObject ();
			  return net;
		}
	}
	
	//Speichert ein Network in einem für diese Bibliothek einzigartigem Dateiformat
	//Wie diese Datein aufgebaut sind, ist der Dokumentation zu entnehmen
	//sollte das bekannt sein, dürfte dieser Code recht selbsterklärend sein
	public static void writeToFile (String destination, Network net) throws IOException {
		
		DataOutputStream out = new DataOutputStream(new FileOutputStream(destination));
		out.writeLong(Network.getSerialversionuid());
		out.writeInt(net.getNumberOfLayers());
		
		for (int i = 0; i < net.getNumberOfLayers(); i++) {
			out.writeInt(net.getLayerSizes()[i]);
		}
		
		for (int i = 0; i < net.getNumberOfLayers()-1; i++) {
			RealMatrix cWeights = net.getWeights()[i];
			RealVector cBiases = net.getBiases()[i];
			
			out.writeInt(cWeights.getRowDimension());
			out.writeInt(cWeights.getColumnDimension());
			for (int j = 0; j < cWeights.getRowDimension(); j++) {
				for (int j2 = 0; j2 < cWeights.getColumnDimension(); j2++) {
					out.writeDouble(cWeights.getEntry(j, j2));
				}
			}
			
			out.writeInt(cBiases.getDimension());
			for (int j = 0; j < cBiases.getDimension(); j++) {
				out.writeDouble(cBiases.getEntry(j));
			}
			
		}
		
		out.close();
	}
	//ließt ein Netzwerk aus einer solchen Datei
	public static Network readFromFile(String location) throws IOException{
		
		DataInputStream in = new DataInputStream(new FileInputStream(location));
		
		long versionID = in.readLong();
		assert (versionID==Network.getSerialversionuid()); //sollte ich das Dateiformat zwischendurch geändert haben, habe ich auch eine Andere Version ID
		int numOfLayers = in.readInt();
		int[] layers = new int[numOfLayers];
		
		for (int i = 0; i < layers.length; i++) {
			layers[i]=in.readInt();
		}
		
		Network net = new Network(layers);
		
		RealMatrix[] weights = new RealMatrix[numOfLayers-1];
		RealVector[] biases = new RealVector[numOfLayers-1];
		
		for (int i = 0; i < numOfLayers-1; i++) {
			
			int cWeightsRows = in.readInt();
			int cWeightsColumns = in.readInt();
			weights[i]=new Array2DRowRealMatrix(cWeightsRows, cWeightsColumns);
			for (int j = 0; j < cWeightsRows; j++) {
				for (int j2 = 0; j2 < cWeightsColumns; j2++) {
					weights[i].setEntry(j, j2, in.readDouble());
				}
			}
			
			int cBiasRows = in.readInt();
			biases[i]=new ArrayRealVector(cBiasRows);
			for (int j = 0; j <cBiasRows; j++) {
				biases[i].setEntry(j, in.readDouble());
			}
			
		}
		
		net.setWeights(weights);
		net.setBiases(biases);
		
		in.close();
		
		return net;
	}
	
}
