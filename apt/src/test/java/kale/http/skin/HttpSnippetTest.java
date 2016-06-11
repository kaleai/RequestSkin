package kale.http.skin;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Jack Tony
 * @date 2015/8/19
 */
public class HttpSnippetTest {

    HttpProcessor processor;

    public HttpSnippetTest() {
        processor = new HttpProcessor();
    }

    @Test
    public void testCreateClsBlock() throws Exception {
        StringBuilder sb = processor.sb;
        String[] strings = {"CustomApiService01", "CustomApiService02"};
        for (String s : strings) {
            sb = processor.createClzMainBlock(s, sb);
        }
        sb.append("\n}");

        assertEquals("package kale.http.skin;\n"
                + "import kale.http.skin.HttpRequest;\n"
                + "import java.util.Map;\n"
                + "import android.support.v4.util.ArrayMap;\n"
                + "public class HttpRequestEntity implements CustomApiService01, CustomApiService02, kale.http.skin.NullInterface {\n"
                + "    private HttpRequest mHttpRequest;\n"
                + "    public HttpRequestEntity(HttpRequest httpRequest) {\n"
                + "        mHttpRequest = httpRequest;\n"
                + "    }\n"
                + "}", sb.toString());
    }

}