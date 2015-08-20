package physics;

import game.entity.GameComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

import math.Vec2D;
import physics.collision.CollisionFilter;
import physics.collision.shape.CShape;

public class PhysicsComponent implements GameComponent {

	public static final int COMPONENT_ID = 1;

	/**
	 * A constant representing infinite mass. If setMass(GameObject.INFINITE_MASS) is called, this object will not move.
	 */
	public static final float INFINITE_MASS = 0;

	public static final float INFINITE_ROT_INERTIA = 0;

	// if the object's velocity was below the sleep threshold for more than
	// FRAMES_STILL_TO_SLEEP frames
	public boolean sleeping;
	// holds the number of frames this object has been still (below the sleep
	// threshold)
	public int framesStill;

	private float invMass;
	private float invRotInertia;
	private Vec2D velocity = new Vec2D();

	private float radialVelocity;

	private Material material;

	public CShape shape;

	// all entities that we've checked collisions with so far this tick
	public final Set<PhysicsComponent> checkedCollisionThisTick = Collections.newSetFromMap(new IdentityHashMap<PhysicsComponent, Boolean>());

	public CollisionFilter collisionFilter = CollisionFilter.ALL_COLLISIONS;

	public void update(final float dt) {
		moveRelative(getVelocity().multiply(dt));
		shape.rotate(getRadialVelocity() * dt);
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
		// radialVelocity += getInvRotInertia() *
		// contactVector.crossProduct(force);
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
		return material.restitution;
	}

	public void setMaterial(final Material m) {
		if (m == null) {
			throw new NullPointerException("Material cannot be null");
		}
		material = m;
	}

	public float getDynamicFriction() {
		return material.dynamicFriction;
	}

	public float getStaticFriction() {
		return material.staticFriction;
	}

	public Vec2D getVelocity() {
		return velocity;
	}

	public void setVelocity(final Vec2D velocity) {
		if (velocity == null) {
			throw new NullPointerException("Velocity cannot be null");
		}
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
		if (i == PhysicsComponent.INFINITE_ROT_INERTIA) {
			invRotInertia = 0;
		} else {
			invRotInertia = 1f / i;
		}
	}

	public boolean isFullyConstructed() {
		return getMissingAttributes().isEmpty();
	}

	/**
	 * A debug method that returns all missing attributes for this entity to work properly
	 *
	 * @return a list of missing attributes
	 */
	public List<String> getMissingAttributes() {
		final List<String> missingAttributes = new ArrayList<>();
		if (material == null) {
			missingAttributes.add("Material");
		}
		if (shape == null) {
			missingAttributes.add("Shape");
		}
		if (collisionFilter == null) {
			missingAttributes.add("Collision Filter");
		}

		return missingAttributes;
	}

	@Override
	public int getComponentId() {
		return COMPONENT_ID;
	}
}
