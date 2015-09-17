package physics.constraints;

import math.Vec2D;
import physics.CollisionComponent;
import physics.collision.handling.CManifold;
import physics.collision.handling.Collisions;

public class StringJoint extends Joint {
	public float distance;

	public StringJoint(final CollisionComponent a, final CollisionComponent b, final float distance) {
		super(a, b);
		this.distance = distance;
	}

	public StringJoint(final CollisionComponent a, final CollisionComponent b) {
		this(a, b, a.getPos().minus(b.getPos()).length());
	}

	@Override
	public void update() {
		assert getA() != null && getB() != null;

		final CManifold m = new CManifold();
		m.a = getA();
		m.b = getB();

		final Vec2D n = getA().getPos().minus(getB().getPos());

		final float d = n.length();
		if (d <= distance) {
			// we don't need to do anything
			return;
		}

		m.setNormal(d < distance ? n.divide(d).multiply(-1) : n.divide(d));
		m.setPenetration(Math.abs(d - distance));

		Collisions.fixCollision(m, false);
	}
}
