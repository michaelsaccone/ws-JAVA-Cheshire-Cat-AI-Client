package ai.cheshirecat.models;


import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class UserMessage {

    private String text;
    private String userId;
    private Map<String, String> userInfo;

    public UserMessage(String text, String userId) {
        this.text = text;
        this.userId = userId;
        this.userInfo = new HashMap<>();
    }

    public UserMessage(String text) {
        this(text, "user");
    }

    public void addField(String key, String value) {
        this.userInfo.put(key, value);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    @Override
    public String toString() {
        return this.toJson();
    }

}
