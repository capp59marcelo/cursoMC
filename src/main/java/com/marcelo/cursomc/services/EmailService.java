package com.marcelo.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.marcelo.cursomc.domain.Pedido;

public interface EmailService
{
	void sendOrderConfirmation(Pedido pedido);
	void sendEmail(SimpleMailMessage msg);
}
