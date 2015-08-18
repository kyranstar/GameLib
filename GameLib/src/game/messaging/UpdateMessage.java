package game.messaging;

public class UpdateMessage extends Message {

	public float dt;

	public UpdateMessage(final float dt) {
		this.dt = dt;
	}

}
