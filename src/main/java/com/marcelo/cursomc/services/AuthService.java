package com.marcelo.cursomc.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.marcelo.cursomc.domain.Cliente;
import com.marcelo.cursomc.repositories.ClienteRepository;
import com.marcelo.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService
{
	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private Random random = new Random();

	@Autowired
	private EmailService emailService;

	public void sendNewPassword(String email)
	{
		Cliente cliente = clienteRepository.findByEmail(email);

		if(cliente == null)
		{
			throw new ObjectNotFoundException("E-mail não encontrado");
		}

		String newPassowrd = newPassword();
		cliente.setSenha(bCryptPasswordEncoder.encode(newPassowrd));

		clienteRepository.save(cliente);

		emailService.sendNewPasswordEmail(cliente, newPassowrd);
	}

	private String newPassword()
	{
		char[] vet = new char[10];
		for(int i = 0; i < vet.length; i++)
		{
			vet[i] = randomChar();
		}
		return new String(vet);
	}

	private char randomChar()
	{
		int opt = random.nextInt(3);
		if(opt == 0)// gera digito
		{
			return (char) (random.nextInt(10) + 48);
		}
		else if(opt == 0)// gera letra maiuscula
		{
			return (char) (random.nextInt(26) + 65);
		}
		else// gera letra minuscula
		{
			return (char) (random.nextInt(26) + 97);
		}
	}
}
