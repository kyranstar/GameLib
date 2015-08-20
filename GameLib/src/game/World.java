package game;

import game.messaging.GameSystemManager;
import game.messaging.UpdateMessage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import draw.DrawingPanel;

public abstract class World extends GameLoop {

	protected GameSystemManager systemManager = new GameSystemManager();
	private final DrawingPanel drawingPanel;
	private final Dimension bounds;

	public World(final int fps, final int ups, final Dimension bounds) {
		super(fps, ups);
		this.bounds = bounds;
		drawingPanel = new DrawingPanel(new Dimension((int) bounds.getWidth(), (int) bounds.getHeight()), Color.WHITE, this) {
			@Override
			public void draw(final Graphics2D g) {
				World.this.draw(g);
			}
		};
	}

	@Override
	public void draw(final int millis) {
		drawingPanel.repaint(millis);
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

	public Rectangle getBounds() {
		return new Rectangle(0, 0, bounds.width, bounds.height);
	}

	public abstract void draw(final Graphics2D g);

	public World createFrame() {
		drawingPanel.createFrame();
		return this;
	}

}
