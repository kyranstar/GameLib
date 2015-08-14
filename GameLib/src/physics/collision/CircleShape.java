package physics.collision;

import game.Vec2D;

import java.awt.geom.Rectangle2D;

public class CircleShape extends CShape {
	private Vec2D center;
	private final float radius;

	// lazy variable
	private Rectangle2D rect;

	public CircleShape(final Vec2D center, final float radius) {
		this.center = center;
		this.radius = radius;
	}

	@Override
	public Vec2D center() {
		return getCenter();
	}

	@Override
	public void moveRelative(final Vec2D v) {
		center = center.plus(v);
		rect = null;
	}

	@Override
	public Rectangle2D getRect() {
		if (rect == null) {
			rect = new Rectangle2D.Float(getCenter().x - getRadius(), getCenter().y - getRadius(), getRadius() * 2, getRadius() * 2);
		}

		return rect;
	}

	public float getRadius() {
		return radius;
	}

	public Vec2D getCenter() {
		return center;
	}
}
