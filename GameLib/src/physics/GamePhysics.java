package physics;

import game.Vec2D;

import java.awt.geom.Rectangle2D;

public final class GamePhysics {
	private GamePhysics() {
		// cant instantiate this class
	}

	public static boolean isColliding(final GameObject a, final GameObject b) {
		if (a instanceof RectObject && b instanceof RectObject) {
			return isColliding((RectObject) a, (RectObject) b);
		}
		if (a instanceof CircleObject && b instanceof CircleObject) {
			return isColliding((CircleObject) a, (CircleObject) b);
		}
		if (a instanceof RectObject && b instanceof CircleObject) {
			return isColliding((RectObject) a, (CircleObject) b);
		}
		if (a instanceof CircleObject && b instanceof RectObject) {
			return isColliding((RectObject) b, (CircleObject) a);
		}

		throw new UnsupportedOperationException();
	}

	private static boolean isColliding(final RectObject a, final RectObject b) {
		final float w = 0.5f * (a.width() + b.width());
		final float h = 0.5f * (a.height() + b.height());
		final float dx = a.center().x - b.center().x;
		final float dy = a.center().y - b.center().y;

		return Math.abs(dx) <= w && Math.abs(dy) <= h;
	}

	private static boolean isColliding(final CircleObject o1, final CircleObject o2) {
		final float c = o1.radius + o2.radius;
		final float b = o1.center.x - o2.center.x;
		final float a = o1.center.y - o2.center.y;

		return c * c > b * b + a * a;
	}

	private static boolean isColliding(final RectObject a, final CircleObject b) {
		final float circleDistance_x = Math.abs(b.center().x - (a.min.x + a.width() / 2));
		final float circleDistance_y = Math.abs(b.center().y - (a.min.y + a.height() / 2));

		if (circleDistance_x > a.width() / 2 + b.radius) {
			return false;
		}
		if (circleDistance_y > a.height() / 2 + b.radius) {
			return false;
		}

		if (circleDistance_x <= a.width() / 2) {
			return true;
		}
		if (circleDistance_y <= a.height() / 2) {
			return true;
		}

		final int cornerDistance_sq = (int) Math.pow(circleDistance_x - a.width() / 2, 2) + (int) Math.pow(circleDistance_y - a.height() / 2, 2);

		return cornerDistance_sq <= (int) Math.pow(b.radius, 2);

	}

	private static Vec2D collisionNormal(final RectObject a, final RectObject b) {
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
		throw new IllegalArgumentException("Rectangles must be colliding");
	}

	public static <A extends GameObject, B extends GameObject> void fixCollision(final A a, final B b) {
		final CollisionManifold<A, B> m = generateManifold(a, b);

		// Calculate relative velocity
		final Vec2D rv = b.velocity.minus(a.velocity);

		// Calculate relative velocity in terms of the normal direction
		final float velAlongNormal = rv.dotProduct(m.normal);

		// Calculate restitution
		final float e = Math.min(a.restitution, b.restitution);

		// Calculate impulse scalar
		float j = -(1 + e) * velAlongNormal;
		j /= a.getInvMass() + b.getInvMass();

		// Apply impulse
		final Vec2D impulse = m.normal.multiply(j);
		a.velocity = a.velocity.minus(impulse.multiply(a.getInvMass()));
		b.velocity = b.velocity.plus(impulse.multiply(b.getInvMass()));

		applyFriction(m, j);

		positionalCorrection(m);
	}

	public static <A extends GameObject, B extends GameObject> void applyFriction(final CollisionManifold<A, B> m, final float normalForce) {
		final A a = m.a;
		final B b = m.b;

		// relative velocity
		final Vec2D rv = b.velocity.minus(a.velocity);
		// normalized tangent force
		final Vec2D tangent = rv.minus(m.normal.multiply(m.normal.dotProduct(rv))).unitVector();
		// friction magnitude
		final float jt = -rv.dotProduct(tangent) / (a.getInvMass() + b.getInvMass());

		// friction coefficient
		final float mu = (a.staticFriction + b.staticFriction) / 2;
		final float dynamicFriction = (a.dynamicFriction + b.dynamicFriction) / 2;

		// Coulomb's law: force of friction <= force along normal * mu
		final Vec2D frictionImpulse = Math.abs(jt) < normalForce * mu ? tangent.multiply(jt) : tangent.multiply(-normalForce * dynamicFriction);

		a.velocity = a.velocity.minus(frictionImpulse.multiply(a.getInvMass()));
		b.velocity = b.velocity.plus(frictionImpulse.multiply(b.getInvMass()));
	}

