package eu.zofar.cockpit.service;

import java.io.Serializable;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import eu.zofar.cockpit.service.transfer.ClientTransfer;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface StatusService extends Serializable {

	@WebMethod
	ClientTransfer status() throws Throwable;

}
