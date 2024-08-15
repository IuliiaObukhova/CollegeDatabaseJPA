/********************************************************************************************************2*4*w*
 * File:  DurationAndStatus.java Course materials CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * 
 */
package acmecollege.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.*;

//This class is not an entity however it can be embedded in other entities.
@Embeddable
public class DurationAndStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column (name="start_date")
	private LocalDateTime startDate;
	
	@Column (name="end_date")
	private LocalDateTime endDate;

	@Column
	private byte active;

	public DurationAndStatus() {
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public void setDurationAndStatus(LocalDateTime startDate, LocalDateTime endDate, String active) {
		setStartDate(startDate);
		setEndDate(endDate);
		byte p = 0b1;
		byte n = 0b0;
		setActive(("+".equals(active) ? p : n));
	}

	public byte getActive() {
		return active;
	}

	public void setActive(byte active) {
		this.active = active;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		
		return prime * result + Objects.hash(getStartDate(), getEndDate(), getActive());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}

		if (obj instanceof DurationAndStatus otherDurationAndStatus) {
			
			return Objects.equals(this.getStartDate(), otherDurationAndStatus.getStartDate()) &&
					Objects.equals(this.getEndDate(), otherDurationAndStatus.getEndDate()) &&
					Objects.equals(this.getActive(), otherDurationAndStatus.getActive());
		}
		return false;
	}

}
