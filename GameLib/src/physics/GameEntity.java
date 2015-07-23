package physics;

import game.Vec2D;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

import physics.collision.CShape;

public class GameEntity {
	/**
	 * A constant representing infinite mass. If setMass(GameObject.INFINITE_MASS) is called, this object will not move.
	 */
	public static final float INFINITE_MASS = 0;
	/**
	 * The velocity the object has to be below to be considered still
	 */
	public static final float SLEEP_THRESHOLD = 1f;
	/**
	 * The consecutive amount of frames the object has to be still to be considered sleeping
	 */
	public static final int FRAMES_STILL_TO_SLEEP = 15;
	public static final boolean SLEEPING_ENABLED = false;

	// if the object's velocity was below the sleep threshold for more than one frame
	public boolean sleeping;
	// holds the number of frames this object has been still (below the sleep threshold)
	public int framesStill;

	private float invMass;
	public Vec2D velocity;
	private float restitution;
	private float staticFriction;
	private float dynamicFriction;

	public CShape shape;

	public Set<GameEntity> checkedCollisionThisTick = Collections.newSetFromMap(new IdentityHashMap<GameEntity, Boolean>());;

	public void update(final float dt) {
		moveRelative(velocity.multiply(dt));
	}

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

	public Vec2D center() {
		return shape.center();
	}

	public void moveRelative(final Vec2D v) {
		shape.moveRelative(v);
	}

	public float getRestitution() {
		return restitution;
	}

	public void setMaterial(final Material m) {
		dynamicFriction = m.dynamicFriction;
		staticFriction = m.staticFriction;
		restitution = m.restitution;
	}

	public float getDynamicFriction() {
		return dynamicFriction;
	}

	public float getStaticFriction() {
		return staticFriction;
	}
}
