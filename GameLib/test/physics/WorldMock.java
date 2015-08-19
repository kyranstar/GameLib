package physics;

import game.GameSystem;
import game.World;
import game.messaging.CreateConstraintMessage;
import game.messaging.CreateEntityMessage;
import game.messaging.GameSystemManager;
import game.messaging.Message;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Queue;

public class WorldMock extends World {

	private final EntityCounterSystem entityCounter;

	public WorldMock() {
		super(60, 60, new Dimension(500, 500));
		entityCounter = new EntityCounterSystem(systemManager);
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
		return entityCounter.entityCount;
	}

	public int getConstraintCount() {
		return entityCounter.constraintCount;
	}

	private static class EntityCounterSystem extends GameSystem {
		private int entityCount = 0;
		private int constraintCount = 0;

		public EntityCounterSystem(final GameSystemManager systemManager) {
			super(systemManager);
		}

		@Override
		public void recieveMessage(final Message m) {
			if (m instanceof CreateEntityMessage) {
				entityCount++;
			} else if (m instanceof CreateConstraintMessage) {
				constraintCount++;
			}
		}

	}

}
