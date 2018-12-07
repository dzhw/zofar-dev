package main;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tests.common.MessageProvider;


public class Verifier {

	// @SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory
			.getLogger(Verifier.class);

	public static void main(String[] args) {
		
		final Map<String, String> additionalArgs = new HashMap<String, String>();
		if ((args != null) && (args.length > 0)) {
			final int argCount = args.length;
			for (int a = 0; a < argCount; a++) {
				final String arg = args[a];
				final String[] splittedArg = arg.split("=");
				if ((splittedArg != null) && (splittedArg.length == 2)) {
					final String key = splittedArg[0].trim();
					final String value = splittedArg[1].trim();
					boolean flag = false;
					if (key.equals("contentPath"))
						flag = true;
					if (key.equals("schemaPath"))
						flag = true;
					if (flag)
						additionalArgs.put(key, value);
				}
				// LOGGER.info("Argument {}",args[a]);
			}

		}
		
		boolean help = true;
		if((additionalArgs.containsKey("contentPath"))&&(additionalArgs.containsKey("schemaPath")))help=false;

		if (help) {
			final StringBuffer buffer = new StringBuffer();
			buffer.append("java -jar Verifier [contentPath=<PATHTOQML>] [schemaPath=<PATHTOSCHEMA>]");
		} else {
			System.setProperty("suite", "true");
			System.setProperty("runMode", "main");
			if (!additionalArgs.isEmpty()) {
				final Iterator<String> it = additionalArgs.keySet().iterator();
				while (it.hasNext()) {
					final String key = it.next();
					final String value = additionalArgs.get(key);
					System.setProperty(key, value);
				}
			}

			final JUnitCore runner = new JUnitCore();
			final Result result = runner.run(AllTests.class);
			for (Failure failure : result.getFailures()) {
				MessageProvider.info(failure.getDescription().getTestClass(),
						"!! {}", failure.getMessage());
			}
			System.clearProperty("suite");
			System.clearProperty("runMode");
			if (!additionalArgs.isEmpty()) {
				final Iterator<String> it = additionalArgs.keySet().iterator();
				while (it.hasNext()) {
					final String key = it.next();
					System.clearProperty(key);
				}
			}
		}

	}

}
