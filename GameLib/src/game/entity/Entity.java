package game.entity;

import java.util.UUID;

public class Entity {
	private static final int LONG_BITS = 64;

	public final UUID id;
	private long componentSignature = 0;
	private final GameComponent[] components = new GameComponent[LONG_BITS];

	public Entity() {
		id = UUID.randomUUID();
	}

	public void addComponent(GameComponent c) {
		componentSignature |= 1 << c.getComponentId();
		components[c.getComponentId()] = c;
	}

	/**
	 * Gives the bitfield signature for all added components. Each bit is set
	 * based on the component's ID. For example, if a component with the ID 1
	 * was added to this entity, the signature would be 00..01. If components
	 * with IDs 1 and 3 were added, the signature would be 00..101.
	 *
	 * @return
	 */
	public long getComponentSignature() {
		return componentSignature;
	}
}
