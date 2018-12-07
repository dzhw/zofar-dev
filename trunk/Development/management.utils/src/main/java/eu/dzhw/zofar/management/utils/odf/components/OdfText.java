package eu.dzhw.zofar.management.utils.odf.components;

@Deprecated
public class OdfText extends OdfTextElement {

	public OdfText(final String content) {
		super(content);
	}

	@Override
	public String toString() {
		return "OdfText [content=" + this.getContent() + "]";
	}
}
