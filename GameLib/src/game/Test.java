package game;

import game.entity.GameEntity;
import game.messaging.CreateConstraintMessage;
import game.messaging.CreateEntityMessage;
import game.messaging.RenderMessage;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import math.AngleUtils;
import math.Vec2D;
import physics.Material;
import physics.PhysicsComponent;
import physics.PhysicsSystem;
import physics.collision.CollisionFilter;
import physics.collision.shape.CircleShape;
import physics.collision.shape.RectShape;
import physics.constraints.AngleJoint;
import physics.constraints.Constraint;
import physics.constraints.DistanceJoint;
import physics.constraints.SpringPointConstraint;
import draw.RenderSystem;

public class Test extends World {

	public Test(final Dimension bounds) {
		super(60, 120, bounds);
		systemManager.addSystem(new RenderSystem(systemManager, this));
		systemManager.addSystem(new PhysicsSystem(systemManager, new Rectangle(0, 0, bounds.width, bounds.height)));

		ball();
		// collisionFilteringTest();
	}

	private void collisionFilteringTest() {
		final int staticGeom = 1 << 0;

		final PhysicsComponent ob = new PhysicsComponent();
		ob.setMaterial(Material.STEEL);
		ob.shape = new RectShape(new Vec2D(0, 900), new Vec2D(1000, 1000));
		ob.setMass(PhysicsComponent.INFINITE_MASS);
		ob.collisionFilter = new CollisionFilter(staticGeom, 0, 0);
		addEntity(ob);

		for (int i = 1; i <= 5; i++) {
			final PhysicsComponent ob2 = new PhysicsComponent();
			ob2.setMaterial(Material.STEEL);
			ob2.shape = new RectShape(new Vec2D(i * 100 + 201, 600), new Vec2D(i * 100 + 250, 700));
			ob2.setMass(100 * 100);
			final int filter = 1 << i | staticGeom;
			ob2.collisionFilter = new CollisionFilter(1 << i, filter, filter);
			addEntity(ob2);
		}
		for (int i = 1; i <= 5; i++) {
			final PhysicsComponent ob2 = new PhysicsComponent();
			ob2.setMaterial(Material.STEEL);
			ob2.shape = new CircleShape(new Vec2D(i * 100 + 200, 400), 20);
			ob2.setMass(AngleUtils.TWO_PI * 20);
			final int filter = 1 << i | staticGeom;
			ob2.collisionFilter = new CollisionFilter(1 << i, filter, filter);
			addEntity(ob2);
		}
	}

	private void waves() {

		final List<PhysicsComponent> entities = new ArrayList<>();
		final int parts = 30;

		for (int i = 0; i < parts; i++) {
			final int size = getWidth() / parts;
			final PhysicsComponent part = new PhysicsComponent();
			part.shape = new RectShape(new Vec2D(i * size, 500), new Vec2D((i + 1) * size - size / 5, 500 + size / 2));
			part.setMass(size * size / 100f);
			part.setMaterial(Material.STEEL);
			addConstraint(new SpringPointConstraint(part, new Vec2D(i * size + size / 2, 900), 0.1f));
			if (i != 0) {
				addConstraint(new DistanceJoint(part, entities.get(entities.size() - 1)));
			}
			if (i == 0 || i == parts - 1) {
				part.setMass(PhysicsComponent.INFINITE_MASS);
			}

			addEntity(part);
		}
	}

	private void ball() {
		final PhysicsComponent ob = new PhysicsComponent();
		ob.setMaterial(Material.STEEL);
		ob.shape = new RectShape(new Vec2D(0, 900), new Vec2D(1000, 1000));
		ob.setMass(PhysicsComponent.INFINITE_MASS);
		addEntity(ob);

		final Vec2D centerV = new Vec2D(500, 700);

		final PhysicsComponent center = createBall(centerV, 75);
		center.setMaterial(Material.RUBBER);
		center.setMass(PhysicsComponent.INFINITE_MASS);
		addEntity(center);

		final float vertices = 24;
		final float dist = 120;

		PhysicsComponent first = null;
		PhysicsComponent last = null;
		for (int i = 0; i < vertices; i++) {
			final float angle = (float) (2 * Math.PI / vertices * i);
			final Vec2D newCenter = new Vec2D((float) (centerV.x + Math.cos(angle) * dist), (float) (centerV.y + Math.sin(angle) * dist));
			final PhysicsComponent vertex = createBall(newCenter, 10);
			addEntity(vertex);
			if (last != null) {
				// addConstraint(new SpringJoint(last, vertex, 0.001f));
			} else {
				first = vertex;
			}
			addConstraint(new DistanceJoint(center, vertex));
			addConstraint(new AngleJoint(center, vertex, angle, 0.15f));
			last = vertex;
			if (i == vertices - 1 && first != null) {
				// addConstraint(new SpringJoint(first, vertex, 0.001f));
			}
		}
	}

	private void addEntity(final PhysicsComponent pc) {
		final GameEntity e = new GameEntity();
		e.addComponent(pc);
		systemManager.broadcast(new CreateEntityMessage(e));
	}

	private void addConstraint(final Constraint c) {
		systemManager.broadcast(new CreateConstraintMessage(c));
	}

	private PhysicsComponent createBall(final Vec2D center, final int radius) {
		final PhysicsComponent ob = new PhysicsComponent();

		ob.setMaterial(Material.STEEL);
		ob.shape = new CircleShape(center, radius);
		ob.setMass(radius * radius);
		ob.setRotationalInertia(1);
		return ob;
	}

	public static void main(final String[] args) throws HeadlessException, InvocationTargetException, InterruptedException {
		new Test(new Dimension(1000, 1000)).createFrame().run();

	}

	@Override
	public void processInput(final Queue<KeyEvent> keyEvents, final Queue<EventPair<MouseEvent, MouseEventType>> mouseEvents,
			final Queue<MouseWheelEvent> mouseWheelEvents2) {
		for (final EventPair<MouseEvent, MouseEventType> e : mouseEvents) {
			if (e.type != MouseEventType.CLICK) {
				continue;
			}
			addEntity(createBall(new Vec2D(e.event.getX(), e.event.getY()), 10));
		}
	}

	@Override
	public void draw(final Graphics2D g) {
		systemManager.broadcast(new RenderMessage(g));
	}

	int i = 0;

	@Override
	public void updateWorld(final float dt) {
		if (i % 40 == 0) {
			final PhysicsComponent e = createBall(new Vec2D(501, 0), 10);

			addEntity(e);
		}
		i++;
	}

}
