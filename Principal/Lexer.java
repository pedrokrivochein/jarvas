package Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Collections;

public class Lexer {
    public String input;
    public int linha;
    public int coluna;

    public List<Token> tokens;

    public Lexer(String input){
        this.input = input;
        this.tokens = new ArrayList<Token>();
    }

    public List<Token> getTokens() {
        //Separa o codigo por linhas
        String[] lines = input.split("\\r?\\n");

        int i = 0;
        for (i = 0; i < lines.length; i++) { //Roda por todas as linhas

            coluna = 0;
            String line = lines[i];

            //Ele roda por todos os lexemes dessa linha, Tokenizando ate a linha estar vazia
            while (!line.replace(" ", "").isEmpty()) {
                System.out.println(line);

                Matcher matcher;
                for(var tipo : TiposToken.values()){ //Rodar pelos tipos de token, procurando pelo regex

                    Pattern tipo_pattern = TiposRegexToken.patterns.get((TiposToken) tipo);
                    
                    matcher = tipo_pattern.matcher(line);
                    
                    if (matcher.find()){ //Encontrou
                        String value = matcher.group();
                        coluna = matcher.end() - value.length();
                        tokens.add(new Token(tipo, value, i, coluna)); //Adiciona na lista de tokens
                        line = line.substring(0, coluna) + " " + line.substring(matcher.end());
                    }
                }
            }
        }

        //Adiciona o fim do arquivo
        tokens.add(new Token(TiposToken.EOF, "", lines.length, 0));

        //Ordena a lista de tokens por linha e coluna
        Collections.sort(tokens, new TokenComparator());
    
        //Devolve a lista
        return tokens;
    }
}

