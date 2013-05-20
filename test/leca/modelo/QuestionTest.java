package leca.modelo;

import java.util.ArrayList;

import junit.framework.TestCase;
import leca.exceptions.IncompleteDataException;
import leca.exceptions.MinimumAlternativesException;
import leca.exceptions.NoCorrectAnswerException;
import leca.util.Utilities;

public class QuestionTest extends TestCase {

	public void test1() throws IncompleteDataException{
		Question q = new Question("title test");
		q.Autor = "Bruno";
		q.Enunciado = "";
		q.setPermuta(true);
		int flag1 = 0;

		try {
			q.addAlternativa("");
		} catch (IncompleteDataException e1) {
			flag1 = 1;
		}
		
		assertEquals(1, flag1);

		q.addAlternativa("Alternativa Remove 3");
		q.addAlternativa("Alternativa Correta");
		q.addAlternativa("Alternativa teste 1");
		q.addAlternativa("Alternativa teste 2");
		q.addAlternativa("Alternativa Remove 1");
		q.addAlternativa("Alternativa teste 3");
		q.addAlternativa("Alternativa teste 4");
		q.addAlternativa("Alternativa Remove 2");
		q.setCorreta(1);
		
		q.Alternativas.removeAlternative(0);
		assertEquals(0, q.getCorreta());
		q.Alternativas.moveUp(1);
		q.Alternativas.moveUp(2);
		assertEquals(2, q.getCorreta());
		q.Alternativas.moveDown(1);
		assertEquals(1, q.getCorreta());
		q.Alternativas.removeAlternative(3);
		q.Alternativas.removeAlternative(5);
		
		
		q.Alternativas.removeAlternative(1);
		assertEquals(-1, q.getCorreta());
		assertEquals(4, q.Alternativas.size());
		q.addAlternativa("Alternativa correta nova");
		ArrayList<String> a = q.Alternativas.getArrayListString();
		for(String s : a){
			System.out.println(s);
		}
		System.out.println();
		q.setCorreta(4);
		q.Alternativas.moveUp(4);
		a = q.Alternativas.getArrayListString();
		for(String s : a){
			System.out.println(s);
		}
		System.out.println();
		assertEquals(5, q.size());
		
		
		int flag2 = 0;
		try {
			q.verificarQuestao();
		} catch (MinimumAlternativesException e) {
			flag2 = 1;
		} catch (NoCorrectAnswerException e) {
			flag2 = 2;
		} catch (IncompleteDataException e1) {
			flag2 = 3;
		}
		assertEquals(3, flag2);
		q.Enunciado = "Enunciado teste";
		
		
		//salvar em arquivo.
		try {
			Utilities.saveStringToFile("QuestionTest.xml", q.toXML());
		} catch (MinimumAlternativesException e2) {
			e2.printStackTrace();
		} catch (NoCorrectAnswerException e2) {
			e2.printStackTrace();
		}
		Question qc = new Question(q);
		
		
		q.Alternativas.removeAlternative(q.getCorreta());
		flag2 = 0;
		try {
			q.verificarQuestao();
		} catch (MinimumAlternativesException e) {
			flag2 = 1;
		} catch (NoCorrectAnswerException e) {
			flag2 = 2;
		} catch (IncompleteDataException e1) {
			flag2 = 3;
		}
		assertEquals(2, flag2);
		flag2 = 0;
		q.setCorreta(0);


		try {
			q.verificarQuestao();
		} catch (MinimumAlternativesException e) {
			flag2 = 1;
		} catch (NoCorrectAnswerException e) {
			flag2 = 2;
		} catch (IncompleteDataException e1) {
			flag2 = 3;
		}
		assertEquals(0, flag2);
		
		q.Alternativas.removeAlternative(0);
		q.Alternativas.removeAlternative(0);
		q.Alternativas.removeAlternative(0);

		try {
			q.verificarQuestao();
		} catch (MinimumAlternativesException e) {
			flag2 = 1;
		} catch (NoCorrectAnswerException e) {
			flag2 = 2;
		} catch (IncompleteDataException e1) {
			flag2 = 3;
		}
		assertEquals(1, flag2);
		
		//------------------------------------------
		Question w = Question.fromXML(Utilities.getStringFromFile("QuestionTest.xml"));
		assertEquals(qc.Enunciado,w.Enunciado);
		assertEquals(qc.getCorreta(),w.getCorreta());
		assertEquals(qc.size(),w.size());
		ArrayList<String> qca = qc.getArrayListStringAlternatives();
		ArrayList<String> wa = w.getArrayListStringAlternatives();
		System.out.println("=====================================");
		int i=0;
		for(String s : qca)
		{
			assertEquals(s,wa.get(i));
			boolean x = w.verificarResposta(i);
			System.out.print(s);
			if(x){
				System.out.println("*");
			}else
			{
				System.out.println();
			}
			i++;
		}
		assertEquals(true,w.verificarResposta(w.getCorreta()));
		assertEquals(true,w.verificarResposta(qc.getCorreta()));
		System.out.println("=====================================");
		w.permutarAlternativas();
		wa = w.getArrayListStringAlternatives();
		int j=0,correta=-1;
		for(String s : wa)
		{
			boolean x = w.verificarResposta(j);
			System.out.print(s);
			if(x){
				System.out.println("*");
				correta = j;
			}else
			{
				System.out.println();
			}
			j++;
		}
		assertEquals(true,w.verificarResposta(correta));
	}
}
