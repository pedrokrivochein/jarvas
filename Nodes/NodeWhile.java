package Nodes;

public class NodeWhile extends Node {
    private Node condicao;
    private Node bloco;

    public NodeWhile(Node condicao, Node bloco) {
        this.condicao = condicao;
        this.bloco = bloco;
    }

    public Node getCondicao(){
        return condicao;
    }

    public Node getBloco(){
        return bloco;
    }
}
