package JNNMain;

import org.apache.commons.math3.linear.RealVector;

import JNNMain.exceptions.*;

//Diese Klasse Repräsentiert ein Set aus Trainigsdaten, also ein Paar aus einem Input und dem dazu optimalen output
//im prinzip ist diese Klasse recht selbsterklärend, ich bin mir auch nicht sicher, ob das in Java auch schneller geht
public class TrainDataSet {
	
	private RealVector inputs;
	private RealVector outputs;
	private int setLength;
	
	public TrainDataSet(RealVector inputs, RealVector outputs) throws TrainingInputLengthDoesNotMatchOutput{
		this.inputs = inputs;
		this.outputs = outputs;
		
		if (this.inputs.getDimension() == this.outputs.getDimension()) {
			throw new TrainingInputLengthDoesNotMatchOutput (this.inputs.getDimension(), this.outputs.getDimension());
		}
		
		setLength = inputs.getDimension();
		
	}
	
	//Getter und Setter
	public RealVector getInputs() {
		return inputs;
	}

	public void setInputs(RealVector inputs) {
		this.inputs = inputs;
	}

	public RealVector getOutputs() {
		return outputs;
	}

	public void setOutputs(RealVector outputs) {
		this.outputs = outputs;
	}

	public int getSetLength() {
		return setLength;
	}

	public void setSetLength(int setLength) {
		this.setLength = setLength;
	}
	
}
