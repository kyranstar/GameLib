package physics;

import game.Vec2D;
import game.World;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

import physics.collision.CShape;
import physics.collision.CollisionFilter;

public class PhysicsEntity {
	/**
	 * A constant representing infinite mass. If setMass(GameObject.INFINITE_MASS) is called, this object will not move.
	 */
	public static final float INFINITE_MASS = 0;

	public static final float INFINITE_ROT_INERTIA = 0;

	// if the object's velocity was below the sleep threshold for more than FRAMES_STILL_TO_SLEEP frames
	public boolean sleeping;
	// holds the number of frames this object has been still (below the sleep threshold)
	public int framesStill;

	private float invMass;
	private float invRotInertia;
	private Vec2D velocity = new Vec2D();

	private float radialVelocity;

	private float restitution;
	private float staticFriction;
	private float dynamicFriction;

	public CShape shape;

	// all entities that we've checked collisions with so far this tick
	public final Set<PhysicsEntity> checkedCollisionThisTick = Collections.newSetFromMap(new IdentityHashMap<PhysicsEntity, Boolean>());
	private final World world;

	public CollisionFilter collisionFilter = CollisionFilter.ALL_COLLISIONS;

	public PhysicsEntity(final World world) {
		this.world = world;
	}

	public void update(final float dt) {
		moveRelative(getVelocity().multiply(dt));
		shape.rotate(getRadialVelocity() * dt);
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

	public void applyForce(final Vec2D force, final Vec2D contactVector) {
		setVelocity(getVelocity().plus(force.multiply(invMass)));
		radialVelocity += getInvRotInertia() * contactVector.perpendicular().dotProduct(getVelocity());
		// radialVelocity += getInvRotInertia() * contactVector.crossProduct(force);
	}

	public void applyForce(final Vec2D force) {
		applyForce(force, Vec2D.ZERO);
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

	public float getOrientation() {
		return shape.getOrientation();
	}

	public float getRadialVelocity() {
		return radialVelocity;
	}

	public void setRadialVelocity(final float radialVelocity) {
		this.radialVelocity = radialVelocity;
	}

	public float getInvRotInertia() {
		return invRotInertia;
	}

	public void setRotationalInertia(final float i) {
		if (i == PhysicsEntity.INFINITE_ROT_INERTIA) {
			invRotInertia = 0;
		} else {
			invRotInertia = 1f / i;
		}
	}
}
