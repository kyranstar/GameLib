package physics.constraints;

import math.Vec2D;
import physics.PhysicsComponent;

public class SpringPointConstraint extends PointBodyJoint {

	private final float restitution;
	private final float distance;

	public SpringPointConstraint(final PhysicsComponent object, final Vec2D point, final float restitution) {
		this(object, point, point.minus(object.center()).length(), restitution);
	}

	public SpringPointConstraint(final PhysicsComponent object, final Vec2D point, final float distance, final float restitution) {
		super(object, point);
		this.restitution = restitution;
		this.distance = distance;
	}

	@Override
	public void update() {
		final Vec2D n = getB().minus(getA().center());

		final float d = n.length();

		final Vec2D normal = d == 0 ? new Vec2D(0, 1) : n.divide(d);
		final float penetration = d - distance;

		final Vec2D springForce = normal.multiply(penetration * (1f / restitution));

		getA().applyForce(springForce);
	}

}
