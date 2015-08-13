package draw;

import game.GameLoop;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

import javax.swing.JPanel;

public abstract class DrawingPanel extends GameLoop {

	private BufferedImage image;
	private Graphics2D ig;
	private Graphics pg;

	private int backgroundRGB;

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

	public DrawingPanel(final int ups, final JPanel panel, final Color background) {
		super(ups);
		initialize(panel, background);
	}

	private void initialize(final JPanel panel, final Color background) {
		image = GraphicsUtils.createImage(panel.getWidth(), panel.getHeight(), Transparency.OPAQUE);
		ig = (Graphics2D) image.getGraphics();
		pg = panel.getGraphics();
		GraphicsUtils.prettyGraphics(ig);
		backgroundRGB = background.getRGB();

		panel.addMouseListener(this);
		panel.addMouseMotionListener(this);
		panel.addKeyListener(this);
		panel.addMouseWheelListener(this);

		panel.setFocusable(true);
		panel.requestFocusInWindow();
	}

	@Override
	public void draw() {
		// set background
		Arrays.fill(((DataBufferInt) image.getRaster().getDataBuffer()).getData(), backgroundRGB);

		// draw on the buffer
		draw(ig);
		// draw our buffer to the screen
		pg.drawImage(image, 0, 0, null);
	}

	public abstract void draw(Graphics2D g);

}
