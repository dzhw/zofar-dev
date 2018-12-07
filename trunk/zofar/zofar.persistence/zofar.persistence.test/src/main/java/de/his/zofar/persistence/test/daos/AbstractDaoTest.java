/**
 * 
 */
package de.his.zofar.persistence.test.daos;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * @author Reitmann
 * 
 */
@TransactionConfiguration
@ContextConfiguration({ "classpath:application-context-persistence-test.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractDaoTest {

    protected Logger logger = LoggerFactory.getLogger(getClass());

}
