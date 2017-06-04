package json;

import java.io.Serializable;

/**
 * Created by arturo on 04-06-17.
 */
public class NodeJson implements Serializable {

    private String name;
    private String type;

    private NodeJson() {

    }

    public NodeJson(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
