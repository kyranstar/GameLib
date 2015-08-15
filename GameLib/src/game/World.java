package game;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import physics.PhysicsEntity;
import physics.collision.Collisions;
import physics.collision.quadtree.Quadtree;
import physics.constraints.Constraint;
import draw.DrawingPanel;

public abstract class World extends DrawingPanel {

	public final float PIXELS_PER_METER = 50f;
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
	protected int collisionChecksThisTick;
	protected int collisionSolvesThisTick;

	public World(final int fps, final int ups, final JPanel panel) {
		super(fps, ups, panel, Color.WHITE);
		quadtree = new Quadtree(panel.getBounds());
		bounds = panel.getBounds();
	}

	public World(final int ups, final JPanel panel) {
		super(ups, panel, Color.WHITE);

		quadtree = new Quadtree(panel.getBounds());
		bounds = panel.getBounds();
	}

	@Override
	public void update(final float dt) {
		for (int i = 0; i < COLLISION_ITERATIONS; i++) {
			handleCollisions(dt / COLLISION_ITERATIONS);
		}

		updateWorld(dt);
	}

	public abstract void updateWorld(float dt);

	private void handleCollisions(final float dt) {
		collisionChecksThisTick = collisionSolvesThisTick = 0;

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
			}
			if (!bounds.contains(a.center().toPoint())) {
				// entities.remove(i--);
				continue;
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

	public int getHeight() {
		return bounds.height;
	}

	public int getWidth() {
		return bounds.width;
	}

}
