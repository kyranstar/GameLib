package physics.collision.handling;

import game.Vec2D;
import physics.collision.PolygonShape;

public final class SATAlgorithm {
	private SATAlgorithm() {
	}

	/**
	 * Will return a manifold of the two objects (m.a = null, m.b = null), or null if the objects aren't colliding.
	 *
	 * @param shape1
	 * @param shape2
	 * @return
	 */
	public static CManifold generateManifold(final PolygonShape shape1, final PolygonShape shape2) {
		float overlap = Float.MAX_VALUE;// really large value;
		Vec2D smallest = null;
		final Vec2D[] axes1 = getAxes(shape1);
		final Vec2D[] axes2 = getAxes(shape2);
		// loop over the axes1
		for (int i = 0; i < axes1.length; i++) {
			final Vec2D axis = axes1[i];
			// project both shapes onto the axis
			final Projection p1 = project(shape1, axis);
			final Projection p2 = project(shape2, axis);
			// do the projections overlap?
			if (!p1.overlap(p2)) {
				// then we can guarantee that the shapes do not overlap
				return null;
			} else {
				// get the overlap
				final float o = p1.getOverlap(p2);
				// check for minimum
				if (o < overlap) {
					// then set this one as the smallest
					overlap = o;
					smallest = axis;
				}
			}
		}
		// loop over the axes2
		for (int i = 0; i < axes2.length; i++) {
			final Vec2D axis = axes2[i];
			// project both shapes onto the axis
			final Projection p1 = project(shape1, axis);
			final Projection p2 = project(shape2, axis);
			// do the projections overlap?
			if (!p1.overlap(p2)) {
				// then we can guarantee that the shapes do not overlap
				return null;
			} else {
				// get the overlap
				final float o = p1.getOverlap(p2);
				// check for minimum
				if (o < overlap) {
					// then set this one as the smallest
					overlap = o;
					smallest = axis;
				}
			}
		}
		assert overlap != Float.MAX_VALUE;
		final CManifold m = new CManifold();
		m.setNormal(smallest.unitVector());
		m.setPenetration(overlap);
		// if we get here then we know that every axis had overlap on it
		// so we can guarantee an intersection
		return m;
	}

	private static Vec2D[] getAxes(final PolygonShape shape) {
		final Vec2D[] axes = new Vec2D[shape.getNumVertices()];
		// loop over the vertices
		for (int i = 0; i < shape.getNumVertices(); i++) {
			// get the current vertex
			final Vec2D p1 = shape.getVertex(i);
			// get the next vertex
			final Vec2D p2 = shape.getVertex(i + 1 == shape.getNumVertices() ? 0 : i + 1);
			// subtract the two to get the edge Vec2D
			final Vec2D edge = p1.minus(p2);
			// get either perpendicular Vec2D
			final Vec2D normal = edge.perpendicular();
			axes[i] = normal.unitVector();
		}
		return axes;
	}

	private static Projection project(final PolygonShape shape, final Vec2D axis) {
		float min = axis.dotProduct(shape.getVertex(0));
		float max = min;
		for (int i = 1; i < shape.getNumVertices(); i++) {
			// NOTE: the axis must be normalized to get accurate projections
			final float p = axis.dotProduct(shape.getVertex(i));
			if (p < min) {
				min = p;
			} else if (p > max) {
				max = p;
			}
		}
		return new Projection(min, max);
	}

	private static final class Projection {
		private final float min, max;

		public Projection(final float min, final float max) {
			this.min = min;
			this.max = max;
		}

		public float getOverlap(final Projection p2) {
			if (contains(min, p2.min, p2.max)) {
				// min is in between p2
				return p2.max - min;
			}
			if (contains(max, p2.min, p2.max)) {
				return max - p2.min;
			}
			if (contains(p2.min, min, max)) {
				return max - p2.min;
			}
			if (contains(p2.max, min, max)) {
				return p2.max - min;
			}
			// no intersection
			if (min > p2.max) {
				return p2.max - min;
			} else {
				return max - p2.min;
			}
		}

		private static boolean contains(final float f, final float min, final float max) {
			return f > min && f < max;
		}

		public boolean overlap(final Projection p2) {
			if (max <= p2.min || p2.max <= min) {
				assert getOverlap(p2) <= 0 : "Overlap: " + getOverlap(p2);
				return false; // no overlap
			} else {
				assert getOverlap(p2) > 0 : "Overlap: " + getOverlap(p2);
				return true; // yes overlap
			}
		}

		@Override
		public String toString() {
			return "Projection [min=" + min + ", max=" + max + "]";
		}
	}
}
