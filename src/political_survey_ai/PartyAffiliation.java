package political_survey_ai;

public class PartyAffiliation {
	private int timesWasUsed = 0;
	private double probability = 0.0;

	public int getTimesWasUsed() {
		return timesWasUsed;
	}
	public void setTimesWasUsed(int timesWasUsed) {
		this.timesWasUsed = timesWasUsed;
	}
	public double getProbability() {
		return probability;
	}
	public void setProbability(double probability) {
		this.probability = probability;
	}
}
