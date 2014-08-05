package org.nusco.narjillos.creature.genetics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ChromosomeTest {

	@Test
	public void returnsASingleGene() {
		Chromosome chromosome = new Chromosome(1, 2, 3, 4, 5, 6);

		assertEquals(4, chromosome.getGene(3));
	}

	@Test
	public void padsMissingGenesWithZeroes() {
		Chromosome chromosome = new Chromosome(1, 2, 3, 4);

		Chromosome expected = new Chromosome(1, 2, 3, 4, 0, 0);
		
		assertEquals(expected, chromosome);
	}

	@Test(expected=RuntimeException.class)
	public void chokesOnNegativeGenes() {
		new Chromosome(-1, -2, -3, -4, -5, -6);
	}

	@Test(expected=RuntimeException.class)
	public void chokesOnGenesOver255() {
		new Chromosome(1, 2, 3, 4, 5, 256);
	}

	@Test
	public void convertsToAString() {
		Chromosome chromosome = new Chromosome(1, 2, 3, 4, 5, 6);
		
		assertEquals("{001_002_003_004_005_006}", chromosome.toString());
	}
}