package test;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import draw.RenderSystem;
import game.World;
import game.entity.GameEntity;
import game.messaging.CreateConstraintMessage;
import game.messaging.CreateEntityMessage;
import game.messaging.RenderMessage;
import game.messaging.UpdateMessage;
import math.MathUtils;
import math.Vec2D;
import physics.Material;
import physics.CollisionComponent;
import physics.PhysicsSystem;
import physics.collision.shape.CircleShape;
import physics.constraints.Constraint;
import physics.constraints.StringJoint;

public class Test extends World {

	final static Logger logger = LoggerFactory.getLogger(Test.class);

	public Test(final Dimension bounds) {
		super(60, 120, bounds);
		systemManager.addSystem(new RenderSystem(systemManager, this));
		systemManager.addSystem(new PhysicsSystem(systemManager, new Rectangle(0, 0, bounds.width, bounds.height)));

		rope();
	}

	private void rope() {
		final List<CollisionComponent> entities = new ArrayList<>();
		final int parts = 20;
		final int rad = 5;

		for (int i = 0; i < parts; i++) {
			final CollisionComponent part = new CollisionComponent();
			part.setPos(new Vec2D(i * (rad * 2 + 5) + 200, 100));
			part.setShape(new CircleShape(rad));
			part.setMass(rad * rad * MathUtils.PI);
			part.setMaterial(Material.STEEL);
			if (i != 0) {
				addConstraint(new StringJoint(part, entities.get(entities.size() - 1)));
			}
			if (i == 0) {
				part.setMass(CollisionComponent.INFINITE_MASS);
			}
			entities.add(part);
			addEntity(part);
		}
	}

	private void addEntity(final CollisionComponent pc) {
		final GameEntity e = new GameEntity();
		e.addComponent(pc);
		systemManager.broadcast(new CreateEntityMessage(e));
	}

	private void addConstraint(final Constraint c) {
		systemManager.broadcast(new CreateConstraintMessage(c));
	}

	private CollisionComponent createBall(final Vec2D center, final int radius) {
		final CollisionComponent ob = new CollisionComponent();

		ob.setMaterial(Material.STEEL);
		ob.setPos(center);
		ob.setShape(new CircleShape(radius));
		ob.setMass(radius * radius);
		return ob;
	}

	public static void main(final String[] args) throws HeadlessException, InvocationTargetException, InterruptedException {
		logger.info("Application started");
		new Test(new Dimension(1000, 1000)).createFrame().run();

	}

	@Override
	public void processInput(final Queue<EventPair<KeyEvent, KeyEventType>> keyEvents,
			final Queue<EventPair<MouseEvent, MouseEventType>> mouseEvents, final Queue<MouseWheelEvent> mouseWheelEvents2) {
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
	@Override
	public void update(final float dt) {
		systemManager.broadcast(new UpdateMessage(dt));
	}

}
