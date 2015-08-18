package game.messaging;

import physics.constraints.Constraint;

public class ConstraintCreatedMessage extends Message {

	public Constraint constraint;

	public ConstraintCreatedMessage(final Constraint constraint) {
		this.constraint = constraint;
	}

}
