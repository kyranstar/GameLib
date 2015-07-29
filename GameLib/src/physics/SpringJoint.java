package physics;

import game.Vec2D;

public class SpringJoint extends Joint {
	public float distance;
	private final float restitution;

	public SpringJoint(final GameEntity a, final GameEntity b, final float distance, final float restitution) {
		super(a, b);
		this.distance = distance;
		this.restitution = restitution;
	}

	@Override
	public void update() {

		final Vec2D n = getA().center().minus(getB().center());

		final float d = n.length();

		if (d < distance) {
			return;
		}

		final Vec2D normal = d < distance ? n.divide(d).multiply(-1) : n.divide(d);
		final float penetration = Math.abs(d - distance);

		getA().applyForce(normal.multiply(penetration * (1f / restitution)));
		getB().applyForce(normal.multiply(penetration * (1f / restitution)));

	}

}
