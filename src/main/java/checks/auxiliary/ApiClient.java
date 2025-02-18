package checks.auxiliary;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ApiClient {
    private final Page page;

    public ApiClient(Page page) {
        this.page = page;
    }

    public APIResponse sendPostRequest(String url, String body) {
        return page.request().post(url,
                RequestOptions.create()
                        .setData(body)
                        .setHeader("Content-Type", "application/json"));
    }

    public String convertByte2String(APIResponse resp) {
        return new String(resp.body(), StandardCharsets.UTF_8);
    }

    public String loadJsonFromFile(String path) throws Exception {
        return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
    }

    public APIResponse sendGetRequest(String url) {
        return page.request().get(url);
    }

}