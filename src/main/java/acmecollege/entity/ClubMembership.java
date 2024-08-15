/********************************************************************************************************2*4*w*
 * File:  ClubMembership.java Course materials CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * 
 */
package acmecollege.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;

@SuppressWarnings("unused")

/**
 * The persistent class for the club_membership database table.
 */
@Table (name="club_membership")
@Entity 
@AttributeOverride(name = "id", column = @Column(name = "membership_id"))
public class ClubMembership extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	//  M:1  Changes to this class should cascade to StudentClub.
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "club_id", referencedColumnName = "club_id")
	private StudentClub club;

	//1:1.  Changes to this class should not cascade to MembershipCard.
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membership_id", referencedColumnName = "card_id")
	private MembershipCard card;
	

	@Embedded
	private DurationAndStatus durationAndStatus;

	public ClubMembership() {
		durationAndStatus = new DurationAndStatus();
	}

	public StudentClub getStudentClub() {
		return club;
	}

	public void setStudentClub(StudentClub club) {
		this.club = club;
		//We must manually set the 'other' side of the relationship (JPA does not 'do' auto-management of relationships)
		if (club != null) {
			club.getClubMemberships().add(this);
		}
	}

	public MembershipCard getCard() {
		return card;
	}

	public void setCard(MembershipCard card) {
		this.card = card;
		
	}

	public DurationAndStatus getDurationAndStatus() {
		return durationAndStatus;
	}

	public void setDurationAndStatus(DurationAndStatus durationAndStatus) {
		this.durationAndStatus = durationAndStatus;
	}
	
	//Inherited hashCode/equals NOT sufficient for this Entity class
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		
		// include DurationAndStatus in identity
		return prime * result + Objects.hash(getId(), getDurationAndStatus());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof ClubMembership otherClubMembership) {
			
			return Objects.equals(this.getId(), otherClubMembership.getId()) &&
				Objects.equals(this.getDurationAndStatus(), otherClubMembership.getDurationAndStatus());
		}
		return false;
	}
}
