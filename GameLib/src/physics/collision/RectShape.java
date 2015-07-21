package physics.collision;

import game.Vec2D;

import java.awt.geom.Rectangle2D;

public class RectShape extends CShape {
	// upper left
	public Vec2D min;
	// bottom right
	public Vec2D max;

	public RectShape(final Vec2D min, final Vec2D max) {
		this.min = min;
		this.max = max;
	}

	@Override
	public Vec2D center() {
		return new Vec2D((max.x + min.x) / 2, (max.y + min.y) / 2);
	}

	public Rectangle2D.Float toRectangle() {
		return new Rectangle2D.Float(min.x, min.y, width(), height());
	}

	public float width() {
		return max.x - min.x;
	}

	public float height() {
		return max.y - min.y;
	}

	@Override
	public void moveRelative(final Vec2D v) {
		min = min.plus(v);
		max = max.plus(v);
	}

	public float area() {
		final Vec2D area = max.minus(min);
		return area.x * area.y;
	}
}
