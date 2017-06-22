package expressivo;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import lib6005.parser.*;

/**
 * An immutable data type representing a polynomial expression of: + and *
 * nonnegative integers and floating-point numbers variables (case-sensitive
 * nonempty strings of letters)
 * 
 * <p>
 * PS1 instructions: this is a required ADT interface. You MUST NOT change its
 * name or package or the names or type signatures of existing methods. You may,
 * however, add additional methods, or strengthen the specs of existing methods.
 * Declare concrete variants of Expression in their own Java source files.
 */
public interface Expression {

	// Datatype definition
	// Expression =
	// Constant(n: double)
	// + Variable(s: String)
	// + Plus(left: Expression, right: Expression)
	// + Multiply (left: Expression, right: Expression)

	enum IntegerGrammar {
		ROOT, EXPRESSION, ADDITION, SUBTRACTION, TERM, PRODUCT, DIVISION, FACTOR, NUMBER, VARIABLE, WHITESPACE
	};

	/**
	 * Parse an expression.
	 * 
	 * @param input
	 *            expression to parse, as defined in the PS1 handout.
	 * @return expression AST for the input
	 * @throws IllegalArgumentException
	 *             if the expression is invalid
	 */
	public static Expression parse(String input) throws IllegalArgumentException {
		Expression res = null;
		try {
			Parser<IntegerGrammar> parser = GrammarCompiler.compile(new File("./src/expressivo/Expression.g"),
					IntegerGrammar.ROOT);
			ParseTree<IntegerGrammar> tree = parser.parse(input.trim());

			// System.out.println(tree.toString());
			//tree.display();
			res = buildAST(tree);
		} catch (UnableToParseException | IOException e) {
			System.err.println("Exception" + e.toString());
			throw new IllegalArgumentException();
		}
		return res;
	}

	public static Expression buildAST(ParseTree<IntegerGrammar> p) {

		switch (p.getName()) {
		case NUMBER:
		case VARIABLE:
			throw new RuntimeException("You should never reach here:" + p);
		case ROOT:
		case EXPRESSION:
		case TERM:
		case FACTOR:
			if (!p.childrenByName(IntegerGrammar.TERM).isEmpty()) {
				return buildAST(p.childrenByName(IntegerGrammar.TERM).get(0));
			} else if (!p.childrenByName(IntegerGrammar.EXPRESSION).isEmpty()) {
				return buildAST(p.childrenByName(IntegerGrammar.EXPRESSION).get(0));
			} else if (!p.childrenByName(IntegerGrammar.ADDITION).isEmpty()) {
				return buildAST(p.childrenByName(IntegerGrammar.ADDITION).get(0));
			} else if (!p.childrenByName(IntegerGrammar.PRODUCT).isEmpty()) {
				return buildAST(p.childrenByName(IntegerGrammar.PRODUCT).get(0));
			} else if (!p.childrenByName(IntegerGrammar.NUMBER).isEmpty()) {
				return new Constant(Double.parseDouble(p.childrenByName(IntegerGrammar.NUMBER).get(0).getContents()));
			} else if (!p.childrenByName(IntegerGrammar.VARIABLE).isEmpty()) {
				return new Variable(p.childrenByName(IntegerGrammar.VARIABLE).get(0).getContents());
			}
			throw new RuntimeException("You should never reach here:" + p);
		case PRODUCT:
		case ADDITION:
			boolean first = false;
			ParseTree<IntegerGrammar> firstChild = null;
			for (ParseTree<IntegerGrammar> child : p.children()) {
				if (child.getName() == IntegerGrammar.WHITESPACE) {
					continue;
				}
				if (!first) {
					firstChild = child;
					first = true;
				} else {
					if (p.getName() == IntegerGrammar.ADDITION) {
						return new Addition(buildAST(firstChild), buildAST(child));
					} else if (p.getName() == IntegerGrammar.PRODUCT) {
						return new Multiply(buildAST(firstChild), buildAST(child));
					}
				}

			}
		case DIVISION:
		case SUBTRACTION:
		case WHITESPACE:
			/*
			 * Since we are always avoiding calling buildAST with whitespace,
			 * the code should never make it here.
			 */
			throw new RuntimeException("You should never reach here:" + p);
		}
		/*
		 * The compiler should be smart enough to tell that this code is
		 * unreachable, but it isn't.
		 */
		throw new RuntimeException("You should never reach here:" + p);
	}

	/**
	 * @return a parsable representation of this expression, such that for all
	 *         e:Expression, e.equals(Expression.parse(e.toString())).
	 */
	@Override
	public String toString();

	/**
	 * @param thatObject
	 *            any object
	 * @return true if and only if this and thatObject are structurally-equal
	 *         Expressions, as defined in the PS1 handout.
	 */
	@Override
	public boolean equals(Object thatObject);

	/**
	 * @return hash code value consistent with the equals() definition of
	 *         structural equality, such that for all e1,e2:Expression,
	 *         e1.equals(e2) implies e1.hashCode() == e2.hashCode()
	 */
	@Override
	public int hashCode();

	/**
	 * Constructs an expression representing the derivative of this expression
	 * with respect to a given variable
	 * 
	 * @param var
	 *            the variable represented by an Expression object with respect
	 *            to which the expression is differentiated
	 * @return an Expression object representing the differentiated form of this
	 *         object
	 */
	public Expression differentiate(Expression var);

	/**
	 * Constructs an expression by simplifying this expression by evaluating it
	 * using a mapping of variable names to numerical values. If the expression
	 * only contains numerical values as a result of simplification returns an
	 * Expression representing a single number.
	 * 
	 * @param values
	 *            a mapping of variables to numerical values at which they
	 *            should be evaluated. Requires that the numerical values are
	 *            positive
	 * @return an Expression object represented the simplified form of this
	 *         object with
	 */
	public Expression simplify(Map<Expression, Double> values);
	/*
	 * Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
	 * Redistribution of original or derived work requires permission of course
	 * staff.
	 */
}
