/********************************************************************************************************2*4*w*
 * File:  PeerTutorRegistrationPK.java Course materials CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author Mike Norman
 * 
 */
package acmecollege.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@SuppressWarnings("unused")
/**
 * The primary key class for the peer_tutor_registration database table.
 */
// an object which can be embedded in other entities
@Embeddable
//@Access is used to establish where the annotation for JPA will be placed, field or properties. 
@Access(AccessType.FIELD)
public class PeerTutorRegistrationPK implements Serializable {
	// Default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@Column(name = "student_id", nullable = false)
	private int studentId;

	@Basic(optional = false)
	@Column(name = "course_id", nullable = false)
	private int courseId;

	public PeerTutorRegistrationPK() {
	}

	public PeerTutorRegistrationPK(int studentId, int courseId) {
		setStudentId(studentId);
		setCourseId(courseId);
	}

	public int getStudentId() {
		return this.studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public int getCourseId() {
		return this.courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		
		return prime * result + Objects.hash(getStudentId(), getCourseId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof PeerTutorRegistrationPK otherPeerTutorRegistrationPK) {
			
			return Objects.equals(this.getStudentId(), otherPeerTutorRegistrationPK.getStudentId()) &&
				Objects.equals(this.getCourseId(),  otherPeerTutorRegistrationPK.getCourseId());
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PeerTutorRegistrationPK [studentId = ");
		builder.append(studentId);
		builder.append(", courseId = ");
		builder.append(courseId);
		builder.append("]");
		return builder.toString();
	}

}
