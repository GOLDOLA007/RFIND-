package com.rfindapi.api_rfind.controller;

import com.rfindapi.api_rfind.Service.ProdutoService;
import model.Funcionario;
import model.Produto;
import com.rfindapi.api_rfind.repository.ProdutoRepository;
import model.dto.FuncionarioUpdateDTO;
import model.dto.ProdutoUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.awt.*;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("apiRfind/produto")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping(value = "/cadastrar")
    public void cadastrar(@RequestBody Produto produto){
        produtoService.insert(produto);
    }

    @GetMapping(value = "/buscar/ativos/empresa/{empresaId}")
    public List<Produto> buscarAtivosPorEmpresa(@PathVariable("empresaId") int empresaId){
        try {
            return produtoService.buscarAtivosporEmpresa(empresaId);
        } catch (Exception e) {
            e.printStackTrace(); // Mostra no console o erro exato
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar produtos ativos", e);
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Produto> atualizarFuncionario(@PathVariable("id") int id,
                                                            @RequestBody ProdutoUpdateDTO dto) {
        try {
            Produto atualizado = produtoService.update(id, dto);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/buscar/prodPesquisa")
    public ResponseEntity<List<Produto>> buscarProdutos(
            @RequestParam(name = "id", required = false) Integer id,
            @RequestParam(name = "nome", required = false) String nome,
            @RequestParam(name = "preco", required = false) Double preco,
            @RequestParam(name = "descricao", required = false) String descricao,
            @RequestParam(name = "categoria", required = false) String categoria,
            @RequestParam(name = "quantidadeEstoque", required = false) Integer quantidadeEstoque,
            @RequestParam(name = "dataFab", required = false) String dataFab,
            @RequestParam(name = "dataValidade", required = false) String dataValidade,
            @RequestParam(name = "fornecedor", required = false) String fornecedor,
            @RequestParam(name = "empresaId") Integer empresaId) {

        try {
            List<Produto> produtos = produtoService.buscarComFiltros(
                    id, nome, preco, descricao, categoria,
                    quantidadeEstoque, dataFab, dataValidade, fornecedor, empresaId);

            return ResponseEntity.ok(produtos);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/excluir/{id}/{empresaId}")
    public ResponseEntity<Void> excluirProduto(
            @PathVariable("id") int id,
            @PathVariable("empresaId") int empresaId) {

        produtoService.excluirProduto(id, empresaId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/desativar/{id}")
    public ResponseEntity<String> desativarFuncionario(
            @PathVariable("id") Integer id,
            @RequestParam("empresaId") Integer empresaId) {
        try {
            produtoService.desativar(id, empresaId);
            return ResponseEntity.ok("Produto desativado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/ativar/{id}")
    public ResponseEntity<String> ativarProduto(
            @PathVariable("id") Integer id,
            @RequestParam("empresaId") Integer empresaId
    ){
        try {
            produtoService.ativar(id, empresaId);
            return ResponseEntity.ok("Produto ativado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/buscar/desativados/empresa/{empresaId}")
    public List<Produto> buscarDesativadosPorEmpresa(@PathVariable("empresaId") int empresaId){
        return produtoService.buscarDesativadosPorEmpresa(empresaId);
    }
}
