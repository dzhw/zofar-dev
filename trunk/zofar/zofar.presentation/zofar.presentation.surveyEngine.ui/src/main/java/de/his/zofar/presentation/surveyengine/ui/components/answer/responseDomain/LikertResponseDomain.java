package de.his.zofar.presentation.surveyengine.ui.components.answer.responseDomain;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.common.Alignable;
import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.interfaces.IResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.likert.responsedomain.ZofarHorizontalLikertResponseDomainRenderer;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.likert.responsedomain.ZofarVerticalLikertResponseDomainRenderer;

/**
 * Renders a Likertscale. Each answer option will be rendered as a radio
 * button.
 *
 * @author Reitmann
 */
@FacesComponent(value = "org.zofar.LikertResponseDomain")
public class LikertResponseDomain extends UIInput implements IResponseDomain,Identificational,
        Alignable {
	
	public static final String COMPONENT_FAMILY = "org.zofar.LikertResponseDomain";

    public static final String CSS_CLASS_DELIM = ",";
    
    private enum PropertyKeys {
        showValues, itemClasses, missingSeparated, horizontal, labelPosition, alignAttached, direction
    }

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory
            .getLogger(LikertResponseDomain.class);

    private String[] rowClasses;

    private int cellCounter;

    public LikertResponseDomain() {
        super();
        // we render everything ourselves
//        this.setRendererType(null);
    }
    
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public String getRendererType() {
		Boolean isHorizontal = ((String)getAttributes().get("direction")).equals("horizontal");
		if(isHorizontal == null)isHorizontal = false;
        if (isHorizontal) {
        	return ZofarHorizontalLikertResponseDomainRenderer.RENDERER_TYPE;
        }
        else return ZofarVerticalLikertResponseDomainRenderer.RENDERER_TYPE;
	}

//    @Override
//    public void decode(final FacesContext context) {
//        final Map<String, String> requestMap = context.getExternalContext()
//                .getRequestParameterMap();
//        final String clientId = this.getClientId(context);
//        this.setSubmittedValue(requestMap.get(clientId));
//    }
//
//    private void encodeAnswerOptionsHorizontally(final ResponseWriter writer,
//            final String clientId, final List<SingleOption> answerOptions)
//            throws IOException {
//        for (final SingleOption answer : answerOptions) {
//            this.encodeRadioButtonCell(answer, writer, clientId);
//        }
//    }
//
//    private void encodeAnswerOptionsVertically(final FacesContext context,
//            final ResponseWriter writer, final String clientId,
//            final List<SingleOption> answerOptions) throws IOException {
//        for (final Iterator<SingleOption> it = answerOptions.iterator(); it
//                .hasNext();) {
//            final SingleOption answer = it.next();
//            this.encodeRadioButtonCell(answer, writer, clientId);
//            if (it.hasNext()) {
//                writer.endElement("tr");
//                writer.startElement("tr", this);
//            }
//        }
//    }
//
//    private void encodeAnswersAndScaleHeadersVertically(
//            final FacesContext context, final ResponseWriter writer,
//            final String clientId, final List<SingleOption> answerOptions)
//            throws IOException {
//        for (int i = 0; i < answerOptions.size(); i++) {
//            if (i == 0 || i == answerOptions.size() / 2
//                    || i == answerOptions.size() - 1) {
//                this.encodeScaleHeader(answerOptions.get(i), context, writer,
//                        clientId);
//            } else {
//                writer.startElement("td", this);
//                writer.endElement("td");
//            }
//            this.encodeRadioButtonCell(answerOptions.get(i), writer, clientId);
//            if (i < answerOptions.size() - 1) {
//                writer.endElement("tr");
//                writer.startElement("tr", this);
//            }
//        }
//    }
//
//    @Override
//    public void encodeBegin(final FacesContext context) throws IOException {
//        this.cellCounter = 0;
//        final ResponseWriter writer = context.getResponseWriter();
//        writer.startElement("table", this);
//        writer.writeAttribute("class", "zofar-likert-table", null);
//        writer.startElement("tr", this);
//    }
//
//    @Override
//    public void encodeChildren(final FacesContext context) throws IOException {
//        final ResponseWriter writer = context.getResponseWriter();
//        final String clientId = this.getClientId(context);
//
//        final List<SingleOption> answerOptions = new ArrayList<SingleOption>();
//        final List<SingleOption> missings = new ArrayList<SingleOption>();
//
//        for (final UIComponent child : this.getChildren()) {
//            if (SingleOption.class.isAssignableFrom(child.getClass())) {
//                final SingleOption currentChild = (SingleOption) child;
//                if (currentChild.isMissing()) {
//                    missings.add(currentChild);
//                } else {
//                    answerOptions.add(currentChild);
//                }
//            }
//        }
//
//        if (!answerOptions.isEmpty()) {
//            if (missings.isEmpty()) {
//                if (!this.isShowMidScaleHeader() && this.isHorizontalAligned()) {
//                    this.encodeScaleHeader(answerOptions.get(0), context, writer,
//                            clientId);
//                    this.encodeAnswerOptionsHorizontally(writer, clientId,
//                            answerOptions);
//                    this.encodeScaleHeader(
//                            answerOptions.get(answerOptions.size() - 1),
//                            context, writer, clientId);
//                } else if (!this.isShowMidScaleHeader() && !this.isHorizontalAligned()) {
//                    this.encodeScaleHeader(answerOptions.get(0), context, writer,
//                            clientId);
//                    writer.endElement("tr");
//                    writer.startElement("tr", this);
//                    this.encodeAnswerOptionsVertically(context, writer, clientId,
//                            answerOptions);
//                    writer.endElement("tr");
//                    writer.startElement("tr", this);
//                    this.encodeScaleHeader(
//                            answerOptions.get(answerOptions.size() - 1),
//                            context, writer, clientId);
//                } else if (this.isShowMidScaleHeader() && this.isHorizontalAligned()) {
//                    this.encodeScaleHeadersHorizontically(context, writer, clientId,
//                            answerOptions);
//                    writer.endElement("tr");
//                    writer.startElement("tr", this);
//                    this.encodeAnswerOptionsHorizontally(writer, clientId,
//                            answerOptions);
//                } else if (this.isShowMidScaleHeader() && !this.isHorizontalAligned()) {
//                    this.encodeAnswersAndScaleHeadersVertically(context, writer,
//                            clientId, answerOptions);
//                }
//            } else {
//                // we have to render missings
//                if (this.isHorizontalAligned() && !this.isShowMidScaleHeader()) {
//                    // empty cell above left scale header
//                    writer.startElement("td", this);
//                    writer.endElement("td");
//                    for (int i = 0; i < answerOptions.size(); i++) {
//                        writer.startElement("td", this);
//                        writer.endElement("td");
//                    }
//                    // empty cell above right scale header
//                    writer.startElement("td", this);
//                    writer.endElement("td");
//                    this.encodeMissingHeadersHorizontically(writer, clientId,
//                            missings);
//                    writer.endElement("tr");
//                    writer.startElement("tr", this);
//
//                    this.encodeScaleHeader(answerOptions.get(0), context, writer,
//                            clientId);
//
//                    this.encodeAnswerOptionsHorizontally(writer, clientId,
//                            answerOptions);
//
//                    this.encodeScaleHeader(
//                            answerOptions.get(answerOptions.size() - 1),
//                            context, writer, clientId);
//                    this.encodeAnswerOptionsHorizontally(writer, clientId, missings);
//                } else if (this.isHorizontalAligned() && this.isShowMidScaleHeader()) {
//                    this.encodeScaleHeadersHorizontically(context, writer, clientId,
//                            answerOptions);
//                    this.encodeMissingHeadersHorizontically(writer, clientId,
//                            missings);
//                    writer.endElement("tr");
//                    writer.startElement("tr", this);
//                    this.encodeAnswerOptionsHorizontally(writer, clientId,
//                            answerOptions);
//                    this.encodeAnswerOptionsHorizontally(writer, clientId, missings);
//                } else if (!this.isHorizontalAligned() && !this.isShowMidScaleHeader()) {
//                    writer.startElement("td", this);
//                    writer.endElement("td");
//                    this.encodeScaleHeader(answerOptions.get(0), context, writer,
//                            clientId);
//                    writer.endElement("tr");
//
//                    writer.startElement("tr", this);
//                    for (final Iterator<SingleOption> it = answerOptions
//                            .iterator(); it.hasNext();) {
//                        final SingleOption answer = it.next();
//                        writer.startElement("td", this);
//                        writer.endElement("td");
//                        this.encodeRadioButtonCell(answer, writer, clientId);
//                        if (it.hasNext()) {
//                            writer.endElement("tr");
//                            writer.startElement("tr", this);
//                        }
//                    }
//                    writer.endElement("tr");
//
//                    writer.startElement("tr", this);
//                    writer.startElement("td", this);
//                    writer.endElement("td");
//                    this.encodeScaleHeader(
//                            answerOptions.get(answerOptions.size() - 1),
//                            context, writer, clientId);
//                    writer.endElement("tr");
//
//                    writer.startElement("tr", this);
//                    this.encodeMissingsVertically(context, writer, clientId,
//                            missings);
//
//                } else if (!this.isHorizontalAligned() && this.isShowMidScaleHeader()) {
//                    this.encodeAnswersAndScaleHeadersVertically(context, writer,
//                            clientId, answerOptions);
//                    writer.endElement("tr");
//                    writer.startElement("tr", this);
//                    this.encodeMissingsVertically(context, writer, clientId,
//                            missings);
//
//                }
//            }
//        }
//    }
//
//    @Override
//    public void encodeEnd(final FacesContext context) throws IOException {
//        final ResponseWriter writer = context.getResponseWriter();
//        writer.endElement("tr");
//        writer.endElement("table");
//    }
//
//    private void encodeInput(final SingleOption child,
//            final ResponseWriter writer, final String clientId)
//            throws IOException {
//        writer.startElement("input", this);
//        writer.writeAttribute("type", "radio", null);
//        writer.writeAttribute("name", clientId, null);
//        writer.writeAttribute("id", clientId + ":" + child.getId(), null);
//        writer.writeAttribute("value", child.getId(), "value");
//        // select radio button representing current value
//        // TODO le: comment stripping parent part of the UID
//        if (this.getValue() != null && !((String) this.getValue()).isEmpty()
//                && child.getId().endsWith((String) this.getValue())) {
//            writer.writeAttribute("checked", "checked", null);
//        }
//        writer.endElement("input");
//    }
//
//    private void encodeLabel(final SingleOption child,
//            final ResponseWriter writer, final String clientId)
//            throws IOException {
//        writer.startElement("label", this);
//        writer.writeAttribute("for", clientId + ":" + child.getId(), null);
//        writer.writeText(
//                JsfUtility.getInstance().evaluateValueExpression(
//                        this.getFacesContext(), child.getLabel(), String.class),
//                null);
//        writer.endElement("label");
//    }
//
//    private void encodeMissingHeadersHorizontically(
//            final ResponseWriter writer, final String clientId,
//            final List<SingleOption> missings) throws IOException {
//        for (final SingleOption singleOption : missings) {
//            writer.startElement("td", this);
//            writer.writeAttribute("class", "zofar-likert-missing-cell", null);
//            this.encodeLabel(singleOption, writer, clientId);
//            writer.endElement("td");
//        }
//    }
//
//    private void encodeMissingsVertically(final FacesContext context,
//            final ResponseWriter writer, final String clientId,
//            final List<SingleOption> missings) throws IOException {
//        for (final Iterator<SingleOption> it = missings.iterator(); it
//                .hasNext();) {
//            final SingleOption missing = it.next();
//            this.encodeScaleHeader(missing, context, writer, clientId);
//            this.encodeRadioButtonCell(missing, writer, clientId);
//            if (it.hasNext()) {
//                writer.endElement("tr");
//                writer.startElement("tr", this);
//            }
//        }
//    }
//
//    private void encodeRadioButtonCell(final SingleOption child,
//            final ResponseWriter writer, final String clientId)
//            throws IOException {
//        writer.startElement("td", this);
//        String cellClass = null;
//        final String[] classes = this.getRowClazzes();
//        if (!child.isMissing()) {
//            cellClass = "zofar-likert-cell";
//            if (classes != null && classes.length > 0) {
//                cellClass = cellClass + " "
//                        + classes[this.cellCounter++ % classes.length];
//            }
//        } else {
//            cellClass = "zofar-likert-missing-cell";
//        }
//        writer.writeAttribute("class", cellClass, null);
//        if (this.isHorizontalAligned()) {
//            writer.startElement("div", this);
//            this.encodeValue(child, writer, clientId);
//            writer.endElement("div");
//            writer.startElement("div", this);
//            this.encodeInput(child, writer, clientId);
//            writer.endElement("div");
//        } else {
//            this.encodeInput(child, writer, clientId);
//            this.encodeValue(child, writer, clientId);
//        }
//        writer.endElement("td");
//    }
//
//    private void encodeScaleHeader(final SingleOption uiComponent,
//            final FacesContext context, final ResponseWriter writer,
//            final String clientId) throws IOException {
//        writer.startElement("td", this);
//        writer.writeAttribute("class", "zofar-likert-scale-header", null);
//        this.encodeLabel(uiComponent, writer, clientId);
//        writer.endElement("td");
//    }
//
//    private void encodeScaleHeadersHorizontically(final FacesContext context,
//            final ResponseWriter writer, final String clientId,
//            final List<SingleOption> answerOptions) throws IOException {
//        for (int i = 0; i < answerOptions.size(); i++) {
//            if (i == 0 || i == answerOptions.size() / 2
//                    || i == answerOptions.size() - 1) {
//                this.encodeScaleHeader(answerOptions.get(i), context, writer,
//                        clientId);
//            } else {
//                writer.startElement("td", this);
//                writer.endElement("td");
//            }
//        }
//    }
//
//    private void encodeValue(final SingleOption child,
//            final ResponseWriter writer, final String clientId)
//            throws IOException {
//        if (!this.isShowValues()) {
//            return;
//        }
//        writer.startElement("label", this);
//        writer.writeAttribute("for", clientId + ":" + child.getId(), null);
//        writer.writeText(child.getValue(), null);
//        writer.endElement("label");
//    }
//
//    /**
//     * This component completely renders its children.
//     */
//    @Override
//    public boolean getRendersChildren() {
//        return true;
//    }
//
//    public String[] getRowClazzes() {
//        if (this.rowClasses != null) {
//            return this.rowClasses.clone();
//        } else {
//            String[] rowClasses = null;
//            final String classes = (String) this.getAttributes().get("rowClasses");
//            if (classes != null) {
//                final String[] tmp = classes.split(CSS_CLASS_DELIM);
//                rowClasses = new String[tmp.length];
//                for (int i = 0; i < tmp.length; i++) {
//                    rowClasses[i] = tmp[i].trim();
//                }
//            } else {
//                rowClasses = new String[0];
//            }
//            return rowClasses;
//        }
//    }
//
    @Deprecated
    @Override
    public String getUID() {
    	return this.getId();
    }
//    
    @Deprecated
    @Override
    public Boolean isHorizontalAligned() {
    	return this.getDirection().equals("horizontal");
    }
    
    @Override
    public String getDirection() {
    	return (String) getStateHelper().eval("direction");
    }
    
    public void setDirection(final String direction) {
        getStateHelper().put("direction", direction);
    }
//
//    public Boolean isShowMidScaleHeader() {
//        final Boolean value = (Boolean) this.getAttributes().get(
//                "showMidScaleHeader");
//        return value;
//    }
//
//    public Boolean isShowValues() {
//        final Boolean value = (Boolean) this.getAttributes().get("showValues");
//        return value;
//    }
//
//    /**
//     * We do not want to participate in state saving.
//     */
//    @Override
//    public boolean isTransient() {
//        return true;
//    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.zofar.presentation.surveyengine.ui.interfaces.IResponseDomain#
     * setItemClasses(java.lang.String)
     */
    @Override
    public void setItemClasses(final String itemClasses) {
        getStateHelper().put(PropertyKeys.itemClasses, itemClasses);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.zofar.presentation.surveyengine.ui.interfaces.IResponseDomain#
     * getItemClasses()
     */
    @Override
    public String getItemClasses() {
        return (String) getStateHelper().eval(PropertyKeys.itemClasses);
    }
}
