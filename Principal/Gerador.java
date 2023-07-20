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
import Nodes.NodePrint;
import Nodes.NodeValor;
import Nodes.NodeWhile;

public class Gerador {
    private static int contadorLabel = 0;
    private static StringBuilder codigoAssembly = new StringBuilder();

    public static List<String> vars = new ArrayList<String>();
    public static Map<String, String> floats = new HashMap<String, String>();

    public static String gerar(Node node) {
        codigoAssembly.setLength(0);
        gerarCodigo(node);
        return codigoAssembly.toString();
    }

    private static void gerarCodigo(Node node) {
        if (node instanceof NodeBloco) {
            NodeBloco node_aux = (NodeBloco) node;
            for (Node aux : node_aux.getChildren())
                gerarCodigo(aux);
        } else if (node instanceof NodeIf) {
            gerarCodigoIf((NodeIf) node);
        } else if (node instanceof NodeAtribuicao) {
            gerarCodigoAtribuicao((NodeAtribuicao) node);
        } else if (node instanceof NodeOperacao) {
            gerarCodigoOperacaoBinaria((NodeOperacao) node);
        } else if (node instanceof NodeValor) {
            gerarCodigoLiteral((NodeValor) node);
        } else if (node instanceof NodePrint) {
            gerarCodigoPrint((NodePrint) node);
        } else if (node instanceof NodeWhile) {
            gerarCodigoWhile((NodeWhile) node);
        }
    }

    private static void gerarCodigoIf(NodeIf node) {
        String labelCondicao = gerarLabel();
        String labelFim = gerarLabel();

        if (node.getCondicao() instanceof NodeComparacao) {
            gerarCodigoComparacao((NodeComparacao) node.getCondicao(), labelCondicao, labelFim);
        } else {
            codigoAssembly.append("\n").append("CMP EAX, 1").append("\n");
        }

        codigoAssembly.append("JE ").append(labelCondicao).append("\n");

        gerarCodigo(node.getElseBloco());
        codigoAssembly.append("\n").append("JMP ").append(labelFim).append("\n");

        codigoAssembly.append(labelCondicao).append(":").append("\n");
        gerarCodigo(node.getIfBloco());
        codigoAssembly.append("\n").append("JMP ").append(labelFim).append("\n");

        codigoAssembly.append(labelFim).append(":").append("\n");
    }

    private static void gerarCodigoWhile(NodeWhile node) {
        String labelInicio = gerarLabel();
        String labelFim = gerarLabel();

        codigoAssembly.append(labelInicio).append(":").append("\n");

        if (node.getCondicao() instanceof NodeComparacao) {
            gerarCodigoComparacao((NodeComparacao) node.getCondicao(), labelInicio, labelFim);
        } else {
            codigoAssembly.append("CMP EAX, 1").append("\n");
            codigoAssembly.append("JNE ").append(labelFim).append("\n");
        }

        gerarCodigo(node.getBloco()); // Adicionado para gerar o código do bloco do while

        codigoAssembly.append("\n").append("JMP ").append(labelInicio).append("\n");

        codigoAssembly.append(labelFim).append(":").append("\n");
    }

    private static void gerarCodigoComparacao(NodeComparacao node, String labelVerdadeiro, String labelFim) {
        gerarCodigo(node.getEsq());
        codigoAssembly.append("\n").append("PUSH EAX").append("\n");
        gerarCodigo(node.getDir());
        codigoAssembly.append("\n").append("POP EBX").append("\n");

        Token operador = node.getCondicao();

        switch (operador.getTipo()) {
            case EQUALS:
                codigoAssembly.append("\n").append("CMP EAX, EBX").append("\n");
                codigoAssembly.append("JE ").append(labelVerdadeiro).append("\n");
                break;
            case NOT_EQUAL:
                codigoAssembly.append("\n").append("CMP EAX, EBX").append("\n");
                codigoAssembly.append("JNE ").append(labelVerdadeiro).append("\n");
                break;
            case GREATER_THAN:
                codigoAssembly.append("\n").append("CMP EAX, EBX").append("\n");
                codigoAssembly.append("JG ").append(labelVerdadeiro).append("\n");
                break;
            case GREATER_EQUAL:
                codigoAssembly.append("\n").append("CMP EAX, EBX").append("\n");
                codigoAssembly.append("JGE ").append(labelVerdadeiro).append("\n");
                break;
            case LESS_THAN:
                codigoAssembly.append("\n").append("CMP EAX, EBX").append("\n");
                codigoAssembly.append("JL ").append(labelVerdadeiro).append("\n");
                break;
            case LESS_EQUAL:
                codigoAssembly.append("\n").append("CMP EAX, EBX").append("\n");
                codigoAssembly.append("JLE ").append(labelVerdadeiro).append("\n");
                break;
        }
    }

