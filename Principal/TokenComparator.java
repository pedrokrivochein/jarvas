package Principal;
import java.util.Comparator;

public class TokenComparator implements Comparator<Token> {
    @Override
    public int compare(Token token1, Token token2) {
        int linhaComp = Integer.compare(token1.getLinha(), token2.getLinha());
        if (linhaComp != 0) {
            return linhaComp;
        } else {
            return Integer.compare(token1.getColuna(), token2.getColuna());
        }
    }
}