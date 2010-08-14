package demoapp.enterprise;

import demoapp.domain.Competence;
import demoapp.security.UserRoles;
import java.util.Collection;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
@RolesAllowed({UserRoles.ROLE_DIRECTOR,
	UserRoles.ROLE_PROJECT_MANAGER})
@TransactionManagement
public class CompetenceBean {

	@PersistenceContext
	private EntityManager entityManager;

	public Collection<Competence> getCompetences() {
		TypedQuery<Competence> query = entityManager.createQuery(
				"select c from Competence c order by c.competenceName",
				Competence.class);
		return query.getResultList();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void insertCompetence(Competence competence) {
		entityManager.persist(competence);
		entityManager.flush();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Competence updateCompetence(Competence competence) {
		Competence updated = entityManager.merge(competence);
		entityManager.flush();
		return updated;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteCompetence(Competence competence) {
		competence = entityManager.merge(competence);
		entityManager.remove(competence);
		entityManager.flush();
	}
}
