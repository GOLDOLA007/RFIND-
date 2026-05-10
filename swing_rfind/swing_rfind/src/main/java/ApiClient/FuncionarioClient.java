package ApiClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import model.Empresa;
import model.Funcionario;
import model.dto.FuncionarioUpdateDTO;
import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FuncionarioClient {

    private OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String BASE_URL = "http://localhost:8080/apiRfind/funcionario";
    private String json="";
    private Request request=null;
    private  RequestBody body=null;

    public void cadastrarFuncionario(Funcionario funcionario) throws JsonProcessingException {

        // converter Objeto pra JSON
        json = mapper.writeValueAsString(funcionario);

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
                System.out.println("Erro ao cadastrar funcionario: " + response.code());
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public Empresa localizarIdFuncionario(int id) throws JsonProcessingException {

        //requisição GET
        request = new Request.Builder()
                .url(BASE_URL + "/buscar/" + id)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println("Código HTTP: " + response.code());
            if (response.body() != null) {
                String resp = response.body().string();

                System.out.println("Corpo: " + resp);
                if (response.isSuccessful()) {
                    return mapper.readValue(resp, Empresa.class);
                }
            }
            return null;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Funcionario> buscarAtivosPorEmpresa (int empresaId) throws IOException {
        // Monta a URL do endpoint REST

        // Cria requisição GET
        request = new Request.Builder()
                .url(BASE_URL + "/buscar/ativos/empresa/" + empresaId)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("Erro ao buscar funcionários ativos: " + response.code());
                return null;
            }
            if (response.body() != null) {
                String resp = response.body().string();
                // Converte JSON para lista de Funcionarios
                Funcionario[] funcionariosArray = mapper.readValue(resp, Funcionario[].class);
                return (List<Funcionario>) Arrays.asList(funcionariosArray);
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void excluirFuncionario(int id, int empresaId) throws JsonProcessingException {
        String url = BASE_URL + "/excluir/" + id + "/" + empresaId;

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Erro ao excluir funcionário: " + response.code());
                if (response.body() != null)
                    System.err.println(response.body().string());
            } else {
                System.out.println("Funcionário excluído com sucesso.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Funcionario> buscarNaoAtivosPorEmpresa (int empresaId) throws IOException {
        // Monta a URL do endpoint REST

        // Cria requisição GET
        request = new Request.Builder()
                .url(BASE_URL + "/buscar/desativados/empresa/" + empresaId)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("Erro ao buscar funcionários desativados: " + response.code());
                return null;
            }
            if (response.body() != null) {
                String resp = response.body().string();
                // Converte JSON para lista de Funcionarios
                Funcionario[] funcionariosArray = mapper.readValue(resp, Funcionario[].class);
                return (List<Funcionario>) Arrays.asList(funcionariosArray);
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public List<Funcionario> buscarComFiltros(Integer id,
                                              String nome,
                                              String sobrenome,
                                              String setor,
                                              String tag,
                                              Integer empresaId) throws IOException {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "/buscar/funcPesquisa").newBuilder();

        urlBuilder.addQueryParameter("empresaId", empresaId.toString());

        if (id != null) urlBuilder.addQueryParameter("id", id.toString());
        if (nome != null && !nome.isEmpty()) urlBuilder.addQueryParameter("nome", nome);
        if (sobrenome != null && !sobrenome.isEmpty()) urlBuilder.addQueryParameter("sobrenome", sobrenome);
        if (setor != null && !setor.isEmpty()) urlBuilder.addQueryParameter("setor", setor);
        if (tag != null && !tag.isEmpty()) urlBuilder.addQueryParameter("tag", tag);

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("Erro ao buscar funcionários: " + response.code());
                System.out.println(response.body().string());
                return null;
            }

            if (response.body() != null) {
                String resp = response.body().string();
                Funcionario[] funcionarios = mapper.readValue(resp, Funcionario[].class);
                return Arrays.asList(funcionarios);
            }
        }

        return null;
    }

    public void atualizarFuncionario(int id, FuncionarioUpdateDTO dto) throws IOException {
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
                throw new RuntimeException("Erro ao atualizar funcionário. Código: " + response.code() + " - " + erro);
            } else {
                System.out.println("Funcionário atualizado com sucesso.");
            }
        } catch (IOException e) {
            e.printStackTrace(); // Mostra stack trace no console
            throw new RuntimeException("Erro de conexão: " + e.getMessage(), e);
        }

    }

    public void desativarFuncionario(Integer id, Integer empresaId){
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
                System.out.println("Funcionário desativado com sucesso.");
            } else {
                System.out.println("Erro ao desativar funcionário: " + response.code());
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ativarFuncionario(Integer id, Integer empresaId){
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
                System.out.println("Funcionário ativado com sucesso.");
            } else {
                System.out.println("Erro ao ativar funcionário: " + response.code());
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
