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

import physics.GameEntity;
import physics.Material;
import physics.collision.CircleShape;
import physics.collision.Collisions;
import physics.collision.RectShape;
import draw.DrawingPanel;

public class Test extends DrawingPanel {

	private final List<GameEntity> objects = new ArrayList<>();

	public Test(final JPanel panel) {
		super(60, 120, panel, Color.WHITE);

		final GameEntity ob = new GameEntity();

		ob.setMaterial(Material.STEEL);
		ob.shape = new RectShape(new Vec2D(0, 450), new Vec2D(500, 500));
		ob.setMass(GameEntity.INFINITE_MASS);
		ob.velocity = new Vec2D();
		objects.add(ob);

		for (int i = 0; i < 5; i++) {
			for (int j = i; j < 5; j++) {
				final GameEntity o = new GameEntity();
				o.setMaterial(Material.STEEL);
				final int x = i * 40 - j * 20 + 200;
				final int y = j * 40 + 100;
				o.shape = new RectShape(new Vec2D(x, y), new Vec2D(x + 40, y + 40));
				o.setMass(((RectShape) o.shape).area());
				o.velocity = new Vec2D();
				objects.add(o);
			}
			final GameEntity o = new GameEntity();
			o.setMaterial(Material.STEEL);
			final int radius = i * 3;
			o.shape = new CircleShape(new Vec2D(225, i * radius), radius);
			o.setMass(radius * radius);
			o.velocity = new Vec2D(41, 1);
			objects.add(o);
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
		for (final GameEntity object : objects) {
			if (object.shape instanceof RectShape) {
				final int x = (int) ((RectShape) object.shape).min.x;
				final int y = (int) ((RectShape) object.shape).min.y;
				final int width = (int) (((RectShape) object.shape).max.x - ((RectShape) object.shape).min.x);
				final int height = (int) (((RectShape) object.shape).max.y - ((RectShape) object.shape).min.y);
				g.setColor(new Color(50, 100, 200));
				g.fillRect(x, y, width, height);
				g.setColor(Color.BLACK);
				g.drawRect(x, y, width, height);
			} else if (object.shape instanceof CircleShape) {
				final int radius = (int) ((CircleShape) object.shape).radius;
				final int x = (int) ((CircleShape) object.shape).center.x - radius;
				final int y = (int) ((CircleShape) object.shape).center.y - radius;
				g.setColor(new Color(200, 100, 50));
				g.fillOval(x, y, radius * 2, radius * 2);
				g.setColor(Color.BLACK);
				g.drawOval(x, y, radius * 2, radius * 2);
			}
		}
		g.setColor(Color.RED);
		g.drawString("FPS: " + getCurrentFPS(), 10, 30);
		g.drawString("UPS: " + getCurrentUPS(), 10, 45);
	}

	@Override
	public void processInput(final Queue<KeyEvent> keyEvents, final Queue<MouseEvent> mouseEvent, final Queue<MouseWheelEvent> mouseWheelEvents2) {

	}

	@Override
	public void update(final float dt) {
		final Vec2D gravity = new Vec2D(0, .98f);
		for (int i = 0; i < objects.size(); i++) {
			final GameEntity a = objects.get(i);
			if (a.center().y > 700) {
				objects.remove(i);
				continue;
			}
			if (a.getMass() != GameEntity.INFINITE_MASS) {
				a.applyForce(gravity.divide(a.getInvMass()));
			}
			a.update(dt);
			for (int j = i + 1; j < objects.size(); j++) {
				final GameEntity b = objects.get(j);
				if (Collisions.isColliding(a, b)) {
					Collisions.fixCollision(a, b);
				}
			}
		}
	}
}
