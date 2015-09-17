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
		new CircleShape(1);
	}
	@Test(expected = IllegalArgumentException.class)
	public void testFailedCreation2() {
		new CircleShape(0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFailedCreation3() {
		new CircleShape(Float.NaN);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFailedCreation4() {
		new CircleShape(Float.POSITIVE_INFINITY);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFailedCreation5() {
		new CircleShape(Float.NEGATIVE_INFINITY);
	}
	@Test
	public void testGetRadius() {
		Assert.assertEquals(1, new CircleShape(1).getRadius(), TestConstants.EPSILON);
	}

	@Test
	public void testGetBoundingBox() {
		Assert.assertEquals(new Rectangle2D.Float(-1, -1, 2, 2), new CircleShape(1).getRect());
	}

}
