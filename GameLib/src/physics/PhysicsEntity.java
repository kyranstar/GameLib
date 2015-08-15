package physics;

import game.Vec2D;
import game.World;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

import physics.collision.CShape;

public class PhysicsEntity {
	/**
	 * A constant representing infinite mass. If setMass(GameObject.INFINITE_MASS) is called, this object will not move.
	 */
	public static final float INFINITE_MASS = 0;

	// if the object's velocity was below the sleep threshold for more than FRAMES_STILL_TO_SLEEP frames
	public boolean sleeping;
	// holds the number of frames this object has been still (below the sleep threshold)
	public int framesStill;

	private float invMass;
	private Vec2D velocity = new Vec2D();
	private float restitution;
	private float staticFriction;
	private float dynamicFriction;

	public CShape shape;

	// all entities that we've checked collisions with so far this tick
	public final Set<PhysicsEntity> checkedCollisionThisTick = Collections.newSetFromMap(new IdentityHashMap<PhysicsEntity, Boolean>());
	private final World world;

	public PhysicsEntity(final World world) {
		this.world = world;
	}

	public void update(final float dt) {
		moveRelative(getVelocity().multiply(dt));
		applyForce(velocity.multiply(-world.AIR_FRICTION * dt));
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
		if (mass < 0) {
			throw new IllegalArgumentException("Mass must be > 0 or equal to INFINITE_MASS");
		}

		if (mass == INFINITE_MASS) {
			invMass = 0;
		} else {
			invMass = 1 / mass;
		}
	}

	public void applyForce(final Vec2D force) {
		setVelocity(getVelocity().plus(force.multiply(invMass)));
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

	public Vec2D getVelocity() {
		return velocity;
	}

	public void setVelocity(final Vec2D velocity) {
		this.velocity = velocity;
	}
}
