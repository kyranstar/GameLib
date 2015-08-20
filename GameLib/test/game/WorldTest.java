package game;

import game.entity.GameEntity;
import game.messaging.CreateEntityMessage;
import game.messaging.GameSystemManager;

import org.junit.Assert;
import org.junit.Test;

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

		final GameEntity e = new GameEntity();

		manager.broadcast(new CreateEntityMessage(e));
		Assert.assertEquals(1, world.getEntityCount());
	}
}
