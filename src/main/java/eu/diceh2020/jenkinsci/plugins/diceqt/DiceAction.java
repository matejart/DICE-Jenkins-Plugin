package eu.diceh2020.jenkinsci.plugins.diceqt;

import hudson.model.Action;

public class DiceAction implements Action {
	private double latency = 0.0;

	public DiceAction(double latency) {
		this.latency = latency;
	}
	
	@Override
	public String getIconFileName() {
		return "clipboard.png";
	}

	@Override
	public String getDisplayName() {
		return "DICE report";
	}

	@Override
	public String getUrlName() {
		return "diceReport";
	}
	
	public double getLatency() {
		return this.latency;
	}
	
	public void setLatency(double latency) {
		this.latency = latency;
	}

}
