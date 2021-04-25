package JNNMain;

import javax.management.RuntimeErrorException;
/*
 * Die Klasse fasst ein Network und einen Learner zusammen
 * So muss der Benutzer der Bibliothek nicht beides verwenden
 * Es ist besonders bei der Verwendung von JSON-Dateien praktisch
 */
public class LearningNetwork extends Network{
	
	private static final long serialVersionUID = 1L; //Da Network serialisierbar ist, muss auch hier eine serienNummer vorhanden sein
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
		//wenn kein Evaluierungs Frequenz gegeben ist, ist eval=false
		if (evaluationFreq != null) {
			eval = true;
			this.evaluationFreq = evaluationFreq;
		} else {
			eval = false;
		}
	}
	
	//Die zwei Methoden, um den Learner zu initialisieren
	public void learnerInit(TrainDataSet[] trainData) {
		learner = new Learner(this, trainData);
	}
	
	public void learnerInit(TrainDataSet[] trainData, TrainDataSet[] testData) {
		learner = new Learner(this, trainData, testData);
	}
	
	//Es muss zum trainieren lediglich eine Methode aufgerufen werden
	public Network train() {
		Network net=null;
		if (eval) {
			//durch den switch können einfach weitere Algorithmen hinzugefügt werden
			switch(learningType) {
			case GradientDescent:
				System.out.println("trainig with Gradientdescent and evaluation");
				net = learner.trainGradientDecent_ev(learningRate, epochs, evaluationFreq);
				break;
			case StochasticGradientDescnet:
				System.out.println("trainig with stochastic Gradientdescent and evaluation");
				net = learner.trainStochastikGradientDescent_ev(learningRate, epochs, miniBatchSize, evaluationFreq);
			default:
				break;
		
			}
		} else {
			switch(learningType) {
			case GradientDescent:
				System.out.println("trainig with Gradientdescent and without evaluation");
				net = learner.trainGradientDecent(learningRate, epochs);
				break;
			case StochasticGradientDescnet:
				System.out.println("trainig with stochastic Gradientdescent and without evaluation");
				net = learner.trainStochastikGradientDescent(learningRate, epochs, miniBatchSize);
			default:
				break;
		
			}
		}
		if (net==null) {
			throw new RuntimeException("Irgendwas ist grundsätzlich falsch gelaufen, nicht ihr Fehler");
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