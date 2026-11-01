package br.com.mssystems.armazenagem;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/locais-armazenagem")
@RequiredArgsConstructor
public class LocalArmazenagemController {

    private final LocalArmazenagemService service;

    @PostMapping
    public ResponseEntity<LocalArmazenagemResponse> criar(@Valid @RequestBody LocalArmazenagemRequest request) {
        LocalArmazenagemResponse criado = service.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @GetMapping
    public Page<LocalArmazenagemResponse> listar(
            @PageableDefault(size = 20, sort = {"tipo", "rua", "posicao"}) Pageable pageable) {
        return service.listar(pageable);
    }

    @GetMapping("/{id}")
    public LocalArmazenagemResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PutMapping("/{id}")
    public LocalArmazenagemResponse atualizar(@PathVariable Long id,
                                              @Valid @RequestBody LocalArmazenagemRequest request) {
        return service.atualizar(id, request);
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        service.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        service.ativar(id);
        return ResponseEntity.noContent().build();
    }
}
