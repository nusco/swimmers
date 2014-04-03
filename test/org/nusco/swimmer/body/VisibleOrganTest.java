package org.nusco.swimmer.body;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.nusco.swimmer.body.Organ;
import org.nusco.swimmer.body.VisibleOrgan;

public abstract class VisibleOrganTest {

	protected static int THICKNESS = 8;
	
	protected VisibleOrgan organ;

	@Before
	public void setUpPart() {
		organ = createVisibleOrgan();
	}

	public abstract VisibleOrgan createVisibleOrgan();

	@Test
	public void isVisible() {
		assertTrue(organ.isVisible());
	}

	@Test
	public void hasALength() {
		assertEquals(20, organ.getLength());
	}

	@Test
	public void hasAThickness() {
		assertEquals(THICKNESS, organ.getThickness());
	}

	@Test
	public abstract void hasAnEndPoint();

	@Test
	public abstract void hasAParent();

	@Test
	public void hasAnRGBValue() {
		assertEquals(100, organ.getRGB());
	}

	@Test
	public void returnsItselfAsAParent() {
		assertEquals(organ, organ.getAsParent());
	}
	
	@Test
	public void hasAnEmptyListOfChildPartsByDefault() {
		assertEquals(Collections.EMPTY_LIST, organ.getChildren());
	}

	@Test
	public void canSproutVisibleOrgans() {
		VisibleOrgan child = organ.sproutVisibleOrgan(20, 12, 45, 100);
		assertEquals(20, child.getLength());
		assertEquals(12, child.getThickness());
		assertEquals(45, child.getRelativeAngle(), 0);
	}

	@Test
	public void canSproutInvisibleOrgans() {
		Organ child = organ.sproutInvisibleOrgan();
		assertFalse(child.isVisible());
	}
	
	@Test
	public void knowsItsChildren() {
		VisibleOrgan child1 = organ.sproutVisibleOrgan(20, THICKNESS, 45, 100);
		VisibleOrgan child2 = organ.sproutVisibleOrgan(20, THICKNESS, 45, 100);

		List<VisibleOrgan> expected = new LinkedList<>();
		expected.add(child1);
		expected.add(child2);
		
		assertEquals(expected, organ.getChildren());
	}
}