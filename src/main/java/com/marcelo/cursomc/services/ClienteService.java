package com.marcelo.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.marcelo.cursomc.domain.Cliente;
import com.marcelo.cursomc.dto.ClienteDTO;
import com.marcelo.cursomc.repositories.ClienteRepository;
import com.marcelo.cursomc.services.exceptions.DataIntegrityException;
import com.marcelo.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService
{
	@Autowired
	private ClienteRepository repo;
	
	public List<Cliente> findAll()
	{
		return repo.findAll();
	}

	public Cliente find(Integer id)
	{
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}

	public Cliente insert(Cliente cliente)
	{
		cliente.setId(null);
		return repo.save(cliente);
	}

	public Cliente update(Cliente cliente)
	{
		Cliente newCliente = find(cliente.getId());
		updateData(newCliente, cliente);
		return repo.save(newCliente);
	}

	public void delete(Integer id)
	{
		find(id);
		try
		{
			repo.deleteById(id);
		}
		catch (DataIntegrityViolationException e)
		{
			throw new DataIntegrityException("Não é possivel excluir um cliente por que possui entidades relacionadas");
		}
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction)
	{
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO clienteDTO)
	{
		return new Cliente(clienteDTO.getId(), clienteDTO.getNome(), clienteDTO.getEmail(), null, null);
	}
	
	

	private void updateData(Cliente newCliente, Cliente cliente)
	{
		newCliente.setNome(cliente.getNome());
		newCliente.setEmail(cliente.getEmail() );
		
	}
}
