package com.rfindapi.api_rfind.Service;

import com.rfindapi.api_rfind.repository.ProdutoRepository;
import com.rfindapi.api_rfind.repository.RegistroRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import model.Funcionario;
import model.Local;
import model.Produto;
import model.Registro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import model.SensorRfid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RegistroService {
    @Autowired
    private RegistroRepository registroRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public Registro insert(Registro registro) {
        if (registro.getId() != 0 && registroRepository.findById(registro.getId()).isPresent()) {
            throw new RuntimeException("Registro Already Exists");
        }
        return registroRepository.save(registro);
    }

    public List<Registro> buscarporEmpresa(int empresaId) {
        try {
            List<Registro> registros = registroRepository.findPorEmpresa(empresaId);
            if (registros == null) {
                return Collections.emptyList();
            }
            return registros;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar registros", e);
        }
    }

    public List<Registro> buscarComFiltros(Integer idFuncionario,
                                           String funcionarioNome,
                                           String local,
                                           String sensor,
                                           String dataEntrada,
                                           String horarioEntrada,
                                           String dataSaida,
                                           String horarioSaida,
                                           Integer empresaId) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Registro> cq = cb.createQuery(Registro.class);
        Root<Registro> root = cq.from(Registro.class);

        // Join com Funcionario (usando o nome da coluna idFunc)
        Join<Registro, Funcionario> funcionarioJoin = root.join("funcionario", JoinType.INNER);

        // Join com Local (usando o nome da coluna idRfid)
        Join<Registro, Local> localJoin = root.join("local", JoinType.INNER);

        List<Predicate> predicates = new ArrayList<>();

        // Filtro obrigatório por empresa (usando o nome da coluna idEmpresa)
        predicates.add(cb.equal(root.get("empresa").get("id"), empresaId));

        // Filtro por ID do funcionário
        if (idFuncionario != null) {
            predicates.add(cb.equal(funcionarioJoin.get("id"), idFuncionario));
        }

        // Filtro por nome do funcionário (nome OU sobrenome)
        if (funcionarioNome != null && !funcionarioNome.isEmpty()) {
            String termoBusca = "%" + funcionarioNome.toLowerCase() + "%";
            predicates.add(cb.or(
                    cb.like(cb.lower(funcionarioJoin.get("nome")), termoBusca),
                    cb.like(cb.lower(funcionarioJoin.get("sobrenome")), termoBusca)
            ));
        }

        // Filtro por local (nome do local)
        if (local != null && !local.isEmpty()) {
            String termoBusca = "%" + local.toLowerCase() + "%";
            predicates.add(cb.or(
                    cb.like(cb.lower(localJoin.get("nome")), termoBusca)
                    //cb.like(cb.lower(localJoin.get("idrfid")), termoBusca)
            ));
        }

        // Filtro por sensor (como não há join explícito com Sensor, usamos o campo local.idrfid)
        if (sensor != null && !sensor.isEmpty()) {
            predicates.add(cb.like(
                    cb.lower(root.get("local").get("idrfid")), // Acessa o idrfid do Local
                    "%" + sensor.toLowerCase() + "%"
            ));
        }

        // Filtro por data de entrada
        if (dataEntrada != null && !dataEntrada.isEmpty()) {
            predicates.add(cb.like(
                    root.get("dataEntrada"),
                    "%" + dataEntrada + "%"
            ));
        }

        // Filtro por horário de entrada
        if (horarioEntrada != null && !horarioEntrada.isEmpty()) {
            // Remove os : extras se existirem
            String horaFormatada = horarioEntrada.replaceAll(":{2,}", ":");
            predicates.add(cb.like(
                    root.get("horaEntrada"),
                    "%" + horaFormatada + "%"
            ));
        }

        // Filtro por data de saída
        if (dataSaida != null && !dataSaida.isEmpty()) {
            predicates.add(cb.like(
                    root.get("dataSaida"),
                    "%" + dataSaida + "%"
            ));
        }

        // Filtro por horário de saída
        if (horarioSaida != null && !horarioSaida.isEmpty()) {
            predicates.add(cb.like(
                    root.get("horaSaida"),
                    "%" + horarioSaida + "%"
            ));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(cq).getResultList();
    }

    @Transactional
    public void excluirTodosRegistrosDaEmpresa(Integer empresaId) {
        // Validação básica
        if (empresaId == null || empresaId <= 0) {
            throw new IllegalArgumentException("ID da empresa inválido");
        }

        // Verificação de existência da empresa (opcional)
        // empresaRepository.existsById(empresaId);

        registroRepository.deleteAllByEmpresaId(empresaId);

        // Log para auditoria
        System.out.println("Todos registros da empresa ID " + empresaId + " foram excluídos");
    }

}
