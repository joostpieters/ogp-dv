package worms.programs.expressions;

import worms.exceptions.IllegalTypeException;
import worms.model.GameObject;
import worms.model.MobileGameObject;
import worms.model.Program;
import worms.programs.Expression;
import worms.programs.Type;
import worms.programs.types.DoubleType;
import worms.programs.types.EntityType;

public class GetDirExpression extends Expression<DoubleType> {

	private Expression<? extends Type> entity;

	public GetDirExpression(Program program, int line, int column, Expression<? extends Type> e) {
		super(program, line, column, DoubleType.class);
		
		this.entity = e;
	}
	
	private boolean isValidType(Expression<? extends Type> expression) {
		return (expression.evaluate().getType() == EntityType.class);
	}

	@Override
	public DoubleType evaluate() {
		if ( !isValidType(entity) ) 
			throw new IllegalTypeException();
		
		GameObject object = (GameObject) entity.evaluate().getValue();
		if (! (object instanceof MobileGameObject) ) 
			throw new IllegalTypeException();
		MobileGameObject mobileEntity = (MobileGameObject) object;
		
		return new DoubleType( mobileEntity.getDirection() );
	}

}
