package math;

import org.junit.Assert;
import org.junit.Test;

import test.TestConstants;

public class AngleUtilsTest {

	private static final float pi = AngleUtils.PI;

	@Test
	public void testAngleDifference() {
		Assert.assertEquals(0, AngleUtils.angleDifference(0, 0), TestConstants.EPSILON);
		Assert.assertEquals(0, AngleUtils.angleDifference(pi, pi), TestConstants.EPSILON);
		Assert.assertEquals(0, AngleUtils.angleDifference(4 * pi, 0), TestConstants.EPSILON);
		Assert.assertEquals(0, AngleUtils.angleDifference(0, 4 * pi), TestConstants.EPSILON);
		Assert.assertEquals(0, AngleUtils.angleDifference(pi, -pi), TestConstants.EPSILON);

		Assert.assertEquals(pi, AngleUtils.angleDifference(0, -pi), TestConstants.EPSILON);
		Assert.assertEquals(pi, AngleUtils.angleDifference(0, pi), TestConstants.EPSILON);
	}

	@Test
	public void testNormalize() {
		Assert.assertEquals(0, AngleUtils.normalize(0), TestConstants.EPSILON);
		Assert.assertEquals(pi, AngleUtils.normalize(-pi), TestConstants.EPSILON);
		Assert.assertEquals(pi, AngleUtils.normalize(pi), TestConstants.EPSILON);

		Assert.assertEquals(pi - 0.1f, AngleUtils.normalize(-pi - 0.1f), TestConstants.EPSILON);
		Assert.assertEquals(-pi + 0.1f, AngleUtils.normalize(pi + 0.1f), TestConstants.EPSILON);
	}
}
