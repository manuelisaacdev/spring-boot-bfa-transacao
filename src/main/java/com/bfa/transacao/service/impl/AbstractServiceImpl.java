package com.bfa.transacao.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.bfa.transacao.exception.EntityNotFoundException;
import com.bfa.transacao.service.AbstractService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractServiceImpl<T, ID, R extends JpaRepository<T, ID>> implements AbstractService<T, ID> {
	protected final R repository;
	protected final HttpServletRequest request;
	protected final MessageSource messageSource;
	
	public T findById(ID id) throws EntityNotFoundException {
		return repository.findById(id)
		.orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("Entity.notfound", new String[] {id.toString()}, request.getLocale())));
	}
	
	@Override
	public Long count() {
		return repository.count();
	}
	
	@Override
	public Long count(Example<T> example) {
		return repository.count(example);
	}
	
	@Override
	public T findOne(Example<T> example) {
		return repository.findOne(example)
		.orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("Entity.notfound", new String[] {example.getProbe().toString()}, request.getLocale())));
	}
	
	@Override
	public List<T> findAllById(Iterable<ID> ids) {
		return repository.findAllById(ids);
	}
	
	public List<T> findAll() {
		return repository.findAll();
	}
	
	public List<T> findAll(Example<T> example, String orderBy, Direction direction) {
		return repository.findAll(example, Sort.by(direction, orderBy));
	}
	
	@Override
	public Page<T> pagination(int page, int size, Example<T> example, String orderBy, Direction direction) {
		return repository.findAll(example, PageRequest.of(page, size, Sort.by(direction, orderBy)));
	}
	
	public T save(T data) {
		return repository.save(data);
	}
	
	public List<T> save(List<T> datas) {
		return repository.saveAll(datas);
	}
	
	@Override
	public T update(ID id, T data, String... ignoreProperties) {
		var entity = this.findById(id);
		BeanUtils.copyProperties(data, entity, ignoreProperties);
		return this.save(entity);
	}
	
	@Override
	public T delete(T entity) {
		this.repository.delete(entity);
		return entity;
	}
	
	public T deleteById(ID id) {
		T entity = this.findById(id);
		repository.deleteById(id);
		return entity;
	}
}
