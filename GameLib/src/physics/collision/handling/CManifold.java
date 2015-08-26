package physics.collision.handling;

import math.Vec2D;
import physics.PhysicsComponent;

/**
 * Holds data about a collision between two GameObjects
 *
 * @author Kyran Adams
 *
 */
public class CManifold {
	// object A
	public PhysicsComponent a;
	// object B
	public PhysicsComponent b;
	// penetration amount
	private float penetration;
	// collision normal
	private Vec2D normal = new Vec2D();

	@Override
	public String toString() {
		return "CollisionManifold [a.shape=" + a.shape.getClass().getSimpleName() + ", b.shape=" + b.shape.getClass().getSimpleName()
				+ ", penetration=" + getPenetration() + ", normal=" + getNormal() + "]";
	}

	public float getPenetration() {
		return penetration;
	}

	public void setPenetration(final float penetration) {
		assert Float.isFinite(penetration);
		assert !Float.isNaN(penetration);

		this.penetration = penetration;
	}

	public Vec2D getNormal() {
		return normal;
	}

	public void setNormal(final Vec2D normal) {
		assert !normal.equals(Vec2D.ZERO);
		assert normal.isNormalized();
		this.normal = normal;
	}

}
