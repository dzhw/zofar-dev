package support.elements.conditionEvaluation;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.ParticipantEntity;

public class ZofarBean implements Serializable {

	private static final long serialVersionUID = 1426727371724620280L;
	final static Logger LOGGER = LoggerFactory.getLogger(ZofarBean.class);
//	private final static DecimalFormatSymbols customFormatSymbols = new DecimalFormatSymbols(Locale.GERMAN);

	private final Map<String, String> missings;
	private final Map<String, Map<String, String>> values;

	public ZofarBean(final Map<String, String> missings, final Map<String, Map<String, String>> values) {
		super();
		this.missings = missings;
		this.values = values;
		// LOGGER.info("init");
	}

	public Boolean isSet(final VariableBean var, final ParticipantEntity participant) {
//		LOGGER.info("isSet {}", var);
		if ((var != null) && (participant != null)) {
			final String varName = var.getVariableName();
			return (participant.getSurveyData().containsKey(varName));
		}
		return false;
	}
	
	public Boolean isMissing(final VariableBean var) {
//		LOGGER.info("isMissing {}", var);
		boolean back = true;
		if (var != null) {
			final Object value = var.getValue();
			back = ((value == null)||(value.equals("")));
			if(!back){
				//check for selected missing Answer Option
				back = (value.equals(missings.get(var.getVariableName())));
			}
		}
		return back;
	}

//	public Boolean isMissing(final VariableBean var) {
//		boolean back = true;
//		if (var != null) {
//			final Object value = var.getValue();
//			back = (value == null);
//		}
//		LOGGER.info("isMissing {} ==> {}", var,back);
//		return back;
//	}

	public Double asNumber(final Object input) {
//		LOGGER.info("asNumber {}", input);
		if (input == null)
			return 0D;
		Object toConvert = input;
		if ((VariableBean.class).isAssignableFrom(input.getClass())) {
			final VariableBean tmp = (VariableBean) input;
			toConvert = tmp.getValue();
			boolean found = false;
			if (this.values != null) {
				if (this.values.containsKey(tmp.getVariableName())) {
					Map<String, String> tmpValues = this.values.get(tmp.getVariableName());
					if ((tmpValues != null) && (tmpValues.containsKey(tmp.getValue()))) {
						toConvert = tmpValues.get(tmp.getValue());
						found = true;
					}
				}
			}
			if (!found && (this.missings != null)) {
				if (this.missings.containsKey(tmp.getVariableName())) {
					toConvert = this.missings.get(tmp.getVariableName());
				}
			}

			try {
				return Double.parseDouble(toConvert + "");
			} catch (NumberFormatException exp) {
				// if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("keine Zahl {}", input);
				// }
			}
		}
		return 0D;
	}

//	public Double asNumber(final Object input, final int digits) {
//		if (digits <= 0)
//			return this.asNumber(input);
//		final Double back = new Double(new DecimalFormat("#." + StringUtils.repeat("#", digits), customFormatSymbols).format(this.asNumber(input)));
//		return back;
//	}

	public String difference(final Double value1, final Double value2) {

		int val1 = 0;
		int val2 = 0;
		try {
			if (value1 != null) {
				val1 = Integer.parseInt(value1 + "");
			} else {
				val1 = 0;
			}
			if (value2 != null) {
				val2 = Integer.parseInt(value2 + "");
			} else {
				val2 = 0;
			}
		} catch (NumberFormatException exp) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("converting {} value {} to number failed");
			}

		}
		return "" + (val1 - val2);
	}

	public synchronized List<Object> list(final Object var1, final Object var2, final Object var3, final Object var4, final Object var5, final Object var6, final Object var7, final Object var8) {
		return listHelper(var1, var2, var3, var4, var5, var6, var7, var8);
	}

	public synchronized List<Object> list(final Object var1, final Object var2, final Object var3, final Object var4, final Object var5, final Object var6, final Object var7, final Object var8, final Object var9) {
		return listHelper(var1, var2, var3, var4, var5, var6, var7, var8, var9);
	}

	public synchronized List<Object> list(final Object var1, final Object var2, final Object var3, final Object var4, final Object var5, final Object var6, final Object var7, final Object var8, final Object var9, final Object var10) {
		return listHelper(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
	}

	private List<Object> listHelper(final Object... objects) {
		return (List<Object>) Arrays.asList(objects);
	}

	public synchronized Integer isSetCounter(final List<VariableBean> vars, final ParticipantEntity participant) {
//		LOGGER.info("isSetCounter");
		if (vars != null) {
			int back = 0;
			for (Object var : vars) {
				if (((VariableBean.class).isAssignableFrom(var.getClass())) && (isBooleanSet((VariableBean) var, participant)))
					back = back + 1;
			}
			return back;
		}
		return 0;
	}

	public synchronized Boolean isBooleanSet(final VariableBean var, final ParticipantEntity participant) {
//		LOGGER.info("isBooleanSet");
		if (var == null)
			return false;
		if (participant == null)
			return false;
		if (participant.getSurveyData().containsKey(var.getVariableName()) && (var.getValue() + "").equals("true")) {
			return true;
		}

		return false;
	}
	
	public synchronized Object valueOf(final support.elements.conditionEvaluation.VariableBean var) {
		// LOGGER.info("valueOf");
		if (var != null) return ((VariableBean) var).getValue();
		return null;
	}

}
