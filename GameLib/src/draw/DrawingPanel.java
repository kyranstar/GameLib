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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DrawingPanel {
	private final Logger logger = LoggerFactory.getLogger(DrawingPanel.class);

	private JPanel panel;
	private Dimension size;

	public DrawingPanel(final Dimension size, final GameLoop input) {
		this(size, Color.WHITE, input);
	}

	public DrawingPanel(final Dimension size, final Color background, final GameLoop input) {

		initialize(background, size, input);
	}

	public void draw(final int millis) {
		panel.repaint(millis);
	}

	@SuppressWarnings("serial")
	private void initialize(final Color background, final Dimension size, final GameLoop input) {
		this.size = size;
		panel = new JPanel() {
			@Override
			protected void paintComponent(final Graphics g) {
				super.paintComponent(g);
				draw((Graphics2D) g);
			}
		};

		panel.setBackground(background);

		panel.addMouseListener(input);
		panel.addMouseMotionListener(input);
		panel.addKeyListener(input);
		panel.addMouseWheelListener(input);

		panel.setFocusable(true);
		panel.requestFocusInWindow();

	}

	public abstract void draw(Graphics2D g);

	public void createFrame() {
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
			logger.error("JFrame was unable to be created!", e);
		}
	}

	public void repaint(final int millis) {
		panel.repaint(millis);
	}

	public void repaint() {
		panel.repaint();
	}

}
