/**
 *
 */
package de.his.zofar.presentation.surveyengine;

import java.util.Map;

import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.controller.SessionController;
import de.his.zofar.presentation.surveyengine.util.JsfUtility;

/**
 * @author le
 *
 */
public abstract class AbstractLabeledAnswerBean extends AbstractAnswerBean {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractLabeledAnswerBean.class);

    /**
     *
     */
    private static final long serialVersionUID = 8239039866530156642L;

    private static final String EL_PATTERN = "#{%s}";

    /**
     * @param sessionController
     * @param variableName
     */
    public AbstractLabeledAnswerBean(final SessionController sessionController,
            final String variableName) {
        super(sessionController, variableName);
    }

    protected abstract Map<String, String> loadLabels();

    public final String getLabel() {
        final Map<String, String> labels = this.loadLabels();

        String result = "";

        final FacesContext context = FacesContext.getCurrentInstance();

        for (final String visibleCondition : labels.keySet()) {
            final Boolean visible = JsfUtility.getInstance()
                    .evaluateValueExpression(context,
                            String.format(EL_PATTERN, visibleCondition),
                            Boolean.class);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("visible condition: {}, result: {}",
                        visibleCondition, visible);
            }
            if (visible) {
                result = JsfUtility.getInstance()
                        .evaluateValueExpression(
                                context,
                                String.format(EL_PATTERN,
                                        labels.get(visibleCondition)),
                                String.class);
                break;
            }
        }

        return result;
    }

}
