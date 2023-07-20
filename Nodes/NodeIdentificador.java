package Nodes;

import Principal.TiposVar;
import Principal.Token;

public class NodeIdentificador extends Node{
    private TiposVar tipo;
    private Token identificador;

    public NodeIdentificador(TiposVar tipo, Token identificador){
        this.tipo = tipo;
        this.identificador = identificador;
    }

    public TiposVar getTipo(){
        return tipo;
    }

    public Token getIdentificador(){
        return identificador;
    }
}
