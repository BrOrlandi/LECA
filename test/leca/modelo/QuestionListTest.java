package leca.modelo;

import java.util.ArrayList;

import junit.framework.TestCase;
import leca.exceptions.IncompleteDataException;
import leca.exceptions.MinimumQuestionsException;
import leca.util.Utilities;

public class QuestionListTest extends TestCase {

	public void testQuestionList() {
		QuestionList ql = new QuestionList();
		ql.Titulo = "Lista de exercicios de C";
		ql.Autor = "Bruno Orlandi";
		ql.Idioma = "Português - Brasil";
		ql.Obs = "Para iniciantes em linguagem C";
		ql.setPermuta(true);
		ql.setAvaliacao(QuestionList.AVALIACAO_IMEDIATA);
		
		int flag = 0;
		try {
			ql.verificarListaQuestao();
		} catch (IncompleteDataException e) {
			flag = 1;
		} catch (MinimumQuestionsException e) {
			flag = 2;
		}
		assertEquals(1,flag);
		
		ql.Tema = "Programação/Computação";
		flag = 0;
		try {
			ql.verificarListaQuestao();
		} catch (IncompleteDataException e) {
			flag = 1;
		} catch (MinimumQuestionsException e) {
			flag = 2;
		}
		assertEquals(2,flag);
		
		
		try{
		Question q1 = new Question();
		q1.Titulo = "Bytes para um int";
		q1.Tema = "Técnico";
		q1.Enunciado = "Quantos bytes são necessários para armazenar um integer?";
		q1.addAlternativa("1 byte");
		q1.addAlternativa("2 bytes");
		q1.addAlternativa("4 bytes");
		q1.addAlternativa("8 bytes");
		q1.setCorreta(2);
		q1.setPermuta(false);
		ql.addLista(q1);

		Question q2 = new Question();
		q2.Titulo = "Vetor estático";
		q2.Tema = "Básico";
		q2.Enunciado = "Como se declara um vetor estático em C?";
		q2.addAlternativa("int vetor;");
		q2.addAlternativa("int vetor[MAX];");
		q2.addAlternativa("int *vetor;");
		q2.addAlternativa("int []vetor;");
		q2.setCorreta(1);
		q2.setPermuta(true);
		ql.addLista(q2);
		
		Question q3 = new Question();
		q3.Titulo = "String";
		q3.Tema = "Básico";
		q3.Enunciado = "Como declarar uma string alocada em C?";
		q3.addAlternativa("String str;");
		q3.addAlternativa("char str;");
		q3.addAlternativa("char *str;");
		q3.addAlternativa("char str[MAX];");
		q3.setCorreta(3);
		q3.setPermuta(true);
		ql.addLista(q3);
		
		Question q4 = new Question();
		q4.Titulo = "Espaço Alocado";
		q4.Tema = "Básico";
		q4.Enunciado = "Qual destes não podem variar no espaço alocado?";
		q4.addAlternativa("struct estrutura;");
		q4.addAlternativa("struct *estrutura;");
		q4.addAlternativa("int *a;");
		q4.addAlternativa("int a;");
		q4.addAlternativa("int **matriz;");
		q4.addAlternativa("void *p;");
		q4.setCorreta(3);
		q4.setPermuta(true);
		ql.addLista(q4);
		
		Question q5 = new Question();
		q5.Titulo = "Funções";
		q5.Tema = "Teorico";
		q5.Enunciado = "O que é uma função em C?";
		q5.addAlternativa("Parte de um programa.");
		q5.addAlternativa("Um bloco de código que pode ser utilizado diversas vezes na execução de um programa.");
		q5.addAlternativa("Uma estrutura da linguagem C que pode ser utilizada para que um programa fique mais organizado.");
		q5.addAlternativa("Um bloco de código que pode receber parâmetros, processá-los e retornar alguma coisa.");
		q5.addAlternativa("Todas opções acima.");
		q5.setCorreta(4);
		q5.setPermuta(false);
		ql.addLista(q5);

		Question q6 = new Question();
		q6.Tema = "Técnico";
		q6.Enunciado = "A linguagem C tem este nome porque foi a sucessora da linguagem B.";
		q6.addAlternativa("Verdadeiro");
		q6.addAlternativa("Falso.");
		q6.setCorreta(0);
		q6.setPermuta(false);
		ql.addLista(q6);

		Question q7 = new Question();
		q7.Tema = "Teorico";
		q7.Enunciado = "A instrução #include <stdio.h>  no programa é colocada para que possamos utilizar as funções scanf e printf";
		q7.addAlternativa("Verdadeiro");
		q7.addAlternativa("Falso.");
		q7.setCorreta(0);
		q7.setPermuta(false);
		ql.addLista(q7);

		Question q8 = new Question();
		q8.Tema = "Basico";
		q8.Enunciado = "Sendo i uma variável inteira, a seguinte chamada a scanf é válida:  scanf(\"%d\", i);";
		q8.addAlternativa("Verdadeiro");
		q8.addAlternativa("Falso.");
		q8.setCorreta(1);
		q8.setPermuta(false);
		ql.addLista(q8);

		Question q = new Question();
		q.Tema = "Técnico";
		q.Enunciado = "Qual dessas linguagens NÃO possui relação de influência com a linguagem C?";
		q.addAlternativa("C++");
		q.addAlternativa("Pascal");
		q.addAlternativa("Java");
		q.addAlternativa("PHP");
		q.addAlternativa("D");
		q.setCorreta(1);
		q.setPermuta(true);
		ql.addLista(q);

		q = new Question();
		q.Tema = "Básico";
		q.Enunciado = "Qual dessas relações de operações matemáticas está incorreta em C?";
		q.addAlternativa("* Multiplicação");
		q.addAlternativa("% Resto da Divisão");
		q.addAlternativa("^ Potenciação");
		q.addAlternativa("/ Divisão");
		q.setCorreta(2);
		q.setPermuta(true);
		ql.addLista(q);

		q = new Question();
		q.Titulo = "Algoritmo";
		q.Tema = "Algoritmo";
		q.Enunciado = "De acordo com o algoritmo seguinte:\nint i,x=1;\nfor(i=10; i&gt;0;i--)\n{\n     x *= i;\n}\nO valor de x equivale a:";
		q.addAlternativa("0");
		q.addAlternativa("10");
		q.addAlternativa("10!");
		q.addAlternativa("9!");
		q.addAlternativa("10! - 1");
		q.setCorreta(2);
		q.setPermuta(true);
		ql.addLista(q);
		
		q = new Question();
		q.Titulo = "Algoritmo números primos";
		q.Tema = "Algoritmo";
		q.Enunciado = "Quantos números primos há de 1 à 101?";
		q.addAlternativa("30");
		q.addAlternativa("26");
		q.addAlternativa("17");
		q.addAlternativa("25");
		q.addAlternativa("23");
		q.setCorreta(1);
		q.setPermuta(true);
		ql.addLista(q);
		
		}
		catch(IncompleteDataException e){
			e.printStackTrace();
		}
		ql.moveUp(1);
		ql.moveDown(10);
		ql.setIds();
		assertEquals(12,ql.size());
		try {
			Utilities.saveStringToFile("ListaC.xml", ql.toXML());
		} catch (IncompleteDataException e) {
			e.printStackTrace();
		} catch (MinimumQuestionsException e) {
			e.printStackTrace();
		}
		
		
		ArrayList<String> al = ql.getArrayListStringQuestions();
		for(String s : al){
			System.out.println(s);
		}
		/*
		ql.moveUp(1);
		ql.moveDown(10);
		ql.removerQuestao(6);
		al = ql.getArrayListStringQuestions();
		for(String s : al){
			System.out.println(s);
		}
		*/
		
		QuestionList ql2 = QuestionList.fromXML(Utilities.getStringFromFile("ListaC.xml"));
		assertEquals(ql.Titulo,ql2.Titulo);
		assertEquals(ql.Tema,ql2.Tema);
		assertEquals(ql.Autor,ql2.Autor);
		assertEquals(ql.getAvaliacao(), ql2.getAvaliacao());
		assertEquals(ql.size(),ql2.size());
		ArrayList<String> al1 = ql.getArrayListStringQuestions();
		ArrayList<String> al2 = ql2.getArrayListStringQuestions();
		int i=0;
		for(String s : al1){
			assertEquals(s,al2.get(i++));
		}
		ArrayList<Question> alq1 = ql.getArrayListQuestion();
		ArrayList<Question> alq2 = ql2.getArrayListQuestion();
		i=0;
		for(Question q : alq1){
			assertEquals(q.ID,alq2.get(i++).ID);
		}
		
	}

}
//
