package game.entity;

import java.util.ArrayList;
import java.util.List;

public class GameEntity {
	private final List<GameComponent> components = new ArrayList<>();

	public GameEntity() {
	}

	public void addComponent(final GameComponent c) {
		resizeIfNecessary(c.getComponentId());
		components.set(c.getComponentId(), c);
	}

	public GameComponent getComponent(final int componentId) {
		resizeIfNecessary(componentId);
		return components.get(componentId);
	}

	private void resizeIfNecessary(final int componentId) {
		while (components.size() <= componentId) {
			components.add(null);
		}
	}

}
