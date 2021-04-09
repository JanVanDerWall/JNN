package JNNMain.exceptions;

@SuppressWarnings("serial")
public class LearningTypeNotSupported extends Exception{

	private String learningType;

	public LearningTypeNotSupported(String learningType) {
		this.learningType = learningType;
	}

	public String getLearningType() {
		return learningType;
	}
	
}
