package game.messaging;

public class UpdateMessage implements Message {

	public float dt;

	public UpdateMessage(final float dt) {
		if (dt < 0) {
			throw new IllegalArgumentException("dt must be >= 0");
		}
		this.dt = dt;
	}

}
