package com.rfindapi.api_rfind.Service;

import jakarta.transaction.Transactional;
import model.dto.FuncionarioUpdateDTO;
import com.rfindapi.api_rfind.repository.FuncionarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import model.Funcionario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @PersistenceContext
    private EntityManager entityManager;


    public Funcionario insert(Funcionario funcionario) {
        if(funcionarioRepository.findById(funcionario.getId())!=null){
            throw new RuntimeException("Funcionario Already Exists");
        }
        return funcionarioRepository.save(funcionario);
    }

    public List<Funcionario> buscarAtivosporEmpresa(int empresaId) {
        return funcionarioRepository.findAtivosPorEmpresa(empresaId);
    }

    public List<Funcionario> buscarDesativadosPorEmpresa(int empresaId) {
        return funcionarioRepository.findNotAtivosPorEmpresa(empresaId);
    }

       public List<Funcionario> buscarComFiltros(Integer id,
                                              String nome,
                                              String sobrenome,
                                              String setor,
                                              String tag,
                                              Integer empresaId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Funcionario> cq = cb.createQuery(Funcionario.class);
        Root<Funcionario> root = cq.from(Funcionario.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(root.get("empresa").get("id"), empresaId));

        if (id != null) {
            predicates.add(cb.equal(root.get("id"), id));
        }
           if (nome != null && !nome.isEmpty()) {
               Predicate nomePredicate = cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
               Predicate sobrenomePredicate = cb.like(cb.lower(root.get("sobrenome")), "%" + nome.toLowerCase() + "%");
               predicates.add(cb.or(nomePredicate, sobrenomePredicate));
           }

           //if (sobrenome != null && !sobrenome.isEmpty()) {
        //    predicates.add(cb.like(cb.lower(root.get("sobrenome")), "%" + sobrenome.toLowerCase() + "%"));
        //}
        if (setor != null && !setor.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("setor")), "%" + setor.toLowerCase() + "%"));
        }
        if (tag != null && !tag.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("tagRfid")), "%" + tag.toLowerCase() + "%"));
        }

        cq.select(root).where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(cq).getResultList();
    }

    @Transactional
    public Funcionario update(int id, FuncionarioUpdateDTO dto) {
        Funcionario funcionario = entityManager.find(Funcionario.class, id);
        if (funcionario == null) throw new RuntimeException("Funcionário não encontrado");

        if (dto.getNome() != null) funcionario.setNome(dto.getNome());
        if (dto.getSobrenome() != null) funcionario.setSobrenome(dto.getSobrenome());
        if (dto.getSetor() != null) funcionario.setSetor(dto.getSetor());
        if (dto.getTag() != null) funcionario.setTagRfid(dto.getTag());


        return entityManager.merge(funcionario);
    }

    @Transactional
    public void desativar(Integer id, Integer empresaId) {
        Funcionario funcionario = entityManager.find(Funcionario.class, id);

        if (funcionario == null || funcionario.getEmpresa().getId() != empresaId) {
            throw new RuntimeException("Funcionário não encontrado ou não pertence à empresa");
        }
        if (!funcionario.getAtivado()) {
            throw new RuntimeException("Funcionário já está desativado");
        }

        funcionario.setAtivado(false);
        entityManager.merge(funcionario);
    }

    @Transactional
    public void ativar(Integer id, Integer empresaId) {
        Funcionario funcionario = entityManager.find(Funcionario.class, id);

        if (funcionario == null || funcionario.getEmpresa().getId() != empresaId) {
            throw new RuntimeException("Funcionário não encontrado ou não pertence à empresa");
        }
        if (funcionario.getAtivado()) {
            throw new RuntimeException("Funcionário já está ativado");
        }

        funcionario.setAtivado(true);
        entityManager.merge(funcionario);
    }

    @Transactional
    public void excluirFuncionario(Integer id, Integer empresaId) {
        Funcionario funcionario = entityManager.find(Funcionario.class, id);

        if (funcionario == null) {
            throw new RuntimeException("Funcionário não encontrado");
        }

        if(funcionario.getEmpresa().getId() != empresaId){
            throw new RuntimeException("Funcionário não encontrado ou não pertence à empresa");
        }

        if (funcionario.getAtivado()) {
            throw new RuntimeException("Funcionário ainda está ativado. Desative-o antes de excluir.");
        }

        funcionarioRepository.deleteById(id);
        entityManager.flush(); // Garante que a exclusão é imediatamente enviada ao banco
    }



}
