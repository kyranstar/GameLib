package physics.collision.handling;

import java.awt.geom.Rectangle2D;

import math.Vec2D;
import physics.CollisionComponent;
import physics.collision.shape.RectShape;

class CollisionRectRect {

	public static boolean isColliding(final CollisionComponent a, final CollisionComponent b) {
		return collisionNormal(a, b) != null;
	}

	public static CManifold generateManifold(final CManifold m) {

		final Rectangle2D r = m.a.getRect().createIntersection(m.b.getRect());

		m.setNormal(collisionNormal(m.a, m.b));
		// penetration is the min resolving distance
		m.setPenetration((float) Math.min(r.getWidth(), r.getHeight()));
		return m;
	}

	/**
	 * Returns the face normal of a collision between two rectangles
	 *
	 * @param a
	 * @param b
	 * @return null if no collision
	 */
	private static Vec2D collisionNormal(final CollisionComponent a, final CollisionComponent b) {
		RectShape s1 = (RectShape) a.getShape();
		RectShape s2 = (RectShape) b.getShape();
		
		final float w = 0.5f * (s1.width() + s2.width());
		final float h = 0.5f * (s1.height() + s2.height());
		final float dx = a.getPos().x - b.getPos().x;
		final float dy = a.getPos().y - b.getPos().y;

		if (Math.abs(dx) <= w && Math.abs(dy) <= h) {
			/* collision! */
			final float wy = w * dy;
			final float hx = h * dx;

			if (wy > hx) {
				if (wy > -hx) {
					/* collision at the top */
					return new Vec2D(0, -1);
				} else {
					/* on the left */
					return new Vec2D(1, 0);
				}
			} else {
				if (wy > -hx) {
					/* on the right */
					return new Vec2D(-1, 0);
				} else {
					/* at the bottom */
					return new Vec2D(0, 1);
				}
			}
		}
		return null;
	}
}
