package physics.constraints;

import game.Vec2D;
import physics.GameEntity;
import physics.collision.CManifold;
import physics.collision.Collisions;

public class AngleJoint extends Joint {

	private float minAngle;
	private float maxAngle;

	public AngleJoint(final GameEntity a, final GameEntity b, final float midAngle, final float tolerance) {
		super(a, b);
		assert tolerance >= 0;
		System.out.println(midAngle);
		minAngle = midAngle - tolerance;
		maxAngle = midAngle + tolerance;

		while (minAngle > Math.PI) {
			minAngle -= 2 * Math.PI;
		}
		while (minAngle < -Math.PI) {
			minAngle += 2 * Math.PI;
		}
		while (maxAngle > Math.PI) {
			maxAngle -= 2 * Math.PI;
		}
		while (maxAngle < -Math.PI) {
			maxAngle += 2 * Math.PI;
		}
		System.out.println(minAngle + ", " + maxAngle);
	}

	@Override
	public void update() {
		assert getA() != null && getB() != null;

		final CManifold m = new CManifold();
		m.a = getA();
		m.b = getB();

		final Vec2D aToB = getB().center().minus(getA().center());
		// angle from A to B
		final float angle = aToB.getTheta();

		if (angle >= minAngle && angle <= maxAngle) {
			// we don't need to do anything
			return;
		}

		final float distBtoA = aToB.length();

		final float closestAngleBound = Math.abs(angle - maxAngle) < Math.abs(angle - minAngle) ? maxAngle : minAngle;

		// where we should be
		final Vec2D solvedLocation = getA().center().plus(
				new Vec2D((float) (Math.cos(closestAngleBound) * distBtoA), (float) (Math.sin(closestAngleBound) * distBtoA)));
		final Vec2D correction = solvedLocation.minus(getB().center());
		final float d = correction.length();

		m.setNormal(correction.divide(d));
		m.setPenetration(d);
		Collisions.fixCollision(m, false);
	}

}
