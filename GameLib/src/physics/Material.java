package physics;

public enum Material {
	STEEL(0.3f, 0.1f, 0.04f),
	RUBBER(0.8f, 0.2f, 0.09f);

	public final float restitution;
	public final float staticFriction;
	public final float dynamicFriction;

	private Material(final float restitution, final float sFric, final float dFric) {
		this.restitution = restitution;
		staticFriction = sFric;
		dynamicFriction = dFric;
	}
}
