package physics.constraints;

import java.awt.Color;
import java.awt.Graphics2D;

import physics.CollisionComponent;

public abstract class Joint extends Constraint {

	private final CollisionComponent a;
	private final CollisionComponent b;

	public Joint(final CollisionComponent a, final CollisionComponent b) {
		if (a == null || b == null) {
			throw new NullPointerException("Neither a nor b can be null");
		}

		this.a = a;
		this.b = b;
	}

	public CollisionComponent getA() {
		return a;
	}

	public CollisionComponent getB() {
		return b;
	}

	@Override
	public void draw(final Graphics2D g) {
		g.setColor(Color.RED);
		final float x1 = getA().getPos().x;
		final float y1 = getA().getPos().y;
		final float x2 = getB().getPos().x;
		final float y2 = getB().getPos().y;
		g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
	}
}
