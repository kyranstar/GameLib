package physics.collision.handling;

import physics.collision.CircleShape;
import game.Vec2D;

class CollisionCircleCircle {

	public static boolean isColliding(final CircleShape o1, final CircleShape o2) {
		final float c = o1.getRadius() + o2.getRadius();
		final float b = o1.getCenter().x - o2.getCenter().x;
		final float a = o1.getCenter().y - o2.getCenter().y;

		return c * c > b * b + a * a;
	}

	public static CManifold generateManifold(final CircleShape a, final CircleShape b, final CManifold m) {
		// A to B
		final Vec2D n = b.getCenter().minus(a.getCenter());
		final float dist = n.length();

		if (dist == 0) {
			// circles are on the same position, choose random but consistent
			// values
			m.setNormal(new Vec2D(0, 1));
			m.setPenetration(Math.min(a.getRadius(), b.getRadius()));
			return m;
		}
		// don't recalculate dist to normalize
		m.setNormal(n.divide(dist));
		m.setPenetration(b.getRadius() + a.getRadius() - dist);
		return m;
	}
}
