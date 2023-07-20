package Nodes;

import Principal.Token;

public class NodeOperacao extends Node {
    private Token operador;
    private Node esq;
    private Node dir;

    public NodeOperacao(Token operador, Node esq, Node dir) {
        this.operador = operador;
        this.esq = esq;
        this.dir = dir;
    }

    public Token getOperador(){
        return operador;
    }

    public Node getEsq(){
        return esq;
    }

    public Node getDir(){
        return dir;
    }
}
