package de.his.zofar.presentation.surveyengine.ui.renderer.matrix.doubleMatrix;

import de.his.zofar.presentation.surveyengine.ui.renderer.answers.ZofarResponseDomainRenderer;

public abstract class ZofarAbstractDoubleMatrixRenderer extends ZofarResponseDomainRenderer {

    protected enum Side {
        LEFT, RIGHT;
    }
    protected static final String QUESTION = "question";
    protected static final String LEFT_QUESTION = "left";
    protected static final String RIGHT_QUESTION = "right";

}
