package physics;

import org.junit.Assert;
import org.junit.Test;

import test.TestConstants;

public class PhysicsComponentTest {

	@Test
	public void testCreation() {
		final PhysicsComponent e = new PhysicsComponent();
		Assert.assertEquals(null, e.shape);
		Assert.assertEquals(false, e.sleeping);
		Assert.assertEquals(0, e.framesStill);
	}

	@Test
	public void testMass() {
		final PhysicsComponent e = new PhysicsComponent();

		e.setMass(10);
		Assert.assertEquals(1 / 10f, e.getInvMass(), TestConstants.EPSILON);
		Assert.assertEquals(10f, e.getMass(), TestConstants.EPSILON);

		e.setMass(PhysicsComponent.INFINITE_MASS);
		Assert.assertEquals(0, e.getInvMass(), TestConstants.EPSILON);
		Assert.assertEquals(PhysicsComponent.INFINITE_MASS, e.getMass(), TestConstants.EPSILON);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidMass() {
		final PhysicsComponent e = new PhysicsComponent();
		e.setMass(-1);
	}

	@Test(expected = NullPointerException.class)
	public void testInvalidVelocity() {
		final PhysicsComponent e = new PhysicsComponent();
		e.setVelocity(null);
	}

	@Test(expected = NullPointerException.class)
	public void testInvalidMaterial() {
		final PhysicsComponent e = new PhysicsComponent();
		e.setMaterial(null);
	}

}
