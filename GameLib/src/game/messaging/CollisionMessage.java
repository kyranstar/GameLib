package game.messaging;

import physics.collision.handling.CManifold;

public class CollisionMessage implements Message {
	public final CManifold manifold;

	public CollisionMessage(final CManifold manifold) {
		this.manifold = manifold;
	}
}
