package json;

import java.io.Serializable;

/**
 * Created by arturo on 04-06-17.
 */
public class EdgeJson implements Serializable {

    private String source;
    private String target;

    private EdgeJson() {

    }

    public EdgeJson(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
