package physics.collision;

import java.awt.geom.Rectangle2D;

import math.Vec2D;

public abstract class CShape {

	protected float orientation;

	/**
	 * Returns the center of mass of the object
	 *
	 * @return
	 */
	public abstract Vec2D center();

	/**
	 * position += v
	 *
	 * @param v
	 */
	public abstract void moveRelative(Vec2D v);

	/**
	 * Gives a rectangle that fully encloses the shape.
	 *
	 * @return
	 */
	public abstract Rectangle2D getRect();

	public void rotate(final float radians) {
		orientation += radians;
	}

	public float getOrientation() {
		return orientation;
	}

}
