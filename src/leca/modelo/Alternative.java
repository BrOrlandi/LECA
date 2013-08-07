package leca.modelo;

import leca.exceptions.IncompleteDataException;

/**
 * Classe que representa uma alternativa. Os objetos criados a partir 
 * desta classe representam alternativas para as questões criadas.
 */
public class Alternative {
	int Id;/**< Um inteiro que identifica a alternativa.
	* @warning O ID da alternativa, não é correspondente à sua posição na lista de alternativas após a permutação na interpretação.
	*/ 
	public String Text; ///< O texto contido na alternativa.
	
	/**
	 * O texto da alternativa não pode ser em branco ou nulo, senão será lançada uma exceção.
	 * @param id O ID a ser atribuido para identificar a alternativa de uma questão
	 * @param text O texto da alternativa
	 * @throws IncompleteDataException 
	 */
	public Alternative(int id, String text) throws IncompleteDataException {
		Id = id;
		if(text == null || text.equals("")){
			throw new IncompleteDataException("Empty alternative");
		}
		Text = text;
	}
	
	/**
	 * Não recebe um ID, o ID associado é -1.
	 * O texto da alternativa não pode ser em branco ou nulo, senão será lançada uma exceção.
	 * @warning Deverá receber um ID quando for adicionada à uma questão.
	 * @param text O texto da alternativa
	 * @throws IncompleteDataException 
	 */
	/*
	public Alternative(String text) throws IncompleteDataException {
		Id = -1;
		if(text == null || text.equals("")){
			throw new IncompleteDataException("Empty alternative");
		}
		Text = text;
	}
	*/
	
	/**
	 * É usado pelo conversor de XML de User para guardar o ID da alternativa.
	 * @return O ID da alternativa
	 */
	public int getId() {
		return Id;
	}
	/**
	 * 
	 * @param id o ID a atribuir a Alternativa
	 * @deprecated o ID é default.
	 */
	public void setId(int id) {
		Id = id;
	}
	/**
	 * @deprecated o texto é default.
	 */
	public String getText() {
		return Text;
	}
	/**
	 * @deprecated o texto é default.
	 */
	public void setText(String text) {
		Text = text;
	}

	@Override
	/**
	 * Sobreescreve o método toString da alternativa.
	 * Esse método irá imprimir um letra como índice para a alternativa de acordo com seu ID.
	 * Ex: B) Alternativa B.
	 * Deverá ser usado somente no sistema de autoria para criação das questões.
	 * Pois os indices são definidos pelos IDs, e na permutação de alternativas os IDs são mantidos.
	 * Neste caso o indice deverá ser indicado exteriormente pelo sistema de interpretação.
	 * @warning Não use este método no sistema de interpretação. Em vez deste, use getArrayListStringAlternatives() da classe Question.
	 * @sa Question
	 * @sa getArrayListStringAlternatives()
	 */
	public String toString() {
		char a = (char) (65 + Id);
		return a +") "+ Text;
	}
	
}
