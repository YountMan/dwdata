/**
 * 拉取异步调用催收分结果集
 *
 * @param token 接口调用凭证
 * @param sid   催收分唯一标识
 * @return
 * @throws IOException
 */
public String getCuiShou(String token, String sid) throws IOException {
    if (isEmpty(token) || isEmpty(sid)) {
        throw new IllegalArgumentException();
    }
    String url = "https://crs-api.dianhua.cn/cuishou?token=" + token + "&sid=" + sid;
    String result = get(url);
    System.out.println("getCuiShou: " + result);
    return result;
}

public boolean isEmpty(String source) {
    return source == null || source.isEmpty();
}


public String get(String link) throws IOException {
    URL url = new URL(link);
    InputStream stream = null;
    HttpsURLConnection connection = null;
    String result = null;
    try {
        connection = (HttpsURLConnection) url.openConnection();
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(10000);
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.connect();
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpsURLConnection.HTTP_OK) {
            throw new IOException("HTTP error code: " + responseCode);
        }
        stream = connection.getInputStream();
        if (stream != null) {
            byte[] bytes = toBytes(stream);
            result = new String(bytes, StandardCharsets.UTF_8);
        }
    } finally {
        if (stream != null) {
            stream.close();
        }
        if (connection != null) {
            connection.disconnect();
        }
    }
    return result;
}

private byte[] toBytes(InputStream from) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] buf = new byte[8096];
    while (true) {
        int r = from.read(buf);
        if (r == -1) {
            break;
        }
        out.write(buf, 0, r);
    }
    return out.toByteArray();
}
