package de.zofar.management.persistence.entities.user;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * @author dick
 * The persistent class for the group database table.
 * 
 */
@Entity
@Table(name = "role")
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "primary_key_generator", sequenceName = "seq_role_id")
public class RoleEntity implements Serializable  {
    

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "oid")
    private int oid;

    @Column(name = "name")
    private String name;

    
    public RoleEntity() {
    }


	public int getOid() {
		return oid;
	}


	public String getName() {
		return name;
	}


	public void setOid(int oid) {
		this.oid = oid;
	}


	public void setName(String name) {
		this.name = name;
	}


	@Override
	public String toString() {
		return "GroupEntity [oid=" + oid + ", name=" + name + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + oid;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoleEntity other = (RoleEntity) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (oid != other.oid)
			return false;
		return true;
	}

}

