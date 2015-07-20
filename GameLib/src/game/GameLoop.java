package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayDeque;
import java.util.Queue;

public abstract class GameLoop implements KeyListener, MouseListener, MouseWheelListener, MouseMotionListener {
	private boolean running = true;
	// the target amount of time between draws in millis
	private long targetDrawTime;
	private long targetUpdateTime;

	// ArrayDeque is supposed to be the fastest collection
	private final Queue<KeyEvent> keyEvents = new ArrayDeque<>();
	private final Queue<MouseEvent> mouseEvents = new ArrayDeque<>();
	private final Queue<MouseWheelEvent> mouseWheelEvents = new ArrayDeque<>();

	private long runningFPS;
	private int runningUPS;

	protected GameLoop(final int fps, final int ups) {
		setTargetFPS(fps);
		setTargetUPS(ups);
	}

	private void setTargetUPS(final int ups) {
		targetUpdateTime = 1000 / ups;
	}

	public void setTargetFPS(final int fps) {
		targetDrawTime = 1000 / fps;
	}

	public void run() {
		int currentFPS = 0;
		int currentUPS = 0;
		long counterstart = System.nanoTime();
		long counterelapsed = 0;
		long lastUpdateTime = System.currentTimeMillis();
		long lastDrawTime = System.currentTimeMillis();

		while (running) {
			processEvents();

			if (System.currentTimeMillis() > lastUpdateTime + targetUpdateTime) {
				final float dt = System.currentTimeMillis() - lastUpdateTime;
				lastUpdateTime = System.currentTimeMillis();
				update(dt / 1000);
				currentUPS++;
			}
			if (System.currentTimeMillis() > lastDrawTime + targetDrawTime) {
				draw();
				currentFPS++;
				lastDrawTime = System.currentTimeMillis();
			}

			counterelapsed = System.nanoTime() - counterstart;

			// at the end of every second
			if (counterelapsed >= 1000000000L) {
				// runningFPS is how many frames we processed last second
				runningFPS = currentFPS;
				currentFPS = 0;
				runningUPS = currentUPS;
				currentUPS = 0;
				System.out.println("FPS: " + runningFPS + " UPS: " + runningUPS);

				counterstart = System.nanoTime();
			}
		}
	}

	private void processEvents() {
		synchronized (keyEvents) {
			synchronized (mouseEvents) {
				synchronized (mouseWheelEvents) {
					processInput(keyEvents, mouseEvents, mouseWheelEvents);
					mouseEvents.clear();
					keyEvents.clear();
					mouseWheelEvents.clear();
				}
			}
		}
	}

	public long getCurrentFPS() {
		return runningFPS;
	}

	public void stop() {
		running = false;
	}

	public abstract void processInput(Queue<KeyEvent> keyEvents, Queue<MouseEvent> mouseEvent, Queue<MouseWheelEvent> mouseWheelEvents2);

	public abstract void update(float dt);

	public abstract void draw();

	@Override
	public void mouseClicked(final MouseEvent e) {
		synchronized (mouseEvents) {
			mouseEvents.add(e);
		}
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
		synchronized (mouseEvents) {
			mouseEvents.add(e);
		}
	}

	@Override
	public void mouseExited(final MouseEvent e) {
		synchronized (mouseEvents) {
			mouseEvents.add(e);
		}
	}

	@Override
	public void mousePressed(final MouseEvent e) {
		synchronized (mouseEvents) {
			mouseEvents.add(e);
		}
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
		synchronized (mouseEvents) {
			mouseEvents.add(e);
		}
	}

	@Override
	public void mouseDragged(final MouseEvent e) {
		synchronized (mouseEvents) {
			mouseEvents.add(e);
		}
	}

	@Override
	public void keyPressed(final KeyEvent e) {
		synchronized (keyEvents) {
			keyEvents.add(e);
		}
	}

	@Override
	public void keyReleased(final KeyEvent e) {
		synchronized (keyEvents) {
			keyEvents.add(e);
		}
	}

	@Override
	public void keyTyped(final KeyEvent e) {
		synchronized (keyEvents) {
			keyEvents.add(e);
		}
	}

	@Override
	public void mouseWheelMoved(final MouseWheelEvent e) {
		synchronized (mouseWheelEvents) {
			mouseWheelEvents.add(e);
		}
	}

	@Override
	public void mouseMoved(final MouseEvent e) {
		// TODO Auto-generated method stub

	}
}