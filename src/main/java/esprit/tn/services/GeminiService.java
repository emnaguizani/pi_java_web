package esprit.tn.services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;

import java.io.IOException;

public class GeminiService {
    private static final String API_KEY = "AIzaSyCONIr7_lJ_KX4v_tgoyThSgR_KB7kv7oM";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

    private final OkHttpClient httpClient = new OkHttpClient();

    /**
     * Summarizes the forum content.
     *
     * @param forumContent The content of the forum (title + description).
     * @return A summary of the forum content.
     * @throws IOException If the API request fails.
     */
    public String summarizeForumContent(String forumContent) throws IOException {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");


        String prompt = "answer the provided question or give more details about the subject do not ask the user to ask you anything else:\n\n" + forumContent;
        String jsonBody = "{\"contents\":[{\"parts\":[{\"text\":\"" + prompt + "\"}]}]}";
        RequestBody body = RequestBody.create(jsonBody, JSON);

        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .build();

        return executeRequest(request);
    }

    /**
     * Summarizes the forum responses.
     *
     * @param responsesContent The concatenated responses.
     * @return A summary of the responses.
     * @throws IOException If the API request fails.
     */
    public String summarizeResponses(String responsesContent) throws IOException {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");


        String prompt = "Summarize the following forum responses in a concise paragraph, highlighting the main points and themes:\n\n" + responsesContent;
        String jsonBody = "{\"contents\":[{\"parts\":[{\"text\":\"" + prompt + "\"}]}]}";
        RequestBody body = RequestBody.create(jsonBody, JSON);

        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .build();

        return executeRequest(request);
    }

    /**
     * Executes the API request and parses the response.
     *
     * @param request The API request.
     * @return The parsed summary.
     * @throws IOException If the API request fails.
     */
    private String executeRequest(Request request) throws IOException {
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body().string();
                System.err.println("API Error Response: " + errorBody);
                throw new IOException("Unexpected code " + response.code() + ": " + errorBody);
            }

            String responseBody = response.body().string();
            System.out.println("API Success Response: " + responseBody);
            return parseSummaryFromResponse(responseBody);
        }
    }

    /**
     * Parses the summary from the API response.
     *
     * @param responseBody The API response body.
     * @return The parsed summary.
     */
    private String parseSummaryFromResponse(String responseBody) {
        try {
            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
            String summary = jsonObject.getAsJsonArray("candidates")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("content")
                    .getAsJsonArray("parts")
                    .get(0).getAsJsonObject()
                    .get("text").getAsString();
            return summary;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse API response: " + e.getMessage(), e);
        }
    }
}