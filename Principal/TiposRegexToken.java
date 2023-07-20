package Principal;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TiposRegexToken
 */
public class TiposRegexToken {

    public static Map<TiposToken, Pattern> patterns = Map.ofEntries(
        // PALAVRAS CHAVE
        Map.entry(TiposToken.IF, Pattern.compile("if\\b")),
        Map.entry(TiposToken.ELSE, Pattern.compile("\\belse\\b")),
        Map.entry(TiposToken.WHILE, Pattern.compile("\\bwhile\\b")),
        Map.entry(TiposToken.FOR, Pattern.compile("\\bfor\\b")),
        Map.entry(TiposToken.DO, Pattern.compile("\\bdo\\b")),
        Map.entry(TiposToken.RETURN, Pattern.compile("\\breturn\\b")),
        Map.entry(TiposToken.BREAK, Pattern.compile("\\bbreak\\b")),
        Map.entry(TiposToken.CLASS, Pattern.compile("\\bclass\\b")),

        //FUNCOES
        Map.entry(TiposToken.PRINT, Pattern.compile("\\bprint\\b")),

        // DELIMITADORES
        Map.entry(TiposToken.PAREN_L, Pattern.compile("\\(")),
        Map.entry(TiposToken.PAREN_R, Pattern.compile("\\)")),
        Map.entry(TiposToken.BRACE_L, Pattern.compile("\\{")),
        Map.entry(TiposToken.BRACE_R, Pattern.compile("\\}")),
        Map.entry(TiposToken.BRACKET_L, Pattern.compile("\\[")),
        Map.entry(TiposToken.BRACKET_R, Pattern.compile("\\]")),
        Map.entry(TiposToken.SEMICOLON, Pattern.compile(";")),
        Map.entry(TiposToken.COMMA, Pattern.compile(",")),
        Map.entry(TiposToken.DOUBLE_DOT, Pattern.compile("\\:")),
        
        // IDENTIFICADORES E LITERAIS
        Map.entry(TiposToken.VAR_INT, Pattern.compile("int")),
        Map.entry(TiposToken.VAR_BOOL, Pattern.compile("bool")),
        Map.entry(TiposToken.VAR_CHAR, Pattern.compile("char")),
        Map.entry(TiposToken.VAR_FLOAT, Pattern.compile("float")),
        Map.entry(TiposToken.IDENTIFIER, Pattern.compile("[a-zA-Z_$][a-zA-Z_$0-9]*")),
        Map.entry(TiposToken.INTEGER, Pattern.compile("\\b(?!\\d+\\.\\d+)\\d+\\b")),
        Map.entry(TiposToken.FLOAT, Pattern.compile("\\d+\\.\\d+")),
        Map.entry(TiposToken.CHAR, Pattern.compile("\'([^\']|\\.)\'")),
        Map.entry(TiposToken.BOOLEAN, Pattern.compile("true|false")),
        
        // ATRIBUICAO
        Map.entry(TiposToken.EQUAL, Pattern.compile("(?<!<>=)=(?!=)")),
        
        // OPERADORES
        Map.entry(TiposToken.PLUS, Pattern.compile("\\+")),
        Map.entry(TiposToken.MINUS, Pattern.compile("\\-")),
        Map.entry(TiposToken.TIMES, Pattern.compile("\\*")),
        Map.entry(TiposToken.DIVIDE, Pattern.compile("\\/")),

        // CONDICIONAIS
        Map.entry(TiposToken.EQUALS, Pattern.compile("(?<![!=])\\={2}(?!=)")),
        Map.entry(TiposToken.NOT_EQUAL, Pattern.compile("\\b!=\\b")),
        Map.entry(TiposToken.GREATER_EQUAL, Pattern.compile(">=")),
        Map.entry(TiposToken.LESS_EQUAL, Pattern.compile("<=")),
        Map.entry(TiposToken.GREATER_THAN, Pattern.compile("(?<!>)>")),
        Map.entry(TiposToken.LESS_THAN, Pattern.compile("<(?!=)")),

        // FIM
        Map.entry(TiposToken.EOF, Pattern.compile("FIMDOARQUIVO"))

    );
}