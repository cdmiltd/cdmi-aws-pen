package pw.cdmi.aws.edu.common.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface BaseService<T,ID> {

	public T getOne(ID id);
	
	
	public <S extends T> T findOne(Example<T> e);
	
	public T save(T entity) ;

	public Optional<T> findById(ID id);

	public boolean existsById(ID id);

	public void deleteById(ID id);

	public void delete(T entity) ;

	public <S extends T> List<S> findAll(Example<S> example) ;
	
	public <S extends T> List<S> findAll(Example<S> example,Sort sort) ;
	
	public Page<T> findAll(Pageable pageable) ;

	public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable);

	public <S extends T> long count(Example<S> example);

	public List<T> findAll();

	public List<T> findAll(Sort sort);

	public List<T> findAllById(Iterable<ID> ids);

	public <S extends T> List<S> saveAll(Iterable<S> entities) ;

	public Object saveAndFlush(T entity) ;

	public void deleteInBatch(Iterable<T> entities);
}
