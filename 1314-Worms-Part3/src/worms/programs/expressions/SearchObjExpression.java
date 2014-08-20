package worms.programs.expressions;

import worms.exceptions.IllegalTypeException;
import worms.model.Position;
import worms.model.Program;
import worms.model.Worm;
import worms.programs.Expression;
import worms.programs.Type;
import worms.programs.types.DoubleType;
import worms.programs.types.EntityType;

public class SearchObjExpression extends Expression<EntityType> {

	private Expression<? extends Type> e;

	public SearchObjExpression(Program program, int line, int column, Expression<? extends Type> e) {
		super(program, line, column, EntityType.class);
			
		this.e = e;
	}
	
	private boolean isValidType(Expression<? extends Type> expression) {
		return (expression.evaluate().getType() == DoubleType.class);
	}
	
	public Worm getOverlappingWorm() {
		Worm actingWorm = (Worm) getProgram().getAgent();
		double angle = (double) e.evaluate().getValue();
		double step = actingWorm.getRadius()/10;
	    double stepX = step * Math.cos(actingWorm.getDirection() + angle);
	    double stepY = step * Math.sin(actingWorm.getDirection() + angle);
	    Position position = actingWorm.getPosition();
	    Worm overlappingWorm = null;
	    while ( actingWorm.getWorld().isInWorld( position, actingWorm.getRadius() ) ) {
	    	position = position.addToX(stepX).addToY(stepY);
	    	overlappingWorm = (Worm) actingWorm.getWorld().getOverlappingObjectsOfType(Worm.class, position, actingWorm.getRadius()).get(0);
	    	if ( overlappingWorm != null )
	    		if ( overlappingWorm != actingWorm )
	    			return overlappingWorm;
	     }
		return overlappingWorm;
	}

	@Override
	public EntityType evaluate() {
		if ( !isValidType(e) )
			throw new IllegalTypeException();
		
		Worm overlappingWorm = getOverlappingWorm();
		return new EntityType(overlappingWorm);
		
	}

}
