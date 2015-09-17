package physics.collision.shape;

import java.awt.geom.Rectangle2D;

import math.Vec2D;

public interface CShape {

	/**
	 * Gives a rectangle that fully encloses the shape.
	 *
	 * @return
	 */
	public abstract Rectangle2D getRect();

}
