package physics;

import org.junit.Assert;
import org.junit.Test;

public class EntityTest {

	private static final float EPSILON = 0.0000001f;

	@Test
	public void testCreation() {
		final WorldMock world = new WorldMock();
		final PhysicsEntity e = new PhysicsEntity(world);
		Assert.assertEquals(null, e.shape);
		Assert.assertEquals(false, e.sleeping);
		Assert.assertEquals(0, e.framesStill);
	}

	@Test
	public void testMass() {
		final WorldMock world = new WorldMock();
		final PhysicsEntity e = new PhysicsEntity(world);

		e.setMass(10);
		Assert.assertEquals(1 / 10f, e.getInvMass(), EPSILON);
		Assert.assertEquals(10f, e.getMass(), EPSILON);

		e.setMass(PhysicsEntity.INFINITE_MASS);
		Assert.assertEquals(0, e.getInvMass(), EPSILON);
		Assert.assertEquals(PhysicsEntity.INFINITE_MASS, e.getMass(), EPSILON);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidMass() {
		final WorldMock world = new WorldMock();
		final PhysicsEntity e = new PhysicsEntity(world);

		e.setMass(-1);
	}

}
