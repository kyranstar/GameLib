package game.messaging;

public class DebugMessage<T> implements Message {
	public InfoType type;
	public T info;

	public DebugMessage(final InfoType type, final T info) {
		this.type = type;
		this.info = info;

		assert info.getClass().equals(type.clazz);
	}

	public static enum InfoType {
		// the number of collision checks this tick
		COLLISION_CHECKS_THIS_TICK(Integer.class),
		// the number of collision solves this tick
		COLLISION_SOLVES_THIS_TICK(Integer.class);

		private final Class<?> clazz;

		private InfoType(final Class<?> clazz) {
			this.clazz = clazz;
		}
	}
}
