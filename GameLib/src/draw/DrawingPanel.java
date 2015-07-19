package draw;

import game.GameLoop;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public abstract class DrawingPanel extends GameLoop {

	private final BufferedImage image;
	private final JPanel panel;
	private final Color background;

	public DrawingPanel(final int fps, final int ups, final JPanel panel, final Color background) {
		super(fps, ups);
		image = GraphicsUtils.createImage(panel.getWidth(), panel.getHeight(), Transparency.TRANSLUCENT);
		this.panel = panel;
		this.background = background;

		panel.addMouseListener(this);
		panel.addMouseMotionListener(this);
		panel.addKeyListener(this);
		panel.addMouseWheelListener(this);

		panel.setFocusable(true);
		panel.requestFocusInWindow();
	}

	/**
	 * Creates a panel with 60 fps and 30 ups
	 *
	 * @param panel
	 * @param background
	 */
	public DrawingPanel(final JPanel panel, final Color background) {
		this(60, 30, panel, background);
	}

	public DrawingPanel(final JPanel panel) {
		this(panel, Color.WHITE);
	}

	@Override
	public void draw() {
		final Graphics2D g = (Graphics2D) image.getGraphics();
		GraphicsUtils.prettyGraphics(g);

		g.setColor(background);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());

		draw(g);

		final Graphics pg = panel.getGraphics();
		pg.drawImage(image, 0, 0, null);

		pg.dispose();
		g.dispose();
	}

	public abstract void draw(Graphics g);

}
