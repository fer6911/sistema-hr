package com.atlashr.atlas_hr.infrastructure.output.persistence;

import com.atlashr.atlas_hr.domain.model.Usuario;
import com.atlashr.atlas_hr.infrastructure.mapper.UsuarioPersistenceMapper;
import com.atlashr.atlas_hr.infrastructure.output.persistence.entity.UsuarioEntity;
import com.atlashr.atlas_hr.infrastructure.output.persistence.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UsuarioPersistenceAdapterTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioPersistenceMapper usuarioPersistenceMapper;

    private UsuarioPersistenceAdapter adapter;

    private void initAdapter() {
        adapter = new UsuarioPersistenceAdapter(usuarioRepository, usuarioPersistenceMapper);
    }

    private Usuario crearUsuario(String username, String email, String password) {
        return Usuario.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();
    }

    @Test
    void guardarYBuscarPorUsername() {
        initAdapter();
        Usuario usuario = crearUsuario("john123", "john@example.com", "secret123");

        adapter.save(usuario);

        Optional<Usuario> encontrado = adapter.findByUsername("john123");
        assertTrue(encontrado.isPresent());
        assertEquals("john123", encontrado.get().getUsername());
        assertEquals("john@example.com", encontrado.get().getEmail());
    }

    @Test
    void findByUsernameInexistenteRetornaEmpty() {
        initAdapter();

        Optional<Usuario> resultado = adapter.findByUsername("noexiste");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void existsByUsernameTrue() {
        initAdapter();
        Usuario usuario = crearUsuario("john123", "john@example.com", "secret123");
        adapter.save(usuario);

        assertTrue(adapter.existsByUsername("john123"));
    }

    @Test
    void existsByUsernameFalse() {
        initAdapter();

        assertFalse(adapter.existsByUsername("noexiste"));
    }

    @Test
    void existsByEmailTrue() {
        initAdapter();
        Usuario usuario = crearUsuario("john123", "john@example.com", "secret123");
        adapter.save(usuario);

        assertTrue(adapter.existsByEmail("john@example.com"));
    }

    @Test
    void existsByEmailFalse() {
        initAdapter();

        assertFalse(adapter.existsByEmail("noexiste@example.com"));
    }
}
