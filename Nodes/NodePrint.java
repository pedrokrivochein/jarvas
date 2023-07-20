package Nodes;

import Principal.TiposVar;
import Principal.Token;

public class NodePrint extends Node{
    private String tipo;
    private Token valor;

    public NodePrint(Token valor, Token tipo) {
        this.valor = valor;
        this.tipo = tipo.getLexeme();
    }
    
    public Token getValor(){
        return valor;
    }

    public String getTipo(){
        return tipo;
    }
}
