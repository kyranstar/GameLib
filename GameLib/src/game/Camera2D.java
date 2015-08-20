package game;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

import math.Vec2D;

public class Camera2D {
	public Rectangle bounds;
	private Shape previousClippingArea = null;

	public void translate(final Graphics2D g) {
		g.translate(-bounds.x, -bounds.y);
	}

	public void untranslate(final Graphics2D g) {
		g.translate(bounds.x, bounds.y);
	}

	public void clip(final Graphics2D g) {
		previousClippingArea = g.getClip();
		g.setClip(bounds);
	}

	public void unclip(final Graphics2D g) {
		g.setClip(previousClippingArea);
	}

	public Vec2D translate(final Vec2D p) {
		return p.minus(new Vec2D(bounds.x, bounds.y));
	}
}
