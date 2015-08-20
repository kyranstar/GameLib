package game.messaging;

import game.GameSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GameSystemManager {
	private final List<GameSystem> systems = new ArrayList<>();

	public synchronized void broadcast(final Message m) {
		for (final GameSystem s : systems) {
			final Set<Class<? extends Message>> accepted = s.getAcceptedMessages();
			if (accepted == GameSystem.ACCEPT_ALL_MESSAGES || accepted.contains(m.getClass())) {
				s.recieveMessage(m);
			}
		}
	}

	public synchronized void addSystem(final GameSystem s) {
		systems.add(s);
	}
}
