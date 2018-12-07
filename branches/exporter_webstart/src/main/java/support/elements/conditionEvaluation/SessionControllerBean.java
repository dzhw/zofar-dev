package support.elements.conditionEvaluation;

import model.ParticipantEntity;

public class SessionControllerBean {
	
	private ParticipantEntity participant;

	public SessionControllerBean(final ParticipantEntity participant) {
		super();
		this.participant = participant;
	}
	
	public ParticipantEntity getParticipant(){
		return this.participant;
	}
}
