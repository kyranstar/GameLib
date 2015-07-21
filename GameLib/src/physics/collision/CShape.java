package physics.collision;

import game.Vec2D;

public abstract class CShape {

	public abstract Vec2D center();

	public abstract void moveRelative(Vec2D v);

}
