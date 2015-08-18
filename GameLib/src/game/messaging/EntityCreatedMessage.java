package game.messaging;

import physics.PhysicsEntity;

public class EntityCreatedMessage implements Message {

	public PhysicsEntity entity;

	public EntityCreatedMessage(final PhysicsEntity e) {
		entity = e;
	}
}
