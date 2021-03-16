package JNNMain;

import org.apache.commons.math3.linear.RealVector;

import JNNMain.exceptions.TrainingInputLengthDoesNotMatchOutput;

public class TestDataSet {
	private RealVector inputs;
	int output;
	private int setLength;
	
	public TestDataSet(RealVector inputs, int output) throws TrainingInputLengthDoesNotMatchOutput{
		this.inputs = inputs;
		this.output = output;
		
		
		
		setLength = inputs.getDimension();
		
	}
	
	//Getter und Setter
	public RealVector getInputs() {
		return inputs;
	}

	public void setInputs(RealVector inputs) {
		this.inputs = inputs;
	}

	public int getOutput() {
		return output;
	}

	public void setOutput(int output) {
		this.output = output;
	}

	public int getSetLength() {
		return setLength;
	}

	public void setSetLength(int setLength) {
		this.setLength = setLength;
	}
}
