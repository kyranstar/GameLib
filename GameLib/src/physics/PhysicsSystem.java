package physics;

import game.GameSystem;
import game.messaging.CollisionMessage;
import game.messaging.CreateConstraintMessage;
import game.messaging.CreateEntityMessage;
import game.messaging.DebugMessage;
import game.messaging.DebugMessage.InfoType;
import game.messaging.GameSystemManager;
import game.messaging.Message;
import game.messaging.UpdateMessage;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import math.Vec2D;
import physics.collision.Quadtree;
import physics.collision.handling.CManifold;
import physics.collision.handling.Collisions;
import physics.constraints.Constraint;

public class PhysicsSystem extends GameSystem {

	public static final float PIXELS_PER_METER = 50f;
	/**
	 * The velocity the object has to be below to be considered still
	 */
	public static final float SLEEP_THRESHOLD = 1f;
	/**
	 * The consecutive amount of frames the object has to be still to be considered sleeping
	 */
	public static final int FRAMES_STILL_TO_SLEEP = 50;
	public static final boolean SLEEPING_ENABLED = true;
	public static final float AIR_FRICTION = 25;

	// meters per second
	protected Vec2D gravity = new Vec2D(0, 9.8f);
	// the number of times to run collision detection and response per frame
	private static final int COLLISION_ITERATIONS = 10;

	protected final List<PhysicsComponent> entities = new ArrayList<>();
	protected final List<Constraint> constraints = new ArrayList<>();
	protected final Quadtree quadtree;

	public PhysicsSystem(final GameSystemManager m, final Rectangle bounds) {
		super(m);
		assert bounds.width >= 0;
		assert bounds.height >= 0;
		quadtree = new Quadtree(bounds);
	}

	private void update(final float dt) {
		for (int i = 0; i < COLLISION_ITERATIONS; i++) {
			handleCollisions(dt / COLLISION_ITERATIONS);
		}
	}

	private void handleCollisions(final float dt) {
		int collisionChecksThisTick = 0;
		int collisionSolvesThisTick = 0;

		final Vec2D gravityForce = gravity.multiply(dt * PIXELS_PER_METER);
		// create quadtree
		quadtree.clear();
		for (int i = 0; i < entities.size(); i++) {
			quadtree.insert(entities.get(i));
		}

		// possible objects to collide with each object
		final List<PhysicsComponent> collidableObjects = new ArrayList<>();
		for (int i = 0; i < entities.size(); i++) {
			final PhysicsComponent a = entities.get(i);

			collidableObjects.clear();
			quadtree.retrieve(collidableObjects, a);

			if (!a.sleeping && a.getMass() != PhysicsComponent.INFINITE_MASS) {
				a.applyForce(gravityForce.divide(a.getInvMass()));
				a.applyForce(a.getVelocity().multiply(-AIR_FRICTION * dt));
			}

			// check collisions
			for (final PhysicsComponent b : collidableObjects) {
				if (b.checkedCollisionThisTick.contains(a) || a.checkedCollisionThisTick.contains(b)) {
					// don't want to check collisions both ways
					continue;
				}

				if (b.sleeping && a.sleeping) {
					continue;
				}
				collisionChecksThisTick++;
				if (Collisions.isColliding(a, b)) {
					collisionSolvesThisTick++;

					final CManifold m = Collisions.fixCollision(a, b);
					systemManager.broadcast(new CollisionMessage(m));
				}
				a.checkedCollisionThisTick.add(b);
				b.checkedCollisionThisTick.add(a);
			}
		}
		for (final Constraint c : constraints) {
			c.update();
		}
		for (final PhysicsComponent e : entities) {
			if (!e.sleeping) {
				e.update(dt);
			}
			e.checkedCollisionThisTick.clear();

			if (SLEEPING_ENABLED) {
				checkSleep(e);
			}
		}
		systemManager.broadcast(new DebugMessage<Integer>(InfoType.COLLISION_CHECKS_THIS_TICK, collisionChecksThisTick));
		systemManager.broadcast(new DebugMessage<Integer>(InfoType.COLLISION_SOLVES_THIS_TICK, collisionSolvesThisTick));
	}

	private void checkSleep(final PhysicsComponent a) {
		if (a.getVelocity().x < SLEEP_THRESHOLD && a.getVelocity().x > -SLEEP_THRESHOLD //
				&& a.getVelocity().y < SLEEP_THRESHOLD && a.getVelocity().y > -SLEEP_THRESHOLD) {
			if (a.sleeping) {
				return;
			}
			if (a.framesStill > FRAMES_STILL_TO_SLEEP) {
				a.sleeping = true;
				a.framesStill = 0;
				a.setVelocity(Vec2D.ZERO);
			} else {
				a.framesStill++;
			}
		} else {
			a.sleeping = false;
			a.framesStill = 0;
		}
	}

	private final Set<Class<? extends Message>> acceptedMessages = new HashSet<>();
	{
		acceptedMessages.add(CreateEntityMessage.class);
		acceptedMessages.add(CreateConstraintMessage.class);
		acceptedMessages.add(UpdateMessage.class);
	}

	@Override
	public void recieveMessage(final Message message) {
		if (message instanceof CreateEntityMessage) {
			handleCreateEntity((CreateEntityMessage) message);
		} else if (message instanceof CreateConstraintMessage) {
			final Constraint c = ((CreateConstraintMessage) message).constraint;
			constraints.add(c);
		} else if (message instanceof UpdateMessage) {
			update(((UpdateMessage) message).dt);
		}
	}

	private void handleCreateEntity(final CreateEntityMessage message) {
		// if it doesn't have a physics component
		if (message.entity.getComponent(PhysicsComponent.COMPONENT_ID) == null) {
			return;
		}

		final PhysicsComponent e = (PhysicsComponent) message.entity.getComponent(PhysicsComponent.COMPONENT_ID);
		if (!e.isFullyConstructed()) {
			throw new IllegalArgumentException("Entity must be full constructed! Missing attributes: " + e.getMissingAttributes());
		}
		entities.add(e);
	}

	@Override
	public Set<Class<? extends Message>> getAcceptedMessages() {
		return acceptedMessages;
	}

}
