package json;

import java.util.List;

/**
 * Created by arturo on 04-06-17.
 */
public class GraphJson {

    private List<NodeJson> nodes;
    private List<EdgeJson> links;

    private GraphJson() {

    }

    public GraphJson(List<NodeJson> nodes, List<EdgeJson> links) {
        this.nodes = nodes;
        this.links = links;
    }

    public List<NodeJson> getNodes() {
        return nodes;
    }

    public void setNodes(List<NodeJson> nodes) {
        this.nodes = nodes;
    }

    public List<EdgeJson> getLinks() {
        return links;
    }

    public void setLinks(List<EdgeJson> links) {
        this.links = links;
    }
}
