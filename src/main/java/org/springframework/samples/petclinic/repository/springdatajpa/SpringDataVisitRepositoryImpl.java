/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Visit;

/**
 * @author Vitaliy Fedoriv
 *
 */

@Profile("spring-data-jpa")
public class SpringDataVisitRepositoryImpl implements VisitRepositoryOverride {

	@PersistenceContext
    private EntityManager em;

	@Override
	public void delete(Visit visit) throws DataAccessException {
		String visitId = visit.getId().toString();
		this.em.createQuery("DELETE FROM Visit visit WHERE id=" + visitId).executeUpdate();
        if (em.contains(visit)) {
            em.remove(visit);
        }
	}

	@Override
	public Collection<Visit> getPlannedVisitsByVet(int vetId) throws DataAccessException {
		TypedQuery<Visit> query = this.em.createQuery("SELECT visit FROM Visit visit WHERE visit.vet.id LIKE :vetId AND visit.date > CURRENT_DATE", Visit.class);
		query.setParameter("vetId", vetId);
		return query.getResultList();
  }

  @Override
    public Collection<Visit> findBySearchTerm(String searchTerm, boolean noLimit) {
        TypedQuery<Visit> query = this.em.createQuery("SELECT visit FROM Visit visit WHERE "
		+ "UPPER(visit.description) LIKE concat('%', UPPER(:searchTerm),'%')"
		+ "OR UPPER(visit.pet.name) LIKE concat('%', UPPER(:searchTerm), '%')"
		+ "OR UPPER(visit.vet.lastName) LIKE concat('%', UPPER(:searchTerm), '%')"
		+ "OR UPPER(visit.vet.firstName) LIKE concat('%', UPPER(:searchTerm), '%')"
		+ "OR UPPER(visit.pet.owner.firstName) LIKE concat('%', UPPER(:searchTerm), '%')"
		+ "OR UPPER(visit.pet.owner.lastName) LIKE concat('%', UPPER(:searchTerm), '%')", Visit.class);

        query.setParameter("searchTerm", searchTerm);
        if (!noLimit){
            query.setMaxResults(5);
        }
		return query.getResultList();
	}

	@Override
	public Collection<Visit> getPastVisitsByVet(int vetId) throws DataAccessException {
		TypedQuery<Visit> query = this.em.createQuery("SELECT visit FROM Visit visit WHERE visit.vet.id LIKE :vetId AND visit.date <= CURRENT_DATE", Visit.class);
		query.setParameter("vetId", vetId);
		return query.getResultList();
	}
}
