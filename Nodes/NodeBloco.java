package Nodes;

import java.util.ArrayList;
import java.util.List;

public class NodeBloco extends Node {
    private List<Node> children;

    public NodeBloco() {
        children = new ArrayList<>();
    }

    public void adicionar(Node child) {
        children.add(child);
    }

    public List<Node> getChildren(){
        return children;
    }
}

