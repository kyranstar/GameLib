package physics.collision.shape;

import java.awt.geom.Rectangle2D;

import math.Vec2D;

public class CircleShape implements CShape {
	private Vec2D center;
	private final float radius;

	// lazy variable
	private Rectangle2D rect;

	public CircleShape(final Vec2D center, final float radius) {
		if (center == null) {
			throw new NullPointerException("Center cannot be null");
		}
		if (radius <= 0 || Float.isInfinite(radius) || Float.isNaN(radius)) {
			throw new IllegalArgumentException("Radius must be >= 0, finite, and cannot be NaN. Was: " + radius);
		}

		this.center = center;
		this.radius = radius;
	}

	@Override
	public Vec2D center() {
		return center;
	}

	@Override
	public void moveRelative(final Vec2D v) {
		center = center.plus(v);
		rect = null;
	}

	@Override
	public Rectangle2D getRect() {
		if (rect == null) {
			rect = new Rectangle2D.Float(center.x - getRadius(), center.y - getRadius(), getRadius() * 2, getRadius() * 2);
		}

		return rect;
	}

	public float getRadius() {
		return radius;
	}
}
