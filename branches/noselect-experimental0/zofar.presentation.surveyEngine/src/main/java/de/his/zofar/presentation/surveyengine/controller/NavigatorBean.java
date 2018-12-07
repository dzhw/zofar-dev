package de.his.zofar.presentation.surveyengine.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import de.his.zofar.presentation.surveyengine.facades.SurveyEngineServiceFacade;
import de.his.zofar.presentation.surveyengine.ui.interfaces.INavigator;
import de.his.zofar.presentation.surveyengine.util.SystemConfiguration;
import de.his.zofar.service.surveyengine.model.Participant;
import de.his.zofar.service.surveyengine.model.SurveyHistory;

/**
 * @author meisner
 * 
 */
@Controller("navigatorBean")
@Scope("session")
public class NavigatorBean implements ActionListener, Serializable, INavigator {

	private enum DirectionType {
		UNKOWN, SAME, FORWARD, BACKWARD
	}

	private static final long serialVersionUID = 7555427732261001816L;

	private static final Logger LOGGER = LoggerFactory.getLogger(NavigatorBean.class);

	private final SurveyEngineServiceFacade surveyEngineService = new SurveyEngineServiceFacade();

	@Inject
	private SessionController session;

	private DirectionType direction;

	private String sendId;
	private Stack<String> movements;
	
	public String showroom;

	// private final String xhtmlPattern = Pattern.quote(".xhtml");
	private final Pattern xhtmlPattern = Pattern.compile("\\.xhtml");

