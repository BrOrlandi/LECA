package leca.modelo;

import java.util.ArrayList;
import java.util.Collections;

import leca.exceptions.IncompleteDataException;

/**
 * Classe que representa a lista de alternativas de uma questão.
 * Foi encapsulada para trabalhar independentemente com a permutação de alternativas e a alternativa correta.
 */
public class AlternativesList {

	/**
	 * Lista de alternativas da questão.
	 * Contém Objetos do tipo Alternativa.
	 * @see nAlternativas Alternative addAlternativa
	 */
	private ArrayList<Alternative> Alternativas;
	private int Correta;				        ///< ID da alternativa correta. É criptografado apenas quando é convertido para XML.
	
	public AlternativesList(){
		Alternativas = new ArrayList<Alternative>();
		Correta = -1;
	}
	
	/**
	 * Construtor de cópia
	 * @param copy recebe a lista a copiar.
	 */
	public AlternativesList(AlternativesList copy){
		Alternativas = new ArrayList<Alternative>();
		Correta = copy.Correta;
		for(Alternative a : copy.Alternativas){
			try {
				Alternativas.add(new Alternative(a.Id,a.Text));
			} catch (IncompleteDataException e) {
				e.printStackTrace();
				//Nunca deve entrar neste catch pois é uma cópia de alternativas que já existem.
			}
		}
	}
	
	/**
	 * Adiciona uma alternativa à lista sem atribuir um ID.
	 * @param alt texto da alternativa.
	 * @throws IncompleteDataException 
	 * @see Alternative
	 */
	/*
	public void addAlternative(String alt) throws IncompleteDataException{
		Alternativas.add(new Alternative(alt));
	}
	*/
	

	/**
	 * Adiciona uma alternativa à lista.
	 * O ID desta alternativa é definido por parâmetro.
	 * @param id ID da alternativa.
	 * @param alt Texto contido na alternativa.
	 * @throws IncompleteDataException 
	 * @warning É usado apenas quando se carrega um conteúdo já existente Não deve ser usado nas aplicações.
	 * @see Alternative
	 */
	public void addAlternative(int id, String alt) throws IncompleteDataException{
		Alternativas.add(new Alternative(id, alt));
	}
	
	/**
	 * Move uma alternativa para cima na lista das alternativas.
	 * Se tentar mover de uma posição invalida ou para uma posição invalida será lançada exceção.
	 * @param pos a posição da alternativa que será movida.
	 * @throws IndexOutOfBoundsException
	 */
	public void moveUp(int pos){
		Collections.swap(Alternativas, pos, pos-1);
		//checar para ver se alternativa movida é a correta
		if(pos == Correta){
			Correta--;
		}
		//checar se a de cima que moveu para baixo é a correta
		else if (pos-1 == Correta)
		{
			Correta++;
		}
	}

	/**
	 * Move uma alternativa para baixo na lista das alternativas.
	 * Se tentar mover de uma posição invalida ou para uma posição invalida será lançada exceção.
	 * @param pos a posição da alternativa que será movida.
	 * @throws IndexOutOfBoundsException
	 */
	public void moveDown(int pos){
		Collections.swap(Alternativas, pos, pos+1);
		//checar para ver se alternativa movida é a correta
		if(pos == Correta)
		{
			Correta++;
		}
		//checar se a de baixo que moveu para cima é a correta
		else if(pos+1 == Correta)
		{
			Correta--;
		}
	}
	
	/**
	 * Remove uma alternativa da lista. 
	 * @param pos a posição da alternativa que será removida da lista.
	 * @return true se a alternativa correta foi removida.
	 */
	public boolean removeAlternative(int pos){
		Alternativas.remove(pos);
		//se removeu uma correta
		if(pos == Correta)
		{
			Correta = -1;
			return true;
		}
		//se removeu antes da correta, a correta desce uma posição
		else if (pos < Correta)
		{
			Correta--;
		}
		return false;
	}

	/**
	 * Pega uma alternativa na sua representação em String com o seu indice na lista.
	 * A alternativa é exibida com um índice, uma letra maiúscula.
	 * Ex: A) Alternativa A.
	 * @param pos A posição da alternativa na lista de alternativas.
	 * @return Uma String com o índice e o texto da alternativa.
	 * @see Alternative
	 */
	public String getAlternativa(int pos){
		char a = (char) (65 + pos);
		return a +") "+Alternativas.get(pos).Text;
	}
	
	/**
	 * Permuta as alternativas na lista a partir do método Colletions.shuffle().
	 * @warning Este método não verifica se há permissão de permutar as alternativas.
	 */
	public void shuffle(){
		Collections.shuffle(Alternativas);
	}
	
	/**
	 * @return ID da alternativa correta.
	 * @see Correta
	 */
	public int getCorreta(){
		return Correta;
	}
	
	/**
	 * Seta a alternativa correta pela posição dela na lista.
	 * Lança exceção se não corresponder à uma posição válida na lista de alternativas.
	 * @see Correta
	 * @throws IndexOutOfBoundsException
	 */
	public void setCorreta(int correta) throws IndexOutOfBoundsException{
		if(correta < 0 || correta >= Alternativas.size())
		{
			throw new IndexOutOfBoundsException();
		}
		Correta = correta;
	}
	
	/**
	 * Pega o número de alternativas da lista.
	 * @return número de alternativas da lista.
	 */
	public int size(){
		return Alternativas.size();
	}
	
	/**
	 * Verifica se a alternativa é a correta comparando o seu ID com o ID da correta.
	 * @param pos A posição da alternativa na lista de alternativas.
	 * @return true se for a alternativa correta, e false se não for.
	 * @warning Só deve ser usado na interpretação.
	 * @see Correta
	 */
	public boolean verificarResposta(int pos){ 
		if(Correta == this.Alternativas.get(pos).Id)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Cria uma ArrayList de String, onde cada String é uma alternativa da lista.
	 * Ex: C) terceira alternativa.
	 * @return A ArrayList criada. 
	 * @warning Deve ser usado somente quando a lista de alternativas não será alterada, como na Interpretação por exemplo.
	 */
	public ArrayList<String> getArrayListString(){
		ArrayList<String> list = new ArrayList<String>();
		int pos = 0;
		char a;
		for(Alternative alt : Alternativas)
		{
			a = (char) (65 + pos++);
			list.add(a +") "+alt.Text);
		}
		return list;
	}
	
	/**
	 * Pega a ArrayList de alternativas.
	 * @return a ArrayList de alternativas da lista.
	 * @warning É usado somente pelo conversor.
	 */
	public ArrayList<Alternative> getAlternatives(){
		return Alternativas;
	}
	
	/**
	 * Atribui IDs para as alternativas na ordem que estão na lista.
	 */
	//TODO para uso futuro.
	public void setIds(){
		int i = 0;
		for(Alternative a : Alternativas){
			a.Id = i++;
		}
	}
}
