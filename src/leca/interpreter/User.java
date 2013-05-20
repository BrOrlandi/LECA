package leca.interpreter;
/**
 * @package leca.interpreter
 * Classes que trabalham na interpretação do conteúdo.
 */
import java.util.ArrayList;

import leca.modelo.Question;
import leca.modelo.QuestionList;
import leca.xmlconverter.UserConverter;
import leca.xmlconverter.UserQuestionConverter;
import leca.xmlconverter.UserQuestionListConverter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Classe que representa um usuário que irá responder as questões de uma lista.
 * 
 *
 */
public class User {
	public static final int ACERTOU = 2; ///< O usuário recebe feedback que Acertou a questão.
	public static final int ERROU = 1; ///< O usuário recebe feedback que Errou a questão.
	public static final int SEM_FEEDBACK = 0; ///< O usuário não recebe feedback quanto acerto ou erro da resposta.
	
	
	
	private String Nome; ///< Nome do Usuário que responde a lista de questões.
	private int[] Respostas; ///< Um vetor que armazena qual alternativa foi assinalada em cada questão.
	private int UltimaQuestao; ///< Ultima questão que o usuário estava respondendo, pois ao carregar um usuário ele continuará a partir desta questão.
//	private int Tentativas; ///< Número de tentativas que tentou até responder corretamente.
	public QuestionList QL; ///< Lista de questões que o usuário está respondendo.

	private int Acertos; ///< Número de questões que o usuário acertou.
	private boolean[] RespostasCorretas; ///< Um vetor em que cada posição representa um questão respondida corretamente(true) ou não(false).
	private int nRespondidas; ///< Número de questões respondidas. É usado para verificar quando o usuário respondeu todas as questões.
	
	
//	public int getTentativas() {
//		return Tentativas;
//	}
//
//	public void setTentativas(int tentativas) {
//		Tentativas = tentativas;
//	}

	/**
	 * Cria um novo usuário para responder uma lista de questões.
	 * A lista de questões e as alternativas das questões são permutadas aqui.
	 * @param nome Nome do usuário que irá responder as questões.
	 * @param qL Lista de questões que ele irá responder.
	 * @warning Deve ser usado este construtor ao iniciar uma nova interpretação de questão.
	 */
	public User(String nome, QuestionList qL) {
		Nome = nome;
		QL = qL;
		Respostas = new int[QL.size()];
		RespostasCorretas = new boolean[QL.size()];
		UltimaQuestao = -1;
		Acertos = 0;
		QL.permutarQuestoes();
		nRespondidas = 0;
//		Tentativas = 0;
	}	
	
	/**
	 * Cria um usuário já existente, que já estava respondendo uma lista de questões.
	 * Usado pelo conversor ao carregar um XML.
	 * @param nome Nome do usuário
	 * @param ultimaQuestao Ultima questão que o usuário estava respondendo.
	 * @param respostas vetor com as respostas feitas pelo usuário.
	 * @param nrespondidas número de questões respondidas
//	 * @param tentativas número de tentativas de responder questões
	 */
	public User(String nome, int ultimaQuestao, int[] respostas) {
		Nome = nome;
		UltimaQuestao = ultimaQuestao;
		QL = null;
		Acertos = 0;
		Respostas = respostas;
		RespostasCorretas = new boolean[respostas.length];
		nRespondidas = 0;
//		Tentativas = tentativas;
	}
	
	/**
	 * @return numero de Acertos
	 * @sa Acertos
	 */
	public int getAcertos(){
		return this.Acertos;
	}
	

	/**
	 * Chama o método de responder questão pela posição na lista de questões.
	 * @param questionPos posição da questão na lista de questões
	 * @param alternativaPos posição da alternativa na lista de alternativas
	 * @return um feedback ou não de acordo com o método responderQuestao
	 */
	public int responderQuestao(int questionPos, int alternativaPos){
		Question q = QL.getQuestion(questionPos);
		return responderQuestao(q, alternativaPos);
	}
	
