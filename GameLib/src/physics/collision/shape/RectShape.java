package physics.collision.shape;

import java.awt.geom.Rectangle2D;

import math.MathUtils;
import math.Vec2D;

public class RectShape implements CShape, PolygonShape {

	public static final int LEFT_TOP = 0;
	public static final int RIGHT_BOTTOM = 2;

	// upper left
	private final Vec2D min;
	// bottom right
	private final Vec2D max;
	// lazy variable
	private Rectangle2D rect;

	public RectShape(final Vec2D min, final Vec2D max) {
		if (min == null || max == null) {
			throw new NullPointerException("Min cannot be null, was: " + min + ". Max cannot be null, was: " + max);
		}
		if(Math.abs((min.x + max.x) / 2 - 0) >= MathUtils.EPSILON || 
				Math.abs((min.y + max.y) / 2 - 0) >= MathUtils.EPSILON){
			throw new IllegalArgumentException("Center must be (0,0)");
		}
		

		this.min = min;
		this.max = max;
	}

	public RectShape(final float x1, final float y1, final float x2, final float y2) {
		this(new Vec2D(x1, y1), new Vec2D(x2, y2));
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
