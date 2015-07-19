package physics;

import game.Vec2D;

public class CircleObject extends GameObject {
	public Vec2D center;
	public float radius;

	@Override
	public void update(final float dt) {
		center = center.plus(velocity.multiply(dt));
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
