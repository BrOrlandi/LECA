package leca.modelo;

import java.util.ArrayList;

import leca.exceptions.IncompleteDataException;
import leca.exceptions.MinimumAlternativesException;
import leca.exceptions.NoCorrectAnswerException;
import leca.xmlconverter.QuestionConverter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
/**
 * Classe que representa uma Questão. São as questões a serem criadas e respondidas pelo usuário.
 */
public class Question {
	public String Titulo; ///< Titulo do enunciado da questão.
	public String Autor;  ///< Autor da questão.
	public String Enunciado; ///< Texto do enunciado. Obrigatório.
	
	public String Tema; ///< Tema da questão.
	
	public AlternativesList Alternativas;

	public int ID; /**< Identificador único da questão em uma lista de questões.
	* @warning O ID da questão, não é correspondente à sua posição na lista de questões após a permutação na interpretação.
	*/ 
	private boolean Permuta; ///< Permite que as alternativas sejam trocadas de ordem na interpretação.
	private int Tentativas = 0; ///< Número de tentativas que tentou até responder corretamente.
	/**
	 * 
	 * @param titulo Titulo da Questão
	 */
	public Question(String titulo) { 
		// construtor da classe com titulo da questão
		Titulo = titulo;
		Tema = "";
		Autor = "";
		Enunciado = "";
		Alternativas = new AlternativesList();
		ID = -1;
		Permuta = false;
	}
	
	public Question() { 
		// construtor da classe sem parametros
		Titulo = "";
		Tema = "";
		Autor = "";
		Enunciado = "";
		Alternativas = new AlternativesList();
		ID = -1;
		Permuta = false;
	}
	
	/**
	 * Construtor de cópia.
	 * @param q questão a ser copiada.
	 */
	public Question(Question q) { 
		Titulo = q.Titulo;
		Tema = q.Tema;
		Autor = q.Autor;
		Enunciado = q.Enunciado;
		Alternativas = new AlternativesList(q.Alternativas);
		ID = q.ID;
		Permuta = q.Permuta;
	}
	
	/*
	public void resetAlternativas(){
		Alternativas.clear();
		nAlternativas = 0;
	}
	*/
	
	/**
	 * Verifica se há permissão de permutar as alternativas da questão.
	 * @return true se sim e false se não.
	 */
	public boolean getPermuta() {
		return Permuta;
	}

	/**
	 * Seta a permissão de permutar as alternativas.
	 * @param permuta o valor booleano para permitir ou impedir que as alternativas sejam permutadas.
	 */
	public void setPermuta(boolean permuta) {
		Permuta = permuta;
	}

	/**
	 * Adiciona uma alternativa à questão.
	 * @param alt Texto contido na alternativa.
	 * @throws IncompleteDataException 
	 * @see AlternativesList Alternative
	 */
	/*
	public void addAlternativa(String alt) throws IncompleteDataException{
		Alternativas.addAlternative(alt);
	}
	*/
	
	/**
	 * Permuta a lista de alternativas se for permitido pela questão.
	 * @warning é usado somente ao carregar os dados já existentes para a interpretação.
	 * @see Permuta
	 */
	public void permutarAlternativas(){
		if(Permuta){
			Alternativas.shuffle();
		}
	}

	/*
	public String getTitulo() {
		return Titulo;
	}

	public void setTitulo(String titulo) {
		Titulo = titulo;
	}

	public String getAutor() {
		return Autor;
	}

	public void setAutor(String autor) {
		Autor = autor;
	}

	public String getEnunciado() {
		return Enunciado;
	}

	public void setEnunciado(String enunciado) {
		Enunciado = enunciado;
	}
	public String getTema() {
		return Tema;
	}

	public void setTema(String tema) {
		Tema = tema;
	}
	 */
	/**
	 * Permite obter a alternativa correta.
	 * @return O objeto da alternativa correta
	 * @see Correta
	 * @deprecated está em desuso por enquanto.
	 */
	public Alternative getAlternativaCorreta(){
		/*
		for(Alternative alt : Alternativas)
		{
			//if(alt.id == id)
			if(alt.id == this.Correta)
			{
				return alt;
			}
		}
		*/
		return null;
	}
	
	/**
	 * 
	 * @return ID da alternativa correta.
	 * @see AlternativesList
	 */
	public int getCorreta(){
		return Alternativas.getCorreta();
	}
	
	/**
	 * Seta a alternativa correta pela posição dela na lista.
	 * Lança exceção se não corresponder à uma posição válida na lista de alternativas.
	 * @see AlternativesList
	 * @throws IndexOutOfBoundsException
	 */
	public void setCorreta(int correta) throws IndexOutOfBoundsException{
		Alternativas.setCorreta(correta);
	}
	
