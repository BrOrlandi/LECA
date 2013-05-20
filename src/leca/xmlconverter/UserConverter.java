package leca.xmlconverter;

import leca.interpreter.User;
import leca.modelo.QuestionList;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
/**
 * Conversor de Objeto-XML para a classe de Usuário.
 * Nao chama os outros conversores, pois armazenará todo o estado da lista de questoes, incluindo os IDs definidos.
 */
public class UserConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert( Class clazz) {
		return clazz.equals(User.class);
	}

	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		User usr = (User) value;
		int rep[] = usr.getRespostas();
		writer.addAttribute("Nome", usr.getNome());
		writer.startNode("tamanhoR");
		writer.setValue(Integer.toString(rep.length));
		writer.endNode();
//		writer.startNode("Tentativas");
//		writer.setValue(Integer.toString(usr.getTentativas()));
//		writer.endNode();
		writer.startNode("UltimaQuestao");
		writer.setValue(Integer.toString(usr.getUltimaQuestao()));
		writer.endNode();
		for(int x : rep)
		{
			writer.startNode("R");
			writer.setValue(Integer.toString(x));
			writer.endNode();
		}
		
		writer.startNode("ListaQuestoes");
		context.convertAnother(usr.QL);
		writer.endNode();
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		String nome;
		int tam;
//		int tentativas;
		int ultima;
		nome = reader.getAttribute("Nome");
		reader.moveDown();
		tam = Integer.parseInt(reader.getValue());
		reader.moveUp();
//		reader.moveDown();
//		tentativas = Integer.parseInt(reader.getValue());
//		reader.moveUp();
		reader.moveDown();
		ultima = Integer.parseInt(reader.getValue());
		reader.moveUp();
		
		int[] respostas = new int[tam];
		for(int i=0;i<tam;i++)
		{
			reader.moveDown();
			respostas[i] = Integer.parseInt(reader.getValue());
			reader.moveUp();
		}
		
//		User usr = new User(nome, ultima, respostas, tentativas);
		User usr = new User(nome, ultima, respostas);
		
		reader.moveDown();
		QuestionList ql = (QuestionList) context.convertAnother(usr, QuestionList.class);
		reader.moveUp();
		usr.setLista(ql);
		return usr;
	}

}
