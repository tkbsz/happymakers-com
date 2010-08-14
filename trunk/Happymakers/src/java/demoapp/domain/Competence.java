package demoapp.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Competence extends AbstractEntity {

	@Column(nullable=false, unique=true)
	private String competenceName = "";
	private String description = "";

	public String getCompetenceName() {
		return competenceName;
	}

	public void setCompetenceName(String competenceName) {
		this.competenceName = competenceName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Competence other = (Competence) obj;
		if ((this.competenceName == null) ? (other.competenceName != null) : !this.competenceName.equals(other.competenceName)) {
			return false;
		}
		if ((this.description == null) ? (other.description != null) : !this.description.equals(other.description)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 17 * hash + (this.competenceName != null ? this.competenceName.hashCode() : 0);
		hash = 17 * hash + (this.description != null ? this.description.hashCode() : 0);
		return hash;
	}

}
