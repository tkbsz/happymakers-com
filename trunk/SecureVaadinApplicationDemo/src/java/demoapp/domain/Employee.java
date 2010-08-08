package demoapp.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Employee extends AbstractEntity {

	@Column(nullable=false)
	private String firstName = "";
	@Column(nullable=false)
	private String lastName = "";
	@Column(nullable=false, unique=true)
	private String personNumber = "";
	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date dateOfBirth;
	@Column(nullable=false)
	private String position = "";

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

	public String getPersonNumber() {
		return personNumber;
	}

	public void setPersonNumber(String personNumber) {
		this.personNumber = personNumber;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Employee other = (Employee) obj;
		if ((this.firstName == null) ? (other.firstName != null) : !this.firstName.equals(other.firstName)) {
			return false;
		}
		if ((this.lastName == null) ? (other.lastName != null) : !this.lastName.equals(other.lastName)) {
			return false;
		}
		if ((this.personNumber == null) ? (other.personNumber != null) : !this.personNumber.equals(other.personNumber)) {
			return false;
		}
		if (this.dateOfBirth != other.dateOfBirth && (this.dateOfBirth == null || !this.dateOfBirth.equals(other.dateOfBirth))) {
			return false;
		}
		if ((this.position == null) ? (other.position != null) : !this.position.equals(other.position)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 97 * hash + (this.firstName != null ? this.firstName.hashCode() : 0);
		hash = 97 * hash + (this.lastName != null ? this.lastName.hashCode() : 0);
		hash = 97 * hash + (this.personNumber != null ? this.personNumber.hashCode() : 0);
		hash = 97 * hash + (this.dateOfBirth != null ? this.dateOfBirth.hashCode() : 0);
		hash = 97 * hash + (this.position != null ? this.position.hashCode() : 0);
		return hash;
	}
}
