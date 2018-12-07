package eu.dzhw.zofar.management.utils.odf.components;

@Deprecated
public abstract class OdfTextElement {

	private final String content;

	public OdfTextElement(final String content) {
		super();
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}

	// public void setContent(String content) {
	// this.content = content;
	// }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.content == null) ? 0 : this.content.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final OdfTextElement other = (OdfTextElement) obj;
		if (this.content == null) {
			if (other.content != null)
				return false;
		} else if (!this.content.equals(other.content))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OdfTextElement [content=" + this.content + "]";
	}

}
