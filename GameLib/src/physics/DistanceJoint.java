package physics;

import game.Vec2D;
import physics.collision.CManifold;
import physics.collision.Collisions;

public class DistanceJoint extends Joint {
	public float distance;

	public DistanceJoint(final GameEntity a, final GameEntity b, final float distance) {
		super(a, b);
		this.distance = distance;
	}

	@Override
	public void update() {
		final CManifold m = new CManifold();
		m.a = getA();
		m.b = getB();

		final Vec2D n = getA().center().minus(getB().center());

		final float d = n.length();

		m.normal = d < distance ? n.divide(d).multiply(-1) : n.divide(d);
		m.penetration = Math.abs(d - distance);

		Collisions.fixCollision(m, false);
	}
}
