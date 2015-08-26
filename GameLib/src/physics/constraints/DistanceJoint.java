package physics.constraints;

import math.Vec2D;
import physics.PhysicsComponent;
import physics.collision.handling.CManifold;
import physics.collision.handling.Collisions;

public class DistanceJoint extends Joint {
	public float distance;

	public DistanceJoint(final PhysicsComponent a, final PhysicsComponent b, final float distance) {
		super(a, b);
		this.distance = distance;
	}

	public DistanceJoint(final PhysicsComponent a, final PhysicsComponent b) {
		this(a, b, a.center().minus(b.center()).length());
	}

	@Override
	public void update() {
		final CManifold m = new CManifold();
		m.a = getA();
		m.b = getB();

		final Vec2D n = getA().center().minus(getB().center());

		final float d = n.length();
		if (Math.abs(d - distance) < 10e-6) {
			// we don't need to do anything
			return;
		}

		m.setNormal(d < distance ? n.divide(d).multiply(-1) : n.divide(d));
		m.setPenetration(Math.abs(d - distance));

		Collisions.fixCollision(m, false);
	}
}
