package physics;

import game.Vec2D;

import java.awt.geom.Rectangle2D;

public class RectObject extends GameObject {
	// upper left
	public Vec2D min;
	// bottom right
	public Vec2D max;

	@Override
	public Vec2D center() {
		return new Vec2D((max.x + min.x) / 2, (max.y + min.y) / 2);
	}

	public Rectangle2D.Float toRectangle() {
		return new Rectangle2D.Float(min.x, min.y, width(), height());
	}

	@Override
	public void update(final float dt) {
		min = min.plus(velocity.multiply(dt));
		max = max.plus(velocity.multiply(dt));
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
