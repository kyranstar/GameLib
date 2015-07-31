package draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import game.GameLoop;

public abstract class DrawingPanel extends GameLoop {

	private BufferedImage image;
	private JPanel panel;
	private Color background;

	public DrawingPanel(final int fps, final int ups, final JPanel panel, final Color background) {
		super(fps, ups);
		initialize(panel, background);
	}

	/**
	 * Creates a panel with 60 fps and 120 ups
	 *
	 * @param panel
	 * @param background
	 */
	public DrawingPanel(final JPanel panel, final Color background) {
		this(60, 120, panel, background);
	}

	public DrawingPanel(final JPanel panel) {
		this(panel, Color.WHITE);
	}

	public DrawingPanel(int ups, JPanel panel, Color background) {
		super(ups);
		initialize(panel, background);
	}

	private void initialize(final JPanel panel, final Color background) {
		image = GraphicsUtils.createImage(panel.getWidth(), panel.getHeight(), Transparency.OPAQUE);
		this.panel = panel;
		this.background = background;

		panel.addMouseListener(this);
		panel.addMouseMotionListener(this);
		panel.addKeyListener(this);
		panel.addMouseWheelListener(this);

		panel.setFocusable(true);
		panel.requestFocusInWindow();
	}

	@Override
	public void draw() {
		final Graphics2D g = (Graphics2D) image.getGraphics();
		GraphicsUtils.prettyGraphics(g);

		g.setColor(background);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());

		draw(g);
		g.dispose();

		final Graphics pg = panel.getGraphics();
		pg.drawImage(image, 0, 0, null);

		pg.dispose();
	}

	public abstract void draw(Graphics g);

}
