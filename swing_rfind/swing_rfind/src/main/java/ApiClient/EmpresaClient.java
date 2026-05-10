package ApiClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import model.Empresa;
import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class EmpresaClient {

    private OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String BASE_URL = "http://localhost:8080/apiRfind/empresa";
    private String json="";
    private Request request=null;
    private  RequestBody body=null;

    public void cadastrarEmpresa(Empresa empresa) throws JsonProcessingException {

        // converter Objeto pra JSON
        json = mapper.writeValueAsString(empresa);

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
                System.out.println("Erro ao cadastrar empresa: " + response.code());
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public Empresa localizarCnpjEmpresa(String cnpj) throws JsonProcessingException {

        //converter o Objeto para JSON
        //json = mapper.writeValueAsString(cnpj);

        //requisição GET
        request = new Request.Builder()
                .url(BASE_URL + "/buscar/" + cnpj)
                .get()
                .build();

        /*
        try(Response respose = client.newCall(request).execute()){
            if(respose.isSuccessful() && respose.body() != null){
                //converte JSON da resposta para objeto Empresa
                return mapper.readValue(respose.body().string(), Empresa.class);
            }
            else {
                System.out.println("Erro ao localizar empresa: " + respose.code());
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        */

        try(Response response = client.newCall(request).execute()){
            System.out.println("Código HTTP: " + response.code());
            if(response.body() != null) {
                String resp = response.body().string();

                System.out.println("Corpo: " + resp);
                if(response.isSuccessful()) {
                    return mapper.readValue(resp, Empresa.class);
                }
            }
            return null;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    public Empresa localizarEmailEmpresa(String email) throws JsonProcessingException {
        //requisição GET
        request = new Request.Builder()
                .url(BASE_URL + "/buscarPorEmail/" + email)
                .get()
                .build();

        try(Response response = client.newCall(request).execute()){
            System.out.println("Código HTTP: " + response.code());
            if(response.body() != null) {
                String resp = response.body().string();

                System.out.println("Corpo: " + resp);
                if(response.isSuccessful()) {
                    return mapper.readValue(resp, Empresa.class);
                }
            }
            return null;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
