package game;

import game.messaging.GameSystemManager;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Queue;

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
	public void update(final float dt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(final Graphics2D g) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processInput(final Queue<EventPair<KeyEvent, KeyEventType>> keyEvents, final Queue<EventPair<MouseEvent, MouseEventType>> mouseEvent,
			final Queue<MouseWheelEvent> mouseWheelEvents2) {
		// TODO Auto-generated method stub

	}

	public GameSystemManager getSystemManager() {
		return systemManager;
	}

	public int getEntityCount() {
		return entityCounter.getEntities().size();
	}

	public int getConstraintCount() {
		return entityCounter.constraints.size();
	}

}
