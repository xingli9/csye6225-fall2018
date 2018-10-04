package csye6225Web.bean;

import java.util.HashMap;
import java.util.Map;

public class JsObject {
    Map<String, String> jsMap;

    public JsObject() {
        jsMap = new HashMap<String, String>();
    }

    public void add(String key, String value) {
        jsMap.put(key, value);

    }

    public  void add(String msg) {
        jsMap.put("Return message", msg);
    }

    //@Override
    public String toString() {
        String rst = "";
        rst += "{\n";
        int n = 0;
        for (Map.Entry<String, String> entry : jsMap.entrySet()) {
            System.out.println("inside for" + entry.getKey());
            n++;
            if (n == 1) {

            } else  {
                rst += ",";
            }
            rst += "\"" + entry.getKey() + "\" : \"" +entry.getValue() + "\"\n";

        }
        rst += "}";
        System.out.println("tostring: " + rst);
        return rst;
    }
}
