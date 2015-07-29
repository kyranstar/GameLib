package game;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import draw.DrawingPanel;
import physics.GameEntity;
import physics.Joint;
import physics.collision.Collisions;
import physics.collision.quadtree.Quadtree;

public abstract class World extends DrawingPanel {

	private static final Vec2D GRAVITY = new Vec2D(0, 980f);
	// the number of times to run collision detection and response per frame
	private static final int COLLISION_ITERATIONS = 10;
	private final Rectangle bounds;

	protected final List<GameEntity> objects = new ArrayList<>();
	protected final List<Joint> joints = new ArrayList<>();
	private final Quadtree quadtree;

	public World(int fps, int ups, JPanel panel) {
		super(fps, ups, panel, Color.WHITE);
		quadtree = new Quadtree(panel.getBounds());
		bounds = panel.getBounds();
	}

	@Override
	public void update(float dt) {
		for (int i = 0; i < COLLISION_ITERATIONS; i++) {
			handleCollisions(dt / COLLISION_ITERATIONS);
		}

		if (GameEntity.SLEEPING_ENABLED) {
			for (final GameEntity e : objects) {
				checkSleep(e);
			}
		}
		updateWorld(dt);
	}

	public abstract void updateWorld(float dt);

	private void handleCollisions(final float dt) {
		final Vec2D gravity = GRAVITY.multiply(dt);
		// create quadtree
		quadtree.clear();
		for (int i = 0; i < objects.size(); i++) {
			quadtree.insert(objects.get(i));
		}

		// possible objects to collide with each object
		final List<GameEntity> collidableObjects = new ArrayList<>();
		for (int i = 0; i < objects.size(); i++) {
			final GameEntity a = objects.get(i);
			a.checkedCollisionThisTick.clear();

			collidableObjects.clear();
			quadtree.retrieve(collidableObjects, a);

			if (!a.sleeping) {
				// remove if out of map
				if (a.center().y > bounds.getHeight()) {
					objects.remove(i);
					i--;
					continue;
				}

				if (a.getMass() != GameEntity.INFINITE_MASS) {
					a.applyForce(gravity.divide(a.getInvMass()));
				}
			}
			// check collisions
			for (final GameEntity b : collidableObjects) {
				if (b.checkedCollisionThisTick.contains(a) || a.checkedCollisionThisTick.contains(b)) {
					// don't want to check collisions both ways
					continue;
				}

				if (b.sleeping && a.sleeping) {
					continue;
				}

				if (Collisions.isColliding(a, b)) {
					Collisions.fixCollision(a, b);
				}
				a.checkedCollisionThisTick.add(b);
				b.checkedCollisionThisTick.add(a);
			}
		}
		for (final Joint j : joints) {
			j.update();
		}
		for (final GameEntity e : objects) {
			e.update(dt);
		}
	}

	private static void checkSleep(final GameEntity a) {
		if (a.getVelocity().x < GameEntity.SLEEP_THRESHOLD && a.getVelocity().x > -GameEntity.SLEEP_THRESHOLD //
				&& a.getVelocity().y < GameEntity.SLEEP_THRESHOLD && a.getVelocity().y > -GameEntity.SLEEP_THRESHOLD) {
			if (a.framesStill > GameEntity.FRAMES_STILL_TO_SLEEP) {
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

}
