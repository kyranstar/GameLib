package game.messaging;

import java.awt.Graphics2D;

public class RenderMessage implements Message {

	public Graphics2D graphics;

	public RenderMessage(final Graphics2D g) {
		graphics = g;
	}

}
