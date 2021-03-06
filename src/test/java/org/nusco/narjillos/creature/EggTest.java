package org.nusco.narjillos.creature;

import org.junit.Test;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.core.utilities.NumGen;
import org.nusco.narjillos.genomics.DNA;

import static org.junit.Assert.*;

public class EggTest {

	private final DNA dna = new DNA(1, "{1_2_3}");

	private final Egg egg = new Egg(dna, Vector.cartesian(10, 20), Vector.ZERO, 100, new NumGen(1));

	@Test
	public void hatchesANarjilloAfterAnIncubationPeriod() {
		assertFalse(egg.getHatchedNarjillo().isPresent());

		waitUntilItHatches(egg);
		Narjillo narjillo = egg.getHatchedNarjillo().get();

		assertEquals(egg.getPosition(), narjillo.getPosition());
	}

	@Test
	public void itsLastInteractingThingIsInitiallyNull() {
		Thing lastInteractingThing = egg.getInteractor();

		assertSame(Thing.NULL, lastInteractingThing);
	}

	@Test
	public void theHatchedNarjilloBecomesItsInteractor() {
		waitUntilItHatches(egg);

		assertSame(egg.getHatchedNarjillo().get(), egg.getInteractor());
	}

	@Test
	public void passesItsEnergyToTheHatchedNarjillo() {
		assertEquals(100, egg.getEnergy().getValue(), 0);

		waitUntilItHatches(egg);
		Narjillo narjillo = egg.getHatchedNarjillo().get();

		assertEquals(0, egg.getEnergy().getValue(), 0);
		assertEquals(100, narjillo.getEnergy().getValue(), 0);
	}

	@Test
	public void putsDNAIntoTheHatchedNarjillo() {
		waitUntilItHatches(egg);
		Narjillo narjillo = egg.getHatchedNarjillo().get();

		assertSame(dna, narjillo.getDNA());
	}

	@Test
	public void onlyHatchesOnce() {
		waitUntilItHatches(egg);

		assertFalse(egg.hatch(new NumGen(1)));
	}

	@Test
	public void decaysUpTo100PercentAfterHatching() {
		assertFalse(egg.isDead());
		assertEquals(0, egg.getFading(), 0);

		waitUntilItHatches(egg);

		for (int i = 0; i < 100; i++) {
			assertFalse(egg.isDead());
			assertEquals(i / 100.0, egg.getFading(), 0);
			egg.tick();
		}

		assertTrue(egg.isDead());
		assertEquals(1, egg.getFading(), 0);

		egg.tick();
		assertEquals(1, egg.getFading(), 0);
	}

	private void waitUntilItHatches(Egg egg) {
		NumGen numGen = new NumGen(1);
		while (!egg.hatch(numGen))
			egg.tick();
	}
}
