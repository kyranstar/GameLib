package physics.collision;

import game.Vec2D;
import physics.GameEntity;

/**
 * Holds data about a collision between two GameObjects
 *
 * @author Kyran Adams
 *
 */
public class CManifold {
	// object A
	public GameEntity a;
	// object B
	public GameEntity b;
	// penetration amount
	public float penetration;
	// collision normal
	public Vec2D normal = new Vec2D();

	@Override
	public String toString() {
		return "CollisionManifold [a.shape=" + a.shape.getClass().getSimpleName() + ", b.shape=" + b.shape.getClass().getSimpleName()
				+ ", penetration=" + penetration + ", normal=" + normal + "]";
	}

}
