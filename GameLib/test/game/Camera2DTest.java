package game;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;

import math.Vec2D;

import org.junit.Assert;
import org.junit.Test;

import draw.GraphicsUtils;

public class Camera2DTest {

	private Graphics2D createGraphics2D() {
		return GraphicsUtils.createImage(10, 10, Transparency.OPAQUE).createGraphics();
	}

	@Test
	public void testTranslateGraphics2D() {
		final Graphics2D g = createGraphics2D();
		final Camera2D cam = new Camera2D();
		cam.bounds = new Rectangle(1, 1, 10, 10);

		final AffineTransform before = g.getTransform();
		cam.translate(g);
		final AffineTransform mid = g.getTransform();
		cam.untranslate(g);
		final AffineTransform after = g.getTransform();

		Assert.assertEquals(before, after);
		Assert.assertNotEquals(before, mid);
		Assert.assertNotEquals(after, mid);
	}

	@Test
	public void testClip() {
		final Graphics2D g = createGraphics2D();
		final Camera2D cam = new Camera2D();
		cam.bounds = new Rectangle(0, 0, 10, 10);

		final Shape before = g.getClip();
		cam.clip(g);
		final Shape mid = g.getClip();
		cam.unclip(g);
		final Shape after = g.getClip();

		Assert.assertEquals(before, after);
		Assert.assertNotEquals(before, mid);
		Assert.assertNotEquals(after, mid);
	}

	@Test
	public void testTranslateVec2D1() {
		final Camera2D cam = new Camera2D();
		cam.bounds = new Rectangle(0, 0, 10, 10);

		Assert.assertEquals(new Vec2D(5, 5), cam.translate(new Vec2D(5, 5)));
	}

	@Test
	public void testTranslateVec2D2() {
		final Camera2D cam = new Camera2D();
		cam.bounds = new Rectangle(-5, -5, 10, 10);

		Assert.assertEquals(new Vec2D(10, 10), cam.translate(new Vec2D(5, 5)));
	}

}
