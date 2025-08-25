package com.veterinaria.cliente.service;

import com.veterinaria.cliente.domain.Cliente;
import com.veterinaria.cliente.repository.ClienteRepository;

import java.util.List;

public class ClienteService {
    private static final ClienteRepository repo = new ClienteRepository();

    public Cliente crear(Cliente c) throws Exception {
        if (c.getNombreCompleto() == null || c.getNombreCompleto().isBlank())
            throw new IllegalArgumentException("nombreCompleto requerido");
        if (c.getEmail() == null || c.getEmail().isBlank())
            throw new IllegalArgumentException("email requerido");
        return repo.insert(c);
    }

    public Cliente obtener(Integer id) throws Exception {
        return repo.findById(id);
    }

    public List<Cliente> listar() throws Exception {
        return repo.findAll();
    }

    public Cliente actualizar(Cliente c) throws Exception {
        return repo.update(c);
    }

    public int eliminar(Integer id) throws Exception {
        return repo.delete(id);
    }
}
