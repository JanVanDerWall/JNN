package JNNMain.exceptions;

@SuppressWarnings("serial")
public class TrainingInputLengthDoesNotMatchOutput extends RuntimeException{
	
private int inputLength;
private int outputLength;
	
	public TrainingInputLengthDoesNotMatchOutput(int inputLength, int outputLength) {
		this.inputLength = inputLength;
		this.outputLength = outputLength;
	}

	public int getInputLength() {
		return inputLength;
	}

	public int getOutputLength() {
		return outputLength;
	}

}
