package Principal;
public class Token {
    private TiposToken tipo;
    private String lexeme;
    private int linha;
    private int coluna;

    public Token(TiposToken tipo, String lexeme, int linha, int coluna) {
        this.tipo = tipo;
        this.lexeme = lexeme;
        this.linha = linha;
        this.coluna = coluna;
    }

    public TiposToken getTipo() {
        return tipo;
    }

    public String getLexeme() {
        return lexeme;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    @Override
    public String toString() {
        return "Token{" +
                "tipo=" + tipo +
                ", lexeme='" + lexeme + '\'' +
                ", linha=" + linha +
                ", coluna=" + coluna +
                '}';
    }
}

