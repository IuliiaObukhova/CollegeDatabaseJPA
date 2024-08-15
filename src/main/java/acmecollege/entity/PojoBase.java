/********************************************************************************************************2*4*w*
 * File:  PojoBase.java Course materials CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author Mike Norman
 */
package acmecollege.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.*;

/**
 * Abstract class that is base of (class) hierarchy for all @Entity classes
 */
// annotation to define this class as superclass of all entities.  
@MappedSuperclass


// annotation to place all JPA annotations on fields.
@Access(AccessType.FIELD)
//annotation for listener class.
@EntityListeners(PojoListener.class)
public abstract class PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected int id;

	@Version
	
	protected int version;


	
	@Column //( nullable = true )
	protected LocalDateTime created;

	
	@Column 
	//( nullable = true )
	protected LocalDateTime updated;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public LocalDateTime getCreated() {
		return created;
	}
	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}
	
	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		
		return prime * result + Objects.hash(getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}

		
		if (obj instanceof PojoBase otherPojoBase) {
			
			return Objects.equals(this.getId(), otherPojoBase.getId());
		}
		return false;
	}
}
