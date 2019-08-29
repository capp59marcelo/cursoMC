package com.marcelo.cursomc.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.marcelo.cursomc.domain.PagamentoComBoleto;

@Service
public class BoletoService
{
	public void preencherPagamentoComBoleto(PagamentoComBoleto pagamentoComBoleto, Date intanteDoPedido)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(intanteDoPedido);
		calendar.add(Calendar.DAY_OF_MONTH, 7);
		pagamentoComBoleto.setDataVencimento(calendar.getTime());
	}
}
