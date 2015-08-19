package game.entity;

public interface GameComponent {
	/**
	 * a long with only one bit set - the id of this component. All components
	 * of one type must return the same id and no components of different types
	 * can return the same id.
	 *
	 * @return 1 <= id < 64
	 */
	public int getComponentId();
}
