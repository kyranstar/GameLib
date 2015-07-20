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
		super(60, 80, panel, Color.WHITE);

		RectObject ob = new RectObject();

		ob.restitution = 0.5f;
		ob.min = new Vec2D(0, 400);
		ob.max = new Vec2D(500, 500);
		ob.setMass(GameObject.INFINITE_MASS);
		ob.velocity = new Vec2D();
		objects.add(ob);

		ob = new RectObject();

		ob.restitution = 0.5f;
		ob.min = new Vec2D(0, 0);
		ob.max = new Vec2D(500, 10);
		ob.setMass(GameObject.INFINITE_MASS);
		ob.velocity = new Vec2D();
		objects.add(ob);

		for (int i = 0; i < 5; i++) {
			final RectObject o = new RectObject();

			o.restitution = 0.5f;
			final int x = i * 40 + 50;
			final int y = i * 40 + 100;
			o.min = new Vec2D(x, y);
			o.max = new Vec2D(x + 10 + i * 5, y + 10 + i * 5);
			o.setMass(o.area());
			o.velocity = new Vec2D();
			objects.add(o);

			final CircleObject o2 = new CircleObject();

			o2.restitution = 0.5f;
			o2.center = new Vec2D(x + 220, i * 40 + 120);
			o2.radius = i * 2 + 5;
			o2.setMass(o2.radius * o2.radius);
			o2.velocity = new Vec2D();
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
