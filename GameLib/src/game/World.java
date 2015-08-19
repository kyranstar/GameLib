package game;

import game.messaging.GameSystemManager;
import game.messaging.UpdateMessage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import physics.PhysicsSystem;
import draw.DrawingPanel;

public abstract class World extends DrawingPanel {

	protected GameSystemManager systemManager = new GameSystemManager();
	private final Dimension bounds;

	public World(final int fps, final int ups, final Dimension bounds) {
		super(fps, ups, Color.WHITE, new Dimension((int) bounds.getWidth(), (int) bounds.getHeight()));
		this.bounds = bounds;
		systemManager.addSystem(new PhysicsSystem(systemManager, new Rectangle(0, 0, bounds.width, bounds.height)));
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