	/**
	 * Pega o ID que indica a alternativa correta retorna o seu texto formatado com o índice.
	 * @return String contendo o índice da alternativa na lista e seu texto.
	 * @sa Correta getCorreta
	 * @deprecated não é usado por enquanto.
	 */
	public String getCorretaText(){
		//int id = Utilities.decrypt(this);
		/*
		int i=0;
		for(Alternative alt : Alternativas)
		{
//			if(alt.id == id)
			if(alt.id == Correta)
			{
				return (char)(i+65)+") "+alt.text;
			}
			i++;
		}*/
		return null;
	}

	/**
	 * Pega o número de alternativas da questão.
	 * @return número de alternativas da questão.
	 */
	public int size(){
		return Alternativas.size();
	}
	
	/**
	 * Verifica se a alternativa é a correta comparando o seu ID com o ID da correta.
	 * @param pos A posição da alternativa na lista de alternativas.
	 * @return true se for a alternativa correta, e false se não for.
	 * @warning Só deve ser usado na interpretação.
	 * @see AlternativesList
	 */
	public boolean verificarResposta(int pos){ 
		return Alternativas.verificarResposta(pos);
	}

	/**
	 * Representa a questão em uma String resumida.
	 * A String é o título da questão se este não for vazio, senão é a parte inicial do Enunciado.
	 * Util para exibir listas de questões.
	 */
	public String toString() {
		int length = 30;
		if(this.Titulo.equals(""))
		{
			if(this.Enunciado.length() <= length)
			{
				return this.Enunciado;
			}
			else
			{
				return this.Enunciado.substring(0, length+1)+"...";
			}
		}
		if(this.Titulo.length() <= length)
		{
			return this.Titulo;
		}
		else
		{
			return this.Titulo.substring(0, length+1)+"...";
		}
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	
	
	/**
	 * Cria uma ArrayList de String, onde cada String é uma alternativa da questão.
	 * Ex: C) terceira alternativa.
	 * @return A ArrayList criada. 
	 * @warning Deve ser usado somente quando a lista de alternativas não será alterada, como na Interpretação por exemplo.
	 */
	public ArrayList<String> getArrayListStringAlternatives(){
		return Alternativas.getArrayListString();
	}
	
	/**
	 * Converte a Questão para um String XML.
	 * Chama o método verificarQuestao e lança exceções se estiver incorreta.
	 * @return String do XML
	 * @throws NoCorrectAnswerException 
	 * @throws MinimumAlternativesException 
	 * @throws IncompleteDataException 
	 * @see verificarQuestao
	 */
	public String toXML() throws IncompleteDataException, MinimumAlternativesException, NoCorrectAnswerException {
		verificarQuestao();
		XStream xStream = new XStream(new DomDriver());
		xStream.registerConverter(new QuestionConverter());
		xStream.alias("Questao", Question.class);
		
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
	 * Converte um XML para Questão. Deve ser usado para ler e importar questões independentes de uma lista de questões.
	 * @param toXML String do XML
	 * @return O objeto Questão
	 */
	public static Question fromXML(String toXML) {
		Question q = null;
		
		XStream xStream = new XStream(new DomDriver());
		xStream.registerConverter(new QuestionConverter());
		xStream.alias("Questao", Question.class);
		
		Object obj = null;
		
		try{
			obj = xStream.fromXML(toXML);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		if(obj != null && obj instanceof Question)
		{
			q = (Question) obj;
		}
		return q;
	}
	
	/**
	 * Verifica se a Questão contém as informações mínimas obrigatórias.
	 * Lança exceções se ouver erros na questão.
	 * @throws IncompleteDataException 
	 * @throws MinimumAlternativesException 
	 * @throws NoCorrectAnswerException 
	 * @attention Este método deve ser chamado pela GUI para garantir que a questão esta corretamente preenchida.
	 * Deverá informar o usuário cada caso que o usuário precisa corrigir para ter seu conteúdo válido.
	 */
	public void verificarQuestao() throws IncompleteDataException, MinimumAlternativesException, NoCorrectAnswerException{
		if(Enunciado.equals("")){
			throw new IncompleteDataException("Enunciado"); // erro: campos obrigatórios não preenchidos
		}
		if(Alternativas.size() < 2){
			
			throw new MinimumAlternativesException(); // erro: possui menos de 2 alternativas
		}
		if(Alternativas.getCorreta() == -1)
		{
			throw new NoCorrectAnswerException(); // não há alternativa correta setada.
		}
	}
	
	public int getTentativas() {
		return Tentativas;
	}

	public void incrementaTentativas() {
		Tentativas++;
	}
	
	public void setTentativas(int tentativas) {
		Tentativas = tentativas;
	}

}
