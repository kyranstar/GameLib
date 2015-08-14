package physics.collision;

import game.Vec2D;

import java.awt.geom.Rectangle2D;

public class RectShape extends CShape {
	// upper left
	private Vec2D min;
	// bottom right
	private Vec2D max;
	// lazy variable
	private Rectangle2D rect;

	public RectShape(final Vec2D min, final Vec2D max) {
		this.min = min;
		this.max = max;
	}

	@Override
	public Vec2D center() {
		return new Vec2D((getMax().x + getMin().x) / 2, (getMax().y + getMin().y) / 2);
	}

	@Override
	public Rectangle2D getRect() {
		if (rect == null) {
			rect = new Rectangle2D.Float(getMin().x, getMin().y, width(), height());
		}

		return rect;
	}

	public float width() {
		return getMax().x - getMin().x;
	}

	public float height() {
		return getMax().y - getMin().y;
	}

	@Override
	public void moveRelative(final Vec2D v) {
		min = getMin().plus(v);
		max = getMax().plus(v);
		rect = null;
	}

	public float area() {
		final Vec2D area = getMax().minus(getMin());
		return area.x * area.y;
	}

	public Vec2D getMin() {
		return min;
	}

	public Vec2D getMax() {
		return max;
	}
}
