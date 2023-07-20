package Nodes;

public class NodeIf extends Node {
    private Node condicao;
    private Node ifBloco;
    private Node elseBloco;

    public NodeIf(Node condicao, Node ifBloco, Node elseBloco) {
        this.condicao = condicao;
        this.ifBloco = ifBloco;
        this.elseBloco = elseBloco;
    }

    public Node getCondicao(){
        return condicao;
    }

    public Node getIfBloco(){
        return ifBloco;
    }

    public Node getElseBloco(){
        return elseBloco;
    }
}
