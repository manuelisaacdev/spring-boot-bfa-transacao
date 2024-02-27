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

import com.bfa.transacao.dto.TransferenciaDTO;
import com.bfa.transacao.model.Transferencia;
import com.bfa.transacao.service.TransferenciaService;
import com.bfa.transacao.util.BaseController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transferencias")
public class TransferenciaController extends BaseController {
	private final TransferenciaService transferenciaService;
	
	@GetMapping
	public ResponseEntity<List<Transferencia>> findAll(
			@RequestParam(required = false) String numeroOrdem, 
			@RequestParam(required = false) String ibanContaOrigem, @RequestParam(required = false) String ibanContaDestino, 
			@RequestParam(defaultValue = "dataTransacao") String orderBy, @RequestParam(defaultValue = "DESC") Direction direction) {
		return super.ok(transferenciaService.findAll(
		Example.of(
			Transferencia.builder().numeroOrdem(numeroOrdem).ibanContaOrigem(ibanContaOrigem).ibanContaDestino(ibanContaDestino).build(),
			ExampleMatcher.matching()
			.withMatcher("numeroConta", matcher -> matcher.contains().ignoreCase())
			.withMatcher("ibanContaOrigem", matcher -> matcher.contains().ignoreCase())
			.withMatcher("ibanContaDestino", matcher -> matcher.contains().ignoreCase())
		), 
		orderBy, direction));
	}
	
	@GetMapping("/iban/{iban}")
	public ResponseEntity<List<Transferencia>> findAllByIBAN(@PathVariable String iban, @RequestParam(defaultValue = "dataTransacao") String orderBy, @RequestParam(defaultValue = "DESC") Direction direction) {
		return super.ok(transferenciaService.findAllByIBAN(iban, orderBy, direction));
	}
	
	@GetMapping("/paginacao")
	public ResponseEntity<Page<Transferencia>> pagination(
			@RequestParam int page, @RequestParam int size, 
			@RequestParam(required = false) String numeroConta, @RequestParam(required = false) String numeroOrdem,
			@RequestParam(required = false) String ibanContaOrigem, @RequestParam(required = false) String ibanContaDestino,
			@RequestParam(defaultValue = "dataTransacao") String orderBy, @RequestParam(defaultValue = "DESC") Direction direction) {
		return super.ok(transferenciaService.pagination(
				page, size, 
				Example.of(
					Transferencia.builder().numeroOrdem(numeroOrdem).ibanContaOrigem(ibanContaOrigem).ibanContaDestino(ibanContaDestino).build(),
					ExampleMatcher.matching()
					.withMatcher("numeroConta", matcher -> matcher.contains().ignoreCase())
					.withMatcher("ibanContaOrigem", matcher -> matcher.contains().ignoreCase())
					.withMatcher("ibanContaDestino", matcher -> matcher.contains().ignoreCase())
				), 
				orderBy, direction));
	}
	
	@GetMapping("/paginacao/iban")
	public ResponseEntity<Page<Transferencia>> paginationByIBAN(
			@RequestParam int page, @RequestParam int size, @RequestParam String iban, 
			@RequestParam(defaultValue = "dataTransacao") String orderBy, @RequestParam(defaultValue = "DESC") Direction direction) {
		return super.ok(transferenciaService.paginationByIBAN(page, size, iban, orderBy, direction));
	}
	
	@PostMapping
	public ResponseEntity<Transferencia> create(@RequestBody @Valid TransferenciaDTO transferenciaDTO) {
		Transferencia transferencia = Transferencia.builder().build();
		BeanUtils.copyProperties(transferenciaDTO, transferencia);
		return super.created(transferenciaService.create(transferencia));
	}
}
