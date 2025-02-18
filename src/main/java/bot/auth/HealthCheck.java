package bot.auth;

import checks.auxiliary.ApiClient;
import checks.auxiliary.ApiConfig;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.json.JSONObject;

public class HealthCheck {

    public static final String LOGIN_BODY = "src/test/java/checks/jsons/loginBody.json";

    public String checkAuth() throws Exception {
        Playwright pw = Playwright.create();
        Browser chromium = pw.chromium().launch();
        Page page = chromium.newPage();
        ApiClient apiClient = new ApiClient(page);
        String message;
        String bad_message = "Authorization - doesn't work";
        String good_message = "Authorization - OK";

        String jsonBody = apiClient.loadJsonFromFile(LOGIN_BODY);
        APIResponse apiResponsee = apiClient.sendPostRequest(ApiConfig.URL_LOGIN, jsonBody);
        if (apiResponsee.status()!=200){
            message = bad_message;
            return message;
        }


        String jsonClient = apiClient.convertByte2String(apiClient.sendGetRequest(ApiConfig.URL_CLIENTS));
        JSONObject jsonObject = new JSONObject(jsonClient);
        String mdmCode = jsonObject.getJSONObject("result") // получаем MDM код организации из структуры JSON
                .getJSONObject("data")
                .getJSONObject("multiClient")
                .getJSONArray("all")
                .getJSONObject(0)
                .getString("mdmCode");

        apiResponsee = apiClient.sendPostRequest(ApiConfig.URL_SET_ACTIVE_CLIENT,
                "{\"mdmCode\":\"" + mdmCode + "\"}");
        if (apiResponsee.status()!=200){
            message = bad_message;
            return message;
        }

        APIResponse getUserInfo = apiClient.sendGetRequest(ApiConfig.URL_GET_USER_INFO);
        if (getUserInfo.status() == 200) {
            return good_message;
        } else {
            return bad_message;
        }
    }
}
