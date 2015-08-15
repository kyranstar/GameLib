package physics;

import game.World;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Queue;

import javax.swing.JPanel;

public class WorldMock extends World {

	public static JPanel panel;
	static {
		panel = new JPanel();
		panel.setSize(500, 500);
	}

	public WorldMock() {
		super(60, 60, panel);
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
