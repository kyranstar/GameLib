package physics.collision.handling;

import math.Vec2D;
import physics.CollisionComponent;
import physics.collision.shape.CircleShape;
import physics.collision.shape.RectShape;

class CollisionRectCircle {
	public static boolean isColliding(final CollisionComponent a, final CollisionComponent b) {
		RectShape s1 = (RectShape) a.getShape();
		CircleShape s2 = (CircleShape) b.getShape();
		
		final float circleDistance_x = Math.abs(b.getPos().x - (a.getPos().x + s1.getMin().x + s1.width() / 2));
		final float circleDistance_y = Math.abs(b.getPos().y - (a.getPos().y + s1.getMin().y + s1.height() / 2));

		if (circleDistance_x > s1.width() / 2 + s2.getRadius()) {
			return false;
		}
		if (circleDistance_y > s1.height() / 2 + s2.getRadius()) {
			return false;
		}

		if (circleDistance_x <= s1.width() / 2) {
			return true;
		}
		if (circleDistance_y <= s1.height() / 2) {
			return true;
		}

		final int cornerDistance_sq = (int) Math.pow(circleDistance_x - s1.width() / 2, 2) + (int) Math.pow(circleDistance_y - s1.height() / 2, 2);

		return cornerDistance_sq <= (int) Math.pow(s2.getRadius(), 2);

	}

	public static CManifold generateManifold(final CManifold m) {
		RectShape s1 = (RectShape) m.a.getShape();
		CircleShape s2 = (CircleShape) m.b.getShape();

		// Vector from A to B
		final Vec2D n = m.b.getPos().minus(m.a.getPos());

		// Closest point on A to center of B
		Vec2D closest = n;

		// Calculate half extents along each axis
		final float x_extent = s1.width() / 2f;
		final float y_extent = s1.height() / 2f;

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
		final float r = s2.getRadius();
		// Collision normal needs to be flipped to point outside if circle was
		// inside the AABB
		m.setNormal(inside ? normal.unitVector().multiply(-1) : normal.unitVector());
		m.setPenetration(r - normal.length());
		return m;
	}

	public static float clamp(final float n, final float lower, final float upper) {
		return Math.max(lower, Math.min(n, upper));
	}
}
