package eu.dzhw.zofar.management.utils.odf.components;

import org.odftoolkit.odfdom.type.Color;

@Deprecated
public class OdfColoredText extends OdfText {

	private final Color color;

	public OdfColoredText(final String content, final Color color) {
		super(content);
		this.color = color;
	}

	public Color getColor() {
		return this.color;
	}

	@Override
	public String toString() {
		return "OdfColoredText [color=" + this.color + " content=" + this.getContent() + "]";
	}
}
