package physics;

import game.Vec2D;

public abstract class GameObject {
	public static final float INFINITE_MASS = 0;

	private float invMass;
	public Vec2D velocity;
	public float restitution;
	public float staticFriction;
	public float dynamicFriction;

	public abstract void update(float dt);

	public float getMass() {
		if (invMass == 0) {
			return INFINITE_MASS;
		}
		return 1 / invMass;
	}

	public float getInvMass() {
		return invMass;
	}

	public void setMass(final float mass) {
		if (mass == INFINITE_MASS) {
			invMass = 0;
		} else {
			invMass = 1 / mass;
		}
	}

	public void applyForce(final Vec2D force) {
		velocity = velocity.plus(force.multiply(invMass));
	}

	public abstract Vec2D center();

	public abstract void moveRelative(Vec2D v);
}
