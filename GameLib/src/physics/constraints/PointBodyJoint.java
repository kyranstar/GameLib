package physics.constraints;

import java.awt.Color;
import java.awt.Graphics2D;

import math.Vec2D;
import physics.PhysicsEntity;

public abstract class PointBodyJoint extends Constraint {
	private final PhysicsEntity a;
	private final Vec2D point;

	public PointBodyJoint(final PhysicsEntity a, final Vec2D point) {
		this.a = a;
		this.point = point;
	}

	public PhysicsEntity getA() {
		return a;
	}

	public Vec2D getB() {
		return point;
	}

	@Override
	public void draw(final Graphics2D g) {
		g.setColor(Color.RED);
		final float x1 = getA().center().x;
		final float y1 = getA().center().y;
		final float x2 = getB().x;
		final float y2 = getB().y;
		g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
	}
}
