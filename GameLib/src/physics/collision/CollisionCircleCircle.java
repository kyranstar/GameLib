package physics.collision;

import game.Vec2D;

class CollisionCircleCircle {

	public static boolean isColliding(final CircleShape o1, final CircleShape o2) {
		final float c = o1.radius + o2.radius;
		final float b = o1.center.x - o2.center.x;
		final float a = o1.center.y - o2.center.y;

		return c * c >= b * b + a * a;
	}

	public static CManifold generateManifold(final CircleShape a, final CircleShape b, final CManifold m) {
		// A to B
		final Vec2D n = b.center.minus(a.center);
		final float dist = n.length();

		if (dist == 0) {
			// circles are on the same position, choose random but consistent
			// values
			m.setNormal(new Vec2D(0, 1));
			m.setPenetration(Math.min(a.radius, b.radius));
			return m;
		}
		// don't recalculate dist to normalize
		m.setNormal(n.divide(dist));
		m.setPenetration(b.radius + a.radius - dist);
		return m;
	}
}
