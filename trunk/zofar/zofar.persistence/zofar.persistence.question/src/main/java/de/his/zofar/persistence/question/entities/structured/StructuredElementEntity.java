package de.his.zofar.persistence.question.entities.structured;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;

import de.his.zofar.persistence.common.entities.BaseEntity;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(initialValue = 1, name = "primary_key_generator",
sequenceName = "SEQ_STRUCTUREDELEMENT_ID")
public abstract class StructuredElementEntity  extends BaseEntity{

	public StructuredElementEntity() {
		super();
	}

}
