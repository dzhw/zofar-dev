package eu.zofar.cockpit.master.service.impl;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.zofar.cockpit.master.service.MasterProxy;
import eu.zofar.cockpit.service.transfer.RegisterTransfer;

@WebService(endpointInterface = "eu.zofar.cockpit.service.RegisterService")
public class Master implements Serializable {

	private static final long serialVersionUID = 8818167763259531670L;
	private static final Logger LOGGER = LoggerFactory.getLogger(Master.class);

	@Resource
	private WebServiceContext wsContext;

	private MasterProxy masterProxy;

	public Master() {
		super();
	}

	@WebMethod(exclude = true)
	public void setMasterProxy(final MasterProxy masterProxy) {
		this.masterProxy = masterProxy;
	}

	public RegisterTransfer register(final String survey_id) {
		LOGGER.info("Registration Request received {} {}", survey_id,
				this.masterProxy);
		final MessageContext mc = this.wsContext.getMessageContext();
		return this.masterProxy.register(survey_id,
				(HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST));
	}
}
