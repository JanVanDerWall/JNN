package JNNMain;

public class LearningNetwork extends Network{
	
	
	private Learner learner;
	private LearningType learnigType;
	
	public LearningNetwork(int[] layers, LearningType type) {
		super(layers);
		learnigType = type; 
		
		
	}

}

