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
public class EstruturaLexica {
    
    private final ArrayList<String> palavrasReservadas = new ArrayList<>();
    private final ArrayList<Character> opAritmeticos = new ArrayList<>();
    private final ArrayList<Character> opRelacionais = new ArrayList<>();
    private final ArrayList<Character> opLogicos = new ArrayList<>();
    private final ArrayList<Character> delimitadores = new ArrayList<>();
    private final ArrayList<Character> simbolos = new ArrayList<>();
    private final ArrayList<Character> letras = new ArrayList<>();
    private final ArrayList<Character> simboloInvalido = new ArrayList<>();

    public EstruturaLexica() {
        
        //adiciona palavras reservadas em uma lista
        palavrasReservadas.add("var");
        palavrasReservadas.add("const");
        palavrasReservadas.add("typedef");
        palavrasReservadas.add("struct");
        palavrasReservadas.add("extends");
        palavrasReservadas.add("procedure");
        palavrasReservadas.add("function");
        palavrasReservadas.add("start");
        palavrasReservadas.add("return");
        palavrasReservadas.add("if");
        palavrasReservadas.add("else");
        palavrasReservadas.add("then");
        palavrasReservadas.add("while");
        palavrasReservadas.add("read");
        palavrasReservadas.add("print");
        palavrasReservadas.add("int");
        palavrasReservadas.add("real");
        palavrasReservadas.add("boolean");
        palavrasReservadas.add("string");
        palavrasReservadas.add("true");
        palavrasReservadas.add("false");
        palavrasReservadas.add("global");
        palavrasReservadas.add("local");
        

        //adiciona operadores aritmeticos em uma lista
        opAritmeticos.add('+');
        opAritmeticos.add('-');
        opAritmeticos.add('*');
        
        //adiciona operadores relacionais em uma lista
        opRelacionais.add('<');
        opRelacionais.add('=');
        opRelacionais.add('>');
        
        //adiciona operadores lógicos em uma lista
        opLogicos.add('!');
        opLogicos.add('&');
        opLogicos.add('|');
        
        //adiciona delimitadores em uma lista
        delimitadores.add(';');
        delimitadores.add(',');
        delimitadores.add('(');
        delimitadores.add(')');
        delimitadores.add('[');
        delimitadores.add(']');
        delimitadores.add('{');
        delimitadores.add('}');
        delimitadores.add('.');
        
        //adiciona letras maiúsculas e minúsculas em uma lista
        for (char i = 'a'; i <= 'z'; i++){
            this.letras.add((char) i);
        }        
        for (char i = 'A'; i <= 'Z'; i++){
            this.letras.add((char) i);
        }
        //adiciona os simbolos
        for(int i = 1; i <= 254; i++){
            if(i >= 1 && i <= 31){
                this.simboloInvalido.add((char) i);
            }else if(i >= 127 && i <= 254){
                this.simboloInvalido.add((char) i);
            }else{
                this.simbolos.add((char) i);
            }
        }
        
    }
    /**
     * Método para verificar se uma string é uma palavra reservada. Usa as palavras reservadas que estão cadastradas em uma lista.
     * @param string
     * @return 
     */
    public boolean verificarPalavrasReservada(String string){
        return this.palavrasReservadas.contains(string);
    }
    /**
     * Método para verificar se um caractere é um operador. Usa os operadores cadastrados em uma lista.
     * @param caractere
     * @return 
     */
    public boolean verificarOperador(char caractere){
        return (this.opAritmeticos.contains(caractere) || this.opRelacionais.contains(caractere) || this.opLogicos.contains(caractere));
    }
    /**
     * Método para verificar se um caractere é um delimitador. Usa os delimitadores cadastrados em uma lista.
     * @param caractere
     * @return 
     */
    public boolean verificarDelimitador(char caractere){
        return this.delimitadores.contains(caractere);
    }
    /**
     * Método para verificar se um caractere é uma letra. Usa as letras cadastradas em uma lista.
     * @param caractere
     * @return 
     */
    public boolean verificarLetra(char caractere){
        return this.letras.contains(caractere);
    }
    /**
     * Método para verificar se um caractere é uma símbolo. Usa os símbolos cadastrados em uma lista.
     * @param caractere
     * @return 
     */
    public boolean verificarSimbolo(char caractere){
        return this.simbolos.contains(caractere);
    }    
    /**
     * Método para verificar se um caractere é uma símbolo inválido. Usa os símbolos inválidos que estão cadastrados em uma lista.
     * @param caractere
     * @return 
     */
    public boolean verificarSimboloInvalido(char caractere){
        return this.simboloInvalido.contains(caractere);
    }
    /**
     * Método para verificar se um caractere é um espaço. Usa uma função ou compara com o valor 9 da tabela ASCII.
     * @param caractere
     * @return 
     */
    public boolean verificarEspaco(char caractere){
        return (Character.isSpaceChar(caractere) || caractere == 9 || caractere == 32);
    }    
}
