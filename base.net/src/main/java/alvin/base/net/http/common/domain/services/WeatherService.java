package alvin.base.net.http.common.domain.services;


import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import alvin.base.net.http.common.configs.WeatherConfig;
import alvin.base.net.http.common.domain.models.LiveWeather;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class WeatherService {
    private static final int TIMEOUT_SECOND = 30;

    private final WeatherConfig config = new WeatherConfig();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public WeatherService() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @NonNull
    public LiveWeather liveWeather() throws IOException, WeatherException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_SECOND, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SECOND, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_SECOND, TimeUnit.SECONDS)
                .build();

        HttpUrl url = HttpUrl.parse(config.getUrl() + "/weather/now");
        if (url == null) {
            throw new WeatherException("Invalid API url");
        }

        url = url.newBuilder()
                .addQueryParameter("location", config.getLocation())
                .addQueryParameter("key", config.getKey())
                .addQueryParameter("lang", config.getLang())
                .addQueryParameter("unit", config.getUnit())
                .build();

        Request request = new Request.Builder()
                .url(url.url())
//              .addHeader("Author", "alvin")
                .build();
        Call call = client.newCall(request);
        Response response = call.execute();

        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            throw new IOException("No response");
        }
        final String bodyContent = responseBody.string();
        JsonNode node = objectMapper.readTree(bodyContent);

        node = fetchResult(node);
        if (!isStatusOk(node)) {
            throw new WeatherException("Get live weather failed");
        }

        return objectMapper.treeToValue(node, LiveWeather.class);
    }

    @NonNull
    private JsonNode fetchResult(@NonNull JsonNode node) {
        return node.get(config.getVersion()).get(0);
    }

    private boolean isStatusOk(@NonNull JsonNode node) {
        String result = node.get("status").asText();
        return "ok".equals(result);
    }
}
