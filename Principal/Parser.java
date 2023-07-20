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

public class Parser {

    private List<Token> tokens;
    private Map<String, TiposVar> identificadores = new HashMap<String, TiposVar>();
    private int token_pos;
    
    public Parser(String conteudo) {
        //Chamada do Lexer
        Lexer lexer = new Lexer(conteudo);
        this.tokens = lexer.getTokens(); //Pega os tokens do lexer
        token_pos = 0;
    }
    
    private void pegar(TiposToken tipo) throws Exception {
        Token currentToken = getCurrentToken();
        System.out.println(currentToken);
        if (currentToken.getTipo() == tipo) {
            token_pos++;
        } else {
            throw new Exception("Erro de sintaxe. Token inesperado: " + currentToken.getTipo());
        }
    }
    
    private Token getCurrentToken() throws Exception {
        if (token_pos >= tokens.size()) {
            throw new Exception("Erro de sintaxe. Fim inesperado do arquivo.");
        }
        return tokens.get(token_pos);
    }

    /*
    *
    * <valor>;
    * <valor> <operador> <valor>;
    * <valor> <operador> (<valor> <operador> <valor>);
    * (<valor> <operador> <valor>);
    * (<valor>);
    *
    */
    private Node parse_expressao(boolean com_valor, Node valor) throws Exception {
        //Retornar Valor ou Expressao
        Token currentToken = getCurrentToken();

        if (currentToken.getTipo() == TiposToken.BOOLEAN){
            pegar(TiposToken.BOOLEAN);
            return new NodeValor(currentToken, TiposVar.BOOL);
        }

        Node node_valor;

        if(!com_valor){

            if(currentToken.getTipo() == TiposToken.PAREN_L){
                pegar(currentToken.getTipo());
            }

            currentToken = getCurrentToken();
            
            node_valor = new NodeValor(currentToken, TiposVar.INT);
            //Checar valor
            if(currentToken.getTipo() == TiposToken.INTEGER || currentToken.getTipo() == TiposToken.FLOAT || currentToken.getTipo() == TiposToken.IDENTIFIER)
                pegar(currentToken.getTipo());
            else
                throw new Exception("Erro de sintaxe. Token inesperado: " + currentToken.getTipo());
 
            currentToken = getCurrentToken();
            
            //Fim?
            if(currentToken.getTipo() == TiposToken.SEMICOLON || currentToken.getTipo() == TiposToken.EQUALS || currentToken.getTipo() == TiposToken.LESS_EQUAL
            || currentToken.getTipo() == TiposToken.LESS_THAN || currentToken.getTipo() == TiposToken.GREATER_EQUAL || currentToken.getTipo() == TiposToken.GREATER_THAN
            || currentToken.getTipo() == TiposToken.NOT_EQUAL){
                return node_valor;
            }

            if(currentToken.getTipo() == TiposToken.PAREN_R){
                return node_valor;
            }

        }else{
            node_valor = valor;
        }
        
        //Logo: expressao
        currentToken = getCurrentToken();

        if(currentToken.getTipo() == TiposToken.BRACE_L){
            return node_valor;
        }
        
        //Checar operador
        Token op = getCurrentToken();
        if(currentToken.getTipo() == TiposToken.PLUS || currentToken.getTipo() == TiposToken.MINUS || currentToken.getTipo() == TiposToken.TIMES)
            pegar(currentToken.getTipo());
        else
            throw new Exception("Erro de sintaxe. Token inesperado: " + currentToken.getTipo());

        return new NodeOperacao(op, node_valor, parse_expressao(false, null));
    }
    
