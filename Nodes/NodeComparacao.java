package Nodes;

import Principal.Token;

public class NodeComparacao extends Node {
    private Token condicao;
    private Node esq;
    private Node dir;

    public NodeComparacao(Token condicao, Node esq, Node dir) {
        this.condicao = condicao;
        this.esq = esq;
        this.dir = dir;
    }

    public Token getCondicao(){
        return condicao;
    }

    public Node getEsq(){
        return esq;
    }

    public Node getDir(){
        return dir;
    }
}
