package physics.collision;

import game.Vec2D;

import java.awt.geom.Rectangle2D;

public class RectShape extends CShape implements PolygonShape {
	// upper left
	private Vec2D min;
	// bottom right
	private Vec2D max;
	// lazy variable
	private Rectangle2D rect;

	public RectShape(final Vec2D min, final Vec2D max) {
		this.min = min;
		this.max = max;
	}

	@Override
	public Vec2D center() {
		return new Vec2D((getMax().x + getMin().x) / 2, (getMax().y + getMin().y) / 2);
	}

	@Override
	public Rectangle2D getRect() {
		if (rect == null) {
			rect = new Rectangle2D.Float(getMin().x, getMin().y, width(), height());
		}

		return rect;
	}

	public float width() {
		return getMax().x - getMin().x;
	}

	public float height() {
		return getMax().y - getMin().y;
	}

	@Override
	public void moveRelative(final Vec2D v) {
		min = getMin().plus(v);
		max = getMax().plus(v);
		rect = null;
	}

	public float area() {
		final Vec2D area = getMax().minus(getMin());
		return area.x * area.y;
	}

	public Vec2D getMin() {
		return min;
	}

	public Vec2D getMax() {
		return max;
	}

	@Override
	public Vec2D getVertex(final int vert) {
		return rotatePoint(getUnrotatedVertex(vert), orientation);
	}

	/**
	 * rotates a point around the center of the box by its orientation
	 *
	 * @param unrotatedVertex
	 * @return
	 */
	private Vec2D rotatePoint(final Vec2D unrotatedVertex, final float orientation) {
		// rotate point around center
		final float centerX = getMin().x + width() / 2;
		final float centerY = getMin().y + height() / 2;
		final float newX = (float) (centerX + (unrotatedVertex.x - centerX) * Math.cos(orientation) - (unrotatedVertex.y - centerY)
				* Math.sin(orientation));
		final float newY = (float) (centerY + (unrotatedVertex.x - centerX) * Math.sin(orientation) + (unrotatedVertex.y - centerY)
				* Math.cos(orientation));
		return new Vec2D(newX, newY);
	}

	private Vec2D getUnrotatedVertex(final int vert) {
		switch (vert) {
		case 0:
			return min;
		case 1:
			return new Vec2D(max.x, min.y);
		case 2:
			return max;
		case 3:
			return new Vec2D(min.x, max.y);
		default:
			throw new IllegalArgumentException("0 <= vert < 4, was " + vert);
		}
	}

	@Override
	public int getNumVertices() {
		return 4;
	}
}
