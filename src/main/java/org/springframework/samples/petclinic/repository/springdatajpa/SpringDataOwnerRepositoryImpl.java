package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.context.annotation.Profile;
import org.springframework.samples.petclinic.model.Owner;

@Profile("spring-data-jpa")
public class SpringDataOwnerRepositoryImpl implements OwnerRepositoryOverride {
	
	@PersistenceContext
    private EntityManager em;
    
    @Override
    public Collection<Owner> findBySearchTerm(String searchTerm, boolean noLimit) {
        TypedQuery<Owner> query = this.em.createQuery("SELECT owner FROM Owner owner WHERE "
        + "UPPER(owner.firstName) LIKE concat('%',UPPER(:searchTerm), '%')" 
        + "OR UPPER(owner.lastName) LIKE concat('%',UPPER(:searchTerm), '%')"
        + "OR UPPER(owner.city) LIKE concat('%',UPPER(:searchTerm), '%')"
        + "OR UPPER(owner.address) LIKE concat('%',UPPER(:searchTerm),'%')"
        + "OR owner.telephone LIKE concat('%',:searchTerm,'%')", Owner.class);

        query.setParameter("searchTerm", searchTerm);
        if (!noLimit){
            query.setMaxResults(5);  
        } 
        return query.getResultList();
    }
}
