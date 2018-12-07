package tests.schema;

import java.util.Iterator;
import java.util.List;

import org.custommonkey.xmlunit.jaxp13.Validator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXParseException;

import tests.common.AbstractVerificationTest;
import tests.common.MessageProvider;


public class SchemaValidation extends AbstractVerificationTest {

	public SchemaValidation() {
		super();
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void test() {
		MessageProvider.info(this,"validate document against schema...");
		final Validator validator = this.getValidator();
		if (!validator.isInstanceValid(this.getStreamSource())) {
			@SuppressWarnings("unchecked")
			List<SAXParseException> errors = (List<SAXParseException>)validator.getInstanceErrors(getStreamSource());
			MessageProvider.error(this,"validation failed : ");
			if(errors != null){
				Iterator<SAXParseException> it = errors.iterator();
				while(it.hasNext()){
					SAXParseException saxException = it.next();
					final int line = saxException.getLineNumber();
					final int column = saxException.getColumnNumber();
					MessageProvider.error(this,"[{}] {}",line+","+column,saxException.getMessage());
				}
			}
		}

	}
}
