package physics;

import game.WorldMock;
import game.entity.GameEntity;
import game.messaging.CreateEntityMessage;
import game.messaging.GameSystemManager;

import java.awt.Rectangle;

import org.junit.Assert;
import org.junit.Test;

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

		final PhysicsComponent e = new PhysicsComponent();
		e.shape = new RectShape(0, 0, 1, 1);
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

		final PhysicsComponent e = new PhysicsComponent();
		e.collisionFilter = new CollisionFilter(0, 0, 0);
		e.setMass(1);
		e.setMaterial(Material.STEEL);

		entity.addComponent(e);

		manager.broadcast(new CreateEntityMessage(entity));
	}

}
