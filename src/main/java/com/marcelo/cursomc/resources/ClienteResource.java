package com.marcelo.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.marcelo.cursomc.domain.Cliente;
import com.marcelo.cursomc.dto.ClienteDTO;
import com.marcelo.cursomc.services.ClienteService;

@RestController
@RequestMapping(value="/clientes")
public class ClienteResource
{
	@Autowired
	private ClienteService service;
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<ClienteDTO>> findAll()
	{
		List<Cliente> clientes = service.findAll();
		List<ClienteDTO> clientesDTOs = clientes.stream().map(obj -> new ClienteDTO(obj) ).collect(Collectors.toList());
		
		return ResponseEntity.ok(clientesDTOs);
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<Cliente> find(@PathVariable Integer id)
	{
		Cliente cliente = service.find(id);
		
		return ResponseEntity.ok(cliente);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody ClienteDTO clienteDTO)
	{
		Cliente cliente = service.fromDTO(clienteDTO);
		cliente = service.insert(cliente);
		URI uri =ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cliente.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO clienteDTO, @PathVariable Integer id)
	{
		Cliente cliente = service.fromDTO(clienteDTO);
		cliente.setId(id);
		cliente = service.update(cliente);
		return ResponseEntity.noContent().build();	
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id)
	{
		service.delete(id);
		
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value="/page", method=RequestMethod.GET)
	public ResponseEntity<Page<ClienteDTO>> findPage(
			@RequestParam(value="page", defaultValue="0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage, 
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy, 
			@RequestParam(value="orderBy", defaultValue="ASC") String direction) {
		
		Page<Cliente> clientes = service.findPage(page, linesPerPage, orderBy, direction);
		Page<ClienteDTO> clientesDTOs = clientes.map(obj -> new ClienteDTO(obj)) ;
		
		return ResponseEntity.ok(clientesDTOs);
	}

}
