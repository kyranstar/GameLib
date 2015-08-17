package physics.collision.handling;

import game.Vec2D;

import java.awt.geom.Rectangle2D;

import physics.collision.RectShape;

class CollisionRectRect {

	public static boolean isColliding(final RectShape a, final RectShape b) {
		return collisionNormal(a, b) != null;
	}

	public static CManifold generateManifold(final RectShape a, final RectShape b, final CManifold m) {

		final Rectangle2D r = a.getRect().createIntersection(b.getRect());

		m.setNormal(collisionNormal(a, b));
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
	private static Vec2D collisionNormal(final RectShape a, final RectShape b) {
		final float w = 0.5f * (a.width() + b.width());
		final float h = 0.5f * (a.height() + b.height());
		final float dx = a.center().x - b.center().x;
		final float dy = a.center().y - b.center().y;

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
