package Principal;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

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

/**
 * Main
 */
public class Main {

    public static void main(String[] args) throws Exception {

        //Leitura do arquivo
        File file = new File(args[0]);
        StringBuilder contentBuilder = new StringBuilder();

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                contentBuilder.append(line).append("\n");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String conteudo = contentBuilder.toString();

        //Cria o parser com a lista de tokens
        Parser parser = new Parser(conteudo);

        //Inicia o parser e retorna a Node Raiz da árvore sintática
        var raiz_arvore = parser.parse();

        //Printar a arvore final
        visualizar(raiz_arvore, 0);

        try{
            //Gerar o codigo da arvore em assembly
            Gerador.gerar(raiz_arvore);

            String cod = Gerador.codigoGerador();
            salvarArquivo(cod, "prog_comp.asm");

            //Roda os comandos no cmd para compilar o codigo asm
            compilar();
        }catch(Exception e){
        }
    }

    private static void salvarArquivo(String codigo, String nome_arquivo) {
        try (FileWriter writer = new FileWriter(nome_arquivo)) {
            writer.write(codigo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void visualizar(Node node, int indentLevel) {
        String indent = " ".repeat(indentLevel * 2);
    
        if (node instanceof NodeBloco) {
            NodeBloco compositeNode = (NodeBloco) node;
            for (Node child : compositeNode.getChildren()) {
                visualizar(child, indentLevel);
            }
        } else if (node instanceof NodeAtribuicao) {
            NodeAtribuicao assignmentNode = (NodeAtribuicao) node;
            System.out.println(indent + "NodeAtribuicao");
            System.out.println(indent + "  Identificador: ");
            visualizar(assignmentNode.getIdentificador(), indentLevel + 1);
            System.out.println(indent + "  Expressao: ");
            visualizar(assignmentNode.getExpressao(), indentLevel + 1);
        } else if (node instanceof NodeOperacao) {
            NodeOperacao binaryOperationNode = (NodeOperacao) node;
            System.out.println(indent + "NodeOperacao");
            System.out.println(indent + "  Operador: " + binaryOperationNode.getOperador());
            System.out.println(indent + "  Esquerda: ");
            visualizar(binaryOperationNode.getEsq(), indentLevel + 1);
            System.out.println(indent + "  Direita: ");
            visualizar(binaryOperationNode.getDir(), indentLevel + 1);
        } else if (node instanceof NodeValor) {
            NodeValor literalNode = (NodeValor) node;
            System.out.println(indent + "NodeValor");
            System.out.println(indent + "  Valor: " + literalNode.getValor());
        } else if (node instanceof NodeIf) {
            NodeIf ifNode = (NodeIf) node;
            System.out.println(indent + "NodeIf");
            System.out.println(indent + "  Condicao: ");
            visualizar(ifNode.getCondicao(), indentLevel + 1);
            System.out.println(indent + "  NodeBloco: ");
            visualizar(ifNode.getIfBloco(), indentLevel + 1);
            System.out.println(indent + "  NodeElseBloco: ");
            visualizar(ifNode.getElseBloco(), indentLevel + 1);
        } else if (node instanceof NodeWhile) {
            NodeWhile ifNode = (NodeWhile) node;
            System.out.println(indent + "NodeWhile");
            System.out.println(indent + "  Condicao: ");
            visualizar(ifNode.getCondicao(), indentLevel + 1);
            System.out.println(indent + "  NodeBloco: ");
            visualizar(ifNode.getBloco(), indentLevel + 1);
        } else if (node instanceof NodeComparacao) {
            NodeComparacao nodeC = (NodeComparacao) node;
            System.out.println(indent + "NodeComparacao");
            System.out.println(indent + "  Condicao: " + nodeC.getCondicao());
            System.out.println(indent + "  Esquerda: ");
            visualizar(nodeC.getEsq(), indentLevel + 1);
            System.out.println(indent + "  Direita: ");
            visualizar(nodeC.getDir(), indentLevel + 1);
        } else if (node instanceof NodeIdentificador) {
            NodeIdentificador nodeC = (NodeIdentificador) node;
            System.out.println(indent + "NodeIdentificador");
            System.out.println(indent + "  Tipo: " + nodeC.getTipo());
            System.out.println(indent + "  Identificador: " + nodeC.getIdentificador());
        } else if (node instanceof NodePrint) {
            NodePrint nodeC = (NodePrint) node;
            System.out.println(indent + "NodePrint");
            System.out.println(indent + "  Tipo: " + nodeC.getTipo());
            System.out.println(indent + "  Valor: " + nodeC.getValor().getLexeme());
        }
    }
    
    public static void compilar(){
        try {
            
            String command = "nasm -f elf32 prog_comp.asm -o prog_comp.o";
            
            Process process = Runtime.getRuntime().exec(command);

            command = "gcc -m32 prog_comp.o -o programa";

            process = Runtime.getRuntime().exec(command);

            process = Runtime.getRuntime().exec("chmod +x ./programa");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}