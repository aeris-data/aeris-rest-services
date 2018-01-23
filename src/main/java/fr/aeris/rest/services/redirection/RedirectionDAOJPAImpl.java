package fr.aeris.rest.services.redirection;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fr.sedoo.commons.util.ListUtil;
import fr.sedoo.commons.util.StringUtil;

@Repository
//@EnableTransactionManagement
public class RedirectionDAOJPAImpl implements RedirectionDao {

	protected EntityManager em;

	public EntityManager getEntityManager() {
		return em;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.em = entityManager;
	}

	@Transactional
	public Redirection save(Redirection redirection) {
		if (redirection.getId() == null) {
			getEntityManager().persist(redirection);
			return redirection;
		} else {
			getEntityManager().merge(redirection);
			return redirection;
		}
	}

	@Override
	public ArrayList<Redirection> findAll() throws Exception {
		List<Redirection> aux = em.createQuery(
				"select redirection from Redirection redirection")
				.getResultList();
		ArrayList<Redirection> result = new ArrayList<Redirection>();
		result.addAll(aux);
		return result;
	}

	@Override
	@Transactional
	public void delete(Long redirectionId) throws Exception {
		Redirection aux = getEntityManager().find(
				Redirection.class, redirectionId);
		if (aux != null) {
			getEntityManager().remove(aux);
		}
		
		
	}

	@Override
	public String getUrlByAerisSuffix(String aerisSuffix) throws Exception {
		List<Redirection> resultList = (List<Redirection>) getEntityManager().createQuery("select redirection FROM Redirection redirection where UPPER(redirection.aerisSuffix) like '"+aerisSuffix.toUpperCase()+"'").getResultList();
		if (resultList.isEmpty()) {
			throw new Exception();
		}
		else {
			return StringUtil.trimToEmpty(resultList.get(ListUtil.FIRST_INDEX).getUrl());
		}
	}

}