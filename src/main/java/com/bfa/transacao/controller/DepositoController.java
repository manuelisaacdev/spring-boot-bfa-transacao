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

import com.bfa.transacao.dto.DepositoDTO;
import com.bfa.transacao.model.Deposito;
import com.bfa.transacao.service.DepositoService;
import com.bfa.transacao.util.BaseController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/depositos")
public class DepositoController extends BaseController {
	private final DepositoService depositoService;
	
	@GetMapping
	public ResponseEntity<List<Deposito>> findAll(
			@RequestParam(required = false) String numeroConta, @RequestParam(required = false) String numeroOrdem, 
			@RequestParam(defaultValue = "dataTransacao") String orderBy, @RequestParam(defaultValue = "DESC") Direction direction) {
		return super.ok(depositoService.findAll(
		Example.of(
			Deposito.builder().numeroConta(numeroConta).numeroOrdem(numeroOrdem).build(),
			ExampleMatcher.matching()
			.withMatcher("numeroConta", matcher -> matcher.contains().ignoreCase())
		), 
		orderBy, direction));
	}
	
	@GetMapping("/iban/{iban}")
	public ResponseEntity<List<Deposito>> findAllByIBAN(@PathVariable String iban, @RequestParam(defaultValue = "dataTransacao") String orderBy, @RequestParam(defaultValue = "DESC") Direction direction) {
		return super.ok(depositoService.findAllByIBAN(iban, orderBy, direction));
	}
	
	@GetMapping("/paginacao")
	public ResponseEntity<Page<Deposito>> pagination(
			@RequestParam int page, @RequestParam int size, 
			@RequestParam(required = false) String numeroConta, @RequestParam(required = false) String numeroOrdem,
			@RequestParam(defaultValue = "dataTransacao") String orderBy, @RequestParam(defaultValue = "DESC") Direction direction) {
		return super.ok(depositoService.pagination(
				page, size, 
				Example.of(
					Deposito.builder().numeroConta(numeroConta).numeroOrdem(numeroOrdem).build(), 
					ExampleMatcher.matching()
					.withMatcher("numeroConta", matcher -> matcher.contains().ignoreCase())
				), 
				orderBy, direction));
	}
	
	@GetMapping("/paginacao/iban")
	public ResponseEntity<Page<Deposito>> paginationByIBAN(
			@RequestParam int page, @RequestParam int size, @RequestParam String iban, 
			@RequestParam(defaultValue = "dataTransacao") String orderBy, @RequestParam(defaultValue = "DESC") Direction direction) {
		return super.ok(depositoService.paginationByIBAN(page, size, iban, orderBy, direction));
	}
	
	@PostMapping
	public ResponseEntity<Deposito> create(@RequestBody @Valid DepositoDTO depositoDTO) {
		Deposito deposito = Deposito.builder().build();
		BeanUtils.copyProperties(depositoDTO, deposito);
		return super.created(depositoService.create(deposito));
	}
}
