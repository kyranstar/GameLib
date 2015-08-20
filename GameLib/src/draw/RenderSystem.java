package draw;

import game.GameSystem;
import game.World;
import game.messaging.CreateConstraintMessage;
import game.messaging.CreateEntityMessage;
import game.messaging.DebugMessage;
import game.messaging.GameSystemManager;
import game.messaging.Message;
import game.messaging.RenderMessage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import physics.PhysicsComponent;
import physics.PhysicsSystem;
import physics.collision.shape.CircleShape;
import physics.collision.shape.RectShape;
import physics.constraints.Constraint;

public class RenderSystem extends GameSystem {

	protected final List<PhysicsComponent> entities = new ArrayList<>();
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

		for (final PhysicsComponent object : entities) {
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
				final int x = (int) ((CircleShape) object.shape).center().x - radius;
				final int y = (int) ((CircleShape) object.shape).center().y - radius;
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

	private static final Set<Class<? extends Message>> acceptedMessages = new HashSet<>();
	{
		acceptedMessages.add(CreateEntityMessage.class);
		acceptedMessages.add(CreateConstraintMessage.class);
		acceptedMessages.add(RenderMessage.class);
		acceptedMessages.add(DebugMessage.class);
	}

	@Override
	public void recieveMessage(final Message m) {
		if (m instanceof CreateEntityMessage) {
			entities.add((PhysicsComponent) ((CreateEntityMessage) m).entity.getComponent(PhysicsComponent.COMPONENT_ID));
		} else if (m instanceof CreateConstraintMessage) {
			constraints.add(((CreateConstraintMessage) m).constraint);
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

	@Override
	public Set<Class<? extends Message>> getAcceptedMessages() {
		return acceptedMessages;
	}
}
