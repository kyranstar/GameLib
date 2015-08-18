package game.messaging;

import game.GameSystem;

import java.util.ArrayList;
import java.util.List;

public class GameSystemManager {
	private final List<GameSystem> systems = new ArrayList<>();

	public void broadcastMessage(final Message m) {
		for (final GameSystem s : systems) {
			s.recieveMessage(m);
		}
	}

	public void addSystem(final GameSystem s) {
		systems.add(s);
	}
}
