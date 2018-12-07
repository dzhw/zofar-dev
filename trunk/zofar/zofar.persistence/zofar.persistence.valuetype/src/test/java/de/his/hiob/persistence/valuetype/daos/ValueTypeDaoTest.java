package de.his.hiob.persistence.valuetype.daos;

import static de.his.zofar.persistence.valuetype.daos.ValueTypeDao.qValueType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import de.his.zofar.persistence.test.daos.AbstractDaoTest;
import de.his.zofar.persistence.valuetype.daos.ValueTypeDao;
import de.his.zofar.persistence.valuetype.entities.MeasurementLevel;
import de.his.zofar.persistence.valuetype.entities.NumberValueTypeEntity;
import de.his.zofar.persistence.valuetype.entities.StringValueTypeEntity;
import de.his.zofar.persistence.valuetype.entities.ValueTypeEntity;
import de.his.zofar.persistence.valuetype.entities.possiblevalues.PossibleNumberValueEntity;
import de.his.zofar.persistence.valuetype.entities.possiblevalues.PossibleStringValueEntity;

public class ValueTypeDaoTest extends AbstractDaoTest {
    @Resource
    private ValueTypeDao valueTypeDao;

    private NumberValueTypeEntity createNumberValueType() {
        final NumberValueTypeEntity myValueType = new NumberValueTypeEntity();
        myValueType.setDecimalPlaces(0);
        myValueType.setDescription("Renés erster valuentyp.");
        myValueType.setIdentifier("HIS0815");
        myValueType.setMinimum(0L);
        myValueType.setMaximum(10L);
        myValueType.setMeasurementLevel(MeasurementLevel.ORDINAL);
        return myValueType;
    }

    private Map<Long, PossibleNumberValueEntity> createPossibleValues(
            final NumberValueTypeEntity numberValueType) {
        final Map<Long, PossibleNumberValueEntity> possibleValues = new HashMap<Long, PossibleNumberValueEntity>(
                10);
        final PossibleNumberValueEntity possibleNumberValue0 = new PossibleNumberValueEntity();
        possibleNumberValue0.setValueType(numberValueType);
        possibleNumberValue0.setIsMissing(false);
        possibleNumberValue0.setValue(0L);
        possibleNumberValue0.addLabel("TU Clausthal");

        final PossibleNumberValueEntity possibleNumberValue1 = new PossibleNumberValueEntity();
        possibleNumberValue1.setValueType(numberValueType);
        possibleNumberValue1.setIsMissing(false);
        possibleNumberValue1.setValue(1L);
        possibleNumberValue1.addLabel("Leibniz Universität Hannover");

        possibleValues.put(possibleNumberValue0.getValue(),
                possibleNumberValue0);
        possibleValues.put(possibleNumberValue1.getValue(),
                possibleNumberValue1);
        return possibleValues;
    }

    private Map<String, PossibleStringValueEntity> createPossibleValues(
            final StringValueTypeEntity stringValueType) {
        final Map<String, PossibleStringValueEntity> possibleValues = new HashMap<String, PossibleStringValueEntity>();
        final PossibleStringValueEntity possibleStringValue = new PossibleStringValueEntity();
        possibleStringValue.setValueType(stringValueType);
        possibleStringValue.setIsMissing(false);
        possibleStringValue.setValue("Hurz");
        possibleStringValue.addLabel("Label für Hurz");

        possibleValues.put(possibleStringValue.getValue(), possibleStringValue);
        return possibleValues;
    }

    private StringValueTypeEntity createStringValueType() {
        final StringValueTypeEntity myvalueType = new StringValueTypeEntity();
        myvalueType.setDescription("Renés zweiter valuentyp.");
        myvalueType.setIdentifier("HIS4911");
        myvalueType.setCanBeEmpty(false);
        myvalueType.setLength(20);
        return myvalueType;
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void saveNumberValueType() {
        final NumberValueTypeEntity numberValueType = createNumberValueType();
        numberValueType
                .setPossibleValues(createPossibleValues(numberValueType));

        this.valueTypeDao.save(numberValueType);

        final List<ValueTypeEntity> types = this.valueTypeDao.findAll();

        Assert.assertEquals(1, types.size());

        this.logger.info("Saved numberValueType has id {}",
                numberValueType.getId());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void saveStringValueType() {
        final StringValueTypeEntity stringValueType = createStringValueType();
        stringValueType
                .setPossibleValues(createPossibleValues(stringValueType));

        this.valueTypeDao.save(stringValueType);

        List<ValueTypeEntity> types = this.valueTypeDao.findAll();
        Assert.assertEquals(1, types.size());

        types = this.valueTypeDao.findAll(qValueType.description
                .containsIgnoreCase("ZWEITER"));
        Assert.assertEquals(1, types.size());

        // Polymorphic queries using the general ValueTypeDao with the
        // QStringValueType do not work
        final List<ValueTypeEntity> stringTypes = this.valueTypeDao
                .findAll(qValueType.description.containsIgnoreCase("ZWEITER")
                        .and(qValueType.identifier.like("HIS%11")));
        Assert.assertEquals(1, stringTypes.size());

        this.logger.info("Saved stringValueType has id {}",
                stringValueType.getId());
    }
}
