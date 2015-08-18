package game.messaging;

public class UpdateMessage implements Message {

	public float dt;

	public UpdateMessage(final float dt) {
		this.dt = dt;
	}

}
