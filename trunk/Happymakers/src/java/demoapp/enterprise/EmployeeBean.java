package demoapp.enterprise;

import demoapp.domain.Employee;
import demoapp.domain.EmployeeCompetences;
import demoapp.domain.SalaryInfo;
import demoapp.security.AuditLog;
import demoapp.security.UserRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;


@Stateless
@TransactionManagement
public class EmployeeBean {

	@PersistenceContext
	private EntityManager entityManager;

	@RolesAllowed(UserRoles.ROLE_DIRECTOR)
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@AuditLog
	public void insertEmployee(Employee employee) {
		entityManager.persist(employee);
		entityManager.flush();
	}

	@RolesAllowed(UserRoles.ROLE_DIRECTOR)
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@AuditLog
	public Employee updateEmployee(Employee employee) {
		Employee updated = entityManager.merge(employee);
		entityManager.flush();
		return updated;
	}

	@RolesAllowed(UserRoles.ROLE_DIRECTOR)
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@AuditLog
	public void deleteEmployee(Employee employee) {
		employee = entityManager.merge(employee);
		// Clean up dependencies
		EmployeeCompetences ec = getCompetences(employee);
		if (ec != null) {
			entityManager.remove(ec);
		}
		SalaryInfo si = getSalaryInfo(employee);
		if (si != null) {
			entityManager.remove(si);
		}
		// Now, delete the employee
		entityManager.remove(employee);
		entityManager.flush();
	}

	@RolesAllowed({UserRoles.ROLE_DIRECTOR, 
		UserRoles.ROLE_PAYROLL_ASSISTANT,
		UserRoles.ROLE_PROJECT_MANAGER})
	@AuditLog
	public Employee getEmployeeByPersonNumber(String personNumber) {
		TypedQuery<Employee> query = entityManager.createQuery(
				"select e from Employee e where e.personNumber = :personNumber",
				Employee.class);
		query.setParameter("personNumber", personNumber);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@RolesAllowed({UserRoles.ROLE_DIRECTOR, UserRoles.ROLE_PAYROLL_ASSISTANT})
	@AuditLog
	public SalaryInfo getSalaryInfo(Employee employee) {
		TypedQuery<SalaryInfo> query = entityManager.createQuery(
				"select si from SalaryInfo si where si.employee = :employee",
				SalaryInfo.class);
		query.setParameter("employee", employee);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	
	@RolesAllowed({UserRoles.ROLE_DIRECTOR})
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@AuditLog
	public SalaryInfo saveSalaryInfo(SalaryInfo salaryInfo) {
		SalaryInfo updated = entityManager.merge(salaryInfo);
		entityManager.flush();
		return updated;
	}

	@RolesAllowed({UserRoles.ROLE_DIRECTOR, UserRoles.ROLE_PROJECT_MANAGER})
	@AuditLog
	public EmployeeCompetences getCompetences(Employee employee) {
		TypedQuery<EmployeeCompetences> query = entityManager.createQuery(
				"select c from EmployeeCompetences c where c.employee = :employee",
				EmployeeCompetences.class);
		query.setParameter("employee", employee);
		try {
			EmployeeCompetences ec = query.getSingleResult();
			entityManager.detach(ec);
			return ec;
		} catch (NoResultException e) {
			return null;
		}
	}

	@RolesAllowed({UserRoles.ROLE_DIRECTOR, UserRoles.ROLE_PROJECT_MANAGER})
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@AuditLog
	public EmployeeCompetences saveCompetences(EmployeeCompetences competences) {
		EmployeeCompetences updated = entityManager.merge(competences);
		entityManager.flush();
		return updated;
	}
}
