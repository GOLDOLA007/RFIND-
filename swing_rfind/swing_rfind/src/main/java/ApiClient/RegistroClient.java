package ApiClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Produto;
import model.Registro;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RegistroClient {

    private OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String BASE_URL = "http://localhost:8080/apiRfind/registro";
    private String json="";
    private Request request=null;
    private RequestBody body=null;


    public List<Registro> buscarPorEmpresa (int empresaId) throws IOException {
        // Monta a URL do endpoint REST

        // Cria requisição GET
        request = new Request.Builder()
                .url(BASE_URL + "/buscar/" + empresaId)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                //System.out.println("Erro ao buscar registros: " + response.code());
                return null;
            }
            if (response.body() != null) {
                String resp = response.body().string();
                // Converte JSON para lista de Produtos
                Registro[] registroArray = mapper.readValue(resp, Registro[].class);
                return (List<Registro>) Arrays.asList(registroArray);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public List<Registro> buscarRegistrosComFiltros(
            Integer id,
            String funcionario,
            String local,
            String sensor,
            String dataEntrada,
            String horarioEntrada,
            String dataSaida,
            String horarioSaida,
            Integer empresaId) throws IOException {

        // Constrói a URL com os parâmetros de query
        StringBuilder urlBuilder = new StringBuilder(BASE_URL)
                .append("/buscar/regPesquisa?");

        // Adiciona parâmetros não nulos
        if (id != null) {
            urlBuilder.append("id=").append(id).append("&");
        }
        if (funcionario != null && !funcionario.isEmpty()) {
            urlBuilder.append("funcionario=").append(funcionario).append("&");
        }
        if (local != null && !local.isEmpty()) {
            urlBuilder.append("local=").append(local).append("&");
        }
        if (sensor != null && !sensor.isEmpty()) {
            urlBuilder.append("sensor=").append(sensor).append("&");
        }
        if (dataEntrada != null && !dataEntrada.isEmpty()) {
            urlBuilder.append("dataEntrada=").append(dataEntrada).append("&");
        }
        if (horarioEntrada != null && !horarioEntrada.isEmpty()) {
            urlBuilder.append("horarioEntrada=").append(horarioEntrada).append("&");
        }
        if (dataSaida != null && !dataSaida.isEmpty()) {
            urlBuilder.append("dataSaida=").append(dataSaida).append("&");
        }
        if (horarioSaida != null && !horarioSaida.isEmpty()) {
            urlBuilder.append("horarioSaida=").append(horarioSaida).append("&");
        }

        // Parâmetro obrigatório
        urlBuilder.append("empresaId=").append(empresaId);

        String url = urlBuilder.toString();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro na requisição: " + response.code());
            }

            if (response.body() != null) {
                String responseBody = response.body().string();
                Registro[] registroArray = mapper.readValue(responseBody, Registro[].class);
                return Arrays.asList(registroArray);
            }
            return List.of();
        }
    }

    public void excluirRegistrosDaEmpresa(int empresaId) throws IOException {

         request = new Request.Builder()
                .url(BASE_URL + "/excluir/" + empresaId)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro: " + response.code());
            }
        }
    }
}
