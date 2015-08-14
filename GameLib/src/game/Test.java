package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.Queue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import physics.GameEntity;
import physics.Material;
import physics.collision.CircleShape;
import physics.collision.RectShape;
import physics.constraints.AngleJoint;
import physics.constraints.Constraint;
import physics.constraints.DistanceJoint;
import physics.constraints.Joint;

public class Test extends World {

	GameEntity center;

	public Test(final JPanel panel) {
		super(60, panel);

		final GameEntity ob = new GameEntity(this);
		ob.setMaterial(Material.STEEL);
		ob.shape = new RectShape(new Vec2D(0, 900), new Vec2D(1000, 1000));
		ob.setMass(GameEntity.INFINITE_MASS);
		entities.add(ob);

		final Vec2D centerV = new Vec2D(500, 700);

		center = createBall(centerV, 75);
		center.setMass(GameEntity.INFINITE_MASS);
		entities.add(center);

		final float vertices = 6;
		final float dist = 120;

		GameEntity first = null;
		GameEntity last = null;
		for (int i = 0; i < vertices; i++) {
			final float angle = (float) (2 * Math.PI / vertices * i);
			final Vec2D newCenter = new Vec2D((float) (centerV.x + Math.cos(angle) * dist), (float) (centerV.y + Math.sin(angle) * dist));
			final GameEntity vertex = createBall(newCenter, 10);
			entities.add(vertex);
			if (last != null) {
				// constraints.add(new DistanceJoint(last, vertex));
			} else {
				first = vertex;
			}
			constraints.add(new DistanceJoint(center, vertex));
			constraints.add(new AngleJoint(center, vertex, angle, .1f));
			last = vertex;
			if (i == vertices - 1 && first != null) {
				// constraints.add(new DistanceJoint(first, vertex));
			}
		}

	}

	private GameEntity createBall(final Vec2D center, final int radius) {
		final GameEntity ob = new GameEntity(this);

		ob.setMaterial(Material.STEEL);
		ob.shape = new CircleShape(center, radius);
		ob.setMass(radius * radius);
		return ob;
	}

	public static void main(final String[] args) throws HeadlessException, InvocationTargetException, InterruptedException {
		final JPanel panel = new JPanel();

		SwingUtilities.invokeAndWait(() -> {
			final JFrame frame = new JFrame();
			panel.setPreferredSize(new Dimension(1000, 1000));
			frame.add(panel);

			frame.pack();
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		});
		new Test(panel).run();

	}

	@Override
	public void processInput(final Queue<KeyEvent> keyEvents, final Queue<EventPair<MouseEvent, MouseEventType>> mouseEvents,
			final Queue<MouseWheelEvent> mouseWheelEvents2) {
		for (final EventPair<MouseEvent, MouseEventType> e : mouseEvents) {
			if (e.type != MouseEventType.CLICK) {
				continue;
			}
			entities.add(createBall(new Vec2D(e.event.getX(), e.event.getY()), 10));
		}
	}

	@Override
	public void draw(final Graphics2D g) {
		for (final GameEntity object : entities) {
			if (object.shape instanceof RectShape) {
				final int x = (int) ((RectShape) object.shape).getMin().x;
				final int y = (int) ((RectShape) object.shape).getMin().y;
				final int width = (int) (((RectShape) object.shape).getMax().x - ((RectShape) object.shape).getMin().x);
				final int height = (int) (((RectShape) object.shape).getMax().y - ((RectShape) object.shape).getMin().y);
				if (!object.sleeping) {
					g.setColor(new Color(50, 100, 200));
				} else {
					g.setColor(new Color(200, 200, 200));
				}
				g.fillRect(x, y, width, height);
				g.setColor(Color.BLACK);
				g.drawRect(x, y, width, height);
			} else if (object.shape instanceof CircleShape) {
				final int radius = (int) ((CircleShape) object.shape).getRadius();
				final int x = (int) ((CircleShape) object.shape).getCenter().x - radius;
				final int y = (int) ((CircleShape) object.shape).getCenter().y - radius;
				if (!object.sleeping) {
					g.setColor(new Color(200, 100, 50));
				} else {
					g.setColor(new Color(200, 200, 200));
				}
				g.fillOval(x, y, radius * 2, radius * 2);
				g.setColor(Color.BLACK);
				g.drawOval(x, y, radius * 2, radius * 2);
			}
		}
		for (final Constraint c : constraints) {
			if (c instanceof Joint) {
				final Joint j = (Joint) c;
				g.setColor(Color.RED);
				final float x1 = j.getA().center().x;
				final float y1 = j.getA().center().y;
				final float x2 = j.getB().center().x;
				final float y2 = j.getB().center().y;
				g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
			}
		}

		g.setColor(Color.RED);
		g.drawString("Entities: " + entities.size(), 10, 15);
		g.drawString("FPS: " + getCurrentFPS(), 10, 30);
		g.drawString("UPS: " + getCurrentUPS(), 10, 45);

		drawMeter(g);
		quadtree.draw(g);
	}

	private void drawMeter(final Graphics g) {
		final int x = 10;
		final int top = 60;
		final int bottom = (int) (top + PIXELS_PER_METER);

		g.setColor(Color.RED);
		g.drawLine(x, top, x + 10, top);
		g.drawLine(x, top, x, bottom);
		g.drawLine(x, bottom, x + 10, bottom);
		g.drawString("1 meter", x + 10, (top + bottom) / 2);
	}

	int i = 0;

	@Override
	public void updateWorld(final float dt) {
		i++;
		if (i % 25 == 0) {
			// entities.add(createBall(new Vec2D(400, 200), 10));
		}
	}

}
