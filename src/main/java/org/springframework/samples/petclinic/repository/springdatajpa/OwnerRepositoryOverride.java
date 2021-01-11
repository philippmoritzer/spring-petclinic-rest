package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Collection;

import org.springframework.context.annotation.Profile;
import org.springframework.samples.petclinic.model.Owner;

@Profile("spring-data-jpa")
public interface OwnerRepositoryOverride {
	
    Collection<Owner> findBySearchTerm(String searchTerm, boolean noLimit);
}
