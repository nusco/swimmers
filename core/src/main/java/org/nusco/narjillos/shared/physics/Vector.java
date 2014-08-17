package org.nusco.narjillos.shared.physics;

/**
 * A point on a plane, in either cartesian or polar coordinates.
 */
public class Vector {

	public static final Vector ZERO = Vector.cartesian(0, 0);

	public final double x;
	public final double y;

	public static Vector polar(double degrees, double length) {
		double sin = FastMath.sin(degrees);
		double cos = FastMath.cos(degrees);
		return Vector.cartesian(cos * length, sin * length);
	}

	public static Vector cartesian(double x, double y) {
		return new Vector(x, y);
	}
	
	private Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getAngle() {
	    return Math.toDegrees(Math.atan2(y, x));
	}

	public double getLength() {
		return Math.sqrt(x * x + y * y);
	}

	public Vector plus(Vector other) {
		return Vector.cartesian(x + other.x, y + other.y);
	}

	public Vector minus(Vector other) {
		return Vector.cartesian(x - other.x, y - other.y);
	}

	public Vector by(double scalar) {
		return Vector.cartesian(x * scalar, y * scalar);
	}

	public Vector invert() {
		return this.by(-1);
	}

	public Vector normalize(double length) {
		if (getLength() == 0)
			return Vector.ZERO; // the zero vector cannot be normalized
		return Vector.polar(getAngle(), length);
	}

	public Vector getNormal() {
		return Vector.polar(getAngle() - 90, 1);
	}

	public Vector getProjectionOn(Vector other) {
		Vector direction = pointsInSameDirectionAs(other) ? other : other.invert();
		double relativeAngle = direction.getAngle() - getAngle();
		double resultLength = FastMath.cos(relativeAngle) * getLength();
		return Vector.polar(direction.getAngle(), resultLength);
	}

	private boolean pointsInSameDirectionAs(Vector other) {
		return other.getAngle() - getAngle() < 90;
	}

	public Vector getNormalComponentOn(Vector other) {
		return getProjectionOn(other.getNormal());
	}

	public double getAngleWith(Vector other) {
		double result = getAngle() - other.getAngle();
		if(result < -180)
			return result + 360;
		if(result > 180)
			return result - 360;
		return result;
	}

	public Vector rotateBy(double degrees) {
		return Vector.polar(getAngle() + degrees, getLength());
	}

	double getDistanceFrom(Vector other) {
		return this.minus(other).getLength();
	}

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		Vector other = (Vector) obj;
		return compare(x, other.x) && compare(y, other.y);
	}

	private boolean compare(double d1, double d2) {
		return Double.doubleToLongBits(d1) == Double.doubleToLongBits(d2);
	}

	public boolean almostEquals(Vector other) {
		final double delta = 0.001;
		return Math.abs(x - other.x) < delta && Math.abs(y - other.y) < delta;
	}
	
	@Override
	public String toString() {
		return "(" + approx(x) + ", " + approx(y) + ")";
	}

	private double approx(double n) {
		final double decimals = 100.0;
		return (Math.round(n * decimals)) / decimals;
	}

	public double getCrossProductWith(Vector other) {
		return getLength() * other.getLength() * FastMath.cos(getAngleWith(other));
	}
}
