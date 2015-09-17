package physics.constraints;

import math.Vec2D;
import physics.CollisionComponent;

public class SpringJoint extends Joint {
	public float distance;
	private final float restitution;

	public SpringJoint(final CollisionComponent a, final CollisionComponent b, final float distance, final float restitution) {
		super(a, b);

		if (restitution == 0.0f) {
			throw new IllegalArgumentException("Restitution must be non-zero");
		}

		this.distance = distance;
		this.restitution = restitution;
	}

	public SpringJoint(final CollisionComponent a, final CollisionComponent b, final float restitution) {
		this(a, b, a.getPos().minus(b.getPos()).length(), restitution);
	}

	@Override
	public void update() {
		assert getA() != null && getB() != null;

		final Vec2D n = getA().getPos().minus(getB().getPos());

		final float d = n.length();

		final Vec2D normal = d == 0 ? new Vec2D(0, 1) : n.divide(d);
		final float penetration = d - distance;

		final Vec2D springForce = normal.multiply(penetration * (1f / restitution));

		getA().applyForce(springForce.multiply(-1));
		getB().applyForce(springForce);

	}

}
