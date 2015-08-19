package game.messaging;

import physics.PhysicsComponent;

public class CreateEntityMessage implements Message {

	public PhysicsComponent entity;

	public CreateEntityMessage(final PhysicsComponent e) {
		if (!e.isFullyConstructed()) {
			throw new IllegalArgumentException("Entity must be full constructed! Missing attributes: " + e.getMissingAttributes());
		}

		entity = e;
	}
}
