package Principal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Nodes.Node;
import Nodes.NodeAtribuicao;
import Nodes.NodeBloco;
import Nodes.NodeComparacao;
import Nodes.NodeIdentificador;
import Nodes.NodeIf;
import Nodes.NodeOperacao;
import Nodes.NodeValor;
import Nodes.NodeWhile;

public class Semantico {

    public static Map<String, TiposVar> identificadores = new HashMap<String, TiposVar>();

    public static void checarSemantica(Node node) throws Exception {
        System.out.println(node.getClass().getName());
        if (node instanceof NodeBloco) {
            NodeBloco node_aux = (NodeBloco) node;
            for (Node aux : node_aux.getChildren())
                checarSemantica(aux);
        } else if (node instanceof NodeIf) {
            NodeIf nodeIf = (NodeIf) node;
            checarSemantica(nodeIf.getCondicao());
            checarSemantica(nodeIf.getIfBloco());
            checarSemantica(nodeIf.getElseBloco());
        } else if (node instanceof NodeAtribuicao) {
            checarSemanticaAtribuicao((NodeAtribuicao) node);
        } else if (node instanceof NodeOperacao) {
            checarSemanticaOperacao((NodeOperacao) node);
        } else if (node instanceof NodeWhile) {
            NodeWhile nodeWhile = (NodeWhile) node;
            checarSemantica(nodeWhile.getCondicao());
            checarSemantica(nodeWhile.getBloco());
        } else if (node instanceof NodeIdentificador) {
            NodeIdentificador nodeIdentificador = (NodeIdentificador) node;
            identificadores.put(nodeIdentificador.getIdentificador().getLexeme(), nodeIdentificador.getTipo());
        }
    }

    private static void checarSemanticaAtribuicao(NodeAtribuicao node) throws Exception{
        NodeIdentificador identificador = (NodeIdentificador) node.getIdentificador();
        checarSemantica(node.getIdentificador());
        checarSemantica(node.getExpressao());

        if (node.getExpressao() instanceof NodeComparacao) {
            checarSemantica(node.getExpressao());

            if (identificador.getTipo() != TiposVar.INT){
                throw new Exception("Erro semantico: Atribuicao de tipos diferentes");
            }
        } else if (node.getExpressao() instanceof NodeIdentificador) {
            NodeIdentificador identificador_2 = (NodeIdentificador) node.getExpressao();
            if (identificador.getTipo() != identificador_2.getTipo()){
                throw new Exception("Erro semantico: Atribuicao de tipos diferentes");
            }
        }
    }

    private static void checarSemanticaOperacao(NodeOperacao node) throws Exception{
        checarSemantica(node.getDir());
        checarSemantica(node.getEsq());

        if (node.getDir() instanceof NodeIdentificador && node.getEsq() instanceof NodeIdentificador) {
            NodeIdentificador dir = (NodeIdentificador) node.getDir();
            NodeIdentificador esq = (NodeIdentificador) node.getEsq();

            if (dir.getTipo() != esq.getTipo()){
                throw new Exception("Erro semantico: Operacao entre tipos diferentes");
            }
        }
        if(node.getDir() instanceof NodeValor && node.getEsq() instanceof NodeValor){
            NodeValor dir = (NodeValor) node.getDir();
            NodeValor esq = (NodeValor) node.getEsq();
            TiposVar tipo_dir = dir.getTipo();
            TiposVar tipo_esq = esq.getTipo();

            if (identificadores.containsKey(dir.getValor()))
                tipo_dir = identificadores.get(dir.getValor());

            if (identificadores.containsKey(esq.getValor()))
                tipo_esq = identificadores.get(esq.getValor());

            if (tipo_dir != tipo_esq){
                throw new Exception("Erro semantico: Operacao entre tipos diferentes");
            }
        }
        if(node.getDir() instanceof NodeIdentificador && (node.getEsq() instanceof NodeValor
        || node.getEsq() instanceof NodeComparacao || node.getEsq() instanceof NodeOperacao)){
            NodeIdentificador dir = (NodeIdentificador) node.getDir();

            if (dir.getTipo() != TiposVar.INT){
                throw new Exception("Erro semantico: Operacao entre tipos diferentes");
            }
        }
        if((node.getDir() instanceof NodeValor || node.getDir() instanceof NodeOperacao || node.getDir() instanceof NodeComparacao)
        && node.getEsq() instanceof NodeIdentificador){
            NodeIdentificador esq = (NodeIdentificador) node.getEsq();

            if (esq.getTipo() != TiposVar.INT){
                throw new Exception("Erro semantico: Operacao entre tipos diferentes");
            }
        }
    }
}
