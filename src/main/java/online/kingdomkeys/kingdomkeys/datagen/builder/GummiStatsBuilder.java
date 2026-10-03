package online.kingdomkeys.kingdomkeys.datagen.builder;

import online.kingdomkeys.kingdomkeys.block.gummi.GummiStats;

public class GummiStatsBuilder {
	private int weight, armour, cost, topSpeed, lowSpeed, horsepower, firepower, fuelPerShot, mobility;

	public GummiStatsBuilder weight(int value) {
		this.weight = value;
		return this;
	}

	public GummiStatsBuilder armour(int value) {
		this.armour = value;
		return this;
	}

	public GummiStatsBuilder cost(int value) {
		this.cost = value;
		return this;
	}

	public GummiStatsBuilder topSpeed(int value) {
		this.topSpeed = value;
		return this;
	}

	public GummiStatsBuilder lowSpeed(int value) {
		this.lowSpeed = value;
		return this;
	}

	public GummiStatsBuilder horsepower(int value) {
		this.horsepower = value;
		return this;
	}

	public GummiStatsBuilder firepower(int value) {
		this.firepower = value;
		return this;
	}

	public GummiStatsBuilder fuelPerShot(int value) {
		this.fuelPerShot = value;
		return this;
	}

	public GummiStatsBuilder mobility(int value) {
		this.mobility = value;
		return this;
	}

	public GummiStats build() {
		return new GummiStats(weight, armour, cost, topSpeed, lowSpeed, horsepower, firepower, fuelPerShot, mobility);
	}
}
