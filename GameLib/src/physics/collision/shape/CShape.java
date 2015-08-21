package physics.collision.shape;

import java.awt.geom.Rectangle2D;

import math.Vec2D;

public interface CShape {

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

}
