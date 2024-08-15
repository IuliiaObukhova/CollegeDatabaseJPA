/********************************************************************************************************2*4*w*
 * File:  PojoBaseCompositeKey.java Course materials CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author Mike Norman
 */
package acmecollege.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@SuppressWarnings("unused")

/**
 * Abstract class that is base of (class) hierarchy for all @Entity classes
 * 
 * @param <ID> - type of composite key used
 */
//annotation to define this class as superclass of all entities.
//annotation to place all JPA annotations on fields.
//annotation for listener class.
public abstract class PojoBaseCompositeKey<ID extends Serializable> implements Serializable {
	private static final long serialVersionUID = 1L;

	
	protected int version;
	
	protected LocalDateTime created;

	protected LocalDateTime updated;

	public abstract ID getId();

	public abstract void setId(ID id);

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
		
		if (obj instanceof PojoBaseCompositeKey otherPojoBaseComposite) {
			
			return Objects.equals(this.getId(), otherPojoBaseComposite.getId());
		}
		return false;
	}
}
