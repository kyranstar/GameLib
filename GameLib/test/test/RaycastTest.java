package test;

import game.EntityCollectorSystem;
import game.World;
import game.entity.GameEntity;
import game.messaging.CreateEntityMessage;
import game.messaging.RenderMessage;
import game.messaging.UpdateMessage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import math.AngleUtils;
import math.Vec2D;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import physics.Material;
import physics.PhysicsComponent;
import physics.PhysicsSystem;
import physics.collision.ray.Ray;
import physics.collision.ray.RaycastDetector;
import physics.collision.ray.RaycastResult;
import physics.collision.shape.CircleShape;
import physics.collision.shape.RectShape;
import draw.RenderSystem;

public class RaycastTest extends World {

	final static Logger logger = LoggerFactory.getLogger(RaycastTest.class);
	private Optional<Point> start = Optional.empty();
	private ObjectType objectType = ObjectType.BALL;
	private final List<Ray> rays = new ArrayList<>();
	EntityCollectorSystem entities;

	public RaycastTest(final Dimension bounds) {
		super(60, 120, bounds);
		systemManager.addSystem(new RenderSystem(systemManager, this));
		systemManager.addSystem(new PhysicsSystem(systemManager, new Rectangle(0, 0, bounds.width, bounds.height)));
		entities = new EntityCollectorSystem(systemManager);
		systemManager.addSystem(entities);

		createWorld();
	}

	public static void main(final String[] args) throws HeadlessException, InvocationTargetException, InterruptedException {
		logger.info("Application started");
		new RaycastTest(new Dimension(1000, 1000)).createFrame().run();

	}

	private void createWorld() {
		final PhysicsComponent part = new PhysicsComponent();
		part.shape = new RectShape(new Vec2D(0, 900), new Vec2D(1000, 1000));
		part.setMass(PhysicsComponent.INFINITE_MASS);
		part.setMaterial(Material.STEEL);
		addEntity(part);
	}

	private void addEntity(final PhysicsComponent pc) {
		final GameEntity e = new GameEntity();
		e.addComponent(pc);
		systemManager.broadcast(new CreateEntityMessage(e));
	}

	@Override
	public void processInput(final Queue<EventPair<KeyEvent, KeyEventType>> keyEvents,
			final Queue<EventPair<MouseEvent, MouseEventType>> mouseEvents, final Queue<MouseWheelEvent> mouseWheelEvents2) {
		for (final EventPair<MouseEvent, MouseEventType> e : mouseEvents) {
			if (e.type != MouseEventType.CLICK) {
				continue;
			}
			if (objectType == ObjectType.RAY) {
				if (start.isPresent()) {
					final Vec2D startVec = new Vec2D(start.get());
					rays.add(new Ray(startVec, new Vec2D(e.event.getPoint()).minus(startVec).unitVector()));
					start = Optional.empty();
				} else {
					start = Optional.of(e.event.getPoint());
				}
			} else if (objectType == ObjectType.BALL) {
				addEntity(createBall(new Vec2D(e.event.getX(), e.event.getY()), 10));
			} else {
				addEntity(createRect(new Vec2D(e.event.getX(), e.event.getY()), 20));
			}
		}
		for (final EventPair<KeyEvent, KeyEventType> event : keyEvents) {
			final KeyEvent e = event.event;
			final KeyEventType type = event.type;
			if (e.getKeyCode() == KeyEvent.VK_T && type == KeyEventType.PRESS) {
				switch (objectType) {
				case RAY:
					objectType = ObjectType.BALL;
					start = Optional.empty();
					break;
				case BALL:
					objectType = ObjectType.BOX;
					break;
				case BOX:
					objectType = ObjectType.RAY;
					break;
				}
			}
		}
	}

	private PhysicsComponent createRect(final Vec2D center, final float size) {
		final PhysicsComponent ob = new PhysicsComponent();
		ob.setMaterial(Material.STEEL);
		ob.shape = new RectShape(center.minus(new Vec2D(size / 2, size / 2)), center.plus(new Vec2D(size / 2, size / 2)));
		ob.setMass(size * size);
		return ob;
	}

	private PhysicsComponent createBall(final Vec2D center, final float radius) {
		final PhysicsComponent ob = new PhysicsComponent();

		ob.setMaterial(Material.STEEL);
		ob.shape = new CircleShape(center, radius);
		ob.setMass(radius * radius * AngleUtils.PI);
		return ob;
	}

	@Override
	public void draw(final Graphics2D g) {
		systemManager.broadcast(new RenderMessage(g));
		g.setColor(Color.BLACK);
		g.drawString("Press T to toggle object type. Current type: " + objectType.name(), 200, 10);
		for (final Ray r : rays) {
			boolean found = false;
			float minDist = Float.MAX_VALUE;
			Vec2D minPoint = null;
			for (final GameEntity e : entities.getEntities()) {
				final RaycastResult result = new RaycastResult();
				if (RaycastDetector.raycast(r, RaycastDetector.INFINITE_LENGTH,
						((PhysicsComponent) e.getComponent(PhysicsComponent.COMPONENT_ID)).shape, result)) {
					if (minDist > result.getDistance()) {
						minDist = (float) result.getDistance();
						minPoint = result.getPoint();
					}
					found = true;
				}
			}
			if (found) {
				g.setColor(Color.BLUE);
				g.drawLine((int) r.getStart().x, (int) r.getStart().y, (int) (r.getStart().x + r.getDirection().x * minDist),
						(int) (r.getStart().y + r.getDirection().y * minDist));
				g.setColor(Color.RED);
				g.drawLine((int) (r.getStart().x + r.getDirection().x * minDist), (int) (r.getStart().y + r.getDirection().y * minDist),
						(int) (r.getStart().x + r.getDirection().x * 2000), (int) (r.getStart().y + r.getDirection().y * 2000));
				g.setColor(Color.GREEN);
				final int rad = 5;
				g.fillOval((int) (minPoint.x - rad), (int) (minPoint.y - rad), rad * 2, rad * 2);
			} else {
				g.setColor(Color.BLUE);
				g.drawLine((int) r.getStart().x, (int) r.getStart().y, (int) (r.getStart().x + r.getDirection().x * 2000),
						(int) (r.getStart().y + r.getDirection().y * 2000));
			}
		}
		if (objectType == ObjectType.RAY && start.isPresent()) {
			g.setColor(Color.BLUE);
			g.drawLine(start.get().x, start.get().y, getMousePosition().x, getMousePosition().y);
		}
	}

	@Override
	public void update(final float dt) {
		systemManager.broadcast(new UpdateMessage(dt));
	}

	private static enum ObjectType {
		RAY,
		BALL,
		BOX;
	}

}
