package demoapp.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Entity
public class SalaryInfo extends AbstractEntity {

	@OneToOne(optional=false, fetch=FetchType.EAGER)
	private Employee employee;

	@Column(nullable=false)
	private Integer salary;

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Money getSalary() {
		return new Money(salary);
	}

	public void setSalary(Money salary) {
		this.salary = salary.getFixedPointInteger();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final SalaryInfo other = (SalaryInfo) obj;
		if (this.employee != other.employee && (this.employee == null || !this.employee.equals(other.employee))) {
			return false;
		}
		if (this.salary != other.salary && (this.salary == null || !this.salary.equals(other.salary))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 71 * hash + (this.employee != null ? this.employee.hashCode() : 0);
		hash = 71 * hash + (this.salary != null ? this.salary.hashCode() : 0);
		return hash;
	}
}
