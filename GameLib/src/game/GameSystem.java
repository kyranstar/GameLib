package game;

import game.messaging.GameSystemManager;
import game.messaging.Message;

public abstract class GameSystem {

	protected GameSystemManager systemManager = new GameSystemManager();

	public GameSystem(final GameSystemManager systemManager) {
		this.systemManager = systemManager;
	}

	public abstract void recieveMessage(Message m);
}