	private String cleanViewId(final String viewId) {
		String tmp = viewId.trim();
		// tmp = tmp.replaceAll(xhtmlPattern, "");
		final Matcher matcher = this.xhtmlPattern.matcher(tmp);
		tmp = matcher.replaceAll("");
		if (tmp.startsWith("/")) {
			tmp = tmp.substring(1);
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("cleanViewId {} ==> {}", viewId, tmp);
		return tmp;
	}

	private void cutMovementStack(final int index) {
		final List<String> cutted = new ArrayList<String>(
				this.movements.subList(0, Math.min(index, this.movements.size() - 1)));
		this.movements.clear();
		this.movements.addAll(cutted);
	}

	/**
	 * Method to retrieve next-to-last visited page
	 * 
	 * @return next-to-last Page-Name
	 */
	@Override
	public String getBackwardViewID() {
		if (this.movements.isEmpty()) {
			return "/index.html";
		} else {
			String beforeLastId = this.movements.peek();
			if (this.movements.size() > 1) {
				beforeLastId = this.movements.get(this.movements.size() - 2);
			}
			return "/" + beforeLastId + ".xhtml";
		}
	}

	/**
	 * Method to retrieve current visited page (needed by onexit-validation
	 * Exception handling)
	 * 
	 * @return current Page-Name
	 */
	@Override
	public String getSameViewID() {
		if (this.movements.isEmpty()) {
			return "/index.html";
		} else {
			final String lastId = this.movements.peek();
			return "/" + lastId + ".xhtml";
		}
	}

	@PostConstruct
	private void init() {
		// LOGGER.debug("init");
		this.direction = DirectionType.UNKOWN;
		this.sendId = "UNKOWN";
	}

	/**
	 * @return true if current Movement Direction is backward
	 */
	@Override
	public boolean isBackward() {
		return this.direction.equals(DirectionType.BACKWARD);
	}

	/**
	 * @return true if current Movement Direction is forward
	 */
	@Override
	public boolean isForward() {
		return this.direction.equals(DirectionType.FORWARD);
	}

	/**
	 * Method used for render-decision of backward-Button
	 * 
	 * @return true, if current history is empty
	 */
	@Override
	public boolean isHistoryEmpty() {
		if (this.movements == null) {
			String token = "UNKOWN";
			if ((session != null) && (session.getParticipant() != null)
					&& (session.getParticipant().getToken() != null))
				token = session.getParticipant().getToken();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("history null while empty check for token {}", token);
			}
			return true;
		} else if (this.movements.isEmpty()) {
			return true;
		} else {
			if ((this.movements.size() == 1) && ((this.movements.get(0)).equals(this.retrieveViewId()))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return true if current Movement Direction is same
	 */
	@Override
	public boolean isSame() {
		return this.direction.equals(DirectionType.SAME);
	}

	// Methods to register next viewId and implement back target (used in
	// faces-config )

	/**
	 * Randomly pick a view and register the chosen viewId.
	 * 
	 * @param randomOnce
	 *            true if we only need to shuffle once
	 * @param viewIds
	 *            List of viewIds, separated by comma.
	 * @return The randomly chosen viewId.
	 */
	@Override
	public String pickAndSendViewID(final boolean randomOnce, final String viewIds) {
		try {
			final String[] viewIdArray = viewIds.split(",");
			if (randomOnce) {
				final String previouslyPickedViewId = this.surveyEngineService
						.loadRandomViewTransition(this.session.getParticipant(), this.getSameViewID());
				if (previouslyPickedViewId != null) {
					return this.sendViewID(previouslyPickedViewId);
				}
			}
			final List<String> viewIdList = Arrays.asList(viewIdArray);
			Collections.shuffle(viewIdList);

			this.surveyEngineService.saveRandomViewTransition(this.session.getParticipant(), this.getSameViewID(),
					viewIdList.get(0));
			return this.sendViewID(viewIdList.get(0));
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("[NAV EXCEPTION] pickAndSendViewID Exception cause for {}: {}",
						this.session.getParticipant().getToken(), e);
			}
		}
		return null;
	}

	private void pushToMovementStack(final String viewId) {
		// LOGGER.debug("pushToMovementStack {}", viewId);
		this.movements.push(viewId);
	}

	private void redirectTo(final String redirect) {
		// LOGGER.info("redirect to {}", redirect);
		this.sendId = redirect;
		final FacesContext context = FacesContext.getCurrentInstance();
		try {
			final ExternalContext externalContext = context.getExternalContext();
			final HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
			response.sendRedirect("./" + redirect + ".html");
		} catch (final IOException e) {
			LOGGER.error(e.getMessage());
		} catch (final IllegalStateException e) {
			LOGGER.error(e.getMessage());
		}
	}

	// private void registerMovement(final DirectionType dir, final String id,
	// final boolean save) {
	private void registerMovement(final String id, final boolean save) {
		final Participant participant = this.session.getParticipant();

		// LOGGER.info("movement ({}) ==> {} ", participant.getToken(), id + ","
		// +" ({"+save+"})");

		if (save) {
			try {
				this.surveyEngineService.saveHistory(new SurveyHistory(participant, "/" + id + ".xhtml"));
			} catch (Exception e) {
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error("[NAV EXCEPTION] registerMovement Exception cause for {}: {}",
							this.session.getParticipant().getToken(), e);
				}
			}
		}
	}

	/**
	 * Register viewId of current requested page and redirect to last valid page
	 * if applicable
	 * 
	 * @param viewId
	 */
	@Override
	public void registerViewID(final String viewId) {
		// LOGGER.info("registerViewID {}", viewId);
		boolean reinit = false;
		if (this.movements == null) {
			this.movements = new Stack<String>();
			final Participant participant = this.session.getParticipant();
			try {
				final List<SurveyHistory> savedLog = this.surveyEngineService.loadHistory(participant);
				if ((savedLog != null) && (!savedLog.isEmpty())) {
					// LOGGER.debug("total history size {}", savedLog.size());
					Iterator<SurveyHistory> it = null;
					final SystemConfiguration system = SystemConfiguration.getInstance();
					if (system.cutHistory()) {
						it = savedLog.iterator();
						SurveyHistory index = null;
						while (it.hasNext()) {
							final SurveyHistory tmp = it.next();
							if ((tmp.getPage() != null) && ((tmp.getPage()).endsWith("index.xhtml"))) {
								index = tmp;
							}
						}
						if (index != null) {
							final int skipToIndex = savedLog.indexOf(index);
							// LOGGER.debug("skip history to index {}",
							// skipToIndex);
							it = savedLog.listIterator(skipToIndex);
						}
					} else {
						it = savedLog.iterator();
					}
					if (it != null) {
						while (it.hasNext()) {
							final SurveyHistory tmp = it.next();
							// LOGGER.info("reinit history {}", tmp);
							this.registerViewIDHelper(this.cleanViewId(tmp.getPage()), false);
							reinit = true;
						}
					}
				}
			} catch (Exception e) {
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error("[NAV EXCEPTION] registerViewID Exception cause for {}: {}",
							this.session.getParticipant().getToken(), e);
				}
			}
		}

		// check if current request is valid
		boolean isValid = false;
		if (this.movements.isEmpty() && viewId.equals("index")) {
			isValid = true;
		} else if (this.movements.contains(viewId)) {
			isValid = true;
		} else if (viewId.equals(this.sendId)) {
			isValid = true;
		}

		if (reinit) {
			// Force redirection to last visited page if history was recovered
			isValid = false;
		}
		final SystemConfiguration system = SystemConfiguration.getInstance();
		if (system.overrideNavigation())
			isValid = true;
		// if (LOGGER.isInfoEnabled()) {
		// LOGGER.info("viewId {}", viewId);
		// LOGGER.info("sendId {}", this.sendId);
		// LOGGER.info("isValid {}", isValid);
		// LOGGER.info("movements {}", this.movements);
		// }
		this.sendId = "UNKOWN";
		// LOGGER.info("system.overrideNavigation() :
		// {}",system.overrideNavigation());
		if (isValid) {
			this.registerViewIDHelper(viewId, true);
		} else {
			final String target;
			if (this.movements.isEmpty()) {
				target = "index";
			} else {
				target = this.movements.peek();
			}
			this.registerViewIDHelper(target, true);
			// redirect to the last valid page
			this.redirectTo(target);
		}
	}

