package eu.dzhw.zofar.testing.condition.term;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.ParserContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.SpelNode;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import eu.dzhw.zofar.testing.condition.term.elements.Element;
import eu.dzhw.zofar.testing.condition.term.elements.ElementVector;

public class TermAnalyzerClient implements Serializable {

	private static final long serialVersionUID = 3105933797437419268L;
	private final static TermAnalyzerClient INSTANCE = new TermAnalyzerClient();

	private TermAnalyzerClient() {
		super();
	}

	public static TermAnalyzerClient getInstance() {
		return INSTANCE;
	}

	public ElementVector mergeVectors(final ElementVector individual, final ElementVector initial) {
		final Map<String, Set<Element>> individualTupels = individual.getTupels();
		final Map<String, Set<Element>> initialTupels = initial.getTupels();
		if (initialTupels != null) {
			for (Map.Entry<String, Set<Element>> initialTupel : initialTupels.entrySet()) {
				if (!individualTupels.containsKey(initialTupel.getKey())) {
					individualTupels.put(initialTupel.getKey(), initialTupel.getValue());
				}
			}
		}
		final ElementVector back = new ElementVector();
		back.setTupels(individualTupels);
		return back;
	}

	public Map<String, ElementVector> analyze(final String expression, final Map<String, Object> contextData,
			final ElementVector initial,final boolean escalateExceptions) throws Exception {
		final Map<String, ElementVector> container = new LinkedHashMap<String, ElementVector>();

		final StandardEvaluationContext evaluationContext = new StandardEvaluationContext() {
		};
		final PropertyAccessor accessor = new PropertyAccessor() {

			public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
				return true;
			}

			public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
				if (target != null) {

					try {
						Field field = target.getClass().getDeclaredField(name);
						field.setAccessible(true);
						Object value = field.get(target);
						TypedValue back = new TypedValue(value);
						return back;
					} catch (Exception e) {
						// e.printStackTrace();
					}
					return null;

				} else {
					Object contextItem = null;
					if (name.equals("zofar"))contextItem = contextData.get(name);
					TypedValue back = new TypedValue(contextItem);
					return back;
				}
			}

			public Class<?>[] getSpecificTargetClasses() {
				return null;
			}

			public boolean canWrite(EvaluationContext context, Object target, String name) throws AccessException {
				return true;
			}

