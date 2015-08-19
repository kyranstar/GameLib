package physics.collision.handling;

import math.Vec2D;
import physics.PhysicsComponent;
import physics.collision.shape.CShape;
import physics.collision.shape.CircleShape;
import physics.collision.shape.RectShape;

/**
 * Holds methods to perform physics operations on GameObjects.
 *
 * @author Kyran Adams
 *
 */
public final class Collisions {
	private Collisions() {
		// cant instantiate this class
	}

	/**
	 * Returns whether two GameObjects are colliding
	 *
	 * @param a
	 *            must be a RectObject or CircleObject
	 * @param b
	 *            must be a RectObject or CircleObject
	 * @return
	 */
	public static boolean isColliding(final PhysicsComponent a, final PhysicsComponent b) {
		final CShape as = a.shape;
		final CShape bs = b.shape;
		if (!a.collisionFilter.shouldCollide(b.collisionFilter)
				&& !b.collisionFilter.shouldCollide(a.collisionFilter)) {
			return false;
		}

		if (as instanceof RectShape && bs instanceof RectShape) {
			return CollisionRectRect.isColliding((RectShape) as, (RectShape) bs);
		}
		if (as instanceof CircleShape && bs instanceof CircleShape) {
			return CollisionCircleCircle.isColliding((CircleShape) as, (CircleShape) bs);
		}
		if (as instanceof RectShape && bs instanceof CircleShape) {
			return CollisionRectCircle.isColliding((RectShape) as, (CircleShape) bs);
		}
		if (as instanceof CircleShape && bs instanceof RectShape) {
			return CollisionRectCircle.isColliding((RectShape) bs, (CircleShape) as);
		}

		throw new UnsupportedOperationException();
	}

	public static void fixCollision(final PhysicsComponent a, final PhysicsComponent b) {
		fixCollision(generateManifold(a, b), true);
	}

	public static void fixCollision(final PhysicsComponent a, final PhysicsComponent b, final float restitution) {
		fixCollision(generateManifold(a, b), true, restitution);
	}

	/**
	 * Fixes a collision between two objects by correcting their positions and
	 * applying impulses.
	 *
	 */
	public static void fixCollision(final CManifold m, final boolean applyFriction) {
		fixCollision(m, applyFriction, Math.min(m.a.getRestitution(), m.b.getRestitution()));
	}

	public static void fixCollision(final CManifold m, final boolean applyFriction, final float restitution) {
		final PhysicsComponent a = m.a;
		final PhysicsComponent b = m.b;
		// Calculate relative velocity
		final Vec2D rv = b.getVelocity().minus(a.getVelocity());

		// Calculate relative velocity in terms of the normal direction
		final float velAlongNormal = Math.abs(rv.dotProduct(m.getNormal()));

		assert velAlongNormal >= 0;

		// Calculate restitution
		final float e = Math.min(a.getRestitution(), b.getRestitution());

		float j = (1 + e) * velAlongNormal;
		j /= a.getInvMass() + b.getInvMass();

		// Apply impulse
		final Vec2D impulse = m.getNormal().multiply(j);

		if (a.collisionFilter.shouldPhysicsRespond(b.collisionFilter)) {
			a.applyForce(impulse.multiply(-1));
		}
		if (b.collisionFilter.shouldPhysicsRespond(a.collisionFilter)) {
			b.applyForce(impulse);
		}

		if (applyFriction) {
			applyFriction(m, j);
		}

		positionalCorrection(m);
	}

	private static void applyFriction(final CManifold m, final float normalMagnitude) {
		final PhysicsComponent a = m.a;
		final PhysicsComponent b = m.b;

		// relative velocity
		final Vec2D rv = b.getVelocity().minus(a.getVelocity());
		// normalized tangent force
		final Vec2D tangent = rv.minus(m.getNormal().multiply(m.getNormal().dotProduct(rv))).unitVector();
		// friction magnitude
		final float jt = -rv.dotProduct(tangent) / (a.getInvMass() + b.getInvMass());

		// friction coefficient
		final float mu = (a.getStaticFriction() + b.getStaticFriction()) / 2;
		final float dynamicFriction = (a.getDynamicFriction() + b.getDynamicFriction()) / 2;

		// Coulomb's law: force of friction <= force along normal * mu
		final Vec2D frictionImpulse = Math.abs(jt) < normalMagnitude * mu ? tangent.multiply(jt)
				: tangent.multiply(-normalMagnitude * dynamicFriction);

		// apply friction
		if (a.collisionFilter.shouldPhysicsRespond(b.collisionFilter)) {
			a.applyForce(frictionImpulse.multiply(-1));
		}
		if (b.collisionFilter.shouldPhysicsRespond(a.collisionFilter)) {
			b.applyForce(frictionImpulse);
		}
	}

	/**
	 * Generates a collision manifold from two colliding objects.
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	private static CManifold generateManifold(final PhysicsComponent a, final PhysicsComponent b) {
		final CManifold m = new CManifold();
		m.a = a;
		m.b = b;
		final CShape as = a.shape;
		final CShape bs = b.shape;

		if (as instanceof RectShape && bs instanceof RectShape) {
			return CollisionRectRect.generateManifold((RectShape) as, (RectShape) bs, m);
		} else if (as instanceof CircleShape && bs instanceof CircleShape) {
			return CollisionCircleCircle.generateManifold((CircleShape) as, (CircleShape) bs, m);
		} else if (as instanceof RectShape && bs instanceof CircleShape) {
			return CollisionRectCircle.generateManifold((RectShape) as, (CircleShape) bs, m);
		} else if (as instanceof CircleShape && bs instanceof RectShape) {
			m.b = a;
			m.a = b;
			return CollisionRectCircle.generateManifold((RectShape) bs, (CircleShape) as, m);
		} else {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * Corrects positions a certain amount between two colliding objects to
	 * avoid "sinking" of one object into another.
	 *
	 * @param m
	 */
	private static void positionalCorrection(final CManifold m) {
		final PhysicsComponent a = m.a;
		final PhysicsComponent b = m.b;

		// the amount to correct by
		final float percent = .4f; // usually .2 to .8
		// the amount in which we don't really care, this avoids vibrating
		// objects.
		final float slop = 0.01f; // usually 0.01 to 0.1

		final float correctionMag = m.getPenetration() > 0 ? Math.max(m.getPenetration() - slop, 0)
				: Math.min(m.getPenetration() + slop, 0);

		final Vec2D correction = m.getNormal().multiply(correctionMag / (a.getInvMass() + b.getInvMass()) * percent);
		if (a.collisionFilter.shouldPhysicsRespond(b.collisionFilter)) {
			a.moveRelative(correction.multiply(-1 * a.getInvMass()));
		}
		if (b.collisionFilter.shouldPhysicsRespond(a.collisionFilter)) {
			b.moveRelative(correction.multiply(b.getInvMass()));
		}
	}

}