	private void registerViewIDHelper(final String viewId, final boolean save) {
		// LOGGER.info("registerViewIDHelper {}", viewId);

		String lastId = "UNKOWN";
		if (this.movements.isEmpty()) {
			this.pushToMovementStack(viewId);
			this.registerMovement(viewId, save);
		} else {
			lastId = this.movements.peek();
			// LOGGER.info("old movements {}",movements);
			// LOGGER.info("lastId {} currentId {} ",lastId,viewId);

			if (lastId.equals(viewId)) {
				this.direction = DirectionType.SAME;
				this.registerMovement(viewId, save);
				this.session.setExceptionFlag(true);
			} else {
				boolean goneBack = false;

				if (this.movements.contains(viewId)) {
					final int index = this.movements.lastIndexOf(viewId);
					this.cutMovementStack(index);

					this.pushToMovementStack(viewId);
					this.registerMovement(viewId, save);
					goneBack = true;
				}

				if (!goneBack) {
					this.pushToMovementStack(viewId);
					this.registerMovement(viewId, save);
				}
			}
		}
	}

	private String retrieveViewId() {
		return this.cleanViewId((FacesContext.getCurrentInstance()).getViewRoot().getViewId());
	}

	/**
	 * Register viewId of page, which was sended by effective Navigation-case
	 * 
	 * @param viewId
	 * @return viewId without File-Suffix and trailing /
	 */
	@Override
	public String sendViewID(final String viewId) {
		// LOGGER.info("sendViewID {}", viewId);
		this.sendId = this.cleanViewId(viewId);
		return viewId;
	}

	public void jumperMove(final String viewId) {
		// LOGGER.info("jumperMove {}", viewId);
		sendViewID(viewId);
		pushToMovementStack(cleanViewId(viewId));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.event.ActionListener#processAction(javax.faces.event.
	 * ActionEvent )
	 */
	@Override
	public void processAction(final ActionEvent actionEvent) throws AbortProcessingException {
		final Object source = actionEvent.getSource();

		if ((source != null) && (HtmlCommandButton.class).isAssignableFrom(source.getClass())) {
			final String id = ((HtmlCommandButton) source).getId();
			final String action = (String) ((HtmlCommandButton) source).getActionExpression().getExpressionString();

			// LOGGER.info("id : {} action : {}",id,action);

			if (id.equals("defaultBt")) {
				this.direction = DirectionType.FORWARD;
			}
			if (id.equals("forwardBt")) {
				this.direction = DirectionType.FORWARD;
			}
			if (id.equals("backwardBt")) {
				this.direction = DirectionType.BACKWARD;
			}
			if (action.equals("same")) {
				this.direction = DirectionType.SAME;
			}
		}
	}

	@Override
	public Stack<String> getMovements() {
		return this.movements;
	}
	
	public String getShowroom() {
		return showroom;
	}

	public void setShowroom(String showroom) {
		
		this.showroom = showroom;
	}
	

}