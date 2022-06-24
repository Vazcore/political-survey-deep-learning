package political_survey_ai;

public class PartyAffiliation {
	private int timesWasUsed = 0;
	private double probability = 0.0;
	private int partyIndex;

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
	
	public void readFromText(String[] affiliation) {
		this.partyIndex = Integer.parseInt(affiliation[0]);
		this.probability = Double.parseDouble(affiliation[1]);
	}
	public int getPartyIndex() {
		return partyIndex;
	}
	public void setPartyIndex(int partyIndex) {
		this.partyIndex = partyIndex;
	}
}
