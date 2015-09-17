package physics.constraints;

import java.awt.Color;
import java.awt.Graphics2D;

import math.Vec2D;
import physics.CollisionComponent;

public abstract class PointBodyJoint extends Constraint {
	private final CollisionComponent a;
	private final Vec2D point;

	public PointBodyJoint(final CollisionComponent a, final Vec2D point) {
		this.a = a;
		this.point = point;
	}

	public CollisionComponent getA() {
		return a;
	}

	public Vec2D getB() {
		return point;
	}

	@Override
	public void draw(final Graphics2D g) {
		g.setColor(Color.RED);
		final float x1 = getA().getPos().x;
		final float y1 = getA().getPos().y;
		final float x2 = getB().x;
		final float y2 = getB().y;
		g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
	}
}
