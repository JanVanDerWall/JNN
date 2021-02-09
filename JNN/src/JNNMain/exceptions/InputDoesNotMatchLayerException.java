package JNNMain.exceptions;

@SuppressWarnings("serial")
public class InputDoesNotMatchLayerException extends RuntimeException{
	
	private double[] input;
	
	public InputDoesNotMatchLayerException(double[] input) {
		this.input = input;
	}
	
	public double[] getInput() {
		return input;
	}

}
