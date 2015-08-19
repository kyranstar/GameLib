package game;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Camera2D {
	public Rectangle bounds;

	public void translate(Graphics2D g) {
		g.translate(-bounds.x, -bounds.y);
	}

	public void untranslate(Graphics2D g) {
		g.translate(bounds.x, bounds.y);
	}

	public void clip(Graphics2D g) {
		g.clip(bounds);
	}
}
