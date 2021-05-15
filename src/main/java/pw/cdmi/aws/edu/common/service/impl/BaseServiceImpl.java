package pw.cdmi.aws.edu.common.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import pw.cdmi.aws.edu.common.service.BaseService;

public class BaseServiceImpl<T, ID> implements BaseService<T, ID>{

	@Autowired
	public JpaRepository<T, ID> repo;

	public T getOne(ID id) {
		return repo.findById(id).orElse(null);
	}
	
	public <S extends T> T findOne(Example<T> e) {
		return repo.findOne(e).orElse(null);
	}
	
	

	public T save(T entity) {
		return repo.save(entity);
	}

	public Optional<T> findById(ID id) {
		return repo.findById(id);
	}

	public boolean existsById(ID id) {
		return repo.existsById(id);
	}

	public void deleteById(ID id) {

		repo.deleteById(id);
	}

	public void delete(T entity) {
		repo.delete(entity);
	}

	public <S extends T> List<S> findAll(Example<S> example) {
		return repo.findAll(example);
	}
	
	public Page<T> findAll(Pageable pageable) {
		return repo.findAll(pageable);
	}

	public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {

		return repo.findAll(example, pageable);
	}

	public <S extends T> long count(Example<S> example) {
		return repo.count(example);
	}

	public List<T> findAll() {
		return repo.findAll();
	}

	public List<T> findAll(Sort sort) {
		return repo.findAll(sort);
	}

	public List<T> findAllById(Iterable<ID> ids) {
		return repo.findAllById(ids);
	}

	public <S extends T> List<S> saveAll(Iterable<S> entities) {
		return repo.saveAll(entities);
	}

	public Object saveAndFlush(T entity) {
		return repo.saveAndFlush(entity);
	}

	public void deleteInBatch(Iterable<T> entities) {

		repo.deleteInBatch(entities);
	}



	@Override
	public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
		return repo.findAll(example, sort);
	}

	

}
