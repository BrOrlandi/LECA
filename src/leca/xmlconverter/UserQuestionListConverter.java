package leca.xmlconverter;

import java.util.ArrayList;

import leca.modelo.Question;
import leca.modelo.QuestionList;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
/**
 * Conversor de Objeto-XML para a classe de Lista de Questoes.
 * Chama o conversor de Questoes para cada questao na lista.
 *
 */
public class UserQuestionListConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert( Class clazz) {
		return clazz.equals(QuestionList.class);
	}

	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		QuestionList ql = (QuestionList) value;
		writer.addAttribute("Titulo", ql.Titulo);
		writer.startNode("Tema");
		writer.setValue(ql.Tema);
		writer.endNode();
		writer.startNode("Autor");
		writer.setValue(ql.Autor);
		writer.endNode();
		writer.startNode("Data");
		writer.setValue(ql.Data);
		writer.endNode();
		writer.startNode("Obs");
		writer.setValue(ql.Obs);
		writer.endNode();
		writer.startNode("Idioma");
		writer.setValue(ql.Idioma);
		writer.endNode();
		/*
		writer.startNode("VersaoPlataforma");
		writer.setValue(Integer.toString(ql.VersaoPlataforma));
		writer.endNode();
		*/
		writer.startNode("Avaliacao");
		writer.setValue(Integer.toString(ql.getAvaliacao()));
		writer.endNode();
		
		ArrayList<Question> al = ql.getArrayListQuestion();
		for(Question q : al)
		{
			writer.startNode("Questao");
			context.convertAnother(q);
			writer.endNode();
		}
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		QuestionList ql = new QuestionList();
		ql.Titulo = reader.getAttribute("Titulo");
		reader.moveDown();
		ql.Tema = reader.getValue();
		reader.moveUp();
		reader.moveDown();
		ql.Autor = reader.getValue();
		reader.moveUp();
		reader.moveDown();
		ql.Data = reader.getValue();
		reader.moveUp();
		reader.moveDown();
		ql.Obs = reader.getValue();
		reader.moveUp();
		reader.moveDown();
		ql.Idioma = reader.getValue();
		reader.moveUp();
		/*
		reader.moveDown();
		ql.VersaoPlataforma = Integer.parseInt(reader.getValue());
		reader.moveUp();
		*/
		reader.moveDown();
		ql.setAvaliacao(Integer.parseInt(reader.getValue()));
		reader.moveUp();
		while(reader.hasMoreChildren()){
			reader.moveDown();
			if("Questao".equals(reader.getNodeName()))
			{
				Question q = (Question) context.convertAnother(ql, Question.class);
				ql.addLista(q);
			}
			reader.moveUp();
		}
		return ql;
	}

}
