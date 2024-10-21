package com.asmin.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asmin.models.Client;
import com.asmin.models.ClientDto;
import com.asmin.repository.ClientRepository;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/clients")
public class ClientsController{
	
	@Autowired
	private ClientRepository repo;
	
	
	@GetMapping({"","/"})
	public String getClients(Model model) {
		var clients = repo.findAll(Sort.by(Sort.Direction.ASC, "id"));
		model.addAttribute("clients",clients);
		return "clients/index";
	}
	
	@GetMapping("/create")
	public String createClient(Model model){
		ClientDto clientDto = new ClientDto();
		model.addAttribute(clientDto);
		return "clients/create";
		
	}
		
	@PostMapping("/create")
	public String createClient(@Valid @ModelAttribute ClientDto clientDto,BindingResult result){
		if(repo.findByEmail(clientDto.getEmail())!=null) {
			result.addError(new FieldError("clientDto", "email", clientDto.getEmail(),false,null,null,"Email address is already used"));
		}
		
		if(result.hasErrors()) {
			return "clients/create";
		}
		
		Client client = new Client();
		client.setFirstName(clientDto.getFirstName());
		client.setLastName(clientDto.getLastName());
		client.setEmail(clientDto.getEmail());
		client.setPhone(clientDto.getPhone());
		client.setAddress(clientDto.getAddress());
		client.setStatus(clientDto.getStatus());
		client.setCreatedAt(new Date());
		repo.save(client);
		
		return "redirect:/clients";
	}
	
	@GetMapping("/edit/{id}")
	public String editClient(@PathVariable("id") Integer id,Model model){
		Client client = repo.findById(id).orElse(null);
		if(client==null) {
			return "redirect:/clients";
		}
		ClientDto clientDto = new ClientDto();
		clientDto.setFirstName(client.getFirstName());
		clientDto.setLastName(client.getLastName());
		clientDto.setEmail(client.getEmail());
		clientDto.setPhone(client.getPhone());
		clientDto.setAddress(client.getAddress());
		clientDto.setStatus(client.getStatus());
		
		model.addAttribute("client",client);
		model.addAttribute("clientDto",clientDto);
		model.addAttribute(clientDto);
		return "clients/edit";		
	}
	
	@PostMapping("/edit/{id}")
	public String editClient(@PathVariable("id") Integer id,Model model,@Valid @ModelAttribute ClientDto clientDto,BindingResult result) {
		
		Client client = repo.findById(id).orElse(null);
		
		if(client == null) {
			return "redirect:/clients";
		}		
		model.addAttribute("client", client);
		
		if(result.hasErrors()) {
			return "clients/edit";
		}	
		
		client.setFirstName(clientDto.getFirstName());
		client.setLastName(clientDto.getLastName());
		client.setEmail(clientDto.getEmail());
		client.setPhone(clientDto.getPhone());
		client.setAddress(clientDto.getAddress());
		client.setStatus(clientDto.getStatus());
		try {
			repo.save(client);
		}catch(Exception e) {
			result.addError(new FieldError("clientDto", "email", clientDto.getEmail(),false,null,null,"Email address is already used"));
			return "clients/edit";
		}
		return "redirect:/clients";
	}
	@GetMapping("/delete/{id}")
	public String deleteClient(@PathVariable int id) {
		Client client = repo.findById(id).orElse(null);
		if(client!=null) {
			repo.delete(client);
		}
		return "redirect:/clients";
	}
}