	/**
	 * Responde uma questão verificando se esta questão não tinha sido respondida antes, ou se a resposta atual é diferente da anterior.
	 * No primeiro caso verifica se acertou, aumentando os acertos, e aumenta o numero de questões respondidas.
	 * No segundo caso verifica se a nova resposta é correta ou é errada em relação a anterior, diminuindo ou aumentando os acertos.
	 * No caso de erro ou acerto, é verificado qual o feedback deve retornar ao usuário de acordo com o modo de avaliação.
	 * Se o usuário responder com a mesma resposta anterior, o retorna será SEM_FEEDBACK independente do modo de avaliação.
	 * @param q questão que está sendo respondida.
	 * @param alternativaPos posição da alternativa na lista de alternativas
	 * @return User.ACERTOU ou User.ERROU ou User.SEM_FEEDBACK
	 * @see ACERTOU ERROU SEM_FEEDBACK
	 */
	public int responderQuestao(Question q, int alternativaPos){
		//Respostas são guardadas com incremento no vetor de respostas. Sendo assim 0 é nenhuma assinalada e 1 para frente é a alternativa assinalada.
		boolean ret = false;
		int aval = QL.getAvaliacao();
		if(Respostas[q.ID] == 0) // ainda não tinha sido respondida
		{
			ret = q.verificarResposta(alternativaPos); // verifica resposta
			if(ret)
			{
				Acertos++; // incrementa acertos se acertou
				RespostasCorretas[q.ID] = true;
			}
			Respostas[q.ID] = alternativaPos+1; // salva a resposta 
			nRespondidas++; // incrementa o numero de questões respondidas
			if(aval == QuestionList.AVALIACAO_IMEDIATA) // dá o feedback em tempo real
			{
				//Tentativas++;
				q.incrementaTentativas();
				if(ret) // se não tinha acertado e agora acertou
				{
					return ACERTOU; 
				}
				else
				{
					return ERROU;
				}
			}
			else
			{
				return SEM_FEEDBACK;
			}
		}
		else
		{
			if(Respostas[q.ID] != alternativaPos+1) // se não assinalou a mesma resposta, a resposta atual é diferente da anterior
			{
				boolean before = RespostasCorretas[q.ID]; // before é true se tinha respondido correto
				ret = q.verificarResposta(alternativaPos); // verificar a resposta
				if(ret) // se acertou agora
				{
					Acertos++;
					RespostasCorretas[q.ID] = true;
				}
				else if(before) // senão: verifica a resposta anterior
				{
					Acertos--; //se ele tinha acertado anteriormente decrementa os acertos, pois ao mudar de alternativa, errou.
					RespostasCorretas[q.ID] = false;
				}
				Respostas[q.ID] = alternativaPos+1; // salva nova alternativa assinalada
				if(aval == QuestionList.AVALIACAO_IMEDIATA) // dá o feedback em tempo real
				{
					//Tentativas++;
					q.incrementaTentativas();
					if(ret) // se não tinha acertado e agora acertou
					{
						return ACERTOU; 
					}
					else
					{
						return ERROU;
					}
				}
			}
		}
		return SEM_FEEDBACK;
	}
	
//	/**
//	 * Deve ser usado quando o usuário responde uma questão.
//	 * Este método verificará a influencia da nova resposta no número de acertos.
//	 * @param questionPos posição da questão que foi respondida na lista de questões.
//	 * @param resposta posição da alternativa na lista de alternativas da questão.
//	 * @return Boolean Se acertou ou não.
//	 */
	/*
	//METODO ANTIGO
	public boolean respondeuQuestao(int questionPos, int resposta){
		Question q = QL.ListaQuestoes.get(questionPos);
		return respondeuQuestao(q, resposta);
	}
	*/
//	/**
//	 * Deve ser usado quando o usuário responde uma questão.
//	 * Este método verificará a influencia da nova resposta no número de acertos.
//	 * @param q Questão que foi respondida.
//	 * @param resposta posição da alternativa na lista de alternativas da questão.
//	 * @return Boolean Se acertou ou não.
//	 */
	//TODO nova funcionalidade do método.
	/*
	//METODO ANTIGO
	public boolean respondeuQuestao(Question q, int resposta){
		boolean ret = false;
		if(Respostas[q.ID] == 0) // ainda não tinha sido respondida
		{
			ret = q.verificarResposta(resposta); // verifica resposta
			if(ret)
			{
				Acertos++; // incrementa acertos se acertou
				RespostasCorretas[q.ID] = true;
			}
			Respostas[q.ID] = resposta+1; // salva a resposta 
			nRespondidas++; // incrementa o numero de questões respondidas
			Tentativas++;
		}
		else
		{
			if(Respostas[q.ID] != resposta+1) // se não assinalou a mesma resposta
			{
				ret = q.verificarResposta(resposta); // verificar a resposta
				if(ret)
				{
					Acertos++; // se acertou
					RespostasCorretas[q.ID] = true;
				}
				else if(q.verificarResposta(Respostas[q.ID]-1)) // se não: verifica a resposta anterior
				{
					Acertos--; //se ele tinha acertado anteriormente decrementa os acertos, pois ao mudar de alternativa, errou.
					RespostasCorretas[q.ID] = false;
				}
				Respostas[q.ID] = resposta+1; // salva nova alternativa assinalada
			}
		}
		return ret;
	}*/
	
