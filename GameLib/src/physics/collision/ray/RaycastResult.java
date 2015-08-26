package physics.collision.ray;

import math.Vec2D;

/**
 * The result of a call to {@link RaycastDetector}. This stores the hit location, normal, and the distance from the
 * start to the hit point.
 *
 * @author Kyran Adams
 *
 */
public class RaycastResult {
	/** The hit point */
	protected Vec2D point;

	/** The normal at the hit point */
	protected Vec2D normal;

	/** The distance from the start of the {@link Ray} to the hit point */
	protected float distance;

	/**
	 * Default constructor.
	 */
	public RaycastResult() {
	}

	/**
	 * Full constructor.
	 *
	 * @param point
	 *            the hit point
	 * @param normal
	 *            the normal at the hit point
	 * @param distance
	 *            the distance from the start of the {@link Ray} to the hit point
	 */
	public RaycastResult(final Vec2D point, final Vec2D normal, final float distance) {
		this.point = point;
		this.normal = normal;
		this.distance = distance;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Raycast[Point=").append(point).append("|Normal=").append(normal).append("|Distance=").append(distance).append("]");
		return sb.toString();
	}

	/**
	 * Clears this object setting all values to their default values.
	 */
	public void clear() {
		point = null;
		normal = null;
		distance = 0.0f;
	}

	/**
	 * Returns the hit point.
	 *
	 * @return {@link Vec2D}
	 */
	public Vec2D getPoint() {
		return point;
	}

	/**
	 * Sets the hit point.
	 *
	 * @param point
	 *            the hit point
	 */
	public void setPoint(final Vec2D point) {
		this.point = point;
	}

	/**
	 * Returns the normal at the hit point.
	 *
	 * @return {@link Vec2D}
	 */
	public Vec2D getNormal() {
		return normal;
	}

	/**
	 * Sets the normal at the hit point.
	 *
	 * @param normal
	 *            the normal at the hit point
	 */
	public void setNormal(final Vec2D normal) {
		this.normal = normal;
	}

	/**
	 * Returns the distance from the start of the {@link Ray} to the hit point.
	 *
	 * @return double
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * Sets the distance from the start of the {@link Ray} to the hit point.
	 *
	 * @param distance
	 *            the distance
	 */
	public void setDistance(final float distance) {
		this.distance = distance;
	}
}
