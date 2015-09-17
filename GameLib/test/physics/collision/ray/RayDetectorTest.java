package physics.collision.ray;

import math.MathUtils;
import math.Vec2D;

import org.junit.Assert;
import org.junit.Test;

import physics.collision.shape.CircleShape;
import physics.collision.shape.RectShape;
import test.TestConstants;

public class RayDetectorTest {

	@Test
	public void testRaycastOriginCircleAndOriginRay() {
		final CircleShape c = new CircleShape(5);
		final RaycastResult r = new RaycastResult();
		final Ray ray = new Ray(new Vec2D(-10, 0), new Vec2D(1, 0));
		Assert.assertEquals(true, RaycastDetector.raycast(ray, RaycastDetector.INFINITE_LENGTH, Vec2D.ZERO, c, r));
		Assert.assertEquals(5, r.distance, TestConstants.EPSILON);
		Assert.assertEquals(-1, r.normal.x, TestConstants.EPSILON);
		Assert.assertEquals(0, r.normal.y, TestConstants.EPSILON);
		Assert.assertEquals(new Vec2D(-5, 0), r.point);
	}

	@Test
	public void testRaycastOriginCircleAndRay() {
		final CircleShape c = new CircleShape(5);
		final RaycastResult r = new RaycastResult();
		// creates ray in upper left pointing diagonally down and to the right
		final Ray ray = new Ray(new Vec2D(3 * MathUtils.PI / 4).multiply(10), new Vec2D(7 * MathUtils.PI / 4));

		Assert.assertEquals(true, RaycastDetector.raycast(ray, RaycastDetector.INFINITE_LENGTH, Vec2D.ZERO, c, r));
		Assert.assertEquals(false, RaycastDetector.raycast(ray, 4.9f, Vec2D.ZERO, c, null));
		Assert.assertEquals(true, RaycastDetector.raycast(ray, 5, Vec2D.ZERO,c, null));
		Assert.assertEquals(true, RaycastDetector.raycast(ray, 10000, Vec2D.ZERO,c, null));

		Assert.assertEquals(5, r.distance, TestConstants.EPSILON);
		Assert.assertEquals(-Math.sqrt(2) / 2, r.normal.x, TestConstants.EPSILON);
		Assert.assertEquals(-Math.sqrt(2) / 2, r.normal.y, TestConstants.EPSILON);
		final Vec2D expectedPoint = new Vec2D(3 * MathUtils.PI / 4).multiply(5);
		Assert.assertEquals(expectedPoint.x, r.point.x, TestConstants.EPSILON);
		Assert.assertEquals(expectedPoint.y, r.point.y, TestConstants.EPSILON);
	}

	@Test
	public void testRaycastCircleAndRay() {
		Vec2D center = new Vec2D(10, -15);
		final CircleShape c = new CircleShape(5);
		final RaycastResult r = new RaycastResult();
		final Ray ray = new Ray(new Vec2D(10, 5), new Vec2D(3 * MathUtils.PI / 2));

		Assert.assertEquals(true, RaycastDetector.raycast(ray, RaycastDetector.INFINITE_LENGTH,center, c, r));

		Assert.assertEquals(15, r.distance, TestConstants.EPSILON);
		Assert.assertEquals(0, r.normal.x, TestConstants.EPSILON);
		Assert.assertEquals(-1, r.normal.y, TestConstants.EPSILON);
		final Vec2D expectedPoint = center.plus(new Vec2D(MathUtils.PI / 2).multiply(5));
		Assert.assertEquals(expectedPoint.x, r.point.x, TestConstants.EPSILON);
		Assert.assertEquals(expectedPoint.y, r.point.y, TestConstants.EPSILON);
	}

	@Test
	public void testRaycastRectHorizontal() {
		Vec2D center = Vec2D.ZERO;
		final RectShape c = new RectShape(new Vec2D(-5, -5), new Vec2D(5, 5));
		final RaycastResult r = new RaycastResult();
		final Ray ray = new Ray(new Vec2D(-10, 0), new Vec2D(1, 0));
		Assert.assertEquals(true, RaycastDetector.raycast(ray, RaycastDetector.INFINITE_LENGTH,center, c, r));
		Assert.assertEquals(false, RaycastDetector.raycast(ray, 4.9f,center, c, null));
		Assert.assertEquals(true, RaycastDetector.raycast(ray, 5.0f,center, c, null));
		Assert.assertEquals(5, r.distance, TestConstants.EPSILON);
		Assert.assertEquals(-1, r.normal.x, TestConstants.EPSILON);
		Assert.assertEquals(0, r.normal.y, TestConstants.EPSILON);
		Assert.assertEquals(new Vec2D(-5, 0), r.point);
	}

	@Test
	public void testRaycastRectVertical() {

		Vec2D center = Vec2D.ZERO;
		final RectShape c = new RectShape(new Vec2D(-5, -5), new Vec2D(5, 5));
		final RaycastResult r = new RaycastResult();
		final Ray ray = new Ray(new Vec2D(0, -10), new Vec2D(0, 1));
		Assert.assertEquals(true, RaycastDetector.raycast(ray, RaycastDetector.INFINITE_LENGTH,center, c, r));
		Assert.assertEquals(false, RaycastDetector.raycast(ray, 4.9f,center, c, null));
		Assert.assertEquals(true, RaycastDetector.raycast(ray, 5.0f,center, c, null));
		Assert.assertEquals(5, r.distance, TestConstants.EPSILON);
		Assert.assertEquals(0, r.normal.x, TestConstants.EPSILON);
		Assert.assertEquals(-1, r.normal.y, TestConstants.EPSILON);
		Assert.assertEquals(new Vec2D(0, -5), r.point);
	}

}
