package br.com.pedro.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.pedro.converter.DozerConverter;
import br.com.pedro.data.model.Book;
import br.com.pedro.data.vo.v1.BookVO;
import br.com.pedro.exception.ResourceNotFoundException;
import br.com.pedro.repository.BookRepository;

@Service
public class BookServices {

	@Autowired
	private BookRepository repository;
	
	public BookVO create(BookVO book) {
		var entity = DozerConverter.parseObject(book, Book.class);
		var vo = DozerConverter.parseObject(repository.save(entity), BookVO.class);
		return vo;
	}
	
	public BookVO update(BookVO book) {
		var entity = repository.findById(book.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("No record found fot this ID"));
		
		entity.setAuthor(book.getAuthor());
		entity.setLaunchDate(book.getLaunchDate());
		entity.setPrice(book.getPrice());
		entity.setTitle(book.getTitle());
		
		var vo = DozerConverter.parseObject(repository.save(entity), BookVO.class);
		return vo;		
	}
	
	public Page<BookVO> findAll(Pageable pageable) {
		var page = repository.findAll(pageable);
		return page.map(this::convertToBookVO);
	}
	
	private BookVO convertToBookVO(Book book) {
		return DozerConverter.parseObject(book, BookVO.class);
	}
	
	public BookVO findById(Long id) {
		var vo = DozerConverter.parseObject(repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No record found for this ID")), BookVO.class);
		return vo;
	}
	
	public void delete(Long id) {
		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No record found for this ID"));
		repository.delete(entity);
	}
	
}
