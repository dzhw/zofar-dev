package service.navigation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserNavigationService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserNavigationService.class);
	
	private static UserNavigationService INSTANCE;

	private UserNavigationService() {
		super();
	}

	public static UserNavigationService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new UserNavigationService();
		return INSTANCE;
	}
}
