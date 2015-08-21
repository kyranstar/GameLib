package game.messaging;

import game.GameSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameSystemManager {
	private final Logger logger = LoggerFactory.getLogger(GameSystemManager.class);
	private final List<GameSystem> systems = new ArrayList<>();

	public GameSystemManager() {
		logger.info("System manager created!");
	}

	/**
	 * Sends a message to all systems that will accept it. Systems accept a message when
	 * {@link GameSystem#getAcceptedMessages()} contains the message class.
	 *
	 * @param m
	 *            the message to be sent
	 */
	public synchronized void broadcast(final Message m) {
		for (final GameSystem s : systems) {
			final Set<Class<? extends Message>> accepted = s.getAcceptedMessages();
			if (accepted == GameSystem.ACCEPT_ALL_MESSAGES || accepted.contains(m.getClass())) {
				s.recieveMessage(m);
			}
		}
	}

	public synchronized void addSystem(final GameSystem s) {
		logger.info("Adding system to game: {}", s.getClass().getSimpleName());
		systems.add(s);
	}
}
