package eu.dzhw.zofar.management.generator.qml.slc;

import java.util.List;
import java.util.Map;

public class SplitGenerator extends GenericGenerator {

	private static SplitGenerator INSTANCE;

	private SplitGenerator() {
		super();
	}

	public static SplitGenerator getInstance() {
		if (INSTANCE == null)
			INSTANCE = new SplitGenerator();
		return INSTANCE;
	}
	
	@Override
	public String generatePrefix(String template, List<Map<String, String>> replacements) {
		//Replace Placeholder	
		return super.replacePlaceholder(template, replacements);
	}

	@Override
	public String generatePostfix(String template, List<Map<String, String>> replacements) {
		//Replace Placeholder	
		return super.replacePlaceholder(template, replacements);
	}

	@Override
	public String process(String template, List<Map<String, String>> replacements) {
		//Replace Placeholder	
		return super.replacePlaceholder(template, replacements);
	}
}
