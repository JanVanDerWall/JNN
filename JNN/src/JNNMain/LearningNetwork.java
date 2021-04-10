package JNNMain;

import javax.management.RuntimeErrorException;

public class LearningNetwork extends Network{
	
	
	private static final long serialVersionUID = 1L;
	private Learner learner;
	private LearningType learningType;
	private double learningRate;
	private int epochs;
	private boolean eval;
	private Integer miniBatchSize;
	private Integer evaluationFreq;
	
	
	public LearningNetwork(int[] layers, LearningType type, double learningRate, 
			int epochs, Integer miniBatchSize, Integer evaluationFreq) {
		super(layers);
		this.learningType = type;
		this.learningRate = learningRate;
		this.epochs = epochs;
		if (miniBatchSize!=null) {
			this.miniBatchSize = miniBatchSize;
		}
		if (evaluationFreq != null) {
			eval = true;
			this.evaluationFreq = evaluationFreq;
		} else {
			eval = false;
		}
	}
	
	public void learnerInit(TrainDataSet[] trainData) {
		learner = new Learner(this, trainData);
	}
	
	public void learnerInit(TrainDataSet[] trainData, TrainDataSet[] testData) {
		learner = new Learner(this, trainData, testData);
	}
	
	public Network train() {
		Network net=null;
		if (eval) {
			if (learningType == LearningType.GradientDescent) {
				System.out.println("trainig with Gradientdescent and evaluation");
				net = learner.trainGradientDecent_ev(learningRate, epochs, evaluationFreq);
			} else if(learningType == LearningType.StochasticGradientDescnet) {
				System.out.println("trainig with stochastic Gradientdescent and evaluation");
				net = learner.trainStochastikGradientDescent_ev(learningRate, epochs, miniBatchSize, evaluationFreq);
			}
		} else {
			if (learningType == LearningType.GradientDescent) {
				System.out.println("trainig with Gradientdescent and without evaluation");
				net = learner.trainGradientDecent(learningRate, epochs);
			} else if(learningType == LearningType.StochasticGradientDescnet) {
				System.out.println("trainig with stochastic Gradientdescent and without evaluation");
				net = learner.trainStochastikGradientDescent(learningRate, epochs, miniBatchSize);
			}
		}
		return net;
	}
	
	public boolean getEval() {
		return this.eval;
	}
	public Learner getLearner() {
		return learner;
	}
}