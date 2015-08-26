package physics.collision.handling;

import math.Vec2D;
import physics.collision.shape.CircleShape;

class CollisionCircleCircle {

	public static boolean isColliding(final CircleShape o1, final CircleShape o2) {
		final float c = o1.getRadius() + o2.getRadius();
		final float b = o1.center().x - o2.center().x;
		final float a = o1.center().y - o2.center().y;

		return c * c > b * b + a * a;
	}

	public static CManifold generateManifold(final CircleShape a, final CircleShape b, final CManifold m) {
		// A to B
		final Vec2D n = b.center().minus(a.center());
		if (n.length() == 0) {
			// circles are on the same position, choose random but consistent
			// values
			m.setNormal(new Vec2D(0, 1));
			m.setPenetration(Math.min(a.getRadius(), b.getRadius()));
			return m;
		}
		m.setNormal(n.unitVector());
		m.setPenetration(b.getRadius() + a.getRadius() - n.length());
		return m;
	}
}
