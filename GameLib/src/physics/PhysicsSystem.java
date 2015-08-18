package physics;

import game.GameSystem;
import game.messaging.DebugMessage;
import game.messaging.DebugMessage.InfoType;
import game.messaging.EntityCreatedMessage;
import game.messaging.GameSystemManager;
import game.messaging.Message;
import game.messaging.UpdateMessage;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import math.Vec2D;
import physics.collision.handling.Collisions;
import physics.collision.quadtree.Quadtree;
import physics.constraints.Constraint;

public class PhysicsSystem extends GameSystem {

	public final static float PIXELS_PER_METER = 50f;
	/**
	 * The velocity the object has to be below to be considered still
	 */
	public final float SLEEP_THRESHOLD = 1f;
	/**
	 * The consecutive amount of frames the object has to be still to be considered sleeping
	 */
	public final int FRAMES_STILL_TO_SLEEP = 25;
	public final boolean SLEEPING_ENABLED = true;
	public final float AIR_FRICTION = 25;

	// meters per second
	protected Vec2D gravity = new Vec2D(0, 9.8f);
	// the number of times to run collision detection and response per frame
	private static final int COLLISION_ITERATIONS = 3;
	private final Rectangle bounds;

	protected final List<PhysicsEntity> entities = new ArrayList<>();
	protected final List<Constraint> constraints = new ArrayList<>();
	protected final Quadtree quadtree;

	// debug
	public int collisionChecksThisTick;
	public int collisionSolvesThisTick;

	public PhysicsSystem(final GameSystemManager m, final Rectangle bounds) {
		super(m);
		quadtree = new Quadtree(bounds);
		this.bounds = bounds;
	}

	public void update(final float dt) {
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
		final List<PhysicsEntity> collidableObjects = new ArrayList<>();
		for (int i = 0; i < entities.size(); i++) {
			final PhysicsEntity a = entities.get(i);

			collidableObjects.clear();
			quadtree.retrieve(collidableObjects, a);

			if (!a.sleeping && a.getMass() != PhysicsEntity.INFINITE_MASS) {
				a.applyForce(gravityForce.divide(a.getInvMass()));
				a.applyForce(a.getVelocity().multiply(-AIR_FRICTION * dt));
			}

			// check collisions
			for (final PhysicsEntity b : collidableObjects) {
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

					Collisions.fixCollision(a, b);
					a.sleeping = b.sleeping = false;
				}
				a.checkedCollisionThisTick.add(b);
				b.checkedCollisionThisTick.add(a);
			}
		}
		for (final Constraint c : constraints) {
			c.update();
		}
		for (final PhysicsEntity e : entities) {
			e.update(dt);
			e.checkedCollisionThisTick.clear();

			if (SLEEPING_ENABLED) {
				checkSleep(e);
			}
		}
		systemManager.broadcastMessage(new DebugMessage<Integer>(InfoType.COLLISION_CHECKS_THIS_TICK, collisionChecksThisTick));
		systemManager.broadcastMessage(new DebugMessage<Integer>(InfoType.COLLISION_SOLVES_THIS_TICK, collisionSolvesThisTick));
	}

	private void checkSleep(final PhysicsEntity a) {
		if (a.getVelocity().x < SLEEP_THRESHOLD && a.getVelocity().x > -SLEEP_THRESHOLD //
				&& a.getVelocity().y < SLEEP_THRESHOLD && a.getVelocity().y > -SLEEP_THRESHOLD) {
			if (a.framesStill > FRAMES_STILL_TO_SLEEP) {
				a.sleeping = true;
				a.setVelocity(Vec2D.ZERO);
			} else {
				a.framesStill++;
			}
		} else {
			a.sleeping = false;
			a.framesStill = 0;
		}
	}

	private void addEntity(final PhysicsEntity e) {
		entities.add(e);
	}

	public void addConstraint(final Constraint c) {
		constraints.add(c);
	}

	@Override
	public void recieveMessage(final Message message) {
		if (message instanceof EntityCreatedMessage) {
			addEntity(((EntityCreatedMessage) message).entity);
		} else if (message instanceof UpdateMessage) {
			update(((UpdateMessage) message).dt);
		}
	}

}
