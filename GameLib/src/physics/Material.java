package physics;

public enum Material {
	STEEL(0.6f, 0.04f, 0.03f);

	public float restitution;
	public float staticFriction;
	public float dynamicFriction;

	private Material(final float restitution, final float sFric, final float dFric) {
		this.restitution = restitution;
		staticFriction = sFric;
		dynamicFriction = dFric;
	}
}
