package com.bfa.transacao.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bfa.transacao.dto.LevantamentoDTO;
import com.bfa.transacao.model.Levantamento;
import com.bfa.transacao.service.LevantamentoService;
import com.bfa.transacao.util.BaseController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/levantamentos")
public class LevantamentoController extends BaseController {
	private final LevantamentoService levantamentoService;
	
	@GetMapping
	public ResponseEntity<List<Levantamento>> findAll(
			@RequestParam(required = false) String numeroConta, @RequestParam(required = false) String numeroOrdem, 
			@RequestParam(defaultValue = "dataTransacao") String orderBy, @RequestParam(defaultValue = "DESC") Direction direction) {
		return super.ok(levantamentoService.findAll(
		Example.of(
			Levantamento.builder().numeroConta(numeroConta).numeroOrdem(numeroOrdem).build(),
			ExampleMatcher.matching()
			.withMatcher("numeroConta", matcher -> matcher.contains().ignoreCase())
		), 
		orderBy, direction));
	}
	
	@GetMapping("/iban/{iban}")
	public ResponseEntity<List<Levantamento>> findAllByIBAN(@PathVariable String iban, @RequestParam(defaultValue = "dataTransacao") String orderBy, @RequestParam(defaultValue = "DESC") Direction direction) {
		return super.ok(levantamentoService.findAllByIBAN(iban, orderBy, direction));
	}
	
	@GetMapping("/paginacao")
	public ResponseEntity<Page<Levantamento>> pagination(
			@RequestParam int page, @RequestParam int size, 
			@RequestParam(required = false) String numeroConta, @RequestParam(required = false) String numeroOrdem,
			@RequestParam(defaultValue = "dataTransacao") String orderBy, @RequestParam(defaultValue = "DESC") Direction direction) {
		return super.ok(levantamentoService.pagination(
				page, size, 
				Example.of(
					Levantamento.builder().numeroConta(numeroConta).numeroOrdem(numeroOrdem).build(), 
					ExampleMatcher.matching()
					.withMatcher("numeroConta", matcher -> matcher.contains().ignoreCase())
				), 
				orderBy, direction));
	}
	
	@GetMapping("/paginacao/iban")
	public ResponseEntity<Page<Levantamento>> paginationByIBAN(
			@RequestParam int page, @RequestParam int size, @RequestParam String iban, 
			@RequestParam(defaultValue = "dataTransacao") String orderBy, @RequestParam(defaultValue = "DESC") Direction direction) {
		return super.ok(levantamentoService.paginationByIBAN(page, size, iban, orderBy, direction));
	}
	
	@PostMapping
	public ResponseEntity<Levantamento> create(@RequestBody @Valid LevantamentoDTO levantamentoDTO) {
		Levantamento levantamento = Levantamento.builder().build();
		BeanUtils.copyProperties(levantamentoDTO, levantamento);
		return super.created(levantamentoService.create(levantamento));
	}
}
