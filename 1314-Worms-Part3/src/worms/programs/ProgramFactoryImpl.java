package worms.programs;

import java.util.List;

import worms.programs.expressions.*;
import worms.programs.statements.*;
import worms.model.Program;
import worms.model.programs.ProgramFactory;
import worms.programs.types.*;

/**
 * @author Delphine
 *
 */
public class ProgramFactoryImpl implements ProgramFactory<Expression<? extends Type>, Statement, Type> {
	

	public ProgramFactoryImpl(Program program) throws IllegalArgumentException {
		if (!isValidProgram(program))
			throw new IllegalArgumentException();
		this.program = program;
	}

	public Program getProgram() {
		return this.program;
	}
	
	public static boolean isValidProgram(Program program) {
		return program != null;
	}

	private final Program program;

	@Override
	public Expression<DoubleType> createDoubleLiteral(int line, int column, double d) {
		return new DoubleLiteral(getProgram(), line, column, d);
	}

	@Override
	public Expression<BoolType> createBooleanLiteral(int line, int column, boolean b) {
		return new BooleanLiteral(getProgram(), line, column, b);
	}

	@Override
	public Expression<EntityType> createNull(int line, int column) {
		return new NullExpression(getProgram(), line, column);
	}

	@Override
	public Expression<EntityType> createSelf(int line, int column) {
		return new SelfExpression(getProgram(), line, column);
	}

	@Override
	public Expression<DoubleType> createGetX(int line, int column, Expression<? extends Type> e) {
		return new GetXExpression(getProgram(), line, column, e);
	}

	@Override
	public Expression<DoubleType> createGetY(int line, int column, Expression<? extends Type> e) {
		return new GetYExpression(getProgram(), line, column, e);
	}

	@Override
	public Expression<DoubleType> createGetRadius(int line, int column, Expression<? extends Type> e) {
		return new GetRadiusExpression(getProgram(), line, column, e);
	}

	@Override
	public Expression<DoubleType> createGetDir(int line, int column, Expression<? extends Type> e) {
		return new GetDirExpression(getProgram(), line, column, e);
	}

	@Override
	public Expression<DoubleType> createGetAP(int line, int column, Expression<? extends Type> e) {
		return new GetAPExpression(getProgram(), line, column, e);
	}

	@Override
	public Expression<DoubleType> createGetMaxAP(int line, int column, Expression<? extends Type> e) {
		return new GetMaxAPExpression(getProgram(), line, column, e);
	}

	@Override
	public Expression<DoubleType> createGetHP(int line, int column, Expression<? extends Type> e) {
		return new GetHPExpression(getProgram(), line, column, e);
	}

	@Override
	public Expression<DoubleType> createGetMaxHP(int line, int column, Expression<? extends Type> e) {
		return new GetMaxHPExpression(getProgram(), line, column, e);
	}

	@Override
	public Expression<EntityType> createSearchObj(int line, int column, Expression<? extends Type> e) {
		return new SearchObjExpression(getProgram(), line, column, e);
	}

	@Override
	public Expression<BoolType> createIsWorm(int line, int column, Expression<? extends Type> e) {
		return new IsWormExpression(getProgram(), line, column, e);
	}

	@Override
	public Expression<BoolType> createIsFood(int line, int column, Expression<? extends Type> e) {
		return new IsFoodExpression(getProgram(), line, column, e);
	}

	@Override
	public Expression<Type> createVariableAccess(int line, int column, String name) {
		return new VariableAccessExpression(getProgram(), line, column, name);
	}

	@Override
	public Expression<BoolType> createLessThan(int line, int column, Expression<? extends Type> e1, Expression<? extends Type> e2) {
		return new LessThanExpression(getProgram(), line, column, e1, e2);
	}

	@Override
	public Expression<BoolType> createGreaterThan(int line, int column, Expression<? extends Type> e1, Expression<? extends Type> e2) {
		return new GreaterThanExpression(getProgram(), line, column, e1, e2);
	}

	@Override
	public Expression<BoolType> createLessThanOrEqualTo(int line, int column, Expression<? extends Type> e1,
			Expression<? extends Type> e2) {
		return new LessThanOrEqualToExpression(getProgram(), line, column, e1, e2);
	}

	@Override
	public Expression<BoolType> createGreaterThanOrEqualTo(int line,
			int column, Expression<? extends Type> e1, Expression<? extends Type> e2) {
		return new GreaterThanOrEqualToExpression(getProgram(), line, column, e1, e2);
	}

