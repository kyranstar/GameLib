package physics;

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

	public void update(final float dt) {
		final CManifold m = new CManifold();
		m.a = a;
		m.b = b;

		m.normal = a.center().minus(b.center()).unitVector();

		final float x1 = a.center().x;
		final float y1 = a.center().y;
		final float x2 = b.center().x;
		final float y2 = b.center().y;
		final int distance = (int) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
		m.penetration = distance - this.distance;

		Collisions.fixCollision(m);
	}
}
