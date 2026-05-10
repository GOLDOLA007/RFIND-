package ApiClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Funcionario;
import model.Local;
import model.dto.FuncionarioUpdateDTO;
import model.dto.LocalUpdateDTO;
import okhttp3.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class LocalClient {

    private OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String BASE_URL = "http://localhost:8080/apiRfind/local";
    private String json="";
    private Request request=null;
    private RequestBody body=null;

    public void cadastrarLocal(Local local) throws JsonProcessingException {

        // converter Objeto pra JSON
        json = mapper.writeValueAsString(local);

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

    public List<Local> buscarPorEmpresa (int empresaId) throws IOException {
        // Monta a URL do endpoint REST

        // Cria requisição GET
        request = new Request.Builder()
                .url(BASE_URL + "/buscar/empresa/" + empresaId)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("Erro ao buscar Local: " + response.code());
                return null;
            }
            if (response.body() != null) {
                String resp = response.body().string();
                // Converte JSON para lista de Funcionarios
                Local[] localArray = mapper.readValue(resp, Local[].class);
                return (List<Local>) Arrays.asList(localArray);
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void atualizarLocal(int id, LocalUpdateDTO dto) throws IOException {
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
                throw new RuntimeException("Erro ao atualizar local. Código: " + response.code() + " - " + erro);
            } else {
                System.out.println("Local atualizado com sucesso.");
            }
        } catch (IOException e) {
            e.printStackTrace(); // Mostra stack trace no console
            throw new RuntimeException("Erro de conexão: " + e.getMessage(), e);
        }

    }

    public void excluirLocal(int id, int empresaId) throws JsonProcessingException {
        String url = BASE_URL + "/excluir/" + id + "/" + empresaId;

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Erro ao excluir local: " + response.code());
                if (response.body() != null)
                    System.err.println(response.body().string());
            } else {
                System.out.println("Local excluído com sucesso.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Local> buscarLocaisComFiltros(Integer id,
                                              String nome,
                                              String descricao,
                                              String sensor,
                                              Integer empresaId) throws IOException {

        // Construindo a URL com parâmetros de query
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "/buscar/localPesquisa").newBuilder();

        // Adicionando parâmetros apenas se não forem nulos/vazios
        if (id != null) {
            urlBuilder.addQueryParameter("id", id.toString());
        }
        if (nome != null && !nome.isEmpty()) {
            urlBuilder.addQueryParameter("nome", nome);
        }
        if (descricao != null && !descricao.isEmpty()) {
            urlBuilder.addQueryParameter("descricao", descricao);
        }
        if (sensor != null && !sensor.isEmpty()) {
            urlBuilder.addQueryParameter("sensor", sensor);
        }
        if (empresaId != null) {
            urlBuilder.addQueryParameter("empresaId", empresaId.toString());
        }

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "";
                throw new IOException("Erro na requisição: " + response.code() + " - " + errorBody);
            }

            if (response.body() != null) {
                String responseBody = response.body().string();
                Local[] locaisArray = mapper.readValue(responseBody, Local[].class);
                return Arrays.asList(locaisArray);
            }
            return null;
        }
    }

}
