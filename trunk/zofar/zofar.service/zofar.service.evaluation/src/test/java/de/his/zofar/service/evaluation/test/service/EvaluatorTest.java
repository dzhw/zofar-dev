package de.his.zofar.service.evaluation.test.service;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import de.his.zofar.service.common.util.CoveredMap;
import de.his.zofar.service.evaluation.service.EvaluationService;
import de.his.zofar.service.evaluation.service.interfaces.EvaluatorContextHolder;
import de.his.zofar.service.evaluation.service.interfaces.impl.EvaluatorContextHolderProxy;
import de.his.zofar.service.test.AbstractServiceTest;

public class EvaluatorTest extends AbstractServiceTest {
	
	
	private class Answer{

		private Object answerValue;
		private String answerLabel;
		
		public Answer(){
			super();
		}

		@SuppressWarnings("unused")
		public Object getAnswerValue() {
			return answerValue;
		}

		public void setAnswerValue(final Object answerValue) {
			this.answerValue = answerValue;
		}

		public String getAnswerLabel() {
			return answerLabel;
		}

		public void setAnswerLabel(final String answerLabel) {
			this.answerLabel = answerLabel;
		}
	};
	

    @Inject
    private EvaluationService evalationService;
    private EvaluatorContextHolder evaluatorContextHolder;
	private Map<String, Answer> valueMap;
    
	
	@Test
	public void checkEvaluator() {
        final Answer optionA = new Answer();
        optionA.setAnswerValue(2L);
        this.getValueMap().put("gender", optionA);
                
        final boolean result = this.evaluate("__gender != 1", this.getEvaluatorContextHolder());
        Assert.assertEquals(true, result);
	}
	
	public Object getEmptyEntry(final String obj) {
		if (obj == null)
			return null;

		final Answer back = new Answer();
		back.setAnswerLabel("");

		back.setAnswerValue(Long.MIN_VALUE);
		return back;
	}
	
	
	public EvaluatorContextHolder getEvaluatorContextHolder() {
		if (evaluatorContextHolder == null) {
			evaluatorContextHolder = new EvaluatorContextHolderProxy() {

				private static final long serialVersionUID = -5077903088724688513L;

				@Override
				public Map<String, ?> getEvaluatorMap() {
					return EvaluatorTest.this.getValueMap();
				}

				@Override
				public String getLabel(final Object obj) {
					if (obj == null)
						return null;
					if ((Answer.class).isAssignableFrom(obj.getClass())) {
						final Answer tmp = (Answer) obj;
						return tmp.getAnswerLabel();
					}
					return null;
				}
			};
		}
		return evaluatorContextHolder;
	}
	
	
	
	@Transactional(readOnly = true)
	public boolean evaluate(final String condition, final EvaluatorContextHolder contextHolder) {
		final boolean result = evalationService.evaluate(condition, contextHolder);
		return result;
	}
	
	@Transactional(readOnly = true)
	public String evaluateText(final String text, final EvaluatorContextHolder contextHolder) {
		final String result = evalationService.evaluateText(text, contextHolder);
		return result;
	}
	
	public Map<String, Answer> getValueMap() {
		if (valueMap == null) {
			this.valueMap = new CoveredMap<String, Answer>(
					new HashMap<String, Answer>(), this, "getEmptyEntry",
					String.class);
		}
		return valueMap;
	}

}