	@Override
	public Expression<BoolType> createEquality(int line, int column,
			Expression<? extends Type> e1, Expression<? extends Type> e2) {
		return new EqualityExpression(getProgram(), line, column, e1, e2);
	}

	@Override
	public Expression<BoolType> createInequality(int line, int column,
			Expression<? extends Type> e1, Expression<? extends Type> e2) {
		return new InequalityExpression(getProgram(), line, column, e1, e2);
	}

	@Override
	public Expression<DoubleType> createAdd(int line, int column,
			Expression<? extends Type> e1, Expression<? extends Type> e2) {
		return new AddExpression(getProgram(), line, column, e1, e2);
	}

	@Override
	public Expression<DoubleType> createSubtraction(int line, int column, 
			Expression<? extends Type> e1, Expression<? extends Type> e2) {
		return new SubtractionExpression(getProgram(), line, column, e1, e2);
	}

	@Override
	public Expression<DoubleType> createMul(int line, int column, Expression<? extends Type> e1, Expression<? extends Type> e2) {
		return new MulExpression(getProgram(), line, column, e1, e2);
	}

	@Override
	public Expression<DoubleType> createDivision(int line, int column, Expression<? extends Type> e1, Expression<? extends Type> e2) {
		return new DivisionExpression(getProgram(), line, column, e1, e2);
	}

	@Override
	public Expression<DoubleType> createSqrt(int line, int column, Expression<? extends Type> e) {
		return new SqrtExpression(getProgram(), line, column, e);
	}
	
	

	
	@Override
	public Expression<EntityType> createVariableAccess(int line, int column, String name, Type type) {
		return null;
	}
	
	@Override
	public Expression<DoubleType> createSin(int line, int column, Expression<? extends Type> e) {
		return null;
	}

	@Override
	public Expression<DoubleType> createCos(int line, int column, Expression<? extends Type> e) {
		return null;
	}
	
	@Override
	public Expression<BoolType> createAnd(int line, int column, Expression<? extends Type> e1, Expression<? extends Type> e2) {
		return null;
	}

	@Override
	public Expression<BoolType> createOr(int line, int column, Expression<? extends Type> e1, Expression<? extends Type> e2) {
		return null;
	}
	
	@Override
	public Expression<BoolType> createNot(int line, int column, Expression<? extends Type> e) {
		return new NotExpression(getProgram(), line, column, e);
	}
	
	@Override
	public Expression<BoolType> createSameTeam(int line, int column, Expression<? extends Type> e) {
		return new SameTeamExpression(getProgram(), line, column, e);
	}

	@Override
	public Statement createTurn(int line, int column, Expression<? extends Type> angle) {
		return new TurnStatement(getProgram(), line, column, angle);
	}

	@Override
	public Statement createMove(int line, int column) {
		return new MoveStatement(getProgram(), line, column);
	}

	@Override
	public Statement createJump(int line, int column) {
		return new JumpStatement(getProgram(), line, column);
	}

	@Override
	public Statement createToggleWeap(int line, int column) {
		return new ToggleWeapStatement(getProgram(), line, column);
	}

	@Override
	public Statement createFire(int line, int column, Expression<? extends Type> yield) {
		return new FireStatement(getProgram(), line, column, yield);
	}

	@Override
	public Statement createSkip(int line, int column) {
		return new SkipStatement(getProgram(), line, column);
	}

	@Override
	public Statement createAssignment(int line, int column, String variableName, Expression<? extends Type> rhs) {
		return new AssignmentStatement(getProgram(), line, column, variableName, rhs);
	}

	@Override
	public Statement createIf(int line, int column, Expression<? extends Type> condition, Statement then, Statement otherwise) {
		return new IfStatement(getProgram(), line, column, condition, then, otherwise);
	}

	@Override
	public Statement createWhile(int line, int column, Expression<? extends Type> condition, Statement body) {
		return new WhileStatement(getProgram(), line, column, condition, body);
	}
	
	@Override
	public Statement createSequence(int line, int column, List<Statement> statements) {
		return new SequenceStatement(getProgram(), line, column, statements);
	}

	@Override
	public Statement createPrint(int line, int column, Expression<? extends Type> e) {
		return new PrintStatement(getProgram(), line, column, e);
	}

	@Override
	public Statement createForeach(int line, int column, worms.model.programs.ProgramFactory.ForeachType type, String variableName, Statement body) {
		return null;
	}

	
	@Override
	public DoubleType createDoubleType() {
		return new DoubleType();
	}

	@Override
	public BoolType createBooleanType() {
		return new BoolType();
	}

	@Override
	public EntityType createEntityType() {
		return new EntityType();
	}


	}