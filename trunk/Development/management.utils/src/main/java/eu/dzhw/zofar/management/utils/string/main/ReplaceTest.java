package eu.dzhw.zofar.management.utils.string.main;

import eu.dzhw.zofar.management.utils.string.ReplaceClient;

@Deprecated
public class ReplaceTest {
	public static void main(final String[] args) {
		final ReplaceClient replacer = ReplaceClient.getInstance();
		final String startTag = "START";
		final String stopTag = "STOP";
		final String header = "Header Content";
		final String content = startTag
				+ "huschelpuschel"
				+ stopTag
				+ "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
		final String newContent = replacer.replaceBetweenTags(content, startTag, stopTag, startTag + header + stopTag);
		System.out.println("old : " + content);
		System.out.println("new : " + newContent);
	}
}
