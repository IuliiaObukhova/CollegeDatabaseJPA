/********************************************************************************************************2*4*w*
 * File:  StudentClub.java Course materials CST 8277
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
import javax.persistence.*;

/**
 * The persistent class for the student_club database table.
 */
@Entity
@AttributeOverride(name = "id", column = @Column(name = "club_id"))
//StudentClub has subclasses AcademicStudentClub and NonAcademicStudentClub.
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name="student_club")
@DiscriminatorColumn(name="academic", length=1,discriminatorType=DiscriminatorType.INTEGER)

public abstract class StudentClub extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column
	private String name;

	//1:M annotation.  This list is effected by changes to this object (cascade).
	@OneToMany(mappedBy="club", cascade = CascadeType.ALL)
	private Set<ClubMembership> clubMemberships = new HashSet<>();

	public StudentClub() {
	}

	public Set<ClubMembership> getClubMemberships() {
		return clubMemberships;
	}

	public void setClubMembership(Set<ClubMembership> clubMemberships) {
		this.clubMemberships = clubMemberships;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		
		// The database schema for the STUDENT_CLUB table has a unique constraint for the NAME column
		
		
		return prime * result + Objects.hash(getId(), getName());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		
		if (obj instanceof StudentClub otherStudentClub) {
			
			return Objects.equals(this.getId(), otherStudentClub.getId()) &&
				Objects.equals(this.getName(), otherStudentClub.getName());
		}
		return false;
	}
}