    private static void gerarCodigoAtribuicao(NodeAtribuicao node) {
        gerarCodigo(node.getExpressao());
        NodeIdentificador iden = (NodeIdentificador) node.getIdentificador();

        String var = iden.getIdentificador().getLexeme();
        if (!vars.contains(var))
            vars.add(var);
        
        if (iden.getTipo() == TiposVar.FLOAT)
            codigoAssembly.append("\n").append("fstp dword [").append(var).append("]").append("\n");
        else
            codigoAssembly.append("\n").append("MOV [").append(var).append("], EAX").append("\n");
    }

    private static void gerarCodigoPrint(NodePrint node) {
        switch(node.getTipo()){
            case "int":
                codigoAssembly.append("\n\npush dword [").append(node.getValor().getLexeme()).append("]\npush dword printint\ncall printf\nadd esp, 8");
                break;
            case "bool":
                codigoAssembly.append("\n\npush dword [").append(node.getValor().getLexeme()).append("]\npush dword printint\ncall printf\nadd esp, 8");
                break;
            case "char":
                codigoAssembly.append("\n\npush dword [").append(node.getValor().getLexeme()).append("]\npush dword printchar\ncall printf\nadd esp, 8");
                break;
            case "float":
                codigoAssembly.append("\n\nfld dword [").append(node.getValor().getLexeme()).append("]\nsub esp, 8\nfstp qword [esp]\npush printfloat\ncall printf\nadd esp, 12");
                break;
        }
    }

    private static void gerarCodigoOperacaoBinaria(NodeOperacao node) {
        gerarCodigo(node.getDir());
        codigoAssembly.append("PUSH EAX").append("\n");
        gerarCodigo(node.getEsq());
        codigoAssembly.append("POP EBX").append("\n");

        Token operador = node.getOperador();
        switch (operador.getTipo()) {
            case PLUS:
                codigoAssembly.append("ADD EAX, EBX").append("\n");
                break;
            case MINUS:
                codigoAssembly.append("SUB EAX, EBX").append("\n");
                break;
            case TIMES:
                codigoAssembly.append("IMUL EAX, EBX").append("\n");
                break;
            case DIVIDE:
                codigoAssembly.append("MOV EDX, 0").append("\n");
                codigoAssembly.append("IDIV EBX").append("\n");
                break;
        }
    }

    private static void gerarCodigoLiteral(NodeValor node) {
        String val = node.getValor();
        val = val.replace("true", "1");
        val = val.replace("false", "0");

        String label_float = "";
        if (val.contains(".")){
            label_float = gerarLabel();
            floats.put(label_float, val);

            codigoAssembly.append("\nfld dword [").append(label_float).append("]\n");
        }else{
            if (val.replace(".", "").matches("\\d+") || val.contains("'")){
                codigoAssembly.append("\nMOV EAX, ").append(val).append("\n");
            }else{
                codigoAssembly.append("\nMOV EAX, [").append(val).append("]\n");
            }
        }
    }

    private static String gerarLabel() {
        contadorLabel++;
        return "L" + contadorLabel;
    }

    public static String codigoGerador() {
        StringBuilder codigoFinal = new StringBuilder();
        codigoFinal.append("section .data").append("\n");
        for (var aux : vars)
            codigoFinal.append("    " + aux + ":  dd 0").append("\n"); // Declaração da variável x
        for (var aux : floats.keySet())
            codigoFinal.append("    " + aux + ":  dd " + floats.get(aux)).append("\n"); // Declaração da variável x
        codigoFinal.append("    " + "printint: db " + '"' + "%d" + '"' + ", 10, 0").append("\n");
        codigoFinal.append("    " + "printchar: db " + '"' + "%c" + '"' + ", 10, 0").append("\n");
        codigoFinal.append("    " + "printfloat: db " + '"' + "%f" + '"' + ", 10, 0").append("\n");
        codigoFinal.append("\n");
        codigoFinal.append("section .text").append("\n");
        codigoFinal.append("    global main").append("\n\n");
        codigoFinal.append("extern printf, exit");
        codigoFinal.append("\n\n");
        codigoFinal.append("main:").append("\n");
        codigoFinal.append(codigoAssembly);

        codigoFinal.append("\nMOV EAX, 1").append("\n").append("XOR EBX, EBX").append("\n").append("INT 0x80").append("\n");
        return codigoFinal.toString();
    }
}
