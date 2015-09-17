package physics.shape;

import java.awt.geom.Rectangle2D;

import math.Vec2D;

import org.junit.Assert;
import org.junit.Test;

import physics.collision.shape.RectShape;

public class RectTest {

	@Test
	public void testCreation() {
		new RectShape(new Vec2D(-0.5f, -0.5f), new Vec2D(0.5f, 0.5f));
	}
	@Test(expected = IllegalArgumentException.class)
	public void testFailedCreation0() {
		// center must be (0,0)
		new RectShape(new Vec2D(0,0), new Vec2D(1, 1));
	}

	@Test(expected = NullPointerException.class)
	public void testFailedCreation1() {
		new RectShape(null, new Vec2D(1, 1));
	}

	@Test(expected = NullPointerException.class)
	public void testFailedCreation2() {
		new RectShape(new Vec2D(0, 0), null);
	}

	@Test(expected = NullPointerException.class)
	public void testFailedCreation3() {
		new RectShape(null, null);
	}

	@Test
	public void testGetBoundingBox() {
		Assert.assertEquals(new Rectangle2D.Float(-0.5f, -0.5f, 1, 1), new RectShape(new Vec2D(-0.5f, -0.5f), new Vec2D(0.5f, 0.5f)).getRect());
	}

}
