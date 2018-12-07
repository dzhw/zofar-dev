/*
 * Class to handle decisions
 */
package eu.dzhw.zofar.management.utils.bool;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * The Class DecisionClient.
 */
public class DecisionClient {
	
	/** The Constant INSTANCE. */
	private static final DecisionClient INSTANCE = new DecisionClient();

	/** The Constant LOGGER. */
	final static Logger LOGGER = LoggerFactory.getLogger(DecisionClient.class);

	/** The random. */
	private final Random random;

	/**
	 * Instantiates a new decision client.
	 */
	private DecisionClient() {
		super();
		this.random = new Random();
	}

	/**
	 * Gets the single instance of DecisionClient.
	 * 
	 * @return single instance of DecisionClient
	 */
	public static synchronized DecisionClient getInstance() {
		return INSTANCE;
	}

	/**
	 * Generate a random Boolean Decision with an probability of 50%.
	 * 
	 * @return random boolean
	 */
	public boolean randomBoolean() {
		return this.random.nextBoolean();
	}

	/**
	 * Generate a weighted random Boolean Decision.
	 * 
	 * @param percent
	 *            Value between 1 and 100
	 * @return random boolean
	 */
	public boolean randomBoolean(final int percent) {
		final double weight = ((double) percent) / 100;
		final double rnd = this.random.nextDouble();
		final boolean back = rnd < weight;
		return back;
	}
	
	public boolean evaluateSpel(final String condition, final Map<String,Object> data) throws Exception{
		try{
			final StandardEvaluationContext context = new StandardEvaluationContext();

			final PropertyAccessor accessor = new PropertyAccessor() {

				public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
					return true;
				}

				public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
//					LOGGER.info("read ({}) {}",target,name);
					if (target != null) {
						try {
							Field field = target.getClass().getDeclaredField(name);
							field.setAccessible(true);
							final Object value = field.get(target);
//							LOGGER.info("value ({}) {}",target+", "+value.getClass().getSimpleName(),name+"="+value);
							final TypedValue back =  new TypedValue(value);
							return back;
						} catch (Exception e) {

						}
						return null;

					} else{
						final TypedValue back =  new TypedValue(data.get(name));
						return back;
					}
				}

				public Class<?>[] getSpecificTargetClasses() {
					return null;
				}

				public boolean canWrite(EvaluationContext context, Object target, String name) throws AccessException {
					return true;
				}

				public void write(EvaluationContext context, Object target, String name, Object newValue) throws AccessException {
				}
			};
			

			context.addPropertyAccessor(accessor);

			ExpressionParser parser = new SpelExpressionParser();
			Expression exp = parser.parseExpression(condition);
//			LOGGER.info("evaluate : {}",condition);
			Boolean message = (Boolean) exp.getValue(context);
			if (LOGGER.isDebugEnabled())LOGGER.debug("{} => {}",condition,message);
			if(message != null)return message;
		}
		catch(Exception e){
			throw new Exception(e);
		}
		return false;
	}
}
