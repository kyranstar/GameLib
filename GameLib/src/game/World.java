package game;

import game.messaging.GameSystemManager;
import game.messaging.UpdateMessage;

import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.JPanel;

import physics.PhysicsSystem;
import draw.DrawingPanel;

public abstract class World extends DrawingPanel {

	protected GameSystemManager systemManager = new GameSystemManager();
	private final Rectangle bounds;

	public World(final int fps, final int ups, final JPanel panel) {
		super(fps, ups, panel, Color.WHITE);
		bounds = panel.getBounds();
		systemManager.addSystem(new PhysicsSystem(systemManager, panel.getBounds()));
	}

	public World(final int ups, final JPanel panel) {
		super(ups, panel, Color.WHITE);
		bounds = panel.getBounds();
		systemManager.addSystem(new PhysicsSystem(systemManager, panel.getBounds()));
	}

	@Override
	public void update(final float dt) {
		systemManager.broadcastMessage(new UpdateMessage(dt));

		updateWorld(dt);
	}

	public abstract void updateWorld(float dt);

	public int getHeight() {
		return bounds.height;
	}

	public int getWidth() {
		return bounds.width;
	}

}