    /*
     * 
     * <tipo> <identificador> = <expressao>;
     * 
     */
    private Node parse_atribuicao(boolean inicializar) throws Exception {
        Token currentToken = getCurrentToken();

        TiposVar tipo = null;
        if (inicializar){
            switch(currentToken.getTipo()){
                case VAR_INT:
                    pegar(currentToken.getTipo());
                    tipo = TiposVar.INT;
                    break;
                case VAR_BOOL:
                    pegar(currentToken.getTipo());
                    tipo = TiposVar.BOOL;
                    break;
                case VAR_FLOAT:
                    pegar(currentToken.getTipo());
                    tipo = TiposVar.FLOAT;
                    break;
                case VAR_CHAR:
                    pegar(currentToken.getTipo());
                    tipo = TiposVar.CHAR;
                    break;
                default:
                    throw new Exception("Erro de sintaxe. Token inesperado: " + currentToken.getTipo());
            }
            currentToken = getCurrentToken();
            if (!identificadores.containsKey(currentToken.getLexeme()))
                identificadores.put(currentToken.getLexeme(), tipo);
        }else{
            if (identificadores.containsKey(currentToken.getLexeme()))
                tipo = identificadores.get(currentToken.getLexeme());
        }

        Token identifierToken = getCurrentToken(); //Guarda o identificador
        NodeIdentificador identificador = new NodeIdentificador(tipo, identifierToken);

        pegar(TiposToken.IDENTIFIER);

        pegar(TiposToken.EQUAL);

        currentToken = getCurrentToken();

        Node final_expression = null;
        switch(tipo){
            case BOOL:
                final_expression = new NodeValor(getCurrentToken(), TiposVar.BOOL);
                pegar(TiposToken.BOOLEAN);
                tipo = TiposVar.BOOL;
                break;
            case CHAR:
                final_expression = new NodeValor(getCurrentToken(), TiposVar.CHAR);
                pegar(TiposToken.CHAR);
                tipo = TiposVar.CHAR;
                break;
            case FLOAT:
            case INT:
                Node expression = parse_expressao(false, null);

                currentToken = getCurrentToken();
        
                final_expression = expression;
        
                if (currentToken.getTipo() == TiposToken.PAREN_R){
                    pegar(TiposToken.PAREN_R);
        
                    final_expression = parse_expressao(true, expression);
                }
        
                currentToken = getCurrentToken();
                if (currentToken.getTipo() == TiposToken.PAREN_R)
                    pegar(TiposToken.PAREN_R);

                break;
            default:
                throw new Exception("Erro de sintaxe. Token inesperado: " + currentToken.getTipo());
        }

        pegar(TiposToken.SEMICOLON);

        return new NodeAtribuicao(identificador, final_expression);
    }

    /*
     * 
     * if (<expressao>) {}
     * if (<expressao> == <expressao>) {}
     * 
     */
    private Node parse_if() throws Exception {
        pegar(TiposToken.IF);
        
        pegar(TiposToken.PAREN_L);

        Token currentToken = getCurrentToken();
        Node nodeFinal = null;
        if (currentToken.getTipo() == TiposToken.BOOLEAN){
            nodeFinal = new NodeValor(currentToken, TiposVar.BOOL);
            pegar(currentToken.getTipo());
            pegar(TiposToken.PAREN_R);
        }else{

            Node expression = parse_expressao(false, null);
            Node final_expression_left = expression;
            if (currentToken.getTipo() == TiposToken.PAREN_R){
                pegar(TiposToken.PAREN_R);
                final_expression_left = parse_expressao(true, expression);
            }
            currentToken = getCurrentToken();

            if (currentToken.getTipo() == TiposToken.PAREN_R){
                nodeFinal = new NodeValor(currentToken, TiposVar.BOOL);
                pegar(currentToken.getTipo());
            }else{
                currentToken = getCurrentToken();

                Token cond = currentToken;
                
                if (currentToken.getTipo() == TiposToken.EQUALS || currentToken.getTipo() == TiposToken.LESS_EQUAL || currentToken.getTipo() == TiposToken.LESS_THAN
                    || currentToken.getTipo() == TiposToken.GREATER_EQUAL || currentToken.getTipo() == TiposToken.GREATER_THAN)
                    pegar(currentToken.getTipo());
                else
                    throw new Exception("Erro de sintaxe. Token inesperado: " + currentToken.getTipo());
                
                expression = parse_expressao(false, null);
                Node final_expression_right = expression;
                currentToken = getCurrentToken();
                if (currentToken.getTipo() == TiposToken.PAREN_R){
                    pegar(TiposToken.PAREN_R);

                    final_expression_right = parse_expressao(true, expression);
                }

                nodeFinal = new NodeComparacao(cond, final_expression_left, final_expression_right);
            }
        }

        Node blocoIf = parse_bloco();
        currentToken = getCurrentToken();

        Node blocoElse = null;
        if (currentToken.getTipo() == TiposToken.ELSE){
            pegar(currentToken.getTipo());
            blocoElse = parse_bloco();
        }

        return new NodeIf(nodeFinal, blocoIf, blocoElse);
    }

