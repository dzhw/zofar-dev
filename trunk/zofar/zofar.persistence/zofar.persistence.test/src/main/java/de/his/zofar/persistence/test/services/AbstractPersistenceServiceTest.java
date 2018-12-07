/**
 * 
 */
package de.his.zofar.persistence.test.services;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * the base class for tests on persistence services.
 * 
 * @author le
 * 
 */
@ContextConfiguration({ "classpath:application-context-persistence-test.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class AbstractPersistenceServiceTest {

}
