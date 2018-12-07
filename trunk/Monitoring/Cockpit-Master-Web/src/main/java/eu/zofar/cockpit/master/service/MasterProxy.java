package eu.zofar.cockpit.master.service;

import javax.servlet.http.HttpServletRequest;

import eu.zofar.cockpit.service.transfer.RegisterTransfer;

public interface MasterProxy {

	public RegisterTransfer register(final String survey_id,
			HttpServletRequest request);

	public RegisterTransfer forceRegistration(final String survey_id);

	// public void update(final String survey_id, final long limit);
}
