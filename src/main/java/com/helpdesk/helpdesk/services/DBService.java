package com.helpdesk.helpdesk.services;

import org.springframework.stereotype.Service;

import com.helpdesk.helpdesk.domain.Chamado;
import com.helpdesk.helpdesk.domain.Cliente;
import com.helpdesk.helpdesk.domain.Tecnico;
import com.helpdesk.helpdesk.domain.enums.Perfil;
import com.helpdesk.helpdesk.domain.enums.Prioridade;
import com.helpdesk.helpdesk.domain.enums.Status;
import com.helpdesk.helpdesk.repositories.ChamadoRepository;
import com.helpdesk.helpdesk.repositories.ClienteRepository;
import com.helpdesk.helpdesk.repositories.TecnicoRepository;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class DBService {

    @Autowired
	private TecnicoRepository tecnicoRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private ChamadoRepository chamadoRepository;
    
    public void instanciaDB(){
        Tecnico tec1 = new Tecnico(null,"Lucas","63653230268","lucas@mail.com","123");
		tec1.addPerfil(Perfil.ADMIN);

		Cliente cli1 = new Cliente(null, "Linuns Torvalds", "80527954780","torvalds@mail.com","123");

		Chamado c1 = new Chamado(null,Prioridade.MEDIA, Status.ANDAMENTO, "Chamado 01","Primeiro chamado", tec1, cli1);

		tecnicoRepository.saveAll(Arrays.asList(tec1));
		clienteRepository.saveAll(Arrays.asList(cli1));
		chamadoRepository.saveAll(Arrays.asList(c1));
    }
}