package physics.collision;

import game.Vec2D;

class CollisionRectCircle {
	public static boolean isColliding(final RectShape a, final CircleShape b) {
		final float circleDistance_x = Math.abs(b.center().x - (a.getMin().x + a.width() / 2));
		final float circleDistance_y = Math.abs(b.center().y - (a.getMin().y + a.height() / 2));

		if (circleDistance_x > a.width() / 2 + b.getRadius()) {
			return false;
		}
		if (circleDistance_y > a.height() / 2 + b.getRadius()) {
			return false;
		}

		if (circleDistance_x <= a.width() / 2) {
			return true;
		}
		if (circleDistance_y <= a.height() / 2) {
			return true;
		}

		final int cornerDistance_sq = (int) Math.pow(circleDistance_x - a.width() / 2, 2)
				+ (int) Math.pow(circleDistance_y - a.height() / 2, 2);

		return cornerDistance_sq <= (int) Math.pow(b.getRadius(), 2);

	}

	public static CManifold generateManifold(final RectShape a, final CircleShape b, final CManifold m) {
		assert m.a.shape == a && m.b.shape == b;

		// Vector from A to B
		final Vec2D n = b.center().minus(a.center());

		// Closest point on A to center of B
		Vec2D closest = n;

		// Calculate half extents along each axis
		final float x_extent = a.width() / 2f;
		final float y_extent = a.height() / 2f;

		// Clamp point to edges of the AABB
		closest = new Vec2D(clamp(closest.x, -x_extent, x_extent), clamp(closest.y, -y_extent, y_extent));

		final boolean inside = n.equals(closest);

		// Circle is inside the AABB, so we need to clamp the circle's center
		// to the closest edge
		if (inside) {
			// Find closest axis
			if (Math.abs(closest.x) > Math.abs(closest.y)) {
				// Clamp to closest extent
				closest = new Vec2D(closest.x > 0 ? x_extent : -x_extent, closest.y);
			}
			// y axis is shorter
			else {
				// Clamp to closest extent
				closest = new Vec2D(closest.x, closest.y > 0 ? y_extent : -y_extent);
			}
		}
		// vector from closest to the center of the circle
		final Vec2D normal = n.minus(closest);
		final float d = normal.length();
		final float r = b.getRadius();
		// Collision normal needs to be flipped to point outside if circle was
		// inside the AABB
		m.setNormal(inside ? normal.divide(d).multiply(-1) : normal.divide(d));
		m.setPenetration(r - d);
		return m;
	}

	public static float clamp(final float n, final float lower, final float upper) {
		return Math.max(lower, Math.min(n, upper));
	}
}
