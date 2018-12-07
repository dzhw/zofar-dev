/**
 *
 */
package de.his.zofar.service.valuetype.test.internal;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import de.his.zofar.service.common.exceptions.NotYetImplementedException;
import de.his.zofar.service.test.AbstractServiceTest;
import de.his.zofar.service.valuetype.internal.ValueTypeInternalService;

/**
 * @author le
 *
 */
public class ValueTypeInternalServiceTest extends AbstractServiceTest {

    @Inject
    private ValueTypeInternalService valueTypeInternalService;

    /**
     * reminder test case to write a test if method is implemented.
     */
    @Test(expected = NotYetImplementedException.class)
    @Transactional(readOnly = true)
    @Rollback(value = true)
    public void testLoadByIdentifier() {
        valueTypeInternalService.loadByIdentifier("");
    }

    /**
     * reminder test case to write a test if method is implemented.
     */
    @Test(expected = NotYetImplementedException.class)
    @Transactional
    @Rollback(value = true)
    public void testSave() {
        valueTypeInternalService.save(null);
    }

    /**
     * reminder test case to write a test if method is implemented.
     */
//    @Test(expected = NotYetImplementedException.class)
//    @Transactional(readOnly = true)
//    @Rollback(value = true)
//    public void testSearchAll() {
//        valueTypeInternalService.searchAll(null);
//    }

}
