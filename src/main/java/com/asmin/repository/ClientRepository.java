package com.asmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asmin.models.Client;


public interface ClientRepository extends JpaRepository<Client, Integer>{
	public Client findByEmail(String email);
	}
