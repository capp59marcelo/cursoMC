package com.marcelo.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marcelo.cursomc.domain.Cidade;
import com.marcelo.cursomc.domain.Cliente;
import com.marcelo.cursomc.domain.Endereco;
import com.marcelo.cursomc.domain.enums.TipoCliente;
import com.marcelo.cursomc.dto.ClienteDTO;
import com.marcelo.cursomc.dto.ClienteNewDTO;
import com.marcelo.cursomc.repositories.ClienteRepository;
import com.marcelo.cursomc.repositories.EnderecoRepository;
import com.marcelo.cursomc.services.exceptions.DataIntegrityException;
import com.marcelo.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService
{
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

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

	@Transactional
	public Cliente insert(Cliente cliente)
	{
		cliente.setId(null);
		enderecoRepository.saveAll(cliente.getEnderecos());
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
		} catch (DataIntegrityViolationException e)
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
		return new Cliente(clienteDTO.getId(), clienteDTO.getNome(), clienteDTO.getEmail(), null, null, null);
	}

	public Cliente fromDTO(ClienteNewDTO clienteNewDTO)
	{
		Cliente cliente = new Cliente(null, clienteNewDTO.getNome(), clienteNewDTO.getEmail(), clienteNewDTO.getCpfOuCnpj(), TipoCliente.toEnum(clienteNewDTO.getTipo()), bCryptPasswordEncoder.encode(clienteNewDTO.getSenha()));
		Cidade cidade = new Cidade(clienteNewDTO.getCidadeId(), null, null);
		Endereco endereco = new Endereco(null,clienteNewDTO.getLogradouro(),
				clienteNewDTO.getNumero(), clienteNewDTO.getComplemento(),
				clienteNewDTO.getBairro(), clienteNewDTO.getCep(), cliente, cidade);
		
		cliente.getEnderecos().add(endereco);
		cliente.getTelefones().add(clienteNewDTO.getTelefone1());
		
		if(clienteNewDTO.getTelefone2() != null) { cliente.getTelefones().add(clienteNewDTO.getTelefone2()); }
		if(clienteNewDTO.getTelefone3() != null) { cliente.getTelefones().add(clienteNewDTO.getTelefone3()); }
		
		return cliente;
	}

	private void updateData(Cliente newCliente, Cliente cliente)
	{
		newCliente.setNome(cliente.getNome());
		newCliente.setEmail(cliente.getEmail());

	}
}
