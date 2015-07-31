package physics.constraints;

import physics.GameEntity;

public abstract class Joint extends Constraint {

	protected final GameEntity a;
	protected final GameEntity b;

	public Joint(GameEntity a, GameEntity b) {
		this.a = a;
		this.b = b;
	}

	public GameEntity getA() {
		return a;
	}

	public GameEntity getB() {
		return b;
	}
}
