package ApiClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Local;
import model.SensorRfid;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SensorRfidClient {
    private OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String BASE_URL = "http://localhost:8080/apiRfind/sensor";
    private String json="";
    private Request request=null;
    private RequestBody body=null;

    public List<SensorRfid> buscarPorEmpresa (int empresaId) throws IOException {
        // Monta a URL do endpoint REST

        // Cria requisição GET
        request = new Request.Builder()
                .url(BASE_URL + "/buscar/empresa/" + empresaId)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("Erro ao buscar SensorRfid: " + response.code());
                return null;
            }
            if (response.body() != null) {
                String resp = response.body().string();
                // Converte JSON para lista de Funcionarios
                SensorRfid[] sensorRfidArray = mapper.readValue(resp, SensorRfid[].class);
                return (List<SensorRfid>) Arrays.asList(sensorRfidArray);
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public List<SensorRfid> buscarNaoVinculados(int empresaId) throws IOException {
        // Monta a URL do endpoint REST
        //String url = BASE_URL + "/nao-vinculados/" + empresaId;

        // Cria requisição GET
        request = new Request.Builder()
                .url(BASE_URL + "/nao-vinculados/" + empresaId)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro ao buscar sensores não vinculados. Código: " + response.code());
            }

            if (response.body() != null) {
                String responseBody = response.body().string();

                // Verifica se a resposta não está vazia
                if (responseBody.isEmpty()) {
                    return Collections.emptyList();
                }

                // Converte JSON para lista de SensorRfid
                return Arrays.asList(mapper.readValue(responseBody, SensorRfid[].class));
            } else {
                return Collections.emptyList();
            }
        } catch (IOException e) {
            System.err.println("Erro na comunicação com a API: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

}
