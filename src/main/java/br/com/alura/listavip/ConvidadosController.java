package br.com.alura.listavip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.alura.enviadorEmail.EmailService;
import br.com.alura.listavip.model.Convidado;
import br.com.alura.listavip.repository.ConvidadoRepository;
import br.com.alura.listavip.service.ConvidadoService;

@Controller
public class ConvidadosController {
	
	@Autowired
	public ConvidadoService service;

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/listaconvidado")
	public String listaConvidados(Model model) {

		Iterable<Convidado> convidados = service.obterTodos();
		model.addAttribute("convidados", convidados);

		return "listaconvidado";
	}

	@RequestMapping("/carregarEditar")
	public String carregarEditar(@RequestParam("id") Long id, Model model) {

		Convidado convidado = service.buscaUm(id);
		model.addAttribute("convidadoEditar", convidado);

		Iterable<Convidado> convidados = service.obterTodos();
		model.addAttribute("convidados", convidados);

		return "listaconvidado";
	}

	@RequestMapping(value = "/salvar", method = RequestMethod.POST)
	public String salvar(@RequestParam("nome") String nome, @RequestParam("email") String email,
			@RequestParam("telefone") String telefone, @RequestParam("id") Long id, Model model) {

		Convidado novoConvidado = new Convidado(id, nome, email, telefone);
		service.salvar(novoConvidado);

		new EmailService().enviar(nome, email);

		Iterable<Convidado> convidados = service.obterTodos();
		model.addAttribute("convidados", convidados);

		return "listaconvidado";
	}

	@RequestMapping("/remover")
	public String remover(@RequestParam("id") Long id, Model model) {

		service.apagar(id);

		Iterable<Convidado> convidados = service.obterTodos();
		model.addAttribute("convidados", convidados);

		return "listaconvidado";
	}
}
