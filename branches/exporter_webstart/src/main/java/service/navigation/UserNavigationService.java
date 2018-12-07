package service.navigation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class UserNavigationService.
 */
public class UserNavigationService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(UserNavigationService.class);

	/** The instance. */
	private static UserNavigationService INSTANCE;

	/**
	 * Instantiates a new user navigation service.
	 */
	private UserNavigationService() {
		super();
	}

	/**
	 * Gets the single instance of UserNavigationService.
	 * 
	 * @return single instance of UserNavigationService
	 */
	public static UserNavigationService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new UserNavigationService();
		return INSTANCE;
	}
}
