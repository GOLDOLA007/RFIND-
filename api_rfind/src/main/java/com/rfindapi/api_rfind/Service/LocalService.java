package com.rfindapi.api_rfind.Service;

import com.rfindapi.api_rfind.repository.FuncionarioRepository;
import com.rfindapi.api_rfind.repository.LocalRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import model.Funcionario;
import model.Local;
import model.dto.FuncionarioUpdateDTO;
import model.dto.LocalUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocalService {
    @Autowired
    private LocalRepository localRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public Local insert(Local local) {
        if(localRepository.findById(local.getId())!=null){
            throw new RuntimeException("Local Already Exists");
        }
        return localRepository.save(local);
    }

    public List<Local> buscarporEmpresa(int empresaId) {
        return localRepository.findPorEmpresa(empresaId);
    }

    @Transactional
    public Local update(int id, LocalUpdateDTO dto) {
        Local local = entityManager.find(Local.class, id);
        if (local == null) throw new RuntimeException("Local não encontrado");

        if (dto.getNome() != null) local.setNome(dto.getNome());
        if (dto.getDescricao() != null) local.setDescricao(dto.getDescricao());
        if (dto.getSensor() != null) local.setIdrfid(dto.getSensor());

        return entityManager.merge(local);
    }

    public List<Local> buscarComFiltros(Integer id,
                                        String nome,
                                        String descricao,
                                        String sensor,
                                        Integer empresaId) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Local> cq = cb.createQuery(Local.class);
        Root<Local> root = cq.from(Local.class);

        List<Predicate> predicates = new ArrayList<>();

        // Filtro obrigatório por empresa
        predicates.add(cb.equal(root.get("empresa").get("id"), empresaId));

        // Filtros opcionais
        if (id != null) {
            predicates.add(cb.equal(root.get("id"), id));
        }

        // Pesquisa por nome (contains)
        if (nome != null && !nome.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("nome")),
                    "%" + nome.toLowerCase() + "%"));
        }

        // Pesquisa por descrição (contains)
        if (descricao != null && !descricao.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("descricao")),
                    "%" + descricao.toLowerCase() + "%"));
        }

        // Pesquisa por sensor (contains)
        if (sensor != null && !sensor.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("idrfid")),
                    "%" + sensor.toLowerCase() + "%"));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(cq).getResultList();
    }

    @Transactional
    public void excluirLocal(Integer id, Integer empresaId) {
        Local local = entityManager.find(Local.class, id);

        if (local == null) {
            throw new RuntimeException("Local não encontrado");
        }

        if(local.getEmpresa().getId() != empresaId){
            throw new RuntimeException("Local não encontrado ou não pertence à empresa");
        }

        localRepository.deleteById(id);
        entityManager.flush(); // Garante que a exclusão é imediatamente enviada ao banco
    }
}
