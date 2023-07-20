package Nodes;

import Principal.TiposVar;
import Principal.Token;

public class NodeValor extends Node {
    private TiposVar tipo;
    private Token valor;

    public NodeValor(Token valor, TiposVar tipo) {
        this.valor = valor;
        this.tipo = tipo;
    }

    public String getValor(){
        return valor.getLexeme();
    }

    public TiposVar getTipo(){
        return tipo;
    }
}