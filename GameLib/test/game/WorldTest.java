package game;

import game.messaging.CreateEntityMessage;
import game.messaging.GameSystemManager;

import org.junit.Assert;
import org.junit.Test;

import physics.Material;
import physics.PhysicsComponent;
import physics.collision.CollisionFilter;
import physics.collision.shape.RectShape;

public class WorldTest {

	@Test
	public void testWorldCreation() {
		final WorldMock world = new WorldMock();
		Assert.assertNotEquals(null, world.getSystemManager());
	}

	@Test
	public void testAddValidEntity() {
		final WorldMock world = new WorldMock();
		final GameSystemManager manager = world.getSystemManager();

		Assert.assertEquals(0, world.getEntityCount());

		final PhysicsComponent e = new PhysicsComponent();
		e.shape = new RectShape(0, 0, 1, 1);
		e.collisionFilter = new CollisionFilter(0, 0, 0);
		e.setMass(1);
		e.setRotationalInertia(1);
		e.setMaterial(Material.STEEL);

		manager.broadcastMessage(new CreateEntityMessage(e));
		Assert.assertEquals(1, world.getEntityCount());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddEntityMissingMaterial() {
		final WorldMock world = new WorldMock();
		final GameSystemManager manager = world.getSystemManager();

		Assert.assertEquals(0, world.getEntityCount());

		final PhysicsComponent e = new PhysicsComponent();
		e.shape = new RectShape(0, 0, 1, 1);
		e.collisionFilter = new CollisionFilter(0, 0, 0);
		e.setMass(1);
		e.setRotationalInertia(1);

		manager.broadcastMessage(new CreateEntityMessage(e));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddEntityMissingShape() {
		final WorldMock world = new WorldMock();
		final GameSystemManager manager = world.getSystemManager();

		Assert.assertEquals(0, world.getEntityCount());

		final PhysicsComponent e = new PhysicsComponent();
		e.collisionFilter = new CollisionFilter(0, 0, 0);
		e.setMass(1);
		e.setRotationalInertia(1);
		e.setMaterial(Material.STEEL);

		manager.broadcastMessage(new CreateEntityMessage(e));
	}

}
