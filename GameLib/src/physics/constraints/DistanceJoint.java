package physics.constraints;

import math.MathUtils;
import math.Vec2D;
import physics.CollisionComponent;
import physics.collision.handling.CManifold;
import physics.collision.handling.Collisions;

public class DistanceJoint extends Joint {
	public float distance;

	public DistanceJoint(final CollisionComponent a, final CollisionComponent b, final float distance) {
		super(a, b);
		this.distance = distance;
	}

	public DistanceJoint(final CollisionComponent a, final CollisionComponent b) {
		this(a, b, a.getPos().minus(b.getPos()).length());
	}

	@Override
	public void update() {
		final CManifold m = new CManifold();
		m.a = getA();
		m.b = getB();

		final Vec2D n = getA().getPos().minus(getB().getPos());

		if (Math.abs(n.length() - distance) < MathUtils.EPSILON) {
			// we don't need to do anything
			return;
		}

		m.setNormal(n.length() < distance ? n.unitVector().multiply(-1) : n.unitVector());
		m.setPenetration(Math.abs(n.length() - distance));

		Collisions.fixCollision(m, false);
	}
}
