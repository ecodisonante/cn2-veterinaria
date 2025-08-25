package com.veterinaria.mascota.service;

import com.veterinaria.mascota.domain.Mascota;
import com.veterinaria.mascota.repository.MascotaRepository;

import java.util.List;

public class MascotaService {
    private static final MascotaRepository repo = new MascotaRepository();

    public Mascota crear(Mascota m) throws Exception {
        // validaciones de dominio simples (ejemplo)
        if (m.getNombre() == null || m.getNombre().isBlank())
            throw new IllegalArgumentException("nombre requerido");
        return repo.insert(m);
    }

    public Mascota obtener(Integer id) throws Exception {
        return repo.findById(id);
    }

    public List<Mascota> listar() throws Exception {
        return repo.findAll();
    }

    public Mascota actualizar(Mascota m) throws Exception {
        return repo.update(m);
    }

    public int eliminar(Integer id) throws Exception {
        return repo.delete(id);
    }
}
