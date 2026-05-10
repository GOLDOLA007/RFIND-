package ApiClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Funcionario;
import model.Produto;
import model.dto.FuncionarioUpdateDTO;
import model.dto.ProdutoUpdateDTO;
import okhttp3.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProdutoClient {
    private OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String BASE_URL = "http://localhost:8080/apiRfind/produto";
    private String json="";
    private Request request=null;
    private RequestBody body=null;

    public void cadastrarProduto(Produto produto) throws JsonProcessingException {

        // converter Objeto pra JSON
        json = mapper.writeValueAsString(produto);

        //corpo da requisição com JSON
        body = RequestBody.create(
                json,
                MediaType.parse("application/json; charset=utf-8")
        );

        //requisição POST
        request = new Request.Builder()
                .url(BASE_URL + "/cadastrar")
                .post(body)
                .build();

        //executa a requisição
        try(Response response = client.newCall(request).execute()){
            if(!response.isSuccessful()){
                System.out.println("Erro ao cadastrar produto: " + response.code());
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void atualizarProduto(int id, ProdutoUpdateDTO dto) throws IOException {
        String json = mapper.writeValueAsString(dto);

        RequestBody body = RequestBody.create(
                json,
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(BASE_URL + "/atualizar/" + id)
                .put(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println("Enviando PUT para: " + BASE_URL + "/atualizar/" + id);
            System.out.println("JSON enviado: " + json);

            if (!response.isSuccessful()) {
                String erro = response.body() != null ? response.body().string() : "Resposta vazia";
                throw new RuntimeException("Erro ao atualizar produto. Código: " + response.code() + " - " + erro);
            } else {
                System.out.println("Produto atualizado com sucesso.");
            }
        } catch (IOException e) {
            e.printStackTrace(); // Mostra stack trace no console
            throw new RuntimeException("Erro de conexão: " + e.getMessage(), e);
        }

    }

    public List<Produto> buscarAtivosPorEmpresa (int empresaId) throws IOException {
        // Monta a URL do endpoint REST

        // Cria requisição GET
        request = new Request.Builder()
                .url(BASE_URL + "/buscar/ativos/empresa/" + empresaId)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("Erro ao buscar produtos ativos: " + response.code());
                return null;
            }
            if (response.body() != null) {
                String resp = response.body().string();
                // Converte JSON para lista de Produtos
                Produto[] produtoArray = mapper.readValue(resp, Produto[].class);
                return (List<Produto>) Arrays.asList(produtoArray);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public List<Produto> buscarProdutos(
            Integer id,
            String nome,
            Double preco,
            String descricao,
            String categoria,
            Integer quantidadeEstoque,
            String dataFab,
            String dataValidade,
            String fornecedor,
            Integer empresaId) throws IOException {

        // Construir a URL com os parâmetros de consulta
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "/buscar/prodPesquisa").newBuilder();

        // Adicionar parâmetros não nulos
        if (id != null) urlBuilder.addQueryParameter("id", id.toString());
        if (nome != null) urlBuilder.addQueryParameter("nome", nome);
        if (preco != null) urlBuilder.addQueryParameter("preco", preco.toString());
        if (descricao != null) urlBuilder.addQueryParameter("descricao", descricao);
        if (categoria != null) urlBuilder.addQueryParameter("categoria", categoria);
        if (quantidadeEstoque != null) urlBuilder.addQueryParameter("quantidadeEstoque", quantidadeEstoque.toString());
        if (dataFab != null) urlBuilder.addQueryParameter("dataFab", dataFab);
        if (dataValidade != null) urlBuilder.addQueryParameter("dataValidade", dataValidade);
        if (fornecedor != null) urlBuilder.addQueryParameter("fornecedor", fornecedor);
        urlBuilder.addQueryParameter("empresaId", empresaId.toString());

        String url = urlBuilder.build().toString();

        // Cria requisição GET
        request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("Erro ao buscar produtos: " + response.code());
                if (response.body() != null) {
                    System.out.println(response.body().string());
                }
                return Collections.emptyList();
            }

            if (response.body() != null) {
                String resp = response.body().string();
                // Converte JSON para lista de Produtos
                Produto[] produtoArray = mapper.readValue(resp, Produto[].class);
                return Arrays.asList(produtoArray);
            } else {
                return Collections.emptyList();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar produtos: " + e.getMessage(), e);
        }
    }

    public void excluirProduto(int id, int empresaId) throws JsonProcessingException {
        String url = BASE_URL + "/excluir/" + id + "/" + empresaId;

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Erro ao excluir produto: " + response.code());
                if (response.body() != null)
                    System.err.println(response.body().string());
            } else {
                System.out.println("Produto excluído com sucesso.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Produto> buscarNaoAtivosPorEmpresa (int empresaId) throws IOException {
        // Monta a URL do endpoint REST

        // Cria requisição GET
        request = new Request.Builder()
                .url(BASE_URL + "/buscar/desativados/empresa/" + empresaId)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("Erro ao buscar produtos desativados: " + response.code());
                return null;
            }
            if (response.body() != null) {
                String resp = response.body().string();
                // Converte JSON para lista de Funcionarios
                Produto[] produtosArray = mapper.readValue(resp, Produto[].class);
                return (List<Produto>) Arrays.asList(produtosArray);
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void desativarProduto(Integer id, Integer empresaId){
        //String url = BASE_URL + "/desativar/" + id;

        HttpUrl url = HttpUrl.parse(BASE_URL + "/desativar/" + id)
                .newBuilder()
                .addQueryParameter("empresaId", empresaId.toString())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .put(RequestBody.create("", null))
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                System.out.println("Produto desativado com sucesso.");
            } else {
                System.out.println("Erro ao desativar produto: " + response.code());
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ativarProduto(Integer id, Integer empresaId){
        //String url = BASE_URL + "/desativar/" + id;

        HttpUrl url = HttpUrl.parse(BASE_URL + "/ativar/" + id)
                .newBuilder()
                .addQueryParameter("empresaId", empresaId.toString())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .put(RequestBody.create("", null))
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                System.out.println("Produto ativado com sucesso.");
            } else {
                System.out.println("Erro ao ativar produto: " + response.code());
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
