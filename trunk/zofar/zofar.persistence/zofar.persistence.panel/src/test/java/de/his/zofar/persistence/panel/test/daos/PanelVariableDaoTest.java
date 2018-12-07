package de.his.zofar.persistence.panel.test.daos;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import de.his.zofar.persistence.panel.daos.PanelVariableDao;
import de.his.zofar.persistence.panel.entities.PanelEntity;
import de.his.zofar.persistence.panel.entities.PanelVariableEntity;
import de.his.zofar.persistence.panel.entities.PanelistEntity;
import de.his.zofar.persistence.panel.entities.panelistvalues.PanelistNumberValueEntity;
import de.his.zofar.persistence.panel.entities.panelistvalues.PanelistValueEntity;
import de.his.zofar.persistence.test.daos.AbstractDaoTest;
import de.his.zofar.persistence.valuetype.entities.NumberValueTypeEntity;
import de.his.zofar.persistence.valuetype.entities.ValueTypeEntity;

public class PanelVariableDaoTest extends AbstractDaoTest {
    @Resource
    private PanelVariableDao panelVariableDao;

    private NumberValueTypeEntity createNumberValueType() {
        final NumberValueTypeEntity numberValueType = new NumberValueTypeEntity();
        numberValueType.setDecimalPlaces(0);
        numberValueType.setMinimum(0L);
        numberValueType.setMaximum(10L);
        numberValueType.setIdentifier("MyNumberVariable");
        return numberValueType;
    }

    private PanelEntity createPanel() {
        final PanelEntity panel = new PanelEntity();
        panel.setName("MyFavouritePanel");

        return panel;
    }

    private PanelistEntity createPanelist(final PanelEntity panel) {
        final PanelistEntity panelist = new PanelistEntity();
        panelist.setEMail("reitmann@his.de");
        panelist.setPanel(panel);
        return panelist;
    }

    private PanelistValueEntity createPanelistValue(final PanelistEntity panelist,
            final PanelVariableEntity panelVariable) {
        if (panelVariable.getValueType().getClass()
                .isAssignableFrom(NumberValueTypeEntity.class)) {
            final PanelistValueEntity panelistValue = new PanelistNumberValueEntity(
                    panelVariable, panelist, 5L);
            return panelistValue;
        }
        return null;
    }

    private PanelVariableEntity createPanelVariable(final ValueTypeEntity valueType) {
        final PanelVariableEntity variable = new PanelVariableEntity();
        variable.setUuid(UUID.randomUUID().toString());
        variable.setName("MD0815");
        variable.setValueType(valueType);
        return variable;
    }

    @Test
    @Transactional
    @Rollback(true)
    public void savePanelistValue() {
        final PanelistEntity panelist = createPanelist(createPanel());

        final PanelVariableEntity panelVariable = createPanelVariable(createNumberValueType());

        panelVariable.addValue(createPanelistValue(panelist, panelVariable));

        this.panelVariableDao.save(panelVariable);

        final List<PanelVariableEntity> variables = this.panelVariableDao.findAll();

        Assert.assertEquals(1, variables.size());

    }

}
