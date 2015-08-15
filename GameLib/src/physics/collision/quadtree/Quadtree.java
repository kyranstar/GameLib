package physics.collision.quadtree;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import physics.PhysicsEntity;

public class Quadtree {
	private final int MAX_OBJECTS = 10;
	private final int MAX_LEVELS = 10;

	private final int level;
	private final List<PhysicsEntity> objects;
	private final Rectangle bounds;
	private final Quadtree[] nodes;

	public Quadtree(final Rectangle bounds) {
		this(0, bounds);
	}

	private Quadtree(final int pLevel, final Rectangle pBounds) {
		level = pLevel;
		objects = new ArrayList<PhysicsEntity>();
		bounds = pBounds;
		nodes = new Quadtree[4];
	}

	/*
	 * Insert the object into the quadtree. If the node exceeds the capacity, it will split and add all objects to their
	 * corresponding nodes.
	 */
	public void insert(final PhysicsEntity e) {

		if (nodes[0] != null) {
			final int index = getIndex(e);

			if (index != -1) {
				nodes[index].insert(e);

				return;
			}
		}

		objects.add(e);

		if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
			if (nodes[0] == null) {
				split();
			}

			int i = 0;
			while (i < objects.size()) {
				final int index = getIndex(objects.get(i));
				if (index != -1) {
					nodes[index].insert(objects.remove(i));
				} else {
					i++;
				}
			}
		}
	}

	/*
	 * Populates returnObjects with all objects that could collide with the given object
	 */
	public void retrieve(final List<PhysicsEntity> returnObjects, final PhysicsEntity e) {
		final int index = getIndex(e);
		if (index != -1 && nodes[0] != null) {
			nodes[index].retrieve(returnObjects, e);
		}
		for (final PhysicsEntity ge : objects) {
			if (ge == e) {
				continue;
			}
			returnObjects.add(ge);
		}
	}

	public void clear() {
		objects.clear();
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] != null) {
				nodes[i].clear();
				nodes[i] = null;
			}
		}
	}

	/*
	 * Splits the node into 4 subnodes
	 */
	private void split() {
		final int subWidth = (int) (bounds.getWidth() / 2);
		final int subHeight = (int) (bounds.getHeight() / 2);
		final int x = (int) bounds.getX();
		final int y = (int) bounds.getY();

		nodes[0] = new Quadtree(level + 1, new Rectangle(x + subWidth, y, subWidth, subHeight));
		nodes[1] = new Quadtree(level + 1, new Rectangle(x, y, subWidth, subHeight));
		nodes[2] = new Quadtree(level + 1, new Rectangle(x, y + subHeight, subWidth, subHeight));
		nodes[3] = new Quadtree(level + 1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));
	}

	/*
	 * Determine which node the object belongs to. -1 means object cannot completely fit within a child node and is part
	 * of the parent node
	 */
	private int getIndex(final PhysicsEntity e) {
		final Rectangle2D pRect = e.shape.getRect();

		int index = -1;
		final double verticalMidpoint = bounds.getX() + bounds.getWidth() / 2;
		final double horizontalMidpoint = bounds.getY() + bounds.getHeight() / 2;

		// Object can completely fit within the top quadrants
		final boolean topQuadrant = pRect.getY() < horizontalMidpoint && pRect.getY() + pRect.getHeight() < horizontalMidpoint;
		// Object can completely fit within the bottom quadrants
		final boolean bottomQuadrant = pRect.getY() > horizontalMidpoint;

		// Object can completely fit within the left quadrants
		if (pRect.getX() < verticalMidpoint && pRect.getX() + pRect.getWidth() < verticalMidpoint) {
			if (topQuadrant) {
				index = 1;
			} else if (bottomQuadrant) {
				index = 2;
			}
		}
		// Object can completely fit within the right quadrants
		else if (pRect.getX() > verticalMidpoint) {
			if (topQuadrant) {
				index = 0;
			} else if (bottomQuadrant) {
				index = 3;
			}
		}

		return index;
	}

	public void draw(final Graphics2D g) {
		g.setColor(Color.RED);
		g.draw(bounds);
		for (int i = 0; i < 4; i++) {
			if (nodes[i] != null) {
				nodes[i].draw(g);
			}
		}
	}
}
