package game.messaging;

import game.entity.GameEntity;

public class CreateEntityMessage implements Message {

	public GameEntity entity;

	public CreateEntityMessage(final GameEntity e) {
		entity = e;
	}
}