    /*
     * 
     * while (<expressao>) {}
     * while (<expressao> == <expressao>) {}
     * while (<valor> == <expressao>) {}
     * 
     */
    private Node parse_while() throws Exception {
        pegar(TiposToken.WHILE);
        
        pegar(TiposToken.PAREN_L);

        Token currentToken = getCurrentToken();

        Node nodeFinal = null;
        if (currentToken.getTipo() == TiposToken.BOOLEAN){
            nodeFinal = new NodeValor(currentToken, TiposVar.BOOL);
            pegar(currentToken.getTipo());
            pegar(TiposToken.PAREN_R);
        }else{
            Node expression = parse_expressao(false, null);
            Node final_expression_left = expression;
            if (currentToken.getTipo() == TiposToken.PAREN_R){
                pegar(TiposToken.PAREN_R);
                final_expression_left = parse_expressao(true, expression);
            }
            currentToken = getCurrentToken();

            if (currentToken.getTipo() == TiposToken.PAREN_R){
                nodeFinal = new NodeValor(currentToken, TiposVar.BOOL);
                pegar(currentToken.getTipo());
            }else{
                currentToken = getCurrentToken();

                Token cond = currentToken;
                
                if (currentToken.getTipo() == TiposToken.EQUALS || currentToken.getTipo() == TiposToken.LESS_EQUAL || currentToken.getTipo() == TiposToken.LESS_THAN
                    || currentToken.getTipo() == TiposToken.GREATER_EQUAL || currentToken.getTipo() == TiposToken.GREATER_THAN)
                    pegar(currentToken.getTipo());
                else
                    throw new Exception("Erro de sintaxe. Token inesperado: " + currentToken.getTipo());
                
                expression = parse_expressao(false, null);
                Node final_expression_right = expression;
                currentToken = getCurrentToken();
                if (currentToken.getTipo() == TiposToken.PAREN_R){
                    pegar(TiposToken.PAREN_R);

                    final_expression_right = parse_expressao(true, expression);
                }

                nodeFinal = new NodeComparacao(cond, final_expression_left, final_expression_right);
            }
        }

        Node bloco = parse_bloco();

        return new NodeWhile(nodeFinal, bloco);
    }
    
    /*
     * 
     * print(<valor>)
     * 
     */
    private Node parse_print() throws Exception {
        pegar(TiposToken.PRINT);

        pegar(TiposToken.PAREN_L);

        Token valor = getCurrentToken();

        pegar(valor.getTipo());

        pegar(TiposToken.COMMA);

        Token tipoValor = getCurrentToken();

        pegar(tipoValor.getTipo());

        pegar(TiposToken.PAREN_R);

        pegar(TiposToken.SEMICOLON);

        return new NodePrint(valor, tipoValor);
    }

    /*
     * parse_bloco
     * 
     * Roda por um bloco de Nodes
     */
    private Node parse_bloco() throws Exception {
        pegar(TiposToken.BRACE_L);

        NodeBloco nodeBloco = new NodeBloco();
        while (getCurrentToken().getTipo() != TiposToken.BRACE_R) {
            Token currentToken = getCurrentToken();
            switch(currentToken.getTipo()){
                case VAR_INT:
                    nodeBloco.adicionar(parse_atribuicao(true));
                    break;
                case VAR_BOOL:
                    nodeBloco.adicionar(parse_atribuicao(true));
                    break;
                case VAR_CHAR:
                    nodeBloco.adicionar(parse_atribuicao(true));
                    break;
                case VAR_FLOAT:
                    nodeBloco.adicionar(parse_atribuicao(true));
                    break;
                case IDENTIFIER:
                    nodeBloco.adicionar(parse_atribuicao(false));
                    break;
                case IF:
                    nodeBloco.adicionar(parse_if());
                    break;
                case WHILE:
                    nodeBloco.adicionar(parse_while());
                    break;
                case PRINT:
                    nodeBloco.adicionar(parse_print());
                    break;
                default:
                    throw new Exception("Erro de sintaxe. Token inesperado: " + currentToken.getTipo());
            }
        }

        pegar(TiposToken.BRACE_R);

        return nodeBloco;
    }
    
    public Node parse() throws Exception {
        System.out.println("Tokens (Lexer): ");
        System.out.println(tokens);

        System.out.println("Iniciando Parser");

        //Bloco Inicial
        NodeBloco main = new NodeBloco();

        //Enquanto houver tokens, verificar qual a proxima frase
        while (token_pos < tokens.size()) {
            Token currentToken = getCurrentToken();
            switch(currentToken.getTipo()){
                case VAR_INT:
                    main.adicionar(parse_atribuicao(true));
                    break;
                case VAR_BOOL:
                    main.adicionar(parse_atribuicao(true));
                    break;
                case VAR_FLOAT:
                    main.adicionar(parse_atribuicao(true));
                    break;
                case VAR_CHAR:
                    main.adicionar(parse_atribuicao(true));
                    break;
                case IDENTIFIER:
                    main.adicionar(parse_atribuicao(false));
                    break;
                case IF:
                    main.adicionar(parse_if());
                    break;
                case PRINT:
                    main.adicionar(parse_print());
                    break;
                case WHILE:
                    main.adicionar(parse_while());
                    break;
                case EOF:
                    System.out.println("Iniciando Semantico");
                    Semantico.checarSemantica(main);
                    return main;
                default:
                    throw new Exception("Erro de sintaxe. Token inesperado: " + currentToken.getTipo());
            }
        }

        return main;
    }
}