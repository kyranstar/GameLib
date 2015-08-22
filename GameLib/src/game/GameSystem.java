package game;

import game.messaging.GameSystemManager;
import game.messaging.Message;

import java.util.Set;

public abstract class GameSystem {

	/**
	 * A constant that represents a set containing all message types.
	 */
	public static final Set<Class<? extends Message>> ACCEPT_ALL_MESSAGES = null;

	protected GameSystemManager systemManager;

	public GameSystem(final GameSystemManager systemManager) {
		this.systemManager = systemManager;
	}

	/**
	 * This method is called whenever a message is broadcasted from the {@link GameSystemManager}, and if that message's
	 * class is returned from {@link #getAcceptedMessages()}
	 *
	 * @param m
	 */
	public abstract void recieveMessage(Message m);

	/**
	 * Returns a set of classes of messages that this system should recieve through {@link #recieveMessage(Message)}. If
	 * {@link #ACCEPT_ALL_MESSAGES}, this system accepts all messages.
	 *
	 * @return {@link #ACCEPT_ALL_MESSAGES} or a set of accepted message classes
	 */
	public abstract Set<Class<? extends Message>> getAcceptedMessages();
}
