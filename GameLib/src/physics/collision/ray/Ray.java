package physics.collision.ray;

import math.Vec2D;

public class Ray {

	private final Vec2D start;
	private final Vec2D direction;

	public Ray(final Vec2D start, final Vec2D direction) {
		if (start == null) {
			throw new NullPointerException("start cannot be null");
		}
		if (direction == null) {
			throw new NullPointerException("direction cannot be null");
		}
		if (!direction.isNormalized()) {
			throw new IllegalArgumentException("direction must be normalized, was " + direction);
		}
		if (direction.equals(Vec2D.ZERO)) {
			throw new IllegalArgumentException("direction cannot be 0");
		}
		this.start = start;
		this.direction = direction;
	}

	public Vec2D getStart() {
		return start;
	}

	public Vec2D getDirection() {
		return direction;
	}

}
