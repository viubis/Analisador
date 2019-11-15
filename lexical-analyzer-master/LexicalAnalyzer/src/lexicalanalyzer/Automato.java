/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexicalanalyzer;

import java.util.ArrayList;

/**
 *
 * @author Victo
 */
public class Automato {

    private ArrayList<Token> listarTokens;
    private ArrayList<String> listarErros;
    private ArrayList<String> codigo;
    private static final char EOF = '\0';
    private int linha, aux;
    private boolean linhaVazia;
    private final EstruturaLexica token;

    public Automato() {
        this.listarTokens = new ArrayList<>();
        this.listarErros = new ArrayList<>();
        this.codigo = new ArrayList<>();
        this.linha = 0;
        this.aux = 0;
        this.linhaVazia = false;
        this.token = new EstruturaLexica();
    }
    void analisadorLexico(ArrayList<String> codigoFonte) {
        this.codigo = codigoFonte;
        char a = proximo();
        while (a != EOF) {//aqui ele lê e manda pro automato correspondente até o fima do arquivo.
            testaCaractere(a);//automato correspondente
            a = proximo();
        }
    }

    /**
     * Método que lista a lista de tokens incorreta 
     * @return 
    */
    public ArrayList<String> getListarErros() {
        return listarErros;
    }

    /**
     *Método que lista a lista de tokens correta 
     * @return 
    */
    public ArrayList<Token> getListarTokens() {
        return listarTokens;
    }


    private char proximo() {
        if (!codigo.isEmpty()) {

            //separa todos os caracteres de uma determinada linha em um array
            char c[] = codigo.get(linha).toCharArray();

            //verifica se a linha está vazia
            if (c.length == aux) {
                linhaVazia = false;
                return ' ';
                //verifica se a linha for maior que zero retorna a primeira posição do array  
            } else if (c.length > aux) {
                linhaVazia = false;
                return c[aux];
                //verifica se o tamanho do arquivo tem uma próxima linha
            } else if (codigo.size() > (linha + 1)) {
                linha++;//se tiver ele incrementa a linha
                c = codigo.get(linha).toCharArray();
                //c recebe como array a próxima linha
                aux = 0;//posição volta a ser 0 (é mudado dentro dos métodos)
                if (c.length == 0) {//se a linha atual for igual a zero a linha está vazia
                    this.linhaVazia = true;
                    return ' ';
                }
                return c[aux];//se não retorna a primeira posição da linha
            } else {
                return EOF;
            }
        } else {
            return EOF;
        }
    }

    private void testaCaractere(char a) {
        String lexema;
        if (!this.linhaVazia) {

            lexema = "";
            if (token.verificarEspaco(a)) {//desconsidera espaço
                aux++;
            } else if (token.verificarLetra(a)) {
                letra(lexema, a);
            } else if (Character.isDigit(a)) {
                //numero(lexema, a);
            } else if (token.verificarOperador(a)) {//se for um operador será enviado para o método de operadores
                operador(lexema, a);
            } else if (token.verificarDelimitador(a)) {//método de delimitador
                delimitador(lexema, a);
            } else if (a == '/') {//método de comentário, por lá verifica também se ele for um operador aritmético
                //comentario(lexema, a);
            } else if (a == '"') {//método de cadeia de caractere
                cadeiaDeCaractere(lexema, a);
            } else {//se não entrar em nenhum dessas opções acima é um simbolo incorreto, pois não se encontra na tabela
                //
            }

        } else {
            //linha vazia avança pra próxima
            linhaVazia = false;
            linha++;
        }
    }

    public void letra(String lexema, char a) {

        int linhaInicial = linha;
        int aux1 = aux;
        boolean erro = false;

        lexema = lexema + a;
        this.aux++;
        a = this.proximo();

        while (!(a == EOF || Character.isSpaceChar(a) || token.verificarDelimitador(a) || token.verificarOperador(a) || a == '/' || a == '"')) {
            if (!(a == '_' || token.verificarLetra(a) || Character.isDigit(a))) {
                erro = true;
            }
            lexema = lexema + a;
            aux++;
            a = this.proximo();
        }
        if (!erro) {
            Token tokenaux;
            if (token.verificarPalavrasReservada(lexema)) {
                tokenaux = new Token(linhaInicial + 1, aux1 + 1, "palavraReservada", lexema);
            } else {
                tokenaux = new Token(linhaInicial + 1, aux1 + 1, "identificador", lexema);
            }
            listarTokens.add(tokenaux);
        } else {
            this.addErro("identificadorErrado", lexema, linhaInicial);
        }
    }

    private void numero(String lexema, char a) {
        int linhaInicial = linha;
        int auxiliar = aux;
        boolean ponto = false;
        boolean erro = false;

        lexema = lexema + a;
        this.aux++;
        a = this.proximo();
        while (!(a == EOF || Character.isSpaceChar(a) || token.verificarOperador(a) || token.verificarDelimitador(a) || a == '/' || a == '"')) {

            if (!(Character.isDigit(a)) && a != '.') {
                erro = true;
                lexema = lexema + a;
                aux++;
                a = this.proximo();
            } else if (Character.isDigit(a)) {
                lexema = lexema + a;
                aux++;
                a = this.proximo();
            } else if (a == '.' && ponto == false) {
                lexema = lexema + a;
                aux++;
                ponto = true;
                a = this.proximo();
                if (!(Character.isDigit(a))) {
                    erro = true;
                }
            } else {
                erro = true;
                lexema = lexema + a;
                aux++;
                a = this.proximo();
            }
        }
        if (!erro) {
            Token tokenAuxiliar;
            tokenAuxiliar = new Token(linhaInicial + 1, auxiliar + 1, "numero", lexema);
            listarTokens.add(tokenAuxiliar);
        } else {
            addErro("numeroErrado", lexema, linhaInicial);
        }
    }

