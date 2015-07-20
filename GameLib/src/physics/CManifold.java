package physics;

import game.Vec2D;

/**
 * Holds data about a collision between two GameObjects
 * 
 * @author Kyran Adams
 *
 * @param <A>
 * @param <B>
 */
public class CManifold<A extends GameObject, B extends GameObject> {
	// object A
	public A a;
	// object B
	public B b;
	// penetration amount
	public float penetration;
	// collision normal
	public Vec2D normal = new Vec2D();

	@Override
	public String toString() {
		return "CollisionManifold [a=" + a.getClass().getSimpleName() + ", b=" + b.getClass().getSimpleName() + ", penetration=" + penetration
				+ ", normal=" + normal + "]";
	}

}
