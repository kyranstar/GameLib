package physics;

import game.Vec2D;
import physics.collision.CManifold;
import physics.collision.Collisions;

public class Joint {
	public float distance;
	public float restitution = 0f;
	public GameEntity a, b;

	public Joint(final GameEntity a, final GameEntity b, final float distance) {
		this.a = a;
		this.b = b;
		this.distance = distance;
	}

	public void update() {
		final CManifold m = new CManifold();
		m.a = a;
		m.b = b;

		final Vec2D n = a.center().minus(b.center());

		final float d = n.length();

		m.normal = d < distance ? n.divide(d).multiply(-1) : n.divide(d);
		m.penetration = Math.abs(d - distance);

		Collisions.fixCollision(m, false);
	}
}
