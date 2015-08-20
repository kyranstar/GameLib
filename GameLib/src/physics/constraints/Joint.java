package physics.constraints;

import java.awt.Color;
import java.awt.Graphics2D;

import physics.PhysicsComponent;

public abstract class Joint extends Constraint {

	private final PhysicsComponent a;
	private final PhysicsComponent b;

	public Joint(final PhysicsComponent a, final PhysicsComponent b) {
		if (a == null || b == null) {
			throw new NullPointerException("Neither a nor b can be null");
		}

		this.a = a;
		this.b = b;
	}

	public PhysicsComponent getA() {
		return a;
	}

	public PhysicsComponent getB() {
		return b;
	}

	@Override
	public void draw(final Graphics2D g) {
		g.setColor(Color.RED);
		final float x1 = getA().center().x;
		final float y1 = getA().center().y;
		final float x2 = getB().center().x;
		final float y2 = getB().center().y;
		g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
	}
}
