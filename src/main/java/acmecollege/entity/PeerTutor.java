/********************************************************************************************************2*4*w*
 * File:  PeerTutor.java Course materials CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * 
 */
package acmecollege.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@SuppressWarnings("unused")

/**
 * The persistent class for the peer tutor database table.
 */
// @Entity marks this class as an entity which needs to be mapped by JPA.
@Entity
//@Table defines a specific table on DB which is mapped to this entity.
@Table(name = "peer_tutor")
//@NamedQuery attached to this class which uses JPQL/HQL.  
@NamedQuery(name = "PeerTutor.findAll", query = "SELECT pt FROM PeerTutor pt")
//@AttributeOverride can override column details.  This entity uses peer_tutor_id as its primary key name, it needs to override the name in the mapped super class.
@AttributeOverride(name = "id", column = @Column(name = "peer_tutor_id"))
//PojoBase is inherited by any entity with integer as their primary key.
//PojoBaseCompositeKey is inherited by any entity with a composite key as their primary key.
public class PeerTutor extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	//@Basic(optional = false) is used when the object cannot be null.
	//@Basic is for checking the state of object at the scope of our code.
	@Basic(optional = false)
	// @Column is used to define the details of the column which this object will map to.
	// @Column is for mapping and creation (if needed) of an object to DB.
	@Column(name = "first_name", nullable = false, length = 50)
	private String firstName;

	@Basic(optional = false)
	@Column(name = "last_name", nullable = false, length = 50)
	private String lastName;

	@Basic(optional = false)
	@Column(name = "program", nullable = false, length = 50)
	private String program;

	//@Transient is used to annotate a property or field of an entity class, mapped superclass, or embeddable class which is not persistent.
	@Transient
	private String hobby; // Examples:  Tennis, Cycling, etc.
	
	@Transient
	private String careerGoal; // Examples:  Become a teacher, etc.

	// @OneToMany is used to define 1:M relationship between this entity and another.
	// @OneToMany option cascade can be added to define if changes to this entity should cascade to objects.
	// @OneToMany option cascade will be ignored if not added, meaning no cascade effect.
	// @OneToMany option fetch should be lazy to prevent eagerly initializing all the data.
	@OneToMany(cascade=CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "peerTutor")
	// java.util.Set will be unique and also possibly can provide better get performance with HashCode.
	private Set<PeerTutorRegistration> peerTutorRegistrations = new HashSet<>();

	public PeerTutor() {
		super();
	}
	
	public PeerTutor(String firstName, String lastName, String program, Set<PeerTutorRegistration> peerTutorRegistrations) {
		this();
		this.firstName = firstName;
		this.lastName = lastName;
		this.program = program;
		this.peerTutorRegistrations = peerTutorRegistrations;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getCareerGoal() {
		return careerGoal;
	}

	public void setCareerGoal(String careerGoal) {
		this.careerGoal = careerGoal;
	}

	public Set<PeerTutorRegistration> getPeerTutorRegistrations() {
		return peerTutorRegistrations;
	}

	public void setPeerTutorRegistrations(Set<PeerTutorRegistration> peerTutorRegistrationsistrations) {
		this.peerTutorRegistrations = peerTutorRegistrationsistrations;
	}

	public void setPeerTutor(String firstName, String lastName, String program) {
		setFirstName(firstName);
		setLastName(lastName);
		setProgram(program);
	}

	//Inherited hashCode/equals is sufficient for this entity class

}
