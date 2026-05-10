package com.rfindapi.api_rfind.Service;

import com.rfindapi.api_rfind.repository.FuncionarioRepository;
import com.rfindapi.api_rfind.repository.ProdutoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import model.Funcionario;
import model.Produto;
import model.dto.FuncionarioUpdateDTO;
import model.dto.ProdutoUpdateDTO;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;
    //private static final Logger logger = (Logger) LoggerFactory.getLogger(ProdutoService.class);

    @PersistenceContext
    private EntityManager entityManager;

    public Produto insert(Produto produto) {
        if(produtoRepository.findById(produto.getId())!=null){
            throw new RuntimeException("Produto Already Exists");
        }
        return produtoRepository.save(produto);
    }

    public List<Produto> buscarAtivosporEmpresa(int empresaId) {
        try {
            List<Produto> produtos = produtoRepository.findAtivosPorEmpresa(empresaId);
            if (produtos == null) {
                return Collections.emptyList();
            }
            return produtos;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar produtos ativos", e);
        }
    }

    @Transactional
    public Produto update(int id, ProdutoUpdateDTO dto) {
        Produto produto = entityManager.find(Produto.class, id);
        if (produto == null) throw new RuntimeException("Produto não encontrado");

        if (dto.getNome() != null) produto.setNome(dto.getNome());
        if (dto.getPreco() != null) produto.setPreco(dto.getPreco());
        if (dto.getDescricao() != null) produto.setDescricao(dto.getDescricao());
        if (dto.getCategoria() != null) produto.setCategoria(dto.getCategoria());
        if (dto.getQntEstoque() != null) produto.setCategoria(dto.getCategoria());
        if (dto.getDataFab() != null) produto.setDataFab(dto.getDataFab());
        if (dto.getDataValidade() != null) produto.setDataValidade(dto.getDataValidade());
        if (dto.getFornecedor() != null) produto.setFornecedor(dto.getFornecedor());


        return entityManager.merge(produto);
    }

    public List<Produto> buscarComFiltros(Integer id,
                                          String nome,
                                          Double preco,
                                          String descricao,
                                          String categoria,
                                          Integer quantidadeEstoque,
                                          String dataFab,  // Mantido como String
                                          String dataValidade,  // Mantido como String
                                          String fornecedor,
                                          Integer empresaId) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> cq = cb.createQuery(Produto.class);
        Root<Produto> root = cq.from(Produto.class);

        List<Predicate> predicates = new ArrayList<>();

        // Filtro obrigatório por empresa
        predicates.add(cb.equal(root.get("empresa").get("id"), empresaId));

        // Filtros opcionais
        if (id != null) {
            predicates.add(cb.equal(root.get("id"), id));
        }
        if (nome != null && !nome.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
        }
        if (preco != null) {
            predicates.add(cb.equal(root.get("preco"), preco));
        }
        if (descricao != null && !descricao.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("descricao")), "%" + descricao.toLowerCase() + "%"));
        }
        if (categoria != null && !categoria.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("categoria")), "%" + categoria.toLowerCase() + "%"));
        }
        if (quantidadeEstoque != null) {
            predicates.add(cb.equal(root.get("quantidadeEstoque"), quantidadeEstoque));
        }
        if (dataFab != null && !dataFab.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("dataFab")), "%" + dataFab.toLowerCase() + "%"));
        }
        if (dataValidade != null && !dataValidade.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("dataValidade")), "%" + dataValidade.toLowerCase() + "%"));
        }
        if (fornecedor != null && !fornecedor.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("fornecedor")), "%" + fornecedor.toLowerCase() + "%"));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(cq).getResultList();
    }

    public List<Produto> buscarDesativadosPorEmpresa(int empresaId) {
        return produtoRepository.findNotAtivosPorEmpresa(empresaId);
    }

    @Transactional
    public void desativar(Integer id, Integer empresaId) {
        Produto produto = entityManager.find(Produto.class, id);

        if (produto == null || produto.getEmpresa().getId() != empresaId) {
            throw new RuntimeException("Produto não encontrado ou não pertence à empresa");
        }
        if (!produto.isAtivado()) {
            throw new RuntimeException("Produto já está desativado");
        }

        produto.setAtivado(false);
        entityManager.merge(produto);
    }

    @Transactional
    public void excluirProduto(Integer id, Integer empresaId) {
        Produto produto = entityManager.find(Produto.class, id);

        if (produto == null) {
            throw new RuntimeException("Produto não encontrado");
        }

        if(produto.getEmpresa().getId() != empresaId){
            throw new RuntimeException("Produto não encontrado ou não pertence à empresa");
        }
/*
        if (produto.isAtivado()) {
            throw new RuntimeException("Produto ainda está ativado. Desative-o antes de excluir.");
        }
*/
        produtoRepository.deleteById(id);
        entityManager.flush(); // Garante que a exclusão é imediatamente enviada ao banco
    }

    @Transactional
    public void ativar(Integer id, Integer empresaId) {
        Produto produto = entityManager.find(Produto.class, id);

        if (produto == null || produto.getEmpresa().getId() != empresaId) {
            throw new RuntimeException("Produto não encontrado ou não pertence à empresa");
        }
        if (produto.isAtivado()) {
            throw new RuntimeException("Produto já está ativado");
        }

        produto.setAtivado(true);
        entityManager.merge(produto);
    }

}
