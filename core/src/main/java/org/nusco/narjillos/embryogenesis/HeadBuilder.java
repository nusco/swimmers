package org.nusco.narjillos.embryogenesis;

import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.genomics.Chromosome;

strictfp class HeadBuilder extends OrganBuilder {

	public HeadBuilder(Chromosome chromosome) {
		super(chromosome);
	}

	public double getMetabolicRate() {
		final double MAX_METABOLIC_RATE = 3;
		return getChromosome().getGene(3) * (MAX_METABOLIC_RATE / 255);
	}

	public double getPercentEnergyToChildren() {
		return (getChromosome().getGene(4) + 1) / 256.0;
	}

	public Head build() {
		return new Head(getLength(), getThickness(), getHue(), getMetabolicRate(), getPercentEnergyToChildren());
	}
}