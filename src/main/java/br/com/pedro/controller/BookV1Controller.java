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

import br.com.pedro.data.vo.v1.BookVO;
import br.com.pedro.services.BookServices;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value="Book Endpoint", description = "Description from Book", tags = {"BookEndpoint"})
@RestController
@RequestMapping(value="/api/book/v1")
public class BookV1Controller {
	
	@Autowired
	private BookServices services;
	
	@ApiOperation(value="Find Books recorded")
	@GetMapping(produces = {"application/json", "application/xml", "application/x-yaml"})
	public ResponseEntity<PagedResources<BookVO>> findAll(
			@RequestParam(value="page", defaultValue="0") int page,
			@RequestParam(value="limit", defaultValue="12") int limit,
			@RequestParam(value="direction", defaultValue="asc") String direction,
			PagedResourcesAssembler assembler) {
		
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "title"));
		
		Page<BookVO> books = services.findAll(pageable);
		
		books
		.stream()
		.forEach(p -> p.add(linkTo(methodOn(BookV1Controller.class).findById(p.getKey())).withSelfRel()));
			
		return new ResponseEntity<>(assembler.toResource(books), HttpStatus.OK);
	}
	
	@ApiOperation(value="Find Book by Id")
	@GetMapping(value="/{id}", produces = {"application/json", "application/xml", "application/x-yaml"})
	public BookVO findById(@PathVariable("id") Long id) {
		BookVO bookVO = services.findById(id);
		bookVO.add(linkTo(methodOn(BookV1Controller.class).findById(bookVO.getKey())).withSelfRel());
		return bookVO;
	}
	
	@ApiOperation(value="Record new Book")
	@PostMapping(consumes = {"application/json", "application/xml", "application/x-yaml"},
			produces = {"application/json", "application/xml", "application/x-yaml"})
	public BookVO create(@RequestBody BookVO book) {
		BookVO bookVO = services.create(book);
		bookVO.add(linkTo(methodOn(BookV1Controller.class).findById(bookVO.getKey())).withSelfRel());
		return bookVO;
	}
	
	@ApiOperation(value="Update Book recorded")
	@PutMapping(consumes = {"application/json", "application/xml", "application/x-yaml"},
			produces = {"application/json", "application/xml", "application/x-yaml"})
	public BookVO update(@RequestBody BookVO book) {
		BookVO bookVO = services.update(book);
		bookVO.add(linkTo(methodOn(BookV1Controller.class).findById(bookVO.getKey())).withSelfRel());
		return bookVO;
	}
	
	@ApiOperation(value="Delete Book")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		services.delete(id);
		return ResponseEntity.ok().build();
	}	
}
