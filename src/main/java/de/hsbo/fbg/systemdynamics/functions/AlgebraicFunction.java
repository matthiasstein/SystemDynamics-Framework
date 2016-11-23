package de.hsbo.fbg.systemdynamics.functions;

import net.objecthunter.exp4j.Expression;

public class AlgebraicFunction implements IFunction {

	private Expression expression;
	
	public AlgebraicFunction(Expression expression){
		this.expression=expression;
	}
}
