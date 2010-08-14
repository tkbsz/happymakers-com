package demoapp.security;

import java.util.Date;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
@TransactionManagement
public class AuditService {

	@PersistenceContext
	private EntityManager entityManager;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@RolesAllowed({UserRoles.ROLE_DIRECTOR, UserRoles.ROLE_PAYROLL_ASSISTANT, UserRoles.ROLE_PROJECT_MANAGER})
	public void recordEntry(String userName, String message) {
		AuditEntry entry = new AuditEntry();
		entry.setEntryTimeStamp(new Date());
		entry.setUserName(userName);
		entry.setMessage(message);
		entityManager.persist(entry);
		entityManager.flush();
	}

	@RolesAllowed(UserRoles.ROLE_DIRECTOR)
	public List<AuditEntry> getAuditEntries() {
		TypedQuery<AuditEntry> query = entityManager.createQuery(
				"select ae from AuditEntry ae order by ae.entryTimeStamp",
				AuditEntry.class);
		return query.getResultList();
	}
}
