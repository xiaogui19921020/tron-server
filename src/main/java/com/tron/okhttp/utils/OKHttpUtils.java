package com.tron.okhttp.utils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;

@Slf4j
public class OKHttpUtils {
  public static String post(String url, String content) throws IOException {
    //log.debug(" \nOKHttpUtils start {} \n {}", url, content);
    OkHttpClient client = new OkHttpClient();

    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, content);
    Request request =
        new Request.Builder()
            .url(url)
            .post(body)
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .build();

    Response response = client.newCall(request).execute();
    String result = response.body().string();
    //log.debug(" OKHttpUtils result {} ", result);
    return result;
  }
}
