package physics.constraints;

import math.MathUtils;
import math.Vec2D;
import physics.CollisionComponent;
import physics.collision.handling.CManifold;
import physics.collision.handling.Collisions;

public class AngleJoint extends Joint {

	// angles stored between -Pi and Pi
	private final float minAngle;
	private final float maxAngle;

	/**
	 *
	 * @param a
	 * @param b
	 * @param midAngle
	 *            the angle in the range of 0 to 2*Pi.
	 * @param tolerance
	 *            the angle tolerance in both directions. 0 <= tolerance < Pi
	 */
	public AngleJoint(final CollisionComponent a, final CollisionComponent b, final float midAngle, final float tolerance) {
		super(a, b);
		if (tolerance < 0 || tolerance >= MathUtils.PI) {
			throw new IllegalArgumentException("Tolerance must be >= 0 and < Pi");
		}
		minAngle = MathUtils.normalize(midAngle - tolerance);
		maxAngle = MathUtils.normalize(midAngle + tolerance);
	}

	@Override
	public void update() {
		final CManifold m = new CManifold();
		m.a = getA();
		m.b = getB();

		final Vec2D aToB = getB().getPos().minus(getA().getPos());
		// angle from A to B
		final float angle = aToB.getTheta();

		if (angle >= minAngle && angle <= maxAngle) {
			// we don't need to do anything
			return;
		}
		// if we are in that dumb spot where maxAngle < min Angle (directly to
		// the left) we need extra checks
		if (maxAngle < minAngle && (angle <= maxAngle && angle >= -MathUtils.PI || angle >= minAngle && angle <= MathUtils.PI)) {
			return;
		}

		final float distBtoA = aToB.length();

		final float closestAngleBound = MathUtils.angleDifference(angle, maxAngle) < MathUtils.angleDifference(angle, minAngle) ? maxAngle
				: minAngle;

		// where we should be
		final Vec2D solvedLocation = getA().getPos().plus(
				new Vec2D((float) (Math.cos(closestAngleBound) * distBtoA), (float) (Math.sin(closestAngleBound) * distBtoA)));
		final Vec2D correction = solvedLocation.minus(getB().getPos());
		final float d = correction.length();

		m.setNormal(correction.divide(d));
		m.setPenetration(d);
		Collisions.fixCollision(m, false);
	}

}
