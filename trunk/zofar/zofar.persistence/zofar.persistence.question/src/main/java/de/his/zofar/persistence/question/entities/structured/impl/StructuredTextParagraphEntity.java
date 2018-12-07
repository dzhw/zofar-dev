package de.his.zofar.persistence.question.entities.structured.impl;

import javax.persistence.Entity;

@Entity
public class StructuredTextParagraphEntity extends StructuredTextElementEntity {
	
	

	public StructuredTextParagraphEntity() {
		super();
	}
	
	public StructuredTextParagraphEntity(String content) {
		super();
		this.setContent(content);
	}

}
