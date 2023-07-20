package Nodes;

public class NodeAtribuicao extends Node {
    private Node identificador;
    private Node expressao;

    public NodeAtribuicao(Node identificador, Node expressao) {
        this.identificador = identificador;
        this.expressao = expressao;
    }

    public Node getIdentificador(){
        return identificador;
    }

    public Node getExpressao(){
        return expressao;
    }
}