package JNNMain;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class NetworkFileWriter {
	
	//Serialisiert das Netzwerk in eine Datei
	public void serialize (String destination, Network net) throws FileNotFoundException, IOException {
		try (FileOutputStream fos = new FileOutputStream(destination);
				ObjectOutputStream oos = new ObjectOutputStream(fos)){
			oos.writeObject(net);
		}
	}
	
	//Deserialisiert ein Network und gibt es als Java-Objekt zurück
	public Network deserialize(String location) throws IOException, ClassNotFoundException {
		try (FileInputStream fis = new FileInputStream (location);
			    ObjectInputStream ois = new ObjectInputStream (fis)) {
			  Network net = (Network) ois.readObject ();
			  return net;
		}
	}
}
