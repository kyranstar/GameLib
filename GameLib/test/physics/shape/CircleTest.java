package physics.shape;

import java.awt.geom.Rectangle2D;

import math.Vec2D;

import org.junit.Assert;
import org.junit.Test;

import physics.collision.shape.CircleShape;
import test.TestConstants;

public class CircleTest {

	@Test
	public void testCreation() {
		new CircleShape(new Vec2D(0, 0), 1);
	}

	@Test(expected = NullPointerException.class)
	public void testFailedCreation1() {
		new CircleShape(null, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFailedCreation2() {
		new CircleShape(new Vec2D(0, 0), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFailedCreation3() {
		new CircleShape(new Vec2D(0, 0), Float.NaN);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFailedCreation4() {
		new CircleShape(new Vec2D(0, 0), Float.POSITIVE_INFINITY);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFailedCreation5() {
		new CircleShape(new Vec2D(0, 0), Float.NEGATIVE_INFINITY);
	}

	@Test
	public void testGetCenter() {
		Assert.assertEquals(new Vec2D(0, 0), new CircleShape(new Vec2D(0, 0), 1).center());
	}

	@Test
	public void testGetRadius() {
		Assert.assertEquals(1, new CircleShape(new Vec2D(0, 0), 1).getRadius(), TestConstants.EPSILON);
	}

	@Test
	public void testGetBoundingBox() {
		Assert.assertEquals(new Rectangle2D.Float(-1, -1, 2, 2), new CircleShape(new Vec2D(0, 0), 1).getRect());
		Assert.assertEquals(new Rectangle2D.Float(0, 0, 2, 2), new CircleShape(new Vec2D(1, 1), 1).getRect());
	}

	@Test
	public void testMoveRelative() {
		final CircleShape s = new CircleShape(new Vec2D(0, 0), 1);
		s.moveRelative(new Vec2D(1, 1));
		Assert.assertEquals(new Vec2D(1, 1), s.center());
		s.moveRelative(new Vec2D(-2, 0));
		Assert.assertEquals(new Vec2D(-1, 1), s.center());
	}

}
