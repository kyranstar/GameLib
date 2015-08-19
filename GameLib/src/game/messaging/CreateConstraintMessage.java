package game.messaging;

import physics.constraints.Constraint;

public class CreateConstraintMessage implements Message {

	public Constraint constraint;

	public CreateConstraintMessage(final Constraint constraint) {
		this.constraint = constraint;
	}

}
