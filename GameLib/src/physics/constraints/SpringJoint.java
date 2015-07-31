package physics.constraints;

import game.Vec2D;
import physics.GameEntity;

public class SpringJoint extends Joint {
	public float distance;
	private final float restitution;

	public SpringJoint(final GameEntity a, final GameEntity b, final float distance, final float restitution) {
		super(a, b);

		if (restitution == 0.0f) {
			throw new IllegalArgumentException("Restitution must be non-zero");
		}

		this.distance = distance;
		this.restitution = restitution;
	}

	public SpringJoint(final GameEntity a, final GameEntity b, final float restitution) {
		this(a, b, a.center().minus(b.center()).length(), restitution);
	}

	@Override
	public void update() {
		assert getA() != null && getB() != null;

		final Vec2D n = a.center().minus(b.center());

		final float d = n.length();

		final Vec2D normal = d == 0 ? new Vec2D(0, 1) : n.divide(d);
		final float penetration = d - distance;

		final Vec2D springForce = normal.multiply(penetration * (1f / restitution));

		a.applyForce(springForce.multiply(-1));
		b.applyForce(springForce);

	}

}
