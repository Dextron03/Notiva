package Modules;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Email {

    private final String baseUrl;

    public Email(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void sendSimpleEmail(String sender, String receptor, String password,
                                String subject, String message) throws IOException {

        String escapedMessage = escapeJson(message);

        String json = String.format("""
        {
            "sender": "%s",
            "receptor": "%s",
            "password": "%s",
            "subject": "%s",
            "message": "%s"
        }
        """, sender, receptor, password, subject, escapedMessage);

        postJson(baseUrl + "/api/send-email/", json);
    }

    public void sendTemplateEmail(String sender, String receptor, String password,
                                  String subject, String templateDir, String templateName,
                                  Map<String, String> context) throws IOException {

        String contextJson = mapToJson(context);

        String json = String.format("""
        {
            "sender": "%s",
            "receptor": "%s",
            "password": "%s",
            "subject": "%s",
            "template_dir": "%s",
            "template_name": "%s",
            "context": %s
        }
        """, sender, receptor, password, subject, templateDir, templateName, contextJson);

        postJson(baseUrl + "/api/send-email-template/", json);
    }

    // -------------------- Métodos auxiliares --------------------

    private void postJson(String urlStr, String jsonInputString) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int code = conn.getResponseCode();
        InputStream is = code < 400 ? conn.getInputStream() : conn.getErrorStream();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder resp = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) resp.append(line);
            System.out.println("HTTP " + code + " -> " + resp);
        }
        conn.disconnect();
    }

    // Convierte un Map en un JSON string {"key":"value", ...}
    private String mapToJson(Map<String, String> map) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!first) sb.append(",");
            sb.append("\"").append(escapeJson(entry.getKey())).append("\":\"")
              .append(escapeJson(entry.getValue())).append("\"");
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }

    // Escapa caracteres especiales para JSON
    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "")
                .replace("\n", "\\n");
    }
}
