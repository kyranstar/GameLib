package physics;

import java.awt.Rectangle;

import org.junit.Assert;
import org.junit.Test;

import game.WorldMock;
import game.entity.GameEntity;
import game.messaging.CreateEntityMessage;
import game.messaging.GameSystemManager;
import math.Vec2D;
import physics.collision.CollisionFilter;
import physics.collision.shape.RectShape;

public class PhysicsSystemTest {
	@Test
	public void testCreatePhysicsSystem() {
		final GameSystemManager manager = new GameSystemManager();
		final Rectangle bounds = new Rectangle(0, 0, 10, 10);
		final PhysicsSystem system = new PhysicsSystem(manager, bounds);
		manager.addSystem(system);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddEntityMissingMaterial() {
		final WorldMock world = new WorldMock();
		final GameSystemManager manager = world.getSystemManager();
		manager.addSystem(new PhysicsSystem(manager, new Rectangle(0, 0, 100, 100)));

		Assert.assertEquals(0, world.getEntityCount());

		final GameEntity entity = new GameEntity();

		final CollisionComponent e = new CollisionComponent();
		e.setPos(new Vec2D(0.5f, 0.5f));
		e.setShape((new RectShape(-0.5f, 0.5f, 0.5f, 0.5f)));
		e.collisionFilter = new CollisionFilter(0, 0, 0);
 		e.setMass(1);

		entity.addComponent(e);

		manager.broadcast(new CreateEntityMessage(entity));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddEntityMissingShape() {
		final WorldMock world = new WorldMock();
		final GameSystemManager manager = world.getSystemManager();
		manager.addSystem(new PhysicsSystem(manager, new Rectangle(0, 0, 100, 100)));

		Assert.assertEquals(0, world.getEntityCount());

		final GameEntity entity = new GameEntity();

		final CollisionComponent e = new CollisionComponent();
		e.collisionFilter = new CollisionFilter(0, 0, 0);
		e.setMass(1);
		e.setMaterial(Material.STEEL);

		entity.addComponent(e);

		manager.broadcast(new CreateEntityMessage(entity));
	}

}
