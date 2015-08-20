package game;

import game.entity.GameEntity;
import game.messaging.CreateConstraintMessage;
import game.messaging.CreateEntityMessage;
import game.messaging.GameSystemManager;
import game.messaging.Message;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import physics.constraints.Constraint;

public class WorldMock extends World {

	private final EntityCollectorSystem entityCounter;

	public WorldMock() {
		super(60, 60, new Dimension(500, 500));
		entityCounter = new EntityCollectorSystem(systemManager);
		systemManager.addSystem(entityCounter);
	}

	public void runFrames(final int steps) {
		for (int i = 0; i < steps; i++) {
			runFrame();
		}
	}

	@Override
	public void updateWorld(final float dt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(final Graphics2D g) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processInput(final Queue<KeyEvent> keyEvents, final Queue<EventPair<MouseEvent, MouseEventType>> mouseEvent,
			final Queue<MouseWheelEvent> mouseWheelEvents2) {
		// TODO Auto-generated method stub

	}

	public GameSystemManager getSystemManager() {
		return systemManager;
	}

	public int getEntityCount() {
		return entityCounter.entities.size();
	}

	public int getConstraintCount() {
		return entityCounter.constraints.size();
	}

	private static class EntityCollectorSystem extends GameSystem {
		private final List<GameEntity> entities = new ArrayList<>();
		private final List<Constraint> constraints = new ArrayList<>();

		public EntityCollectorSystem(final GameSystemManager systemManager) {
			super(systemManager);
		}

		@Override
		public void recieveMessage(final Message m) {
			if (m instanceof CreateEntityMessage) {
				entities.add(((CreateEntityMessage) m).entity);
			} else if (m instanceof CreateConstraintMessage) {
				constraints.add(((CreateConstraintMessage) m).constraint);
			}
		}

		@Override
		public Set<Class<? extends Message>> getAcceptedMessages() {
			return GameSystem.ACCEPT_ALL_MESSAGES;
		}

	}

}
