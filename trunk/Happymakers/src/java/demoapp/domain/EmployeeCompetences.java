package demoapp.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class EmployeeCompetences extends AbstractEntity {

	@OneToOne(optional = false, fetch=FetchType.EAGER)
	private Employee employee;
	@OneToMany(fetch=FetchType.EAGER)
	@JoinTable
	private Set<Competence> competences;

	public EmployeeCompetences() {
		super();
		competences = new HashSet<Competence>();
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Set<Competence> getCompetences() {
		return competences;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final EmployeeCompetences other = (EmployeeCompetences) obj;
		if (this.employee != other.employee && (this.employee == null || !this.employee.
				equals(other.employee))) {
			return false;
		}
		if (this.competences != other.competences && (this.competences == null || !this.competences.
				equals(other.competences))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 61 * hash + (this.employee != null ? this.employee.hashCode() : 0);
		hash = 61 * hash + (this.competences != null ? this.competences.hashCode() : 0);
		return hash;
	}

}
