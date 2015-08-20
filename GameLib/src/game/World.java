package game;

import game.messaging.GameSystemManager;
import game.messaging.UpdateMessage;

import java.awt.Color;
import java.awt.Dimension;

import draw.DrawingPanel;

public abstract class World extends DrawingPanel {

	protected GameSystemManager systemManager = new GameSystemManager();
	private final Dimension bounds;

	public World(final int fps, final int ups, final Dimension bounds) {
		super(fps, ups, Color.WHITE, new Dimension((int) bounds.getWidth(), (int) bounds.getHeight()));
		this.bounds = bounds;
	}

	@Override
	public void update(final float dt) {
		systemManager.broadcast(new UpdateMessage(dt));
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