	@SuppressWarnings("unchecked")
	public static <A extends GameObject, B extends GameObject> CollisionManifold<A, B> generateManifold(final A a, final B b) {
		if (a instanceof RectObject && b instanceof RectObject) {
			return (CollisionManifold<A, B>) generateManifold((RectObject) a, (RectObject) b);
		} else if (a instanceof CircleObject && b instanceof CircleObject) {
			return (CollisionManifold<A, B>) generateManifold((CircleObject) a, (CircleObject) b);
		} else if (a instanceof RectObject && b instanceof CircleObject) {
			return (CollisionManifold<A, B>) generateManifold((RectObject) a, (CircleObject) b);
		} else if (a instanceof CircleObject && b instanceof RectObject) {
			return (CollisionManifold<A, B>) generateManifold((RectObject) b, (CircleObject) a);
		} else {
			throw new UnsupportedOperationException();
		}
	}

	private static CollisionManifold<RectObject, RectObject> generateManifold(final RectObject a, final RectObject b) {
		final CollisionManifold<RectObject, RectObject> m = new CollisionManifold<>();

		m.a = a;
		m.b = b;

		final Rectangle2D r = a.toRectangle().createIntersection(b.toRectangle());

		m.normal = collisionNormal(a, b);
		// penetration is the min resolving distance
		m.penetration = (float) Math.min(r.getWidth(), r.getHeight());
		return m;
	}

	private static CollisionManifold<CircleObject, CircleObject> generateManifold(final CircleObject a, final CircleObject b) {
		final CollisionManifold<CircleObject, CircleObject> m = new CollisionManifold<>();
		m.a = a;
		m.b = b;

		// A to B
		final Vec2D n = b.center.minus(a.center);
		final float dist = n.length();

		if (dist == 0) {
			// circles are on the same position, choose random but consistent values
			m.normal = new Vec2D(0, 1);
			m.penetration = Math.min(a.radius, b.radius);
			return m;
		}
		// don't recalculate dist to normalize
		m.normal = n.divide(dist);
		m.penetration = b.radius + a.radius - dist;
		return m;
	}

	private static CollisionManifold<RectObject, CircleObject> generateManifold(final RectObject a, final CircleObject b) {
		final CollisionManifold<RectObject, CircleObject> m = new CollisionManifold<>();

		m.a = a;
		m.b = b;
		// Vector from A to B
		final Vec2D n = b.center.minus(a.center());

		// Closest point on A to center of B
		Vec2D closest = n;

		// Calculate half extents along each axis
		final float x_extent = a.width() / 2;
		final float y_extent = a.height() / 2;

		// Clamp point to edges of the AABB
		closest = new Vec2D(clamp(closest.x, -x_extent, x_extent), clamp(closest.y, -y_extent, y_extent));

		boolean inside = false;

		// Circle is inside the AABB, so we need to clamp the circle's center
		// to the closest edge
		if (n.equals(closest)) {
			inside = true;
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
		// closest point to center of the circle
		final Vec2D normal = n.minus(closest);
		final float d = normal.length();
		final float r = b.radius;
		// Collision normal needs to be flipped to point outside if circle was
		// inside the AABB
		m.normal = inside ? normal.unitVector().multiply(-1) : normal.unitVector();
		m.penetration = r - d;
		return m;
	}

	private static float clamp(final float n, final float lower, final float upper) {
		return Math.max(lower, Math.min(n, upper));
	}

	private static <A extends GameObject, B extends GameObject> void positionalCorrection(final CollisionManifold<A, B> m) {
		final A a = m.a;
		final B b = m.b;

		final float percent = .8f; // usually .2 to .8
		final float slop = 0.01f; // usually 0.01 to 0.1
		final Vec2D correction = m.normal.multiply(Math.max(m.penetration - slop, 0.0f) / (a.getInvMass() + b.getInvMass()) * percent);
		a.moveRelative(correction.multiply(-1 * a.getInvMass()));
		b.moveRelative(correction.multiply(b.getInvMass()));
	}

}
