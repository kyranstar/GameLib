package math;

import org.junit.Assert;
import org.junit.Test;

import test.TestConstants;

public class AngleUtilsTest {

	private static final float pi = MathUtils.PI;

	@Test
	public void testAngleDifference() {
		Assert.assertEquals(0, MathUtils.angleDifference(0, 0), TestConstants.EPSILON);
		Assert.assertEquals(0, MathUtils.angleDifference(pi, pi), TestConstants.EPSILON);
		Assert.assertEquals(0, MathUtils.angleDifference(4 * pi, 0), TestConstants.EPSILON);
		Assert.assertEquals(0, MathUtils.angleDifference(0, 4 * pi), TestConstants.EPSILON);
		Assert.assertEquals(0, MathUtils.angleDifference(pi, -pi), TestConstants.EPSILON);

		Assert.assertEquals(pi, MathUtils.angleDifference(0, -pi), TestConstants.EPSILON);
		Assert.assertEquals(pi, MathUtils.angleDifference(0, pi), TestConstants.EPSILON);
	}

	@Test
	public void testNormalize() {
		Assert.assertEquals(0, MathUtils.normalize(0), TestConstants.EPSILON);
		Assert.assertEquals(pi, MathUtils.normalize(-pi), TestConstants.EPSILON);
		Assert.assertEquals(pi, MathUtils.normalize(pi), TestConstants.EPSILON);

		Assert.assertEquals(pi - 0.1f, MathUtils.normalize(-pi - 0.1f), TestConstants.EPSILON);
		Assert.assertEquals(-pi + 0.1f, MathUtils.normalize(pi + 0.1f), TestConstants.EPSILON);
	}
}
