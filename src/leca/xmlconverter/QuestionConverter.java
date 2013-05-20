package leca.xmlconverter;

import java.util.ArrayList;

import leca.exceptions.IncompleteDataException;
import leca.modelo.Alternative;
import leca.modelo.Question;
import leca.util.Utilities;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Conversor de Objeto-XML para a classe de Questao.
 * Também converte as alternativas.
 *
 */
public class QuestionConverter implements Converter {
	@SuppressWarnings("rawtypes")
	public boolean canConvert( Class clazz) {
		return clazz.equals(Question.class);
	}

	@Override
	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {
			Question q = (Question) value;
			writer.addAttribute("Titulo", q.Titulo);
			writer.startNode("Tema");
			writer.setValue(q.Tema);
			writer.endNode();
			writer.startNode("Autor");
			writer.setValue(q.Autor);
			writer.endNode();
			writer.startNode("Enunciado");
			writer.setValue(q.Enunciado);
			writer.endNode();
			writer.startNode("Correta");
			writer.setValue(Integer.toString(Utilities.encrypt(q.Enunciado,q.getCorreta())));
			writer.endNode();
			writer.startNode("Permutar");
			if(q.getPermuta())
			{
				writer.setValue("Sim");
			}
			else
			{
				writer.setValue("Nao");
			}
			writer.endNode();
			ArrayList<Alternative> al = q.Alternativas.getAlternatives();
			for(Alternative a : al)
			{
				writer.startNode("Alternativa");
				writer.setValue(a.Text);
                writer.endNode();
			}
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		Question q = new Question(reader.getAttribute("Titulo"));
		reader.moveDown();
		q.Tema = reader.getValue();
		reader.moveUp();
		reader.moveDown();
		q.Autor = reader.getValue();
		reader.moveUp();
		reader.moveDown();
		q.Enunciado = reader.getValue();
		reader.moveUp();
		reader.moveDown();
		int Correta = Integer.parseInt(reader.getValue());
		reader.moveUp();
		reader.moveDown();
		if(reader.getValue().equals("Sim"))
		{
			q.setPermuta(true);
		}
		else
		{
			q.setPermuta(false);
		}
		reader.moveUp();
		int idset = 0;
		try {
		while(reader.hasMoreChildren()){
			reader.moveDown();
			if("Alternativa".equals(reader.getNodeName()))
			{
				q.Alternativas.addAlternative(idset++, reader.getValue());
			}
			reader.moveUp();
		}
		} catch (IncompleteDataException e) {
			//não deve entrar neste catch pois está carregando conteudo já existente.
		}
		//set a correta depois pois não tem alternativas antes para setar, causando IndexOutOfBoundsException
		q.setCorreta(Utilities.decrypt(q.Enunciado,Correta));
		
		return q;
	}

}