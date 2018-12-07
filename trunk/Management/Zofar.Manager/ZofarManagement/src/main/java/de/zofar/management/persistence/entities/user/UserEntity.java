/**
 * 
 */
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

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author dick
 * The persistent class for the users database table.
 * 
 */
@Entity
@Table(name = "users")
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "primary_key_generator", sequenceName = "seq_user_id")
public class UserEntity implements Serializable, UserDetails {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "oid")
    private int oid;

    @Column(name = "name")
    private String name;

    @Column(name = "passwd")
    private String passwd;

    public UserEntity() {
    }

    public int getOid() {
        return oid;
    }

    public void setOid(final int oid) {
        this.oid = oid;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(final String passwd) {
        this.passwd = passwd;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + oid;
        result = prime * result + ((passwd == null) ? 0 : passwd.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserEntity other = (UserEntity) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (oid != other.oid) {
            return false;
        }
        if (passwd == null) {
            if (other.passwd != null) {
                return false;
            }
        } else if (!passwd.equals(other.passwd)) {
            return false;
        }
        return true;
    }

    /**
     * every participant has the ROLE_PARTICPANT role and nothing else.
     */
    private static final Collection<? extends GrantedAuthority> AUTHORITIES = new HashSet<GrantedAuthority>(
            Arrays.asList(new GrantedAuthority[] { new SimpleGrantedAuthority(
                    "ROLE_USER") }));

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return AUTHORITIES;
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return passwd;
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }

}
