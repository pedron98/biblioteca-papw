package br.com.pedro.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.pedro.data.vo.v1.PersonVO;
import br.com.pedro.services.PersonServices;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

// @CrossOrigin(origins = "http://localhost:8080")
@Api(value = "Person Endpoint", description = "Description from Person", tags = {"PersonEndpoint"})
@RestController
@RequestMapping(value="/api/person/v1")
public class PersonV1Controller {
	
	@Autowired
	private PersonServices services;
	
	@Autowired
	private PagedResourcesAssembler<PersonVO> assembler;
	
	@ApiOperation(value = "Find all people recorded")
	@GetMapping(produces = {"application/json", "application/xml", "application/x-yaml"} )
	public ResponseEntity<?> findAll(
			@RequestParam(value="page", defaultValue="0") int page,
			@RequestParam(value="limit", defaultValue="12") int limit,
			@RequestParam(value="direction", defaultValue = "asc") String direction) {
		
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "firstName"));
		
		Page<PersonVO> persons = services.findAll(pageable);
		persons
			   .stream()
			   .forEach(p -> p.add(
					   linkTo(methodOn(PersonV1Controller.class).findById(p.getKey())).withSelfRel()
				   )
				);
		
		PagedResources<?> resources = assembler.toResource(persons);
		
		return new ResponseEntity<>(resources, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Find Person by First Name")
	@GetMapping(value="/findPersonByName/{firstName}", produces = {"application/json", "application/xml", "application/x-yaml"} )
	public ResponseEntity<?> findPersonByName (
			@PathVariable("firstName") String firstName,
			@RequestParam(value="page", defaultValue="0") int page,
			@RequestParam(value="limit", defaultValue="12") int limit,
			@RequestParam(value="direction", defaultValue = "asc") String direction) {
		
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "firstName"));
		
		Page<PersonVO> persons = services.findPersonByName(firstName, pageable);
		persons
			   .stream()
			   .forEach(p -> p.add(
					   linkTo(methodOn(PersonV1Controller.class).findById(p.getKey())).withSelfRel()
				   )
				);
		
		PagedResources<?> resources = assembler.toResource(persons);
		
		return new ResponseEntity<>(resources, HttpStatus.OK);
	}
	
	// @CrossOrigin(origins = "http://loacalhost:8080")
	@ApiOperation(value="Find recorded by Id")
	@GetMapping(value="/{id}", produces = {"application/json", "application/xml", "application/x-yaml"} )
	public PersonVO findById(@PathVariable("id") Long id) {
		PersonVO personVO = services.findById(id);
		personVO.add(linkTo(methodOn(PersonV1Controller.class).findById(id)).withSelfRel());
		return personVO;
	}
	
	// @CrossOrigin(origins = {"http://localhost:8080", "http://www.pedroaugusto.com"})
	@ApiOperation(value="Record new Person")
	@PostMapping(consumes = {"application/json", "application/xml", "application/x-yaml"},
				produces = {"application/json", "application/xml", "application/x-yaml"} )
	public PersonVO create(@RequestBody PersonVO person) {
		PersonVO personVO = services.create(person);
		personVO.add(linkTo(methodOn(PersonV1Controller.class).findById(personVO.getKey())).withSelfRel());
		return personVO;
	}
	
	@ApiOperation(value="Update Person recorded")
	@PutMapping(consumes = {"application/json", "application/xml", "application/x-yaml"},
				produces = {"application/json", "application/xml", "application/x-yaml"} )
	public PersonVO update(@RequestBody PersonVO person) {
		PersonVO personVO = services.update(person);
		personVO.add(linkTo(methodOn(PersonV1Controller.class).findById(personVO.getKey())).withSelfRel());
		return personVO;
	}
	
	@ApiOperation(value="Delete Person")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		services.delete(id);
		return ResponseEntity.ok().build();
	}
}
