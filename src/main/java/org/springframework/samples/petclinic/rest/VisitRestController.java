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

package org.springframework.samples.petclinic.rest;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author Vitaliy Fedoriv
 *
 */

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("api/visits")
public class VisitRestController {

	@Autowired
	private ClinicService clinicService;

	
	@PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@RequestMapping(value = "/plannedVisits/{vetId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Collection<Visit>> getPastVisitsByVet(@PathVariable("vetId") int vetId){
		Collection<Visit> visits = new ArrayList<Visit>();
		visits.addAll(this.clinicService.getPastVisitsByVet(vetId));
		if (visits.isEmpty()){
			return new ResponseEntity<Collection<Visit>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Collection<Visit>>(visits, HttpStatus.OK);
	}

	@PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@RequestMapping(value = "/pastVisits/{vetId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Collection<Visit>> getPlannedVisitsByVet(@PathVariable("vetId") int vetId){
		Collection<Visit> visits = new ArrayList<Visit>();
		visits.addAll(this.clinicService.getPlannedVisitsByVet(vetId));
		if (visits.isEmpty()){
			return new ResponseEntity<Collection<Visit>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Collection<Visit>>(visits, HttpStatus.OK);
	}
  
  
	@PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@RequestMapping(value = "/search", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Collection<Visit>> getVisitSearch(@RequestParam("searchTerm") String searchTerm, @RequestParam boolean noLimit) {
		if (searchTerm == null || searchTerm == "") {
			searchTerm = "";
		}
<<<<<<< HEAD
		else if (searchTerm.length() > 50 || searchTerm.length() < 2) {
=======
		if (searchTerm.length() > 50) {
>>>>>>> userStory3
			return new ResponseEntity<Collection<Visit>>(HttpStatus.BAD_REQUEST);
		}
		Collection<Visit> visits = this.clinicService.findVisitsBySearchTerm(searchTerm, noLimit);
		return new ResponseEntity<Collection<Visit>>(visits, HttpStatus.OK);
	}

    @PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Collection<Visit>> getAllVisits(){
		Collection<Visit> visits = new ArrayList<Visit>();
		visits.addAll(this.clinicService.findAllVisits());
		if (visits.isEmpty()){
			return new ResponseEntity<Collection<Visit>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Collection<Visit>>(visits, HttpStatus.OK);
	}

    @PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@RequestMapping(value = "/{visitId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Visit> getVisit(@PathVariable("visitId") int visitId){
		Visit visit = this.clinicService.findVisitById(visitId);
		if(visit == null){
			return new ResponseEntity<Visit>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Visit>(visit, HttpStatus.OK);
	}

    @PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Visit> addVisit(@RequestBody @Valid Visit visit, BindingResult bindingResult, UriComponentsBuilder ucBuilder){
		BindingErrorsResponse errors = new BindingErrorsResponse();
		HttpHeaders headers = new HttpHeaders();
		if(bindingResult.hasErrors() || (visit == null) || (visit.getPet() == null)){
			errors.addAllErrors(bindingResult);
			headers.add("errors", errors.toJSON());
			return new ResponseEntity<Visit>(headers, HttpStatus.BAD_REQUEST);
		}
		this.clinicService.saveVisit(visit);
		headers.setLocation(ucBuilder.path("/api/visits/{id}").buildAndExpand(visit.getId()).toUri());
		return new ResponseEntity<Visit>(visit, headers, HttpStatus.CREATED);
	}

    @PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@RequestMapping(value = "/{visitId}", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Visit> updateVisit(@PathVariable("visitId") int visitId, @RequestBody @Valid Visit visit, BindingResult bindingResult){
		BindingErrorsResponse errors = new BindingErrorsResponse();
		HttpHeaders headers = new HttpHeaders();
		if(bindingResult.hasErrors() || (visit == null) || (visit.getPet() == null)){
			errors.addAllErrors(bindingResult);
			headers.add("errors", errors.toJSON());
			return new ResponseEntity<Visit>(headers, HttpStatus.BAD_REQUEST);
		}
		Visit currentVisit = this.clinicService.findVisitById(visitId);
		if(currentVisit == null){
			return new ResponseEntity<Visit>(HttpStatus.NOT_FOUND);
		}
		currentVisit.setDate(visit.getDate());
		currentVisit.setDescription(visit.getDescription());
		currentVisit.setPet(visit.getPet());
		currentVisit.setVet(visit.getVet());
		this.clinicService.saveVisit(currentVisit);
		return new ResponseEntity<Visit>(currentVisit, HttpStatus.NO_CONTENT);
	}

    @PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@RequestMapping(value = "/{visitId}", method = RequestMethod.DELETE, produces = "application/json")
	@Transactional
	public ResponseEntity<Void> deleteVisit(@PathVariable("visitId") int visitId){
		Visit visit = this.clinicService.findVisitById(visitId);
		if(visit == null){
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		this.clinicService.deleteVisit(visit);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

}
