package physics;

import org.junit.Assert;
import org.junit.Test;

import test.TestConstants;

public class PhysicsComponentTest {

	@Test
	public void testCreation() {
		final CollisionComponent e = new CollisionComponent();
		Assert.assertEquals(null, e.getShape());
		Assert.assertEquals(false, e.sleeping);
		Assert.assertEquals(0, e.framesStill);
	}

	@Test
	public void testMass() {
		final CollisionComponent e = new CollisionComponent();

		e.setMass(10);
		Assert.assertEquals(1 / 10f, e.getInvMass(), TestConstants.EPSILON);
		Assert.assertEquals(10f, e.getMass(), TestConstants.EPSILON);

		e.setMass(CollisionComponent.INFINITE_MASS);
		Assert.assertEquals(0, e.getInvMass(), TestConstants.EPSILON);
		Assert.assertEquals(CollisionComponent.INFINITE_MASS, e.getMass(), TestConstants.EPSILON);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidMass() {
		final CollisionComponent e = new CollisionComponent();
		e.setMass(-1);
	}

	@Test(expected = NullPointerException.class)
	public void testInvalidVelocity() {
		final CollisionComponent e = new CollisionComponent();
		e.setVelocity(null);
	}

	@Test(expected = NullPointerException.class)
	public void testInvalidMaterial() {
		final CollisionComponent e = new CollisionComponent();
		e.setMaterial(null);
	}

}
