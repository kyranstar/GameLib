package physics.collision.handling;

import math.Vec2D;
import physics.CollisionComponent;
import physics.collision.shape.CircleShape;

class CollisionCircleCircle {

	public static boolean isColliding(final CollisionComponent o1, final CollisionComponent o2) {
		CircleShape s1 = (CircleShape) o1.getShape();
		CircleShape s2 = (CircleShape) o2.getShape();
		
		final float c = s1.getRadius() + s2.getRadius();
		final float b = o1.getPos().x - o2.getPos().x;
		final float a = o1.getPos().y - o2.getPos().y;

		return c * c > b * b + a * a;
	}

	public static CManifold generateManifold(final CManifold m) {
		CircleShape s1 = (CircleShape) m.a.getShape();
		CircleShape s2 = (CircleShape) m.b.getShape();
		
		// A to B
		final Vec2D n = m.b.getPos().minus(m.a.getPos());
		if (n.length() == 0) {
			// circles are on the same position, choose random but consistent
			// values
			m.setNormal(new Vec2D(0, 1));
			m.setPenetration(Math.min(s1.getRadius(), s2.getRadius()));
			return m;
		}
		m.setNormal(n.unitVector());
		m.setPenetration(s1.getRadius() + s2.getRadius() - n.length());
		return m;
	}
}
