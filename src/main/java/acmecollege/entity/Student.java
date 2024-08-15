/********************************************************************************************************2*4*w*
 * File:  Student.java Course materials CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * 
 */
package acmecollege.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;



/**
 * The persistent class for the student database table in the acmecollege schema
 */
@SuppressWarnings("unused")

@Entity
public class Student extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	
	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	// 1:M relation.  Changes should not cascade.
	 @OneToMany(mappedBy = "clubMembership")
	private Set<MembershipCard> membershipCards = new HashSet<>();

	// 1:M relation.  Changes should not cascade.
	 @OneToMany(mappedBy = "student")
	 
	private Set<PeerTutorRegistration> peerTutorRegistrations = new HashSet<>();

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

	public Set<MembershipCard> getMembershipCards() {
		return membershipCards;
	}

	public void setMembershipCards(Set<MembershipCard> membershipCards) {
		this.membershipCards = membershipCards;
	}

	public Set<PeerTutorRegistration> getPeerTutorRegistrations() {
		return peerTutorRegistrations;
	}

	public void setPeerTutorRegistrations(Set<PeerTutorRegistration> peerTutorRegistrations) {
		this.peerTutorRegistrations = peerTutorRegistrations;
	}

	public void setFullName(String firstName, String lastName) {
		setFirstName(firstName);
		setLastName(lastName);
	}
	
	//Inherited hashCode/equals is sufficient for this entity class

}
