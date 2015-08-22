package math;

public final class AngleUtils {
	public static final float PI = (float) Math.PI;
	public static final float TWO_PI = (float) (Math.PI * 2);

	private AngleUtils() {
	}

	/**
	 *
	 * @param a
	 *            first angle in radians
	 * @param b
	 *            second angle in radians
	 * @return the smallest difference between the two angles in radians
	 */
	public static float angleDifference(final float a, final float b) {
		final float a1 = (float) Math.toDegrees(a);
		final float a2 = (float) Math.toDegrees(b);

		float dif = Math.abs(a1 - a2) % 360;

		if (dif > 180) {
			dif = 360 - dif;
		}

		return (float) Math.toRadians(dif);
	}

	/**
	 * Takes an angle like 3Pi and normalizes it between -Pi and Pi.
	 *
	 * @param angle
	 *            angle to be normalized in radians
	 * @return -Pi < noramlized angle <= Pi
	 */
	public static float normalize(final float angle) {
		return (float) (angle - TWO_PI * Math.floor((angle + Math.PI) / TWO_PI));
	}
}
