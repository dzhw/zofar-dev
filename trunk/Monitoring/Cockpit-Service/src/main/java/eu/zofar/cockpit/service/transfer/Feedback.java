package eu.zofar.cockpit.service.transfer;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Feedback implements Serializable {

	private static final long serialVersionUID = 4156975436558634097L;

	public enum Status {
		INVITED, PARTICIPATED, FINISHED
	}

	public class Entry implements Serializable {

		private static final long serialVersionUID = 1405603936689918721L;
		private final Status status;
		private final String token;

		public Entry(final Status status, final String token) {
			super();
			this.status = status;
			this.token = token;
		}

		public Status getStatus() {
			return this.status;
		}

		// public void setStatus(Status status) {
		// this.status = status;
		// }

		public String getToken() {
			return this.token;
		}

		// public void setToken(String token) {
		// this.token = token;
		// }

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + this.getOuterType().hashCode();
			result = prime * result
					+ ((this.status == null) ? 0 : this.status.hashCode());
			result = prime * result
					+ ((this.token == null) ? 0 : this.token.hashCode());
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
			final Entry other = (Entry) obj;
			if (!this.getOuterType().equals(other.getOuterType()))
				return false;
			if (this.status != other.status)
				return false;
			if (this.token == null) {
				if (other.token != null)
					return false;
			} else if (!this.token.equals(other.token))
				return false;
			return true;
		}

		private Feedback getOuterType() {
			return Feedback.this;
		}

		@Override
		public String toString() {
			return "Entry [status=" + status + ", token=" + token + "]";
		}
	};

	private final Map<Timestamp, List<Entry>> data;
	private final List<String> startTokens;
	private final Timestamp start;

	public Feedback(final List<String> tokens, final Timestamp start) {
		super();
		this.start = start;
		this.data = new LinkedHashMap<Timestamp, List<Entry>>();
		this.startTokens = tokens;
		for (final String token : startTokens) {
			List<Entry> tokenSet = null;
			if (this.data.containsKey(start))
				tokenSet = this.data.get(start);
			if (tokenSet == null)
				tokenSet = new ArrayList<Entry>();
			tokenSet.add(new Entry(Status.INVITED, token));
			this.data.put(start, tokenSet);
		}
	}

	public void addFeedback(final Map<String, Feedback.Status> tokens,
			final Timestamp timestamp) {
		if (tokens == null)
			return;
		
		for (final String token : startTokens) {
			if(!tokens.containsKey(token))continue;
			Feedback.Status status = tokens.get(token);
			
			List<Entry> tokenSet = null;
			if (this.data.containsKey(timestamp))
				tokenSet = this.data.get(timestamp);
			if (tokenSet == null)
				tokenSet = new ArrayList<Entry>();

			Entry entry = new Entry(status,token);
			tokenSet.add(entry);
			this.data.put(timestamp, tokenSet);
		}
		
//		for (final Map.Entry<String, Feedback.Status> token : tokens.entrySet()) {
//			if (startTokens.contains(token.getKey())) {
//				List<Entry> tokenSet = null;
//				if (this.data.containsKey(timestamp))
//					tokenSet = this.data.get(timestamp);
//				if (tokenSet == null)
//					tokenSet = new ArrayList<Entry>();
//				Status status = Status.INVITED;
//				if(token.getValue().equals(Feedback.Status.PARTICIPATED))status = Status.PARTICIPATED;
//				if(token.getValue().equals(Feedback.Status.FINISHED))status = Status.FINISHED;
//				Entry entry = new Entry(status,token.getKey());
//				tokenSet.add(entry);
//				this.data.put(timestamp, tokenSet);
//			}
//		}
	}

	public Map<Timestamp, List<Entry>> getData() {
		return this.data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.data == null) ? 0 : this.data.hashCode());
		result = prime * result
				+ ((this.start == null) ? 0 : this.start.hashCode());
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
		final Feedback other = (Feedback) obj;
		if (this.data == null) {
			if (other.data != null)
				return false;
		} else if (!this.data.equals(other.data))
			return false;
		if (this.start == null) {
			if (other.start != null)
				return false;
		} else if (!this.start.equals(other.start))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Feedback [data=" + this.data + ", start=" + this.start + "]";
	}
}
