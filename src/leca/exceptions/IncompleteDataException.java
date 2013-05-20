package leca.exceptions;
/**
 * @package leca.exceptions
 * Diversos tipos de exceções que podem ocorrer e devem ser tratadas para garantir o funcionamento ideal.
 */

/**
 * Indica que não possui os dados obrigatórios completos.
 */
public class IncompleteDataException extends Exception{

	public IncompleteDataException(String str){
		super(str);
	}
}
