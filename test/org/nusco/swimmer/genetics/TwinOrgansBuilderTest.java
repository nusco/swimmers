package org.nusco.swimmer.genetics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmer.body.Organ;

public class TwinOrgansBuilderTest {
	private static final int MIRRORING = DNA.MIRROR_ORGAN;
	private static final int NOT_MIRRORING = DNA.MIRROR_ORGAN ^ DNA.MIRROR_ORGAN;

	int[] notMirroringGenes1 = new int[] {NOT_MIRRORING, 60, 70, 80, 90};
	int[] notMirroringGenes2 = new int[] {NOT_MIRRORING, 61, 71, 81, 91};
	int[] mirroringGenes1 = new int[] {MIRRORING, 62, 72, 82, 92};
	int[] mirroringGenes2 = new int[] {MIRRORING, 63, 73, 83, 93};

	Organ parent = new OrganBuilder(new int[]{0, 7, 7, 7, 7}).buildHead();

	@Test
	public void buildsRegularSegmentIfNeitherGenesIsMirroring() {
		Organ[] segments = new TwinOrgansBuilder(notMirroringGenes1, notMirroringGenes2).buildSegments(parent);
		
		assertEquals(60, segments[0].getLength());
		assertEquals(61, segments[1].getLength());
	}

	@Test
	public void buildsMirrorSegmentOfSecondSegmentIfBothGenesAreMirroring() {
		Organ[] segments = new TwinOrgansBuilder(mirroringGenes1, mirroringGenes2).buildSegments(parent);
		
		assertEquals(63, segments[0].getLength());
		assertEqualOrgans(segments[0], segments[1]);
	}

	@Test
	public void buildsMirrorSegmentIfFirstSegmentGenesAreMirroring() {
		TwinOrgansBuilder builder = new TwinOrgansBuilder(mirroringGenes1, notMirroringGenes2);
		Organ[] segments = builder.buildSegments(parent);
		
		assertEqualOrgans(segments[0], segments[1]);
	}

	@Test
	public void buildsMirrorSegmentIfSecondSegmentGenesAreMirroring() {
		TwinOrgansBuilder builder = new TwinOrgansBuilder(notMirroringGenes1, mirroringGenes2);
		Organ[] segments = builder.buildSegments(parent);
		
		assertEqualOrgans(segments[0], segments[1]);
	}

	private void assertEqualOrgans(Organ organ1, Organ organ2) {
		assertEquals(organ1.getLength(), organ2.getLength());
		assertEquals(organ1.getThickness(), organ2.getThickness());
		assertEquals(organ1.getRGB(), organ2.getRGB());
		assertEquals(-organ1.getRelativeAngle(), organ2.getRelativeAngle(), 0);
		assertEquals(organ1.getParent(), organ2.getParent());
	}
}
