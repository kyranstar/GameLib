package draw;

import game.GameLoop;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public abstract class DrawingPanel extends GameLoop {

	private JPanel panel;
	private Dimension size;

	public DrawingPanel(final int fps, final int ups, final Color background, final Dimension size) {
		super(fps, ups);
		initialize(background, size);
	}

	/**
	 * Creates a panel with 60 fps and 120 ups
	 *
	 * @param background
	 * @param size
	 */
	public DrawingPanel(final Color background, final Dimension size) {
		this(60, 120, background, size);
	}

	public DrawingPanel(final Dimension size) {
		this(Color.WHITE, size);
	}

	public DrawingPanel(final int ups, final Dimension size, final Color background) {
		super(ups);
		initialize(background, size);
	}

	@Override
	public void draw(final int millis) {
		panel.repaint(millis);
	}

	@SuppressWarnings("serial")
	private void initialize(final Color background, final Dimension size) {
		this.size = size;
		panel = new JPanel() {
			@Override
			protected void paintComponent(final Graphics g) {
				super.paintComponent(g);
				draw((Graphics2D) g);
			}
		};

		panel.setBackground(background);

		panel.addMouseListener(this);
		panel.addMouseMotionListener(this);
		panel.addKeyListener(this);
		panel.addMouseWheelListener(this);

		panel.setFocusable(true);
		panel.requestFocusInWindow();

	}

	public abstract void draw(Graphics2D g);

	public DrawingPanel createFrame() {
		try {
			SwingUtilities.invokeAndWait(() -> {
				final JFrame frame = new JFrame();
				panel.setPreferredSize(size);
				frame.add(panel);

				frame.pack();
				frame.setVisible(true);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			});
		} catch (HeadlessException | InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
		return this;
	}

}