    private void operador(String lexema, char a) {
        if (a == '+' || a == '-' || a == '*') {
            operadorAritimetico(lexema, a);
            return;
        } else {
            operadorRelacionalLogico(lexema, a);
            return;
        }
    }
    
    private void operadorAritimetico(String lexema, char a) {
        int linhaInicial = this.linha;
        int auxiliar = this.aux;
        Token tokenAuxiliar;

        lexema = lexema + a;
        this.aux++;

        if (a == '+') {
            a = this.proximo();
            if (a == '+') {
                lexema = lexema + a;
                this.aux++;
            }
        } else if (a == '-') {
            a = this.proximo();
            if (Character.isSpaceChar(a)) {
                do {
                    this.aux++;
                    a = this.proximo();
                } while (token.verificarEspaco(a));
                if (Character.isDigit(a)) {
                    this.numero(lexema, a);
                    return;
                }
            } else if (a == '-') {
                lexema = lexema + a;
                this.aux++;
            } else if (Character.isDigit(a)) {
                this.numero(lexema, a);
                return;
            }

        }
        tokenAuxiliar = new Token(linhaInicial + 1, auxiliar + 1, "opAritmetico", lexema);
        listarTokens.add(tokenAuxiliar);
    }

    private void operadorRelacionalLogico(String lexema, char a) {
        int linhaInicial = this.linha;
        int auxiliar = this.aux;
        Token tokenAuxiliar;

        lexema = lexema + a;
        this.aux++;

        switch (a) {
            case '<':
            case '>':
            case '=':
                a = this.proximo();
                if (a == '=') {
                    lexema = lexema + a;
                    this.aux++;
                }   tokenAuxiliar = new Token(linhaInicial + 1, auxiliar + 1, "opRelacional", lexema);
                listarTokens.add(tokenAuxiliar);
                break;
            case '!':
                a = this.proximo();
                if (a == '=') {
                    lexema = lexema + a;
                    this.aux++;
                    tokenAuxiliar = new Token(linhaInicial + 1, auxiliar + 1, "opRelacional", lexema);
                    listarTokens.add(tokenAuxiliar);
                }else{
                    tokenAuxiliar = new Token(linhaInicial + 1, auxiliar + 1, "opLogico", lexema);
                    listarTokens.add(tokenAuxiliar);
                }   break;
            case '&':
                a = this.proximo();
                if (a == '&') {
                    lexema = lexema + a;
                    this.aux++;
                    tokenAuxiliar = new Token(linhaInicial + 1, auxiliar + 1, "opLogico", lexema);
                    listarTokens.add(tokenAuxiliar);
                } else {
                    this.addErro("opLogicoErrado", lexema, linhaInicial);
                }   break;
            case '|':
                a = this.proximo();
                if (a == '|') {
                    lexema = lexema + a;
                    this.aux++;
                    tokenAuxiliar = new Token(linhaInicial + 1, auxiliar + 1, "opLogico", lexema);
                    listarTokens.add(tokenAuxiliar);
                } else {
                    this.addErro("opLogicoErrado", lexema, linhaInicial);
                }   break;
            default:
                break;
        }
    }
   
    private void delimitador(String lexema, char a) {
        int linhaInicial = this.linha;
        int auxiliar = this.aux;

        lexema = lexema + a;
        this.aux++;
        Token tokenAuxiliar = new Token(linhaInicial + 1, auxiliar + 1, "delimitador", lexema);
        listarTokens.add(tokenAuxiliar);
    }

    private void cadeiaDeCaractere(String lexema, char a) {

        int linhaInicial = this.linha;
        boolean simboloInvalido = false;
        int auxiliar = this.aux;
        boolean erro = false;

        lexema = lexema + a;
        this.aux++;
        a = this.proximo();

        while (a != '"' && linha == linhaInicial && a != EOF) {
            if (a == ((char) 92) || Character.isLetterOrDigit(a) || token.verificarSimbolo(a) || Character.isDigit(a)) {
                this.aux++;
                lexema = lexema + a;
                a = this.proximo();               
            } else if (token.verificarSimboloInvalido(a)) {
                this.aux++;
                lexema = lexema + a;
                a = this.proximo();
                erro = true;
                simboloInvalido = true;
            }else { 
                this.aux++;
                lexema = lexema + a;
                a = this.proximo();
                erro = true;
            }
        }

        if (a == '"' && linhaInicial == this.linha) {
            lexema = lexema + a;
            this.aux++;
        } else
        {
            erro = true;
        }

        if (!erro && linhaInicial == this.linha) {
            Token tokenAuxiliar;
            tokenAuxiliar = new Token(linhaInicial + 1, auxiliar + 1, "cadeiaDeCaractere", lexema);
            this.listarTokens.add(tokenAuxiliar);
        } else if (simboloInvalido == true) {
            this.addErro("cadeiaDeCaractereErrada", lexema, linhaInicial);
        } else {
            this.addErro("cadeiaDeCaractereErrada", lexema, linhaInicial + 1);
        }
    }

    private void addErro(String tipo, String erro, int linha) {
        listarErros.add((linha + 1) + "  " + erro + "  " + tipo + "  ");
    }
}
