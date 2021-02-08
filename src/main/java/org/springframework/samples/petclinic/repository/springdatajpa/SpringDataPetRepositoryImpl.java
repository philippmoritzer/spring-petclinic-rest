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
import org.springframework.samples.petclinic.model.Pet;

/**
 * @author Vitaliy Fedoriv
 *
 */

@Profile("spring-data-jpa")
public class SpringDataPetRepositoryImpl implements PetRepositoryOverride {
	
	@PersistenceContext
    private EntityManager em;

	@Override
	public void delete(Pet pet) {
		String petId = pet.getId().toString();
		this.em.createQuery("DELETE FROM Visit visit WHERE pet_id=" + petId).executeUpdate();
		this.em.createQuery("DELETE FROM Pet pet WHERE id=" + petId).executeUpdate();
        if (em.contains(pet)) {
            em.remove(pet);
        }
	}

	@Override
    public Collection<Pet> findBySearchTerm(String searchTerm, boolean noLimit) {
		TypedQuery<Pet> query = this.em.createQuery("SELECT pet FROM Pet pet WHERE "
		+ "UPPER(pet.name) LIKE concat('%', UPPER(:searchTerm), '%')"
		+ "OR UPPER(pet.type.name) LIKE concat('%',UPPER(:searchTerm),'%')"
		+ "OR UPPER(pet.owner.firstName) LIKE concat('%',UPPER(:searchTerm),'%')"
		+ "OR UPPER(pet.owner.lastName) LIKE concat('%',UPPER(:searchTerm),'%')", Pet.class);
		
        query.setParameter("searchTerm", searchTerm);
        if (!noLimit){
			query.setMaxResults(5);  
		}
		return query.getResultList();
	}
}
