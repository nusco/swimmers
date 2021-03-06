package org.nusco.narjillos.creature;

import java.util.List;

import org.nusco.narjillos.core.chemistry.Element;
import org.nusco.narjillos.core.geometry.BoundingBox;
import org.nusco.narjillos.core.geometry.Segment;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.things.Energy;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.core.configuration.Configuration;
import org.nusco.narjillos.core.utilities.NumGen;
import org.nusco.narjillos.creature.body.Body;
import org.nusco.narjillos.creature.body.ConnectedOrgan;
import org.nusco.narjillos.creature.body.Mouth;
import org.nusco.narjillos.creature.embryogenesis.Embryo;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.DNALog;

/**
 * A fully-formed, autonomous creature.
 */
public class Narjillo implements Thing {

	public static final String LABEL = "narjillo";

	private final Body body;

	private final DNA dna;

	private final Energy energy;

	private final Mouth mouth = new Mouth();

	private Vector target = Vector.ZERO;

	private long age = 0;

	private long nextEggAge = 0;

	private transient boolean isInPain = false;

	public Narjillo(DNA dna, Vector position, double angle, Energy energy) {
		this.body = new Embryo(dna).develop();
		body.forcePosition(position, angle);
		this.dna = dna;
		this.energy = energy;
	}

	@Override
	public Segment tick() {
		forgetPain();

		growOlder();

		Vector startingPosition = body.getStartPoint();

		if (isDead())
			return new Segment(startingPosition, Vector.ZERO);

		mouth.tick(getPosition(), getTarget(), getBody().getAngle());

		double energyRequiredToMove = body.tick(getMouth().getDirection());
		getEnergy().tick(-energyRequiredToMove);

		return new Segment(startingPosition, body.getStartPoint().minus(startingPosition));
	}

	@Override
	public Vector getPosition() {
		return body.getStartPoint();
	}

	@Override
	public Energy getEnergy() {
		return energy;
	}

	@Override
	public Vector getCenter() {
		return getCenterOfMass();
	}

	@Override
	public double getRadius() {
		return getBody().getRadius();
	}

	@Override
	public BoundingBox getBoundingBox() {
		return body.getBoundingBox();
	}

	@Override
	public String getLabel() {
		return Narjillo.LABEL;
	}

	@Override
	public Thing getInteractor() {
		return Thing.NULL;
	}

	public DNA getDNA() {
		return dna;
	}

	public synchronized Vector getTarget() {
		return target;
	}

	public synchronized void setTarget(Vector target) {
		this.target = target;
	}

	public Vector getCenterOfMass() {
		return body.getCenterOfMass();
	}

	public boolean isDead() {
		return getEnergy().isZero();
	}

	public Body getBody() {
		return body;
	}

	public List<ConnectedOrgan> getOrgans() {
		return body.getOrgans();
	}

	public Mouth getMouth() {
		return mouth;
	}

	public long getAge() {
		return age;
	}

	/**
	 * Returns the newly laid egg, or null if the narjillo doesn't want to lay it.
	 */
	public Egg layEgg(DNALog dnaLog, NumGen numGen) {
		if (getAge() < nextEggAge)
			return null;

		if (isTooYoungToLayEggs()) {
			// skip this chance to reproduce
			decideWhenToLayTheNextEgg();
			return null;
		}

		double energyToChild = getBody().getEnergyToChildren();
		double energyToEgg = Math.pow(getBody().getEggVelocity() * Configuration.EGG_MASS, 2);

		double totalEnergyRequired = energyToChild + energyToEgg;
		if (getEnergy().getValue() < totalEnergyRequired)
			return null;

		getEnergy().increaseBy(-energyToChild);
		DNA childDNA = mutateDna(dnaLog, getDNA(), numGen);

		decideWhenToLayTheNextEgg();
		Vector position = getNeckLocation();
		Vector velocity = Vector.polar(360 * numGen.nextDouble(), getBody().getEggVelocity());
		return new Egg(childDNA, position, velocity, energyToChild, numGen);
	}

	public Element getBreathedElement() {
		return body.getBreathedElement();
	}

	public Element getByproduct() {
		return body.getByproduct();
	}

	public double getBrainWaveAngle() {
		return body.getBrainWaveAngle();
	}

	public void growToAdultForm() {
		getBody().growToAdultForm();
	}

	public Vector getNeckLocation() {
		return getBody().getHead().getEndPoint();
	}

	public void damage() {
		energy.damage();
		isInPain = true;
	}

	public boolean isInPain() {
		return isInPain;
	}

	private DNA mutateDna(DNALog dnaLog, DNA parent, NumGen numGen) {
		DNA result = parent.mutate(numGen.nextSerial(), numGen);
		dnaLog.save(result);
		return result;
	}

	private void forgetPain() {
		isInPain = false;
	}

	private void decideWhenToLayTheNextEgg() {
		nextEggAge = getAge() + getBody().getEggInterval();
	}

	private boolean isTooYoungToLayEggs() {
		return getAge() < Configuration.CREATURE_MATURE_AGE;
	}

	private void growOlder() {
		age++;
	}
}
