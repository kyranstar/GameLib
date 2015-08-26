package game;

import game.entity.GameEntity;
import game.messaging.CreateConstraintMessage;
import game.messaging.CreateEntityMessage;
import game.messaging.GameSystemManager;
import game.messaging.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import physics.constraints.Constraint;

public class EntityCollectorSystem extends GameSystem {
	private final List<GameEntity> entities = new ArrayList<>();
	final List<Constraint> constraints = new ArrayList<>();

	public EntityCollectorSystem(final GameSystemManager systemManager) {
		super(systemManager);
	}

	@Override
	public void recieveMessage(final Message m) {
		if (m instanceof CreateEntityMessage) {
			getEntities().add(((CreateEntityMessage) m).entity);
		} else if (m instanceof CreateConstraintMessage) {
			constraints.add(((CreateConstraintMessage) m).constraint);
		}
	}

	@Override
	public Set<Class<? extends Message>> getAcceptedMessages() {
		return GameSystem.ACCEPT_ALL_MESSAGES;
	}

	public List<GameEntity> getEntities() {
		return entities;
	}

}