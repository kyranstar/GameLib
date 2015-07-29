package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.Queue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import physics.DistanceJoint;
import physics.GameEntity;
import physics.Joint;
import physics.Material;
import physics.SpringJoint;
import physics.collision.CircleShape;
import physics.collision.RectShape;

public class Test extends World {

	public Test(JPanel panel) {
		super(60, 60, panel);

		final GameEntity ob = new GameEntity();

		ob.setMaterial(Material.STEEL);
		ob.shape = new CircleShape(new Vec2D(200, 100), 10);
		ob.setMass(GameEntity.INFINITE_MASS);
		objects.add(ob);

		final GameEntity ob2 = new GameEntity();

		ob2.setMaterial(Material.STEEL);
		ob2.shape = new CircleShape(new Vec2D(200, 200), 30);
		ob2.setMass(30 * 30);

		objects.add(ob2);

		final GameEntity ob3 = new GameEntity();

		ob3.setMaterial(Material.STEEL);
		ob3.shape = new CircleShape(new Vec2D(200, 260), 20);
		ob3.setMass(10 * 10);
		objects.add(ob3);

		joints.add(new SpringJoint(ob, ob2, 100, 0.001f));
		joints.add(new DistanceJoint(ob2, ob3, 60));

		// ob2.applyForce(new Vec2D(600000, 0));
	}

	public static void main(final String[] args)
			throws HeadlessException, InvocationTargetException, InterruptedException {
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
	public void processInput(final Queue<KeyEvent> keyEvents,
			final Queue<EventPair<MouseEvent, MouseEventType>> mouseEvents,
			final Queue<MouseWheelEvent> mouseWheelEvents2) {
		for (final EventPair<MouseEvent, MouseEventType> e : mouseEvents) {
			if (e.type != MouseEventType.CLICK) {
				continue;
			}

			final GameEntity o = new GameEntity();
			o.setMaterial(Material.STEEL);
			final int radius = 30;
			o.shape = new CircleShape(new Vec2D(e.event.getX(), e.event.getY()), radius);
			o.setMass(radius * radius);
			objects.add(o);
		}
	}

	@Override
	public void draw(Graphics g) {
		for (final GameEntity object : objects) {
			if (object.shape instanceof RectShape) {
				final int x = (int) ((RectShape) object.shape).min.x;
				final int y = (int) ((RectShape) object.shape).min.y;
				final int width = (int) (((RectShape) object.shape).max.x - ((RectShape) object.shape).min.x);
				final int height = (int) (((RectShape) object.shape).max.y - ((RectShape) object.shape).min.y);
				if (!object.sleeping) {
					g.setColor(new Color(50, 100, 200));
				} else {
					g.setColor(new Color(200, 200, 200));
				}
				g.fillRect(x, y, width, height);
				g.setColor(Color.BLACK);
				g.drawRect(x, y, width, height);
			} else if (object.shape instanceof CircleShape) {
				final int radius = (int) ((CircleShape) object.shape).radius;
				final int x = (int) ((CircleShape) object.shape).center.x - radius;
				final int y = (int) ((CircleShape) object.shape).center.y - radius;
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
		for (final Joint j : joints) {
			g.setColor(Color.RED);
			final float x1 = j.getA().center().x;
			final float y1 = j.getA().center().y;
			final float x2 = j.getB().center().x;
			final float y2 = j.getB().center().y;
			g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
		}

		g.setColor(Color.RED);
		g.drawString("Entities: " + objects.size(), 10, 15);
		g.drawString("FPS: " + getCurrentFPS(), 10, 30);
		g.drawString("UPS: " + getCurrentUPS(), 10, 45);
	}

	@Override
	public void updateWorld(float dt) {
		// TODO Auto-generated method stub

	}

}
