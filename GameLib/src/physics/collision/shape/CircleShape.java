package physics.collision.shape;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import math.Vec2D;

public class CircleShape implements CShape {
	private final float radius;

	// lazy variable
	private Rectangle2D rect;

	public CircleShape(final float radius) {
		if (radius <= 0 || Float.isInfinite(radius) || Float.isNaN(radius)) {
			throw new IllegalArgumentException("Radius must be >= 0, finite, and cannot be NaN. Was: " + radius);
		}

		this.radius = radius;
	}

	@Override
	public Rectangle2D getRect() {
		if (rect == null) {
			rect = new Rectangle2D.Float(-getRadius(), -getRadius(), getRadius() * 2, getRadius() * 2);
		}

		return rect;
	}

	public float getRadius() {
		return radius;
	}

	public boolean contains(final Vec2D s) {
		return getEllipse2D().contains(s.x, s.y);
	}

	private Ellipse2D getEllipse2D() {
		return new Ellipse2D.Float(-radius, -radius, radius * 2, radius * 2);
	}
}
