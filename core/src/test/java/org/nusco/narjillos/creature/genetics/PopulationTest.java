package org.nusco.narjillos.creature.genetics;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.shared.physics.Vector;

public class PopulationTest {
	
	Population population = new Population();
	
	@Before
	public void setupPopulation() {
		population.add(createCreature("111_111_111_222_111_000")); // genetic distance: 8
		population.add(createCreature("111_111_111_111_111_000")); // genetic distance: 9
		population.add(createCreature("111_111_111_222_222_000")); // genetic distance: 7
		population.add(createCreature("111_111_222_111_222_000")); // genetic distance: 9
		population.add(createCreature("111_222_222_222_222_000")); // genetic distance: 11
	}
	
	@Test
	public void getsMostTypicalSpecimen() {
		Creature creature = population.getMostTypicalSpecimen();
		
		assertEquals("{111_111_111_222_222_000}", creature.getDNA().toString());
	}

	private Creature createCreature(final String genes) {
		return new Creature() {
			@Override
			public void tick() {
			}

			@Override
			public DNA getDNA() {
				return new DNA(genes);
			}

			@Override
			public Vector getPosition() {
				return null;
			}

			@Override
			public String getLabel() {
				return null;
			}
		};
	}
}