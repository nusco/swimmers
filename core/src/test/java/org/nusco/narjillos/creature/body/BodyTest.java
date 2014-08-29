package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class BodyTest {

	@Test
	public void isPositionedAtZeroByDefault() {
		Body body = new Body(new Head(1, 1, new ColorByte(1), 1));
		assertEquals(Vector.ZERO, body.getStartPoint());
	}

	@Test
	public void canBeTeleportedToAGivenPosition() {
		Body body = new Body(new Head(1, 1, new ColorByte(1), 1));
		
		body.teleportTo(Vector.cartesian(10, -10));

		assertEquals(Vector.cartesian(10, -10), body.getStartPoint());
	}
	
	@Test
	public void hasAMassProportionalToItsSize() {
		int headLengthInMm = 3;
		int headThicknessInMm = 4;
		Head head = new Head(headLengthInMm, headThicknessInMm, new ColorByte(0), 1);
		
		int segmentLengthInMm = 10;
		int segmentThicknessInMm = 20;
		head.addChild(new BodySegment(segmentLengthInMm, segmentThicknessInMm, new ColorByte(0), head, 0, 0, 0));
		Body body = new Body(head);
		
		double expectedMassInGrams = 212;
		assertEquals(expectedMassInGrams, body.getMass(), 0.001);
	}
	
	@Test
	public void hasACenterOfMassAndARadius() {
		Head head = new Head(10, 10, new ColorByte(1), 1);
		
		BodySegment child = new BodySegment(20, 5, new ColorByte(0), head, 0, 0, 0);
		head.addChild(child);

		Body body = new Body(head);

		// calculateRadius() needs an explicit center of mass, because of optimizations.
		// So these two are better tested together: 
		Vector centerOfMass = body.calculateCenterOfMass();
		assertEquals(Vector.cartesian(12.5, 0), centerOfMass);
		assertEquals(17.5, body.calculateRadius(body.calculateCenterOfMass()), 0.0);
	}	
	
	@Test
	public void itsMinimumRadiusIsOne() {
		Head head = new Head(0, 1, new ColorByte(1), 1);
		Body body = new Body(head);
		assertEquals(1, body.calculateRadius(head.getCenterOfMass()), 0.0);
	}
}
