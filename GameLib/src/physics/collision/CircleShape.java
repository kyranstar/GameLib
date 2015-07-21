package physics.collision;

import game.Vec2D;

public class CircleShape extends CShape {
	public Vec2D center;
	public float radius;

	public CircleShape(final Vec2D center, final float radius) {
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
	}
}