			public void write(EvaluationContext context, Object target, String name, Object newValue)
					throws AccessException {
			}
		};
		evaluationContext.addPropertyAccessor(accessor);

		final ExpressionParser parser = new SpelExpressionParser() {
			public Expression parseExpression(String expressionString, ParserContext context) throws ParseException {
				SpelExpression back = (SpelExpression) super.parseExpression(expressionString, context);
				final SpelNode ast = back.getAST();
				try {
					walkAst(ast, initial, container, new HashMap<SpelNode, SpelNode>(), evaluationContext);
				} catch (Exception e) {
//					throw new ParseException(0, ast.toStringAST() + " ==> " + e.getMessage(), e);
					throw new ParseException(0, e.getMessage(), e);
				}
				return back;
			}
		};
		try {
			Expression exp = parser.parseExpression(expression);
			Object message = (Object) exp.getValue(evaluationContext);

		} catch (Exception e) {
			if ((org.springframework.expression.ParseException.class).isAssignableFrom(e.getClass())) {
				if(escalateExceptions)throw e;
				else System.err.println("["+expression+"] ==> "+e.getMessage());
			}
		}
		return container;
	}

	private static void walkAst(SpelNode node, ElementVector initial, Map<String, ElementVector> container,
			final Map<SpelNode, SpelNode> parentMap, EvaluationContext context) throws Exception {
		walkAst(node, true, initial, container, parentMap, context);
	}

	private static void walkAst(SpelNode node, final boolean first, ElementVector initial,
			Map<String, ElementVector> container, final Map<SpelNode, SpelNode> parentMap, EvaluationContext context)
			throws Exception {
		if (first)
			container.clear();

		int childCount = node.getChildCount();
		for (int a = 0; a < childCount; a++) {
			SpelNode child = node.getChild(a);
			parentMap.put(child, node);
			walkAst(child, false, initial, container, parentMap, context);
		}

		if ((org.springframework.expression.spel.ast.PropertyOrFieldReference.class)
				.isAssignableFrom(node.getClass())) {
			org.springframework.expression.spel.ast.PropertyOrFieldReference tmp = (org.springframework.expression.spel.ast.PropertyOrFieldReference) node;
			// System.out.println("PropertyOrFieldReference : " +
			// tmp.toStringAST() + " ("+parentMap.get(tmp).getClass()+")");
			if ((org.springframework.expression.spel.ast.MethodReference.class)
					.isAssignableFrom(parentMap.get(tmp).getClass())) {
				ElementVector newVector = new ElementVector();
				newVector.getTupels().put(tmp.toStringAST(), initial.getTupels().get(tmp.toStringAST() + ".value"));
				container.put(tmp.hashCode() + "", newVector);
			}
		} else if ((org.springframework.expression.spel.ast.CompoundExpression.class)
				.isAssignableFrom(node.getClass())) {
			org.springframework.expression.spel.ast.CompoundExpression tmp = (org.springframework.expression.spel.ast.CompoundExpression) node;
			// System.out.println("CompoundExpression : " + tmp.toStringAST()+
			// "("+parentMap.get(tmp).getClass()+")");

			ElementVector newVector = new ElementVector();

			boolean found = false;
			final int tmpChildCount = tmp.getChildCount();
			for (int a = 0; a < tmpChildCount; a++) {
				final SpelNode tmpChild = tmp.getChild(a);
				if (container.containsKey(tmpChild.hashCode() + "")) {
					newVector = container.get(tmpChild.hashCode() + "");
					found = true;
					break;
				}
			}
			if (!found) {
				final Set<Element> variableTupel = initial.getTupels().get(tmp.toStringAST());
				if(variableTupel == null) throw new ParseException(0,"no Tupel found for "+tmp.toStringAST());
				Set<Element> vector = new LinkedHashSet<Element>();
				if (!parentMap.containsKey(node)) {
					for (Element tmp1 : variableTupel) {
						Object valueObj = tmp1.getValue();
						if (((Boolean.class).isAssignableFrom(valueObj.getClass())) && (!((Boolean) valueObj)))
							continue;
						vector.add(tmp1);
					}
				} else {
					for (Element tmp1 : variableTupel) {
						vector.add(tmp1);
					}
				}
				newVector.getTupels().put(tmp.toStringAST(), vector);

			}

			SpelNode parent = parentMap.get(tmp);
			if (parent != null) {
				if ((org.springframework.expression.spel.ast.OpAnd.class).isAssignableFrom(parent.getClass())) {
					// System.out.println("clean "+parent.toStringAST()+"
					// ("+tmp.toStringAST()+")");
					Map<String, Set<Element>> map = newVector.getTupels();
					if (map.containsKey(tmp.toStringAST())) {
						Set<Element> set = map.get(tmp.toStringAST());
						Set<Element> newSet = new LinkedHashSet<Element>();
						for (Element element : set) {
							if (element.getValue().equals(true))
								newSet.add(element);
						}
						map.put(tmp.toStringAST(), newSet);
					}
					newVector.setTupels(map);
				}
			}

			for (int a = 0; a < childCount; a++) {
				final SpelNode tmpChild = tmp.getChild(a);
				container.remove(tmpChild.hashCode() + "");
			}
			container.put(tmp.hashCode() + "", newVector);
			// System.out.println("OpCompound : " + tmp.toStringAST() + " ==> "
			// + newVector);
		} else if ((org.springframework.expression.spel.ast.OpEQ.class).isAssignableFrom(node.getClass())) {
			org.springframework.expression.spel.ast.OpEQ tmp = (org.springframework.expression.spel.ast.OpEQ) node;
			// System.out.println("OpEQ : " + tmp.toStringAST());

			final ElementVector leftVector = container.get(tmp.getLeftOperand().hashCode() + "");
			final ElementVector rightVector = container.get(tmp.getRightOperand().hashCode() + "");

			// System.out.println("\tEQ left
			// ("+tmp.getLeftOperand().toStringAST()+") => "+leftVector);
			// System.out.println("\tEQ right
			// ("+tmp.getRightOperand().toStringAST()+") => "+rightVector);

			ElementVector newVector = new ElementVector();
			if (leftVector != null) {
				Map<String, Set<Element>> leftTupels = leftVector.getTupels();
				Map<String, Set<Element>> rightTupels = rightVector.getTupels();

				boolean found = false;

				for (Map.Entry<String, Set<Element>> leftSet : leftTupels.entrySet()) {
					for (Element leftElement : leftSet.getValue()) {
						for (Map.Entry<String, Set<Element>> rightSet : rightTupels.entrySet()) {
							for (Element rightElement : rightSet.getValue()) {
								if (leftElement.getValue().equals(rightElement.getValue())) {
									Set<Element> newSet = null;
									if (newVector.getTupels().containsKey(leftSet.getKey()))
										newSet = newVector.getTupels().get(leftSet.getKey());
									if (newSet == null)
										newSet = new LinkedHashSet<Element>();
									newSet.add(rightElement);
									newVector.getTupels().put(leftSet.getKey(), newSet);
									found = true;
								}
							}
						}
					}
				}

				if (!found)
					throw new Exception("No equal Value found " + leftVector + " != " + rightVector + "");
			}
			container.remove(tmp.getLeftOperand().hashCode() + "");
			container.remove(tmp.getRightOperand().hashCode() + "");
			container.put(node.hashCode() + "", newVector);
			// System.out.println("OpEQ : " + tmp.toStringAST() + " ==> " +
			// newVector);

		} else if ((org.springframework.expression.spel.ast.OpNE.class).isAssignableFrom(node.getClass())) {
			org.springframework.expression.spel.ast.OpNE tmp = (org.springframework.expression.spel.ast.OpNE) node;
			// System.out.println("OpNE : " + tmp.toStringAST());

			ElementVector leftVector = container.get(tmp.getLeftOperand().hashCode() + "");
			ElementVector rightVector = container.get(tmp.getRightOperand().hashCode() + "");

			if ((leftVector != null) && (rightVector != null)) {
				boolean found = false;
				
				ElementVector newVectors = new ElementVector();

				for (Map.Entry<String, Set<Element>> leftTupel : leftVector.getTupels().entrySet()) {
					final String variable = leftTupel.getKey();
					final Set<Element> leftValues = leftTupel.getValue();
					for (Map.Entry<String, Set<Element>> rightTupel : rightVector.getTupels().entrySet()) {
						// final String rightVariable = rightTupel.getKey();
						final Set<Element> rightValues = rightTupel.getValue();

						if ((leftValues != null) && (rightValues != null)) {
							Set<Element> vector = new LinkedHashSet<Element>();
							for (final Element leftValue : leftValues) {
								for (final Element rightValue : rightValues) {
									if (!leftValue.getValue().equals(rightValue.getValue())) {
										vector.add(leftValue);
										found = true;
									}
								}
							}
							newVectors.getTupels().put(variable, vector);
						}
					}
				}
				
				if (!found)
					throw new Exception("No non-equal Value found (" + leftVector + ") != (" + rightVector + ")");

				container.remove(tmp.getLeftOperand().hashCode() + "");
				container.remove(tmp.getRightOperand().hashCode() + "");
				container.put(node.hashCode() + "", newVectors);
				// System.out.println("OpNE : " + tmp.toStringAST() + " ==> " +
				// newVectors);
			}
		} else if ((org.springframework.expression.spel.ast.OpGE.class).isAssignableFrom(node.getClass())) {
			org.springframework.expression.spel.ast.OpGE tmp = (org.springframework.expression.spel.ast.OpGE) node;
			// System.out.println("OpGE : " + tmp.toStringAST());
			ElementVector leftTermVector = container.get(tmp.getLeftOperand().hashCode() + "");
			ElementVector rightTermVector = container.get(tmp.getRightOperand().hashCode() + "");

			if ((leftTermVector != null) && (rightTermVector != null)) {
				ElementVector newVectors = new ElementVector();

				for (Map.Entry<String, Set<Element>> leftTupel : leftTermVector.getTupels().entrySet()) {
					final String variable = leftTupel.getKey();
					final Set<Element> leftValues = leftTupel.getValue();
					for (Map.Entry<String, Set<Element>> rightTupel : rightTermVector.getTupels().entrySet()) {
						// final String rightVariable = rightTupel.getKey();
						final Set<Element> rightValues = rightTupel.getValue();

						if ((leftValues != null) && (rightValues != null)) {
							Set<Element> vector = new LinkedHashSet<Element>();
							for (final Element leftValue : leftValues) {
								for (final Element rightValue : rightValues) {
									if (Double.parseDouble(leftValue.getValue() + "") >= Double
											.parseDouble(rightValue.getValue() + "")) {
										vector.add(leftValue);
									}
								}
							}
							newVectors.getTupels().put(variable, vector);
						}
					}
				}

				container.remove(tmp.getLeftOperand().hashCode() + "");
				container.remove(tmp.getRightOperand().hashCode() + "");
				container.put(node.hashCode() + "", newVectors);
				// System.out.println("OpGE : " + tmp.toStringAST() + " ==> " +
				// newVectors);
			}
		} else if ((org.springframework.expression.spel.ast.OpGT.class).isAssignableFrom(node.getClass())) {
			org.springframework.expression.spel.ast.OpGT tmp = (org.springframework.expression.spel.ast.OpGT) node;
			// System.out.println("OpGT : " + tmp.toStringAST());
			ElementVector leftTermVector = container.get(tmp.getLeftOperand().hashCode() + "");
			ElementVector rightTermVector = container.get(tmp.getRightOperand().hashCode() + "");

			if ((leftTermVector != null) && (rightTermVector != null)) {
				ElementVector newVectors = new ElementVector();

				for (Map.Entry<String, Set<Element>> leftTupel : leftTermVector.getTupels().entrySet()) {
					final String variable = leftTupel.getKey();
					final Set<Element> leftValues = leftTupel.getValue();
					for (Map.Entry<String, Set<Element>> rightTupel : rightTermVector.getTupels().entrySet()) {
						// final String rightVariable = rightTupel.getKey();
						final Set<Element> rightValues = rightTupel.getValue();

						if ((leftValues != null) && (rightValues != null)) {
							Set<Element> vector = new LinkedHashSet<Element>();
							for (final Element leftValue : leftValues) {
								for (final Element rightValue : rightValues) {
									if (Double.parseDouble(leftValue.getValue() + "") > Double
											.parseDouble(rightValue.getValue() + "")) {
										vector.add(leftValue);
									}
								}
							}
							newVectors.getTupels().put(variable, vector);
						}
					}
				}

				container.remove(tmp.getLeftOperand().hashCode() + "");
				container.remove(tmp.getRightOperand().hashCode() + "");
				container.put(node.hashCode() + "", newVectors);
				// System.out.println("OpGT : " + tmp.toStringAST() + " ==> " +
				// newVectors);
			}

		} else if ((org.springframework.expression.spel.ast.OpLE.class).isAssignableFrom(node.getClass())) {
			org.springframework.expression.spel.ast.OpLE tmp = (org.springframework.expression.spel.ast.OpLE) node;
			// System.out.println("OpLE : " + tmp.toStringAST());
			ElementVector leftTermVector = container.get(tmp.getLeftOperand().hashCode() + "");
			ElementVector rightTermVector = container.get(tmp.getRightOperand().hashCode() + "");
			// System.out.println("left : "+leftTermVector);
			// System.out.println("right : "+rightTermVector);

			if ((leftTermVector != null) && (rightTermVector != null)) {
				ElementVector newVectors = new ElementVector();

				for (Map.Entry<String, Set<Element>> leftTupel : leftTermVector.getTupels().entrySet()) {
					final String variable = leftTupel.getKey();
					final Set<Element> leftValues = leftTupel.getValue();
					for (Map.Entry<String, Set<Element>> rightTupel : rightTermVector.getTupels().entrySet()) {
						// final String rightVariable = rightTupel.getKey();
						final Set<Element> rightValues = rightTupel.getValue();

						if ((leftValues != null) && (rightValues != null)) {
							Set<Element> vector = new LinkedHashSet<Element>();
							for (final Element leftValue : leftValues) {
								for (final Element rightValue : rightValues) {
									if (Double.parseDouble(leftValue.getValue() + "") <= Double
											.parseDouble(rightValue.getValue() + "")) {
										vector.add(leftValue);
									}
								}
							}
							newVectors.getTupels().put(variable, vector);
						}
					}
				}

				container.remove(tmp.getLeftOperand().hashCode() + "");
				container.remove(tmp.getRightOperand().hashCode() + "");
				container.put(node.hashCode() + "", newVectors);
				// System.out.println("OpLE : " + tmp.toStringAST() + " ==> " +
				// newVectors);
			}

		} else if ((org.springframework.expression.spel.ast.OpLT.class).isAssignableFrom(node.getClass())) {
			org.springframework.expression.spel.ast.OpLT tmp = (org.springframework.expression.spel.ast.OpLT) node;
			// System.out.println("OpLT : " + tmp.toStringAST());
			ElementVector leftTermVector = container.get(tmp.getLeftOperand().hashCode() + "");
			ElementVector rightTermVector = container.get(tmp.getRightOperand().hashCode() + "");
			// System.out.println("left : "+leftTermVector);
			// System.out.println("right : "+rightTermVector);
			if ((leftTermVector != null) && (rightTermVector != null)) {
				ElementVector newVectors = new ElementVector();

				for (Map.Entry<String, Set<Element>> leftTupel : leftTermVector.getTupels().entrySet()) {
					final String variable = leftTupel.getKey();
					final Set<Element> leftValues = leftTupel.getValue();
					for (Map.Entry<String, Set<Element>> rightTupel : rightTermVector.getTupels().entrySet()) {
						// final String rightVariable = rightTupel.getKey();
						final Set<Element> rightValues = rightTupel.getValue();

						if ((leftValues != null) && (rightValues != null)) {
							Set<Element> vector = new LinkedHashSet<Element>();
							for (final Element leftValue : leftValues) {
								for (final Element rightValue : rightValues) {
									if (Double.parseDouble(leftValue.getValue() + "") < Double
											.parseDouble(rightValue.getValue() + "")) {
										vector.add(leftValue);
									}
								}
							}
							newVectors.getTupels().put(variable, vector);
						}
					}
				}

				container.remove(tmp.getLeftOperand().hashCode() + "");
				container.remove(tmp.getRightOperand().hashCode() + "");
				container.put(node.hashCode() + "", newVectors);
				// System.out.println("OpLT : " + tmp.toStringAST() + " ==> " +
				// newVectors);
			}

		} else if ((org.springframework.expression.spel.ast.OperatorNot.class).isAssignableFrom(node.getClass())) {
			org.springframework.expression.spel.ast.OperatorNot tmp = (org.springframework.expression.spel.ast.OperatorNot) node;
			// System.out.println("OperatorNot : " + tmp.toStringAST());

			SpelNode term = tmp.getChild(0);

			ElementVector newVectors = new ElementVector();

			ElementVector termVector = container.get(term.hashCode() + "");

			// //get all others
			Map<String, Set<Element>> termTupel = termVector.getTupels();
			Map<String, Set<Element>> initialTupel = initial.getTupels();

			final Set<Element> binarySet = new HashSet<Element>();
			binarySet.add(new Element(true));
			binarySet.add(new Element(false));

			for (Map.Entry<String, Set<Element>> variableTupel : initialTupel.entrySet()) {
				final String variable = variableTupel.getKey();
				final Set<Element> initials = variableTupel.getValue();
				final Set<Element> possibles = termTupel.get(variable);

				if (possibles != null) {
					if (possibles.equals(binarySet))
						possibles.remove(new Element(false));
					Set<Element> vector = new LinkedHashSet<Element>();
					for (Element tmp1 : initials) {
						if (!possibles.contains(tmp1)) {
							vector.add(tmp1);
						}
					}
					newVectors.getTupels().put(variable, vector);
				}
			}

			// System.out.println("OperatorNot : " + tmp.toStringAST() + " ==> "
			// + newVectors);
			container.remove(term.hashCode() + "");
			container.put(node.hashCode() + "", newVectors);
		} else if ((org.springframework.expression.spel.ast.StringLiteral.class).isAssignableFrom(node.getClass())) {
			org.springframework.expression.spel.ast.StringLiteral tmp = (org.springframework.expression.spel.ast.StringLiteral) node;
			// System.out.println("StringLiteral : " + tmp.toStringAST());
			ElementVector newVector = new ElementVector();
			Set<Element> vector = new LinkedHashSet<Element>();
			vector.add(new Element(tmp.toStringAST()));
			newVector.getTupels().put(tmp.toStringAST(), vector);
			container.put(tmp.hashCode() + "", newVector);
		} else if ((org.springframework.expression.spel.ast.BooleanLiteral.class).isAssignableFrom(node.getClass())) {
			org.springframework.expression.spel.ast.BooleanLiteral tmp = (org.springframework.expression.spel.ast.BooleanLiteral) node;
			// System.out.println("BooleanLiteral : " + tmp.toStringAST());
			ElementVector newVector = new ElementVector();
			Set<Element> vector = new LinkedHashSet<Element>();
			vector.add(new Element(tmp.toStringAST()));
			newVector.getTupels().put(tmp.toStringAST(), vector);
			container.put(tmp.hashCode() + "", newVector);
		} else if ((org.springframework.expression.spel.ast.IntLiteral.class).isAssignableFrom(node.getClass())) {
			org.springframework.expression.spel.ast.IntLiteral tmp = (org.springframework.expression.spel.ast.IntLiteral) node;
			// System.out.println("IntLiteral : " + tmp.toStringAST());
			ElementVector newVector = new ElementVector();
			Set<Element> vector = new LinkedHashSet<Element>();
			vector.add(new Element(tmp.toStringAST()));
			newVector.getTupels().put(tmp.toStringAST(), vector);
			container.put(tmp.hashCode() + "", newVector);
		} else if ((org.springframework.expression.spel.ast.OpOr.class).isAssignableFrom(node.getClass())) {
			org.springframework.expression.spel.ast.OpOr tmp = (org.springframework.expression.spel.ast.OpOr) node;
			// System.out.println("OpOr : " + tmp.toStringAST());
			SpelNode leftTerm = tmp.getLeftOperand();
			String leftKey = leftTerm.hashCode() + "";

			SpelNode rightTerm = tmp.getRightOperand();
			String rightKey = rightTerm.hashCode() + "";

			ElementVector leftVector = container.get(leftKey);
			ElementVector rightVector = container.get(rightKey);

			// System.out.println("\tOR left : "+leftTerm.toStringAST()+" ==>
			// "+leftVector);
			// System.out.println("\tOR right : "+rightTerm.toStringAST()+" ==>
			// "+rightVector);

			ElementVector newVectors = new ElementVector();

			if (leftVector != null) {
				for (Map.Entry<String, Set<Element>> variableTupel : leftVector.getTupels().entrySet()) {
					final String variable = variableTupel.getKey();
					final Set<Element> possibles = leftVector.getTupels().get(variable);

					if (possibles != null) {
						newVectors.getTupels().put(variable, possibles);
					}
				}
			}

			if (rightVector != null) {
				for (Map.Entry<String, Set<Element>> variableTupel : rightVector.getTupels().entrySet()) {
					final String variable = variableTupel.getKey();
					final Set<Element> possibles = rightVector.getTupels().get(variable);

					final Set<Element> leftPossibles = newVectors.getTupels().get(variable);

					final Set<Element> newPossibles = new HashSet<Element>();
					if (leftPossibles != null)
						newPossibles.addAll(leftPossibles);
					if (possibles != null)
						newPossibles.addAll(possibles);
					newVectors.getTupels().put(variable, newPossibles);

				}
			}

			container.remove(leftTerm.hashCode() + "");
			container.remove(rightTerm.hashCode() + "");
			container.put(node.hashCode() + "", newVectors);
			// System.out.println("OperatorOR : " + tmp.toStringAST() + " ==> "
			// + newVectors);

		} else if ((org.springframework.expression.spel.ast.OpAnd.class).isAssignableFrom(node.getClass())) {
			org.springframework.expression.spel.ast.OpAnd tmp = (org.springframework.expression.spel.ast.OpAnd) node;
			// System.out.println("OpAnd : " + tmp.toStringAST());
			SpelNode leftTerm = tmp.getLeftOperand();
			String leftKey = leftTerm.hashCode() + "";

			SpelNode rightTerm = tmp.getRightOperand();
			String rightKey = rightTerm.hashCode() + "";

			ElementVector leftVector = container.get(leftKey);
			ElementVector rightVector = container.get(rightKey);

			// System.out.println("\tAND left : "+leftTerm.toStringAST()+" ==>
			// "+leftVector);
			// System.out.println("\tAND right : "+rightTerm.toStringAST()+" ==>
			// "+rightVector);

			ElementVector newVectors = new ElementVector();

			for (Map.Entry<String, Set<Element>> variableTupel : leftVector.getTupels().entrySet()) {
				final String variable = variableTupel.getKey();
				final Set<Element> possibles = leftVector.getTupels().get(variable);
				if (possibles != null) {
					Set<Element> otherPossibles = rightVector.getTupels().get(variable);
					if (otherPossibles == null)
						otherPossibles = initial.getTupels().get(variable);
					if (otherPossibles != null) {
						Set<Element> vector = new LinkedHashSet<Element>();
						for (Element possible : possibles) {
							vector.add(possible);
						}
						newVectors.getTupels().put(variable, vector);
					}
				}
			}

			for (Map.Entry<String, Set<Element>> variableTupel : rightVector.getTupels().entrySet()) {
				final String variable = variableTupel.getKey();
				final Set<Element> possibles = rightVector.getTupels().get(variable);
				if (possibles != null) {
					Set<Element> otherPossibles = leftVector.getTupels().get(variable);
					if ((otherPossibles == null))
						otherPossibles = initial.getTupels().get(variable);

					if (otherPossibles != null) {
						Set<Element> vector = new LinkedHashSet<Element>();
						for (Element possible : possibles) {
							vector.add(possible);
						}
						newVectors.getTupels().put(variable, vector);
					}
				}
			}
			container.remove(leftTerm.hashCode() + "");
			container.remove(rightTerm.hashCode() + "");
			container.put(node.hashCode() + "", newVectors);

			// System.out.println("OperatorAND : " + tmp.toStringAST() + " ==> "
			// + newVectors);
		} else if ((org.springframework.expression.spel.ast.MethodReference.class).isAssignableFrom(node.getClass())) {
			org.springframework.expression.spel.ast.MethodReference tmp = (org.springframework.expression.spel.ast.MethodReference) node;

			final String method = tmp.toStringAST();

			ElementVector newVectors = new ElementVector();

			if (method.startsWith("asNumber(")) {
				final int methodChildCount = tmp.getChildCount();
				for (int k = 0; k < methodChildCount; k++) {
					SpelNode methodChildNode = tmp.getChild(k);
					ElementVector childVector = container.get(methodChildNode.hashCode() + "");

					// System.out.println("asNumber Child : "+childVector);

					for (Map.Entry<String, Set<Element>> variableTupel : childVector.getTupels().entrySet()) {
						final String variable = variableTupel.getKey() + ".value";
						final Set<Element> possibles = variableTupel.getValue();

						if (possibles != null) {
							Set<Element> resultValues = null;
							if (newVectors.getTupels().containsKey(variable))
								resultValues = newVectors.getTupels().get(variable);
							if (resultValues == null)
								resultValues = new LinkedHashSet<Element>();
							resultValues.addAll(possibles);
							newVectors.getTupels().put(variable, resultValues);
						}
					}
					container.remove(methodChildNode.hashCode() + "");
				}
			} else if (method.startsWith("isMissing(")) {
				final int methodChildCount = tmp.getChildCount();
				for (int k = 0; k < methodChildCount; k++) {
					SpelNode methodChildNode = tmp.getChild(k);
					ElementVector childVector = container.get(methodChildNode.hashCode() + "");
					if (childVector.getTupels().containsKey(methodChildNode.toStringAST()))
						childVector.getTupels().remove(methodChildNode.toStringAST());
					childVector.getTupels().put(methodChildNode.toStringAST() + ".value", new LinkedHashSet<Element>());
					newVectors.getTupels().putAll(childVector.getTupels());
					container.remove(methodChildNode.hashCode() + "");
				}
			}
			container.put(node.hashCode() + "", newVectors);
			// System.out.println("Method " + tmp.toStringAST() + " => " +
			// newVectors);

		} else {
			System.out.println("Node : (" + node.getClass() + ") " + node);
		}
	}

}
