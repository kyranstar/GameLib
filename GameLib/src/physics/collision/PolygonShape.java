package physics.collision;

import math.Vec2D;

public interface PolygonShape {
	public Vec2D getVertex(final int vert);

	public int getNumVertices();
}
