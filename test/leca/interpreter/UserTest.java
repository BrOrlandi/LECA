package leca.interpreter;

import java.util.ArrayList;

import junit.framework.TestCase;
import leca.modelo.Alternative;
import leca.modelo.Question;
import leca.modelo.QuestionList;
import leca.util.Utilities;

public class UserTest extends TestCase {

	//Respostas metade certa:
	static int resps[] = {1,2,3,3,4,0,1,0,0,0,0,0};
	static int respsPos[] = new int[12];
	static int acertosEsp = 0;
	static int qids[] = new int[12];
	
	
	public void testUserStringQuestionList() {
		User u = new User("Bruno",QuestionList.fromXML(Utilities.getStringFromFile("ListaC.xml")));
		System.out.println("\n"+u.getNome()+"\n\n"+u.QL.Titulo+"\n"+u.QL.Autor+"\n"+u.QL.Tema+"\n"+u.QL.Obs+"\n");
		assertEquals(0,u.getAcertos());
		
		u.QL.setAvaliacao(QuestionList.AVALIACAO_IMEDIATA);
		//u.QL.setAvaliacao(QuestionList.AVALIACAO_FINAL);
		Question q = null;
		int altID = -1;
		for(int i=0;i<6;i++){
			q = u.getQuestion(i);
			altID = resps[q.ID];
			ArrayList<Alternative> al = q.Alternativas.getAlternatives();
			int j=0,altPos=-1;
			for(Alternative a : al){
				if(a.getId() == altID){
					altPos = j;
					respsPos[q.ID] = j;
					break;
				}
				j++;
			}
			int ret = u.responderQuestao(q, altPos);
			System.out.println("Respondendo: \""+q.toString()+"\" com "+altPos+")"+al.get(altPos).Text);
			if(q.ID < 6){
				assertEquals(User.ACERTOU,ret);
				acertosEsp++;
			}
			else
			{
				assertEquals(User.ERROU,ret);
			}
			assertEquals(1, q.getTentativas());
		}

		assertEquals(6,u.getnRespondidas());
		assertEquals(acertosEsp,u.getAcertos());
//		assertEquals(6,u.getTentativas());
		assertEquals(5,u.getUltimaQuestao());
		assertEquals(false,u.isCompletelyAnswered());
		Utilities.saveStringToFile("UserTests.xml", u.toXML());
		System.out.println("================================================");
		u.carregarAcertos();
		ArrayList<Question> ql = u.QL.getArrayListQuestion();
		int x=0;
		for(Question q2 : ql){
			qids[x++] = q2.ID;
		}
	}

	public void testUserStringIntIntArrayInt() {
		System.out.println("================================================");
		User u = User.fromXML(Utilities.getStringFromFile("UserTests.xml"));
		System.out.println("\n"+u.getNome()+"\n\n"+u.QL.Titulo+"\n"+u.QL.Autor+"\n"+u.QL.Tema+"\n"+u.QL.Obs+"\n");
		assertEquals(6,u.getnRespondidas());
		assertEquals(acertosEsp,u.getAcertos());
//		assertEquals(6,u.getTentativas());
		assertEquals(5,u.getUltimaQuestao());
		assertEquals(false,u.isCompletelyAnswered());
		assertEquals(QuestionList.AVALIACAO_IMEDIATA,u.QL.getAvaliacao());

		Question q = null;
		int altID = -1;
		for(int i=u.getUltimaQuestao()+1;i<u.QL.size();i++){
			q = u.getQuestion(i);
			altID = resps[q.ID];
			ArrayList<Alternative> al = q.Alternativas.getAlternatives();
			int j=0,altPos=-1;
			for(Alternative a : al){
				if(a.getId() == altID){
					altPos = j;
					respsPos[q.ID] = j;
					break;
				}
				j++;
			}
			int ret = u.responderQuestao(i, altPos);
			System.out.println("Respondendo: \""+q.toString()+"\" com "+altPos+")"+al.get(altPos).Text);
			if(q.ID < 6){
				assertEquals(User.ACERTOU,ret);
			}
			else
			{
				assertEquals(User.ERROU,ret);
			}
			assertEquals(1, q.getTentativas());
		}
		
		int respondeu[] = u.getRespostas();
		boolean cer[] = u.getRespostasCorretas();
		for(int j=0;j<respondeu.length;j++){
			assertEquals(respsPos[j],respondeu[j]-1);
			if(j<6){
				assertEquals(true,cer[j]);
			}
			else{
				assertEquals(false,cer[j]);
			}
		}
		
		assertEquals(true,u.isCompletelyAnswered());
		assertEquals(12,u.getnRespondidas());
		assertEquals(6,u.getAcertos());
//		assertEquals(12,u.getTentativas());
		
		int tentativas = 0;
		for(int i=0; i<12; i++){
			q = u.getQuestion(i);
			tentativas += q.getTentativas();
		}
		
		assertEquals(12,tentativas);
		
		assertEquals(11,u.getUltimaQuestao());
		Utilities.saveStringToFile("UserTestsF.xml", u.toXML());
	}

}
