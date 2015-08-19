package physics.collision;

import org.junit.Assert;
import org.junit.Test;

public class CollisionFilterTest {
	@Test
	public void test1() {
		final CollisionFilter a = new CollisionFilter(0b0001, 0b0010, 0b0010);
		final CollisionFilter b = new CollisionFilter(0b0010, 0b0001, 0b0001);

		Assert.assertTrue(a.shouldCollide(b));
		Assert.assertTrue(a.shouldPhysicsRespond(b));
		Assert.assertTrue(b.shouldCollide(a));
		Assert.assertTrue(b.shouldPhysicsRespond(a));
	}

	@Test
	public void test2() {
		final CollisionFilter a = new CollisionFilter(0b0001, 0b0010, 0b0010);
		final CollisionFilter b = new CollisionFilter(0b0010, 0b0001, 0b0001);
		final CollisionFilter c = new CollisionFilter(0b0100, 0b0011, 0b0011);

		Assert.assertTrue(!a.shouldCollide(c));
		Assert.assertTrue(!a.shouldPhysicsRespond(c));

		Assert.assertTrue(!b.shouldCollide(c));
		Assert.assertTrue(!b.shouldPhysicsRespond(c));

		Assert.assertTrue(c.shouldCollide(b));
		Assert.assertTrue(c.shouldPhysicsRespond(b));

		Assert.assertTrue(c.shouldCollide(b));
		Assert.assertTrue(c.shouldPhysicsRespond(b));
	}

}
