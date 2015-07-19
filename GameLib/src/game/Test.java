package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import physics.CircleObject;
import physics.GameObject;
import physics.GamePhysics;
import physics.RectObject;
import draw.DrawingPanel;

public class Test extends DrawingPanel {

	private final List<GameObject> objects = new ArrayList<>();

	public Test(final JPanel panel) {
		super(60, 60, panel, Color.WHITE);

		final RectObject ob = new RectObject();

		ob.restitution = 0.5f;
		ob.min = new Vec2D(0, 400);
		ob.max = new Vec2D(500, 450);
		ob.setMass(GameObject.INFINITE_MASS);
		ob.velocity = new Vec2D();
		objects.add(ob);
		final Random rand = new Random();
		for (int i = 0; i < 5; i++) {
			final RectObject o = new RectObject();
			o.restitution = 0.5f;
			o.min = new Vec2D(i * 100, 300);
			o.max = new Vec2D(i * 100 + 20 + 10 * rand.nextInt(5), 360);
			o.setMass(o.max.minus(o.min).x * o.max.minus(o.min).y / 5);
			o.velocity = new Vec2D(10, 0);
			o.dynamicFriction = o.staticFriction = 0.3f;
			objects.add(o);

			final CircleObject o2 = new CircleObject();

			o2.restitution = 0.5f;
			o2.center = new Vec2D(rand.nextInt(5) * 60, 200);
			o2.radius = 10 + rand.nextInt(5) * 5;
			o2.setMass(o2.radius);
			o2.velocity = new Vec2D(1, 1);
			o2.dynamicFriction = o2.staticFriction = 0.1f;
			objects.add(o2);
		}
	}

	public static void main(final String[] args) throws HeadlessException, InvocationTargetException, InterruptedException {
		final JPanel panel = new JPanel();

		SwingUtilities.invokeAndWait(() -> {
			final JFrame frame = new JFrame();
			panel.setPreferredSize(new Dimension(500, 500));
			frame.add(panel);

			frame.pack();
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		});
		new Test(panel).run();

	}

	@Override
	public void draw(final Graphics g) {
		for (final GameObject object : objects) {
			if (object instanceof RectObject) {
				final int x = (int) ((RectObject) object).min.x;
				final int y = (int) ((RectObject) object).min.y;
				final int width = (int) (((RectObject) object).max.x - ((RectObject) object).min.x);
				final int height = (int) (((RectObject) object).max.y - ((RectObject) object).min.y);
				g.setColor(new Color(50, 100, 200));
				g.fillRect(x, y, width, height);
				g.setColor(Color.BLACK);
				g.drawRect(x, y, width, height);
			} else if (object instanceof CircleObject) {
				final int radius = (int) ((CircleObject) object).radius;
				final int x = (int) ((CircleObject) object).center.x - radius;
				final int y = (int) ((CircleObject) object).center.y - radius;
				g.setColor(new Color(200, 100, 50));
				g.fillOval(x, y, radius * 2, radius * 2);
				g.setColor(Color.BLACK);
				g.drawOval(x, y, radius * 2, radius * 2);
			}
		}
	}

	@Override
	public void processInput(final Queue<KeyEvent> keyEvents, final Queue<MouseEvent> mouseEvent, final Queue<MouseWheelEvent> mouseWheelEvents2) {

	}

	@Override
	public void update(final float dt) {
		final Vec2D gravity = new Vec2D(0, 9.8f);
		for (int i = 0; i < objects.size(); i++) {
			final GameObject a = objects.get(i);
			if (a.center().y > 700) {
				objects.remove(i);
				continue;
			}
			if (a.getMass() != GameObject.INFINITE_MASS) {
				a.applyForce(gravity.divide(a.getInvMass()));
			}
			a.update(dt);
			for (int j = i + 1; j < objects.size(); j++) {
				final GameObject b = objects.get(j);
				if (GamePhysics.isColliding(a, b)) {
					GamePhysics.fixCollision(a, b);
				}
			}
		}
	}
}
