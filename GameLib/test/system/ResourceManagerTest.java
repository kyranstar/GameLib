package system;

import java.awt.image.BufferedImage;

import org.junit.Assert;
import org.junit.Test;

import system.ResourceManager.Resource;
import test.TestConstants;

public class ResourceManagerTest {

	@Test
	public void testLoadTestImage() {
		final ResourceManager rm = new ResourceManager();

		final Resource<BufferedImage> im = rm.<BufferedImage> loadResource(TestConstants.TEST_IMAGE_NAME);

		Assert.assertEquals("testImage.png", im.name);
		Assert.assertEquals(219, im.data.getWidth());
		Assert.assertEquals(231, im.data.getHeight());
	}
}
