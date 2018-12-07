package eu.zofar.cockpit.service;

import java.io.Serializable;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import eu.zofar.cockpit.service.transfer.RegisterTransfer;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface RegisterService extends Serializable {

	@WebMethod
	RegisterTransfer register(final String survey_id);

}
