package physics.collision.ray;

import math.Vec2D;
import physics.collision.shape.CShape;
import physics.collision.shape.CircleShape;
import physics.collision.shape.RectShape;

public final class RaycastDetector {
	public static final float INFINITE_LENGTH = 0;

	private RaycastDetector() {
	}

	/**
	 * Performs a ray cast against the given circle.
	 *
	 * @param ray
	 *            the {@link Ray}
	 * @param maxLength
	 *            the maximum ray length, 0 if infinite
	 * @param circle
	 *            the {@link CircleShape}
	 * @param result
	 *            the {@link RaycastResult}. If it is null, it will be ignored.
	 * @return boolean true if the ray intersects the circle
	 * @since 2.0.0
	 */
	public static final boolean raycast(final Ray ray, final float maxLength, Vec2D center, final CircleShape circle, final RaycastResult result) {

		// solve the problem algebraically
		final Vec2D s = ray.getStart();
		final Vec2D d = ray.getDirection();
		final Vec2D ce = center;
		final float r = circle.getRadius();

		// make sure the start of the ray is not contained in the circle
		if (circle.contains(s)) {
			return false;
		}

		// any point on a ray can be found by the parametric equation:
		// P = tD + S
		// any point on a circle can be found by:
		// (x - h)^2 + (y - k)^2 = r^2 where h and k are the x and y center coordinates
		// substituting the first equation into the second yields a quadratic equation:
		// |D|^2t^2 + 2D.dotProduct(S - C)t + (S - C)^2 - r^2 = 0
		// using the quadratic equation we can solve for t where
		// a = |D|^2
		// b = 2D.dotProduct(S - C)
		// c = (S - C)^2 - r^2
		final Vec2D sMinusC = s.minus(ce);

		// mag(d)^2
		final float a = d.dotProduct(d);
		// 2d.dotProduct(s - c)
		final float b = 2 * d.dotProduct(sMinusC);
		// (s - c)^2 - r^2
		final float c = sMinusC.dotProduct(sMinusC) - r * r;

		// precompute
		final float inv2a = 1.0f / (2.0f * a);
		final float b24ac = b * b - 4 * a * c;
		// check for negative inside the sqrt
		if (b24ac < 0.0) {
			// if the computation inside the sqrt is
			// negative then this indicates that the
			// ray is parallel to the circle
			return false;
		}
		final float sqrt = (float) Math.sqrt(b24ac);
		// compute the two values of t
		final float t0 = (-b + sqrt) * inv2a;
		final float t1 = (-b - sqrt) * inv2a;

		// find the correct t
		// t cannot be negative since this would make the point
		// in the opposite direction of the ray's direction
		float t = 0.0f;
		// check for negative value
		if (t0 < 0.0) {
			// check for negative value
			if (t1 < 0.0) {
				// return the ray does not intersect the circle
				return false;
			} else {
				// t1 is the answer
				t = t1;
			}
		} else {
			// check for negative value
			if (t1 < 0.0) {
				// t0 is the answer
				t = t0;
			} else if (t0 < t1) {
				// t0 is the answer
				t = t0;
			} else {
				// t1 is the answer
				t = t1;
			}
		}

		// check the value of t
		if (maxLength > 0.0 && t > maxLength) {
			// if the smallest non-negative t is larger
			// than the maximum length then return false
			return false;
		}

		// compute the hit point
		final Vec2D p = d.multiply(t).plus(s);
		// compute the normal
		final Vec2D n = p.minus(ce).unitVector();

		if (result != null) {
			// populate the raycast result
			result.point = p;
			// flip y because our y axis is flipped
			result.normal = new Vec2D(n.x, -n.y);
			result.distance = t;
		}
		// return success
		return true;
	}

	/**
	 * Performs a ray cast against the given rectangle.
	 *
	 * @param ray
	 *            the {@link Ray}
	 * @param maxLength
	 *            the maximum ray length, 0 if infinite
	 * @param rectangle
	 *            the {@link RectShape}
	 * @param raycast
	 *            the {@link RaycastResult} result
	 * @return boolean true if the ray intersects the rectangle
	 * @since 2.0.0
	 */
	public static final boolean raycast(final Ray ray, final float maxLength, Vec2D center, final RectShape rect, final RaycastResult raycast) {

		// r.org is origin of ray
		final Vec2D rorg = ray.getStart();
		// r.dir is unit direction vector of ray
		final Vec2D rdir = ray.getDirection();
		// TODO: Optimize this so that this is only ever calculated once per ray.
		// inverses, but avoid divide by 0.
		final Vec2D dirfrac = new Vec2D(1.0f / (rdir.x == 0 ? Float.MIN_NORMAL : rdir.x), 1.0f / (rdir.y == 0 ? Float.MIN_NORMAL : rdir.y));

		// distances
		final float t1 = (rect.getVertex(RectShape.LEFT_TOP).x + center.x - rorg.x) * dirfrac.x;
		final float t2 = (rect.getVertex(RectShape.RIGHT_BOTTOM).x + center.x - rorg.x) * dirfrac.x;
		final float t3 = (rect.getVertex(RectShape.LEFT_TOP).y + center.y - rorg.y) * dirfrac.y;
		final float t4 = (rect.getVertex(RectShape.RIGHT_BOTTOM).y + center.y - rorg.y) * dirfrac.y;

		final float tmin = Math.max(Math.min(t1, t2), Math.min(t3, t4));
		final float tmax = Math.min(Math.max(t1, t2), Math.max(t3, t4));

		boolean result = true;
		float distance = 0;
		if (tmax < 0) {
			// if tmax < 0, ray (line) is intersecting AABB, but whole AABB is behing us
			distance = tmax;
			result = false;
		} else if (tmin > tmax) {
			// if tmin > tmax, ray doesn't intersect AABB
			distance = tmax;
			result = false;
		} else {
			distance = tmin;

		}
		if (raycast != null) {
			// project the distance along the direction to find collision point
			raycast.point = rorg.plus(rdir.multiply(distance));
			// check distances to find normal, floating point equality is okay because distance was set to one of the
			// four t's
			if (distance == t1) {
				raycast.normal = new Vec2D(-1.f, 0.f); /* left */
			} else if (distance == t2) {
				raycast.normal = new Vec2D(1.f, 0.f); /* right */
			} else if (distance == t3) {
				raycast.normal = new Vec2D(0.f, -1.f); /* top */
			} else if (distance == t4) {
				raycast.normal = new Vec2D(0.f, 1.f); /* bottom */
			} else {
				// shouldn't ever get here
				throw new RuntimeException();
			}
			raycast.distance = distance;
		}
		if (maxLength != INFINITE_LENGTH && distance > maxLength) {
			return false;
		}
		return result;
	}

	public static boolean raycast(final Ray ray, final float maxLength, Vec2D center, final CShape shape, final RaycastResult result) {
		if (shape instanceof CircleShape) {
			return raycast(ray, maxLength, center, (CircleShape) shape, result);
		} else if (shape instanceof RectShape) {
			return raycast(ray, maxLength, center, (RectShape) shape, result);
		} else {
			throw new UnsupportedOperationException("Shape must be circle or rect");
		}
	}
}