	/**
	 * Pega a posição da alternativa que foi assinalada de uma questão na lista de questões.
	 * @param questionId o ID da questão na lista.
	 * @return a posição da alternativa assinalada na lista de alternativas.
	 */
	public int getResposta(int questionId){
		return Respostas[questionId]-1;
	}
	
	/**
	 * Pega todas as respostas assinaladas.
	 * @return o vetor com a resposta assinalada de cada questão.
	 */
	public int[] getRespostas(){
		return Respostas;
	}
	
	/**
	 * Pega o vetor booleano de respostas corretas ou erradas.
	 * Pode ser usado para mostrar para o usuário quais respostas estão corretas e quais estão erradas.
	 * @return o vetor booleano de respostas corretas.
	 */
	public boolean[] getRespostasCorretas(){
		return RespostasCorretas;
	}
	
	/**
	 * É usado pelo ao carregar um usuário do XML.
	 * Gera o número de acertos do usuário a partir das questões e do vetor de respostas.
	 * Gera também o vetor de respostas corretas.
	 * Pois estes dados não são salvos no XML.
	 */
	public void carregarAcertos(){
		ArrayList<Question> ql = QL.getArrayListQuestion();
		//int i = 0;
		for(Question q : ql)
		{
			if(Respostas[q.ID] != 0)
			{
				nRespondidas++;
				if(q.verificarResposta(Respostas[q.ID]-1))
				{
					Acertos++;
					RespostasCorretas[q.ID] = true;
				}
			}
		}
	}

	/**
	 * É usado pelo conversor de Usuário ao carregá-lo do XML.
	 * Chama o método carregarAcertos.
	 * @param qL a lista de questões para setar.
	 * @see carregarAcertos
	 */
	public void setLista(QuestionList qL) {
		QL = qL;
		this.carregarAcertos();
	}

	/**
	 * @return O nome do usuário
	 */
	public String getNome() {
		return Nome;
	}
	/**
	 * @param nome nome do usuário
	 */
	public void setNome(String nome) {
		Nome = nome;
	}
	/**
	 * @return a posição na lista da ultima questão que se estava respondendo
	 */
	public int getUltimaQuestao() {
		return UltimaQuestao;
	}
	/**
	 * @param ultimaQuestao Seta qual é a ultima questão que está respondendo
	 */
	public void setUltimaQuestao(int ultimaQuestao) {
		UltimaQuestao = ultimaQuestao;
	}
	
	/**
	 * Converte o Usuário para XML.
	 * @return String do XML
	 */
	public String toXML() {
		
		XStream xStream = new XStream(new DomDriver());
		xStream.registerConverter(new UserConverter());
		xStream.registerConverter(new UserQuestionListConverter());
		xStream.registerConverter(new UserQuestionConverter());
		xStream.alias("ListaQuestoes", QuestionList.class);
		xStream.alias("Usuario", User.class);
		
		String xml = null;
		try{
			xml = xStream.toXML(this); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return xml;
	}
	
	/**
	 * Converte um XML para Usuário
	 * @param toXML String do XML
	 * @return O objeto do Usuário
	 */
	public static User fromXML(String toXML) {
		User usr = null;
		
		XStream xStream = new XStream(new DomDriver());
		xStream.registerConverter(new UserConverter());
		xStream.registerConverter(new UserQuestionListConverter());
		xStream.registerConverter(new UserQuestionConverter());
		xStream.alias("ListaQuestoes", QuestionList.class);
		xStream.alias("Usuario", User.class);
		
		Object obj = null;
		
		try{
			obj = xStream.fromXML(toXML);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		if(obj != null && obj instanceof User)
		{
			usr = (User) obj;
		}
		return usr;
	}
	
	/**
	 * Verifica se todas as questões foram respondidas.
	 * @return true se todas foram respondidas.
	 */
	public boolean isCompletelyAnswered(){
		if(nRespondidas == QL.size()){
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @return número de questões respondidas.
	 */
	public int getnRespondidas() {
		return nRespondidas;
	}
	
	/**
	 * Pega uma questão da lista.
	 * Atribui a esta questão como a ultima acessada. Util usar este método em vez de chamar direto da lista.
	 * @param pos posição da questão na lista.
	 * @return a questão da lista.
	 */
	public Question getQuestion(int pos){
		Question q = QL.getQuestion(pos);
		UltimaQuestao = pos;
		return q;
	}

	/*
	public int[] getTentativas() {
		int size = QL.size();
		int[] tentativas = new int [size];
		for(int i=0;)
		return ;
	}
	*/
}
