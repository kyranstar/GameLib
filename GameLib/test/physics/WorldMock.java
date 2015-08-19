package physics;

import game.World;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Queue;

public class WorldMock extends World {

	public WorldMock() {
		super(60, 60, new Dimension(500, 500));
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

}
