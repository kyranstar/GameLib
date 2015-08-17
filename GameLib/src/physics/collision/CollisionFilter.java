package physics.collision;

public class CollisionFilter {
	public static final CollisionFilter ALL_COLLISIONS = new CollisionFilter(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

	private final int collisionGroup;
	private final int shouldCollideBitmask;
	private final int shouldPhysicsRespondBitmask;

	public CollisionFilter(final int collisionGroup, final int shouldCollideBitmask, final int shouldPhysicsRespondBitmask) {
		this.collisionGroup = collisionGroup;
		this.shouldCollideBitmask = shouldCollideBitmask;
		this.shouldPhysicsRespondBitmask = shouldPhysicsRespondBitmask;
	}

	/**
	 * If there should be any respone at all, meaning they have one of the same bits set on a bitmask.
	 *
	 * @param other
	 * @return
	 */
	public boolean shouldCollide(final CollisionFilter other) {
		return (shouldCollideBitmask & other.collisionGroup) != 0;
	}

	public boolean shouldPhysicsRespond(final CollisionFilter other) {
		if (!shouldCollide(other)) {
			return false;
		}
		// if we are colliding on the bits we want to physically respond to, return true
		return (shouldPhysicsRespondBitmask & other.collisionGroup) != 0;
	}
}
