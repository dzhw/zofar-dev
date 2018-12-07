package de.his.zofar.persistence.question.entities.structured.impl;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import de.his.zofar.persistence.question.entities.structured.StructuredElementEntity;

@Entity
public abstract class StructuredTextElementEntity extends StructuredElementEntity {
	
	private String content;
	
	@ManyToOne
	private StructuredTextElementEntity parent;
	
	@OneToMany(mappedBy = "parent")
	private List<StructuredTextElementEntity> children;

	public StructuredTextElementEntity() {
		super();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public StructuredTextElementEntity getParent() {
		return parent;
	}

	public void setParent(StructuredTextElementEntity parent) {
		this.parent = parent;
	}

	public List<StructuredTextElementEntity> getChildren() {
		return children;
	}

	public void setChildren(List<StructuredTextElementEntity> children) {
		this.children = children;
	}
	
	

}
