package physics.constraints;

import math.AngleUtils;
import math.Vec2D;
import physics.PhysicsComponent;

public class AnglePointConstraint extends PointBodyJoint {

	// angles stored between -Pi and Pi
	private final float minAngle;
	private final float maxAngle;

	/**
	 *
	 * @param a
	 * @param point
	 * @param midAngle
	 *            the angle in the range of -Pi to Pi.
	 * @param tolerance
	 *            the angle tolerance in both directions. 0 <= tolerance < Pi
	 */
	public AnglePointConstraint(final PhysicsComponent a, final Vec2D point, final float midAngle, final float tolerance) {
		super(a, point);
		if (tolerance < 0 || tolerance >= AngleUtils.PI) {
			throw new IllegalArgumentException("Tolerance must be >= 0 and < Pi");
		}
		minAngle = AngleUtils.normalize(midAngle - tolerance);
		maxAngle = AngleUtils.normalize(midAngle + tolerance);
	}

	@Override
	public void update() {
		final Vec2D aToB = getB().minus(getA().center());
		// angle from A to B
		final float angle = aToB.getTheta();

		if (angle >= minAngle && angle <= maxAngle) {
			// we don't need to do anything
			return;
		}
		// if we are in that dumb spot where maxAngle < min Angle (directly to the left) we need extra checks
		if (maxAngle < minAngle && (angle <= maxAngle && angle >= -AngleUtils.PI || angle >= minAngle && angle <= AngleUtils.PI)) {
			return;
		}

		final float distBtoA = aToB.length();

		final float closestAngleBound = AngleUtils.angleDifference(angle, maxAngle) < AngleUtils.angleDifference(angle, minAngle) ? maxAngle
				: minAngle;

		// where we should be
		final Vec2D solvedLocation = getA().center().plus(
				new Vec2D((float) (Math.cos(closestAngleBound) * distBtoA), (float) (Math.sin(closestAngleBound) * distBtoA)));
		final Vec2D correction = solvedLocation.minus(getB());

		getA().applyForce(correction);
	}

}
