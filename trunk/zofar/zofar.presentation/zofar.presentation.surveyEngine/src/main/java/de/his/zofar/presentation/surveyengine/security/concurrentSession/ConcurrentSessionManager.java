package de.his.zofar.presentation.surveyengine.security.concurrentSession;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;

import de.his.zofar.presentation.surveyengine.util.SystemConfiguration;

/**
 * @author meisner
 * 
 */
@Controller("concurrentSessionManager")
@Scope("application")
public class ConcurrentSessionManager implements Serializable {

	private static final long serialVersionUID = 3062325700762701815L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ConcurrentSessionManager.class);

	private SessionFactory sessionFactory;

	@Autowired
	private SessionRegistry sessionRegistry;

	public ConcurrentSessionManager() {
		super();
	}

	private SessionFactory getSessionFactory() {
		try {
			final Configuration configuration = new Configuration();
			configuration.configure("/session-security.cfg.xml");
			final ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
			return configuration.buildSessionFactory(serviceRegistry);
		} catch (final Throwable ex) {
			LOGGER.error("SessionFactory creation failed. {}", ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	private Session getSession() {
		if (this.sessionFactory == null)
			this.sessionFactory = this.getSessionFactory();
		if (this.sessionFactory != null) {
			return this.sessionFactory.openSession();
		}
		return null;
	}

	@PostConstruct
	private void init() {
		final SystemConfiguration system = SystemConfiguration.getInstance();
		if (!system.cluster())
			return;
		LOGGER.info("init");
		this.getSession();
	}

	@PreDestroy
	private void close() {
		final SystemConfiguration system = SystemConfiguration.getInstance();
		if (!system.cluster())
			return;
		LOGGER.info("close");
		if (this.sessionFactory != null)
			this.sessionFactory.close();

	}

	public boolean check(final String token, final String sessionId) {
		final SystemConfiguration system = SystemConfiguration.getInstance();
		if (!system.cluster())
			return true;

		LOGGER.info("check {} {}", token, sessionId);
		final Session session = this.getSession();
		final Transaction transaction = session.beginTransaction();
		try {
			final Query query = session.createQuery("select count(*) from SessionMap where token = :token AND sessionId = :sessionId");
			query.setParameter("token", token);
			query.setParameter("sessionId", sessionId);
			query.setCacheable(true);

			final Long count = (Long) query.uniqueResult();
			transaction.commit();
			LOGGER.info("{} session Entries found", count);
			if ((count != null) && (count > 0))
				return true;
		} catch (final Throwable t) {
			transaction.rollback();
			throw t;
		} finally {
			if ((session != null) && (session.isOpen()))
				session.close();
		}
		return false;
	}

	private boolean checkToken(final String token, final boolean relogin) {
		final SystemConfiguration system = SystemConfiguration.getInstance();
		if (!system.cluster())
			return true;
		final Session session = this.getSession();
		final Transaction transaction = session.beginTransaction();
		try {
			final Query query = session.createQuery("select count(*) from SessionMap where token = :token");
			query.setParameter("token", token);
			query.setCacheable(true);
			final Long count = (Long) query.uniqueResult();
			transaction.commit();

			if ((count != null) && (count > 0)) {
				if (relogin)
					LOGGER.info("[RELOGIN] ({})", token);
				return true;
			}
		} catch (final Throwable t) {
			transaction.rollback();
			throw t;
		} finally {
			if ((session != null) && (session.isOpen()))
				session.close();
		}
		return false;
	}

	public void set(final String token, final String sessionId, final boolean relogin) {
		final SystemConfiguration system = SystemConfiguration.getInstance();
		if (!system.cluster())
			return;

		final boolean update = this.checkToken(token, relogin);

		final Session session = this.getSession();
		final Transaction transaction = session.beginTransaction();
		try {
			final SessionMap sessionEntry = new SessionMap();
			sessionEntry.setSessionId(sessionId);
			sessionEntry.setToken(token);
			if (update)
				session.update(sessionEntry);
			else
				session.save(sessionEntry);
			transaction.commit();
		} catch (final Throwable t) {
			transaction.rollback();
			throw t;
		} finally {
			if ((session != null) && (session.isOpen()))
				session.close();
		}
	}

	public void remove(final String sessionId) {
		final SystemConfiguration system = SystemConfiguration.getInstance();
		if (!system.cluster())
			return;
		// LOGGER.info("remove {}", sessionId);
		final Session session = this.getSession();
		final Transaction transaction = session.beginTransaction();
		try {
			final Query query = session.createQuery("delete SessionMap where sessionId = :sessionId");
			query.setParameter("sessionId", sessionId);
			query.setCacheable(true);
			query.executeUpdate();
			transaction.commit();
			// LOGGER.info("{} session Entries deleted", result);
		} catch (final Throwable t) {
			transaction.rollback();
			throw t;
		} finally {
			if ((session != null) && (session.isOpen()))
				session.close();
		}

	}

	// public void logout(final HttpSession session, final Authentication auth,
	// final HttpServletRequest request, final HttpServletResponse response) {
	// final SystemConfiguration system = SystemConfiguration.getInstance();
	// if (!system.cluster())
	// return;
	// if (auth != null)
	// LOGGER.info("concurrent Session found for {} => forced logout",
	// auth.getName());
	// else
	// LOGGER.info("concurrent Session found => forced logout");
	// try {
	// if (!response.isCommitted()) {
	// if (auth != null) {
	// auth.setAuthenticated(false);
	// }
	// final FacesContext context = FacesContext.getCurrentInstance();
	// final ExternalContext externalContext = context.getExternalContext();
	// session.invalidate();
	// Object newSession = externalContext.getSession(true);
	// if (newSession != null) {
	// ((HttpSession) session).setMaxInactiveInterval(10);
	// }
	// final RedirectStrategy redirectHandler = new DefaultRedirectStrategy();
	// redirectHandler.sendRedirect(request, response,
	// "/special/login.html?message=InUse");
	//
	// return;
	// } else {
	// LOGGER.info("response already commited ({}) => logout redirect canceled",
	// response.getHeader("Location"));
	// }
	//
	// } catch (final Exception e1) {
	// e1.printStackTrace();
	// }
	// }

	// public void logout(final HttpSession session, final Authentication auth,
	// final HttpServletRequest request, final HttpServletResponse response) {
	// final SystemConfiguration system = SystemConfiguration.getInstance();
	// if (!system.cluster())
	// return;
	//
	// if (auth != null)
	// LOGGER.info("concurrent Session found for {} => forced logout ({})",
	// auth.getName(),session.getId());
	// else
	// LOGGER.info("concurrent Session found => forced logout ({})",session.getId());
	// try {
	// if (!response.isCommitted()) {
	// if (auth != null) {
	// auth.setAuthenticated(false);
	// SecurityContextHolder.getContext().setAuthentication(null);
	// }
	//
	// List<SessionInformation> sessionsInfo =
	// sessionRegistry.getAllSessions(auth.getPrincipal(), false);
	// if(null != sessionsInfo && sessionsInfo.size() > 0) {
	// for (SessionInformation sessionInformation : sessionsInfo) {
	// LOGGER.info("Expire now :" + sessionInformation.getSessionId());
	// sessionInformation.expireNow();
	// }
	// }
	// // session.invalidate();
	// //
	// // ((HttpSession)
	// FacesContext.getCurrentInstance().getExternalContext().getSession(true)).setMaxInactiveInterval(0);
	// // final RedirectStrategy redirectHandler = new
	// DefaultRedirectStrategy();
	// // redirectHandler.sendRedirect(request, response,
	// "/j_spring_security_logout");
	// return;
	//
	// } else {
	// LOGGER.info("response already commited ({}) => logout redirect canceled",
	// response.getHeader("Location"));
	// }
	//
	// } catch (final Exception e1) {
	// e1.printStackTrace();
	// }
	// }

	public void logout(final HttpSession session, final Authentication auth, final HttpServletRequest request, final HttpServletResponse response) {
		final SystemConfiguration system = SystemConfiguration.getInstance();
		if (!system.cluster())
			return;

		if (auth != null)
			LOGGER.info("concurrent Session found for {} => forced logout ({})", auth.getName(), session.getId());
		else
			LOGGER.info("concurrent Session found => forced logout ({})", session.getId());

		// Kill Cookies
		Cookie cookieWithSlash = new Cookie("JSESSIONID", null);
		// Tomcat adds extra slash at the end of context path (e.g. "/foo/")
		cookieWithSlash.setPath(request.getContextPath() + "/");
		cookieWithSlash.setMaxAge(0);

		Cookie cookieWithoutSlash = new Cookie("JSESSIONID", null);
		// JBoss doesn't add extra slash at the end of context path (e.g.
		// "/foo")
		cookieWithoutSlash.setPath(request.getContextPath());
		cookieWithoutSlash.setMaxAge(0);

		// Remove cookies on logout so that invalidSessionURL (session timeout)
		// is not displayed on proper logout event
		response.addCookie(cookieWithSlash); // For cookie added by Tomcat
		response.addCookie(cookieWithoutSlash); // For cookie added by JBoss

		List<SessionInformation> sessionsInfo = sessionRegistry.getAllSessions(auth.getPrincipal(), false);
		if (null != sessionsInfo && sessionsInfo.size() > 0) {
			for (SessionInformation sessionInformation : sessionsInfo) {
				LOGGER.info("Expire now :" + sessionInformation.getSessionId());
				sessionInformation.expireNow();
				sessionRegistry.removeSessionInformation(sessionInformation .getSessionId());
			}
		}
		session.invalidate();
		SecurityContextHolder.getContext().setAuthentication(null);
		//throw new BadCredentialsException("kicked out");
		throw new AccountExpiredException("forced logout");

	}
}
