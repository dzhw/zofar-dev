package eu.dzhw.zofar.management.utils.bool.main;

import eu.dzhw.zofar.management.utils.bool.DecisionClient;

@Deprecated
public class DecisionTest {

	public static void main(final String[] args) {
		final DecisionClient decision = DecisionClient.getInstance();
		final boolean unweighted = decision.randomBoolean();
		final boolean weighed = decision.randomBoolean(80);
		System.out.println("unweighed : " + unweighted);
		System.out.println("weighed with 80% : " + weighed);
	}
}
