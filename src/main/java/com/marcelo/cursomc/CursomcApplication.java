package com.marcelo.cursomc;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.marcelo.cursomc.domain.Categoria;
import com.marcelo.cursomc.domain.Cidade;
import com.marcelo.cursomc.domain.Cliente;
import com.marcelo.cursomc.domain.Endereco;
import com.marcelo.cursomc.domain.Estado;
import com.marcelo.cursomc.domain.ItemPedido;
import com.marcelo.cursomc.domain.Pagamento;
import com.marcelo.cursomc.domain.PagamentoComBoleto;
import com.marcelo.cursomc.domain.PagamentoComCartao;
import com.marcelo.cursomc.domain.Pedido;
import com.marcelo.cursomc.domain.Produto;
import com.marcelo.cursomc.domain.enums.EstadoPagamento;
import com.marcelo.cursomc.domain.enums.TipoCliente;
import com.marcelo.cursomc.repositories.CategoriaRepository;
import com.marcelo.cursomc.repositories.CidadeRepository;
import com.marcelo.cursomc.repositories.ClienteRepository;
import com.marcelo.cursomc.repositories.EnderecoRepository;
import com.marcelo.cursomc.repositories.EstadoRepository;
import com.marcelo.cursomc.repositories.ItemPedidoRepository;
import com.marcelo.cursomc.repositories.PagamentoRepository;
import com.marcelo.cursomc.repositories.PedidoRepository;
import com.marcelo.cursomc.repositories.ProdutoRepository;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner
{
	@Autowired
	private CategoriaRepository categoriaRepository;
	@Autowired
	private ProdutoRepository produtoRepository;
	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	@Override
	public void run(String... args) throws Exception
	{
		Categoria categoria1 = new Categoria(null, "Informática");
		Categoria categoria2 = new Categoria(null, "Escritório");
		
		Produto produto1 = new Produto(null, "Computador", 2000.00);
		Produto produto2 = new Produto(null, "Impressora", 800.00);
		Produto produto3 = new Produto(null, "Mouse", 80.00);
		
		categoria1.getProdutos().addAll(Arrays.asList(produto1, produto2, produto3));
		categoria2.getProdutos().addAll(Arrays.asList(produto2));
		
		produto1.getCategorias().addAll(Arrays.asList(categoria1));
		produto2.getCategorias().addAll(Arrays.asList(categoria1, categoria2));
		produto3.getCategorias().addAll(Arrays.asList(categoria1));
				
		categoriaRepository.saveAll(Arrays.asList(categoria1, categoria2)	);
		produtoRepository.saveAll(Arrays.asList(produto1, produto2, produto3)	);
		
		Estado estado1 = new Estado(null, "Minas Gerais");
		Estado estado2 = new Estado(null, "São Paulo");
		
		Cidade cidade1 = new Cidade(null, "Uberlândia", estado1);
		Cidade cidade2 = new Cidade(null, "São Paulo", estado2);
		Cidade cidade3 = new Cidade(null, "Campinas", estado2);
		
		estado1.getCidades().addAll(Arrays.asList(cidade1));
		estado2.getCidades().addAll(Arrays.asList(cidade2, cidade3));
		
		estadoRepository.saveAll(Arrays.asList(estado1, estado2));
		cidadeRepository.saveAll(Arrays.asList(cidade1, cidade2, cidade3));
		
		Cliente cliente1 = new Cliente(null, "Maria Silva", "mari@gmail.com", "36378912377", TipoCliente.PESSOAFISICA);
		
		cliente1.getTelefones().addAll(Arrays.asList("27363323", "93838393"));
		
		Endereco endereco1 = new Endereco(null, "Rua Flores", "300", "Apt 203", "Jardim", "38220834",cliente1 ,cidade1);
		Endereco endereco2 = new Endereco(null, "Avenida Matos", "105", "Sala 800", "Centro", "38777012",cliente1 ,cidade2);
		
		cliente1.getEnderecos().addAll(Arrays.asList(endereco1, endereco2));
		
		clienteRepository.saveAll(Arrays.asList(cliente1));
		enderecoRepository.saveAll(Arrays.asList(endereco1, endereco2));
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		Pedido pedido1 = new Pedido(null, sdf.parse("30/09/2017 10:32"), cliente1, endereco1);
		Pedido pedido2 = new Pedido(null, sdf.parse("10/10/2017 19:35"), cliente1, endereco2);
		
		Pagamento pagamento1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, pedido1, 6);
		pedido1.setPagamento(pagamento1);
		
		Pagamento pagamento2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, pedido2, sdf.parse("20/10/2017 00:00"), null);
		pedido2.setPagamento(pagamento2);
		
		cliente1.getPedidos().addAll(Arrays.asList(pedido1, pedido2));
		
		pedidoRepository.saveAll(Arrays.asList(pedido1, pedido2));
		pagamentoRepository.saveAll(Arrays.asList(pagamento1, pagamento2));
		
		ItemPedido itemProduto1 = new ItemPedido(pedido1, produto1, 0.00, 1, 2000.00);
		ItemPedido itemProduto2 = new ItemPedido(pedido1, produto3, 0.00, 2, 80.00);
		ItemPedido itemProduto3 = new ItemPedido(pedido2, produto2, 100.00, 1, 800.00);

		pedido1.getItens().addAll(Arrays.asList(itemProduto1, itemProduto2));
		pedido2.getItens().addAll(Arrays.asList(itemProduto3));

		produto1.getItens().addAll(Arrays.asList(itemProduto1));
		produto2.getItens().addAll(Arrays.asList(itemProduto3));
		produto3.getItens().addAll(Arrays.asList(itemProduto2));

		itemPedidoRepository.saveAll(Arrays.asList(itemProduto1, itemProduto2, itemProduto3));	
		
	}

}
