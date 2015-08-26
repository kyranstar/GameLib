package game;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayDeque;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GameLoop implements KeyListener, MouseListener, MouseWheelListener, MouseMotionListener {
	private final Logger logger = LoggerFactory.getLogger(GameLoop.class);

	private final boolean capFPS;
	private boolean running = true;
	// the target amount of time between draws in millis
	private final int targetDrawTime;
	private final float targetDT;

	// ArrayDeque is supposed to be the fastest collection
	private final Queue<EventPair<KeyEvent, KeyEventType>> keyEvents = new ArrayDeque<>();
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

	private Point currentMousePoint;

	protected GameLoop(final int fps, final int ups) {
		targetDrawTime = 1000 / fps;
		targetDT = 1f / ups;
		capFPS = true;

		logger.info("Game loop created. UPS: {}, FPS: {}", ups, fps);
	}

	protected GameLoop(final int ups) {
		targetDrawTime = 0;
		targetDT = 1f / ups;
		capFPS = false;

		logger.info("Game loop created. UPS: {}, FPS: Uncapped", ups);
	}

	public void run() {
		logger.info("Game loop started.");
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
		if (capFPS) {
			draw(targetDrawTime);
		} else {
			draw();
		}
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
				logger.error("Thread interrupted during sleep!", e);
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

	public abstract void processInput(Queue<EventPair<KeyEvent, KeyEventType>> keyEvents2, Queue<EventPair<MouseEvent, MouseEventType>> mouseEvent,
			Queue<MouseWheelEvent> mouseWheelEvents2);

	public abstract void update(float dt);

	public abstract void draw(int targetDrawTime);

	public abstract void draw();

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
			keyEvents.add(new EventPair<>(e, KeyEventType.PRESS));
		}
	}

	@Override
	public void keyReleased(final KeyEvent e) {
		synchronized (keyEvents) {
			keyEvents.add(new EventPair<>(e, KeyEventType.RELEASE));
		}
	}

	@Override
	public void keyTyped(final KeyEvent e) {
		synchronized (keyEvents) {
			keyEvents.add(new EventPair<>(e, KeyEventType.TYPE));
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
		currentMousePoint = e.getPoint();
	}

	protected static class EventPair<A, B> {
		public A event;
		public B type;

		public EventPair(final A e, final B t) {
			event = e;
			type = t;
		}
	}

	protected Point getMousePosition() {
		return currentMousePoint;
	}

	protected static enum MouseEventType {
		PRESS,
		CLICK,
		RELEASE,
		DRAG,
		EXIT,
		ENTER;
	}

	protected static enum KeyEventType {
		PRESS,
		TYPE,
		RELEASE;
	}
}