/********************************************************************************************************2*4*w*
 * File:  MembershipCard.java Course materials CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * 
 */
package acmecollege.entity;

import java.io.Serializable;
import javax.persistence.*;

@SuppressWarnings("unused")

/**
 * The persistent class for the membership_card database table.
 */
@Entity 
@Table(name = "membership_card")
@AttributeOverride(name = "id", column = @Column(name="card_id"))
public class MembershipCard extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	// 1:1 mapping.  Changes here should cascade.
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "membership_id")
	private ClubMembership clubMembership;

	// M:1 mapping.  Changes here should not cascade.
	@ManyToOne
    @JoinColumn(name = "student_id")
	private Student owner;

	@Column
	private byte signed;

	public MembershipCard() {
		super();
	}
	
	public MembershipCard(ClubMembership clubMembership, Student owner, byte signed) {
		this();
		this.clubMembership = clubMembership;
		this.owner = owner;
		this.signed = signed;
	}

	public ClubMembership getClubMembership() {
		return clubMembership;
	}

	public void setClubMembership(ClubMembership clubMembership) {
		this.clubMembership = clubMembership;
		
		if (clubMembership != null) {
			clubMembership.setCard(this);
		}
	}

	public Student getOwner() {
		return owner;
	}

	public void setOwner(Student owner) {
		this.owner = owner;
		
		if (owner != null) {
			owner.getMembershipCards().add(this);
		}
	}

	public byte getSigned() {
		return signed;
	}

	public void setSigned(byte signed) {
		this.signed = signed;
	}

	public void setSigned(boolean signed) {
		this.signed = (byte) (signed ? 0b0001 : 0b0000);
	}
	
	//Inherited hashCode/equals is sufficient for this entity class

}