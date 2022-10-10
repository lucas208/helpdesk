package com.helpdesk.helpdesk.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.helpdesk.helpdesk.domain.Pessoa;
import com.helpdesk.helpdesk.domain.Cliente;
import com.helpdesk.helpdesk.domain.dtos.ClienteDTO;
import com.helpdesk.helpdesk.repositories.PessoaRepository;
import com.helpdesk.helpdesk.repositories.ClienteRepository;
import com.helpdesk.helpdesk.services.exceptions.DataIntegrityViolationException;
import com.helpdesk.helpdesk.services.exceptions.ObjectnotFoundException;

@Service
public class ClienteService {
    
    @Autowired
    private ClienteRepository repository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired 
    private BCryptPasswordEncoder encoder;

    public Cliente findById(Integer id){
        Optional<Cliente> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectnotFoundException("Objeto não encontrado! Id: " + id));
    }

    public List<Cliente> findAll() {
        return repository.findAll();
    }

    public Cliente create(ClienteDTO objDTO) {
        objDTO.setId(null);
        objDTO.setSenha(encoder.encode(objDTO.getSenha()));
        validatesCpfAndEmail(objDTO);
        Cliente newObj = new Cliente(objDTO);
        return repository.save(newObj);
    }

    public Cliente update(Integer id, @Valid ClienteDTO objDTO) {
        objDTO.setId(id);
        Cliente oldObj = findById(id);

        if(!objDTO.getSenha().equals(oldObj.getSenha())){
            objDTO.setSenha(encoder.encode(objDTO.getSenha()));
        } 
        
        validatesCpfAndEmail(objDTO);
        oldObj = new Cliente(objDTO);
        return repository.save(oldObj);
    }

    public void delete(Integer id) {
        Cliente obj = findById(id);
        if(!obj.getChamados().isEmpty()){
            throw new DataIntegrityViolationException("Cliente possui ordens de serviço e não pode ser deletado!");
        }
        repository.deleteById(id);
    }

    private void validatesCpfAndEmail(ClienteDTO objDTO) {
        Optional<Pessoa> obj = pessoaRepository.findByCpf(objDTO.getCpf());
        if(obj.isPresent() && !Objects.equals(obj.get().getId(), objDTO.getId())){
            throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
        }

        obj = pessoaRepository.findByEmail(objDTO.getEmail());
        if(obj.isPresent() && !Objects.equals(obj.get().getEmail(), objDTO.getEmail())){
            throw new DataIntegrityViolationException("E-mail já cadastrado no sistema!");
        }
    }
}