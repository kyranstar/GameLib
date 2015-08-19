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
	private final boolean capFPS;
	private boolean running = true;
	// the target amount of time between draws in millis
	private int targetDrawTime;
	private float targetDT;

	// ArrayDeque is supposed to be the fastest collection
	private final Queue<KeyEvent> keyEvents = new ArrayDeque<>();
	private final Queue<EventPair<MouseEvent, MouseEventType>> mouseEvents = new ArrayDeque<>();
	private final Queue<MouseWheelEvent> mouseWheelEvents = new ArrayDeque<>();

	private int runningFPS;
	private int runningUPS;

	private int currentFPS = 0;
	private int currentUPS = 0;
	private float counterstart = System.nanoTime();

	// the amount of time to update by per update
	private long currentTime = System.currentTimeMillis();
	// accumulates available time for updating.
	private float accumulator = 0.0f;

	protected GameLoop(final int fps, final int ups) {
		setTargetFPS(fps);
		setTargetUPS(ups);
		capFPS = true;
	}

	protected GameLoop(final int ups) {
		setTargetFPS(-1);
		setTargetUPS(ups);
		capFPS = false;
	}

	private void setTargetUPS(final int ups) {
		targetDT = 1f / ups;
	}

	public void setTargetFPS(final int fps) {
		targetDrawTime = 1000 / fps;
	}

	public void run() {
		counterstart = System.nanoTime();

		// the amount of time to update by per update
		currentTime = System.currentTimeMillis();
		// accumulates available time for updating.
		accumulator = 0.0f;

		while (running) {
			runFrame();
		}
	}

	public void runFrame() {
		processEvents();

		final long newTime = System.currentTimeMillis();
		long frameTime = newTime - currentTime;
		if (frameTime > 250) {
			frameTime = 250;
		}
		currentTime = newTime;

		accumulator += frameTime / 1000.;
		while (accumulator >= targetDT) {
			update(targetDT);
			currentUPS++;
			accumulator -= targetDT;
		}
		final long timeBeforeDraw = System.currentTimeMillis();
		draw(targetDrawTime);
		currentFPS++;
		final float counterelapsed = System.nanoTime() - counterstart;

		// at the end of every second
		if (counterelapsed >= 1000000000L) {
			// runningFPS is how many frames we processed last second
			runningFPS = currentFPS;
			currentFPS = 0;
			runningUPS = currentUPS;
			currentUPS = 0;

			counterstart = System.nanoTime();
		}
		if (capFPS) {
			sleep((int) (targetDrawTime - (System.currentTimeMillis() - timeBeforeDraw)));
		}
	}

	private void sleep(final int i) {
		if (i > 0) {
			try {
				Thread.sleep(i);
			} catch (final InterruptedException e) {
				e.printStackTrace();
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

	public long getCurrentUPS() {
		return runningUPS;
	}

	public void stop() {
		running = false;
	}

	public abstract void processInput(Queue<KeyEvent> keyEvents, Queue<EventPair<MouseEvent, MouseEventType>> mouseEvent,
			Queue<MouseWheelEvent> mouseWheelEvents2);

	public abstract void update(float dt);

	public abstract void draw(int targetDrawTime);

	@Override
	public void mouseClicked(final MouseEvent e) {
		synchronized (mouseEvents) {
			mouseEvents.add(new EventPair<MouseEvent, MouseEventType>(e, MouseEventType.CLICK));
		}
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
		synchronized (mouseEvents) {
			mouseEvents.add(new EventPair<MouseEvent, MouseEventType>(e, MouseEventType.ENTER));
		}
	}

	@Override
	public void mouseExited(final MouseEvent e) {
		synchronized (mouseEvents) {
			mouseEvents.add(new EventPair<MouseEvent, MouseEventType>(e, MouseEventType.EXIT));
		}
	}

	@Override
	public void mousePressed(final MouseEvent e) {
		synchronized (mouseEvents) {
			mouseEvents.add(new EventPair<MouseEvent, MouseEventType>(e, MouseEventType.PRESS));
		}
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
		synchronized (mouseEvents) {
			mouseEvents.add(new EventPair<MouseEvent, MouseEventType>(e, MouseEventType.RELEASE));
		}
	}

	@Override
	public void mouseDragged(final MouseEvent e) {
		synchronized (mouseEvents) {
			mouseEvents.add(new EventPair<MouseEvent, MouseEventType>(e, MouseEventType.DRAG));
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

	protected static class EventPair<A, B> {
		A event;
		B type;

		public EventPair(final A e, final B t) {
			event = e;
			type = t;
		}
	}

	protected static enum MouseEventType {
		PRESS,
		CLICK,
		RELEASE,
		DRAG,
		EXIT,
		ENTER;
	}
}