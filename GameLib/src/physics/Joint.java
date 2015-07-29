package physics;

public abstract class Joint {

	private final GameEntity a;
	private final GameEntity b;

	public Joint(GameEntity a, GameEntity b) {
		this.a = a;
		this.b = b;
	}

	public abstract void update();

	public GameEntity getA() {
		return a;
	}

	public GameEntity getB() {
		return b;
	}
}
