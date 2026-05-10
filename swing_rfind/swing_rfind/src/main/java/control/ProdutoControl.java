package control;

import ApiClient.ProdutoClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import model.Empresa;
import model.Produto;
import model.dto.FuncionarioUpdateDTO;
import model.dto.ProdutoUpdateDTO;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProdutoControl {

    private ProdutoClient produtoClient;
    public ProdutoControl()
    {
        produtoClient = new ProdutoClient();
    }
    public void insert(Produto produto){
        try {
            produtoClient.cadastrarProduto(produto);
        } catch (IOException e) {
            // tratar erro (mostrar JOptionPane, logar, etc)
            e.printStackTrace();
        }
    }

    public List<Produto> buscarProdutosAtivos(int empresaId){
        try {
            List<Produto> produtos = produtoClient.buscarAtivosPorEmpresa(empresaId);
            return produtos != null ? produtos : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Produto> buscarProdutosNaoAtivos(int empresaId){
        try {
            List<Produto> produtos = produtoClient.buscarNaoAtivosPorEmpresa(empresaId);
            return produtos != null ? produtos : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void atualizarProduto(int id, ProdutoUpdateDTO dto) {
        try {
            produtoClient.atualizarProduto(id, dto);
            JOptionPane.showMessageDialog(null, "Produto atualizado com sucesso!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar produto.");
        }
    }

    public List<Produto> buscarProdutosComFiltros(
            Integer id,
            String nome,
            Double preco,
            String descricao,
            String categoria,
            Integer quantidadeEstoque,
            String dataFab,
            String dataValidade,
            String fornecedor,
            Integer empresaId) {

        try {
            List<Produto> produtos = produtoClient.buscarProdutos(
                    id, nome, preco, descricao, categoria,
                    quantidadeEstoque, dataFab, dataValidade, fornecedor, empresaId);

            return produtos != null ? produtos : new ArrayList<>();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro na busca de produtos: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void excluirProduto(int id, int empresaId) throws JsonProcessingException {
        produtoClient.excluirProduto(id, empresaId);
    }


    public List<Produto> getProdutos(Empresa empresa){
        //return produtoDAO.getProdutosEmpresa(empresa);
        return null;
    }

    public void desativaProduto(int id, int empresaId) throws JsonProcessingException {
       produtoClient.desativarProduto(id, empresaId);
    }

    public void ativaProduto(int id, int empresaId){
        produtoClient.ativarProduto(id, empresaId);
    }

    public void deletByIdEmpresa(Empresa empresa, int id){
        //produtoDAO.deleteByIdEmpresa(empresa, id);
    }

    public void updateProduto(int id, String nome, String categoria, double preco, int estoque, String desc, Date dataFab, Date dataValidade, String fornecedor, boolean estado){
        //produtoDAO.update(id, nome, categoria, preco
        //, estoque, desc, dataFab, dataValidade, fornecedor, estado);
    }

}
