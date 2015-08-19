package draw;

import game.GameSystem;
import game.World;
import game.messaging.ConstraintCreatedMessage;
import game.messaging.DebugMessage;
import game.messaging.EntityCreatedMessage;
import game.messaging.GameSystemManager;
import game.messaging.Message;
import game.messaging.RenderMessage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import physics.PhysicsEntity;
import physics.PhysicsSystem;
import physics.collision.CircleShape;
import physics.collision.RectShape;
import physics.constraints.Constraint;

public class RenderSystem extends GameSystem {

	protected final List<PhysicsEntity> entities = new ArrayList<>();
	protected final List<Constraint> constraints = new ArrayList<>();
	private final World world;
	private int collisionChecksThisTick;
	private int collisionSolvesThisTick;

	public RenderSystem(final GameSystemManager systemManager, final World world) {
		super(systemManager);
		this.world = world;
	}

	private void render(final Graphics2D g) {
		GraphicsUtils.prettyGraphics(g);

		for (final PhysicsEntity object : entities) {
			if (object.shape instanceof RectShape) {
				final int x = (int) ((RectShape) object.shape).getMin().x;
				final int y = (int) ((RectShape) object.shape).getMin().y;
				final int width = (int) (((RectShape) object.shape).getMax().x - ((RectShape) object.shape).getMin().x);
				final int height = (int) (((RectShape) object.shape).getMax().y - ((RectShape) object.shape).getMin().y);
				if (!object.sleeping) {
					g.setColor(new Color(50, 100, 200));
				} else {
					g.setColor(new Color(200, 200, 200));
				}
				g.translate(x + width / 2, y + height / 2);
				g.rotate(object.getOrientation());
				g.fillRect(-width / 2, -height / 2, width, height);
				g.setColor(Color.BLACK);
				g.drawRect(-width / 2, -height / 2, width, height);
				g.rotate(-object.getOrientation());
				g.translate(-(x + width / 2), -(y + height / 2));
			} else if (object.shape instanceof CircleShape) {
				final int radius = (int) ((CircleShape) object.shape).getRadius();
				final int x = (int) ((CircleShape) object.shape).getCenter().x - radius;
				final int y = (int) ((CircleShape) object.shape).getCenter().y - radius;
				if (!object.sleeping) {
					g.setColor(new Color(200, 100, 50));
				} else {
					g.setColor(new Color(200, 200, 200));
				}

				g.translate(x + radius, y + radius);
				g.rotate(object.getOrientation());

				g.fillOval(-radius, -radius, radius * 2, radius * 2);
				g.setColor(Color.BLACK);
				g.drawOval(-radius, -radius, radius * 2, radius * 2);
				g.drawLine(0, 0, radius, 0);

				g.rotate(-object.getOrientation());
				g.translate(-(x + radius), -(y + radius));
			}
		}
		for (final Constraint c : constraints) {
			c.draw(g);
		}

		g.setColor(Color.RED);
		g.drawString("Entities: " + entities.size(), 10, 15);
		g.drawString("FPS: " + world.getCurrentFPS(), 10, 30);
		g.drawString("UPS: " + world.getCurrentUPS(), 10, 45);
		g.drawString("Collision Checks: " + collisionChecksThisTick, 10, 60);
		g.drawString("Collision Solves: " + collisionSolvesThisTick, 10, 75);

		drawMeter(g);
	}

	private void drawMeter(final Graphics g) {
		final int x = 10;
		final int top = 90;
		final int bottom = (int) (top + PhysicsSystem.PIXELS_PER_METER);

		g.setColor(Color.RED);
		g.drawLine(x, top, x + 10, top);
		g.drawLine(x, top, x, bottom);
		g.drawLine(x, bottom, x + 10, bottom);
		g.drawString("1 meter", x + 10, (top + bottom) / 2);
	}

	@Override
	public void recieveMessage(final Message m) {
		if (m instanceof EntityCreatedMessage) {
			entities.add(((EntityCreatedMessage) m).entity);
		} else if (m instanceof ConstraintCreatedMessage) {
			constraints.add(((ConstraintCreatedMessage) m).constraint);
		} else if (m instanceof RenderMessage) {
			render(((RenderMessage) m).graphics);
		} else if (m instanceof DebugMessage) {
			handleDebugMessage((DebugMessage<?>) m);
		}
	}

	@SuppressWarnings("unchecked")
	private void handleDebugMessage(final DebugMessage<?> m) {
		switch (m.type) {
		case COLLISION_CHECKS_THIS_TICK:
			collisionChecksThisTick = ((DebugMessage<Integer>) m).info;
			break;
		case COLLISION_SOLVES_THIS_TICK:
			collisionSolvesThisTick = ((DebugMessage<Integer>) m).info;
			break;
		}
	}
}
