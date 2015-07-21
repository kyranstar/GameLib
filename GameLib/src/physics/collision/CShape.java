package physics.collision;

import game.Vec2D;

import java.awt.geom.Rectangle2D;

public abstract class CShape {

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
