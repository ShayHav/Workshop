package domain.market.ExternalConnectors;

import okhttp3.*;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class Client {

    private final String url;
    private final OkHttpClient client;

    public Client(String url){
        this.client = new OkHttpClient();
        this.url = url;
    }

    public String sendPost(Map<String, String> params) throws IOException {
        RequestBody requestBody = buildForm(params);
        URL url = new URL(this.url);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try(Response response = client.newCall(request).execute()){
            ResponseBody body =  response.body();
            return body == null ? "" : body.string();
        }
    }

    private RequestBody buildForm(Map<String, String> params){
        FormBody.Builder builder = new FormBody.Builder();
        params.forEach(builder::add);
        return builder.build();
    }
}
