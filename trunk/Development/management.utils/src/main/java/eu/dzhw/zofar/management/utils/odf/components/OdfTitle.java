package eu.dzhw.zofar.management.utils.odf.components;

@Deprecated
public class OdfTitle extends OdfTextElement {

	public OdfTitle(final String content) {
		super(content);
	}

	@Override
	public String toString() {
		return "OdfTitle [content=" + this.getContent() + "]";
	}

}
