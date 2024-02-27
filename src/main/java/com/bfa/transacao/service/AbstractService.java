package com.bfa.transacao.service;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;

import com.bfa.transacao.exception.EntityNotFoundException;

public interface AbstractService<T, ID> {
	public Long count();
	public Long count(Example<T> example);
	public T findOne(Example<T> example);
	public List<T> findAll();
	public List<T> findAllById(Iterable<ID> ids);
	public T findById(ID id) throws EntityNotFoundException;
	public List<T> findAll(Example<T> example, String orderBy, Direction direction);
	public Page<T> pagination(int page, int size, Example<T> example, String orderBy, Direction direction);
	public T save(T data);
	public List<T> save(List<T> datas);
	public T update(ID id, T data, String ... ignoreProperties);
	public T deleteById(ID id);
	public T delete(T entity);
}