package worms.programs.expressions;

import worms.exceptions.IllegalTypeException;
import worms.model.Program;
import worms.programs.Expression;
import worms.programs.Type;
import worms.programs.types.BoolType;

public class ConditionalExpression extends Expression<Type> {

	private Expression<? extends Type> e;
	private Expression<? extends Type> o;
	private Expression<? extends Type> t;

	public ConditionalExpression(Program program, int line, int column, Expression<? extends Type> e, 
			Expression<? extends Type> o, Expression<? extends Type> t) {
		super(program, line, column, Type.class);
			
		this.e = e;
		this.o = o;
		this.t = t;
	}

	private boolean isValidType(Expression<? extends Type> expression) {
		return (expression.getType() == BoolType.class);
	}
	
	@Override
	public Type evaluate() {
		if ( !isValidType(t) ) 
			throw new IllegalTypeException();
		BoolType bool = (BoolType) t.evaluate();
		
		if ( bool.getValue() )
			return e.evaluate();
		else 
			return o.evaluate();
	}

}
