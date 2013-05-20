package leca.util;
/**
 * @package leca.util
 * Classes e métodos uteis para o sistema.
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Random;

/**
 * Classe de métodos estáticos úteis ao sistema.
 * 
 * @author Bruno Henrique Orlandi
 *
 */
public final class Utilities {

	/**
	 * Codifica um valor inteiro baseado em um character de uma string.
	 * A codificação é basicamente a multiplicação de 3 números e somado com um identificador:
	 * O primeiro número é o primeiro char da String passada por parametro.
	 * O identificador é um número aleatório 0,1 ou 2.
	 * O segundo número é escolhido pelo identificador.
	 * O terceiro número é o ID da alternativa.
	 * @param str String de onde pegará o char para usar na codificação.
	 * @param id inteiro que será codificado.
	 * @return o valor inteiro codificado com um char da string.
	 * @attention Esta função é usada somente pelo conversor ao salvar um arquivo XML.
	 */
	public static int encrypt(String str, int id){
		int primeirochar,randnum=1,code;
		primeirochar = str.codePointAt(0); // pega o código do primeiro char do enunciado
		Random rand = new Random();
		int r = rand.nextInt(3);
		switch(r)
		{
		case 0:
			randnum = 47;
			break;
		case 1:
			randnum = 29;
			break;
		case 2:
			randnum = 71;
			break;
		}
		code = primeirochar*randnum*(id+1)*3 + r;
		return code;
	}

	/**
	 * Decodifica um valor inteiro baseado em um character de uma string.
	 * 
	 * A decodificação é basicamente o inverso da codificação. 
	 * Pegar o identificador e subtraí-lo do código.
	 * Dividimos o código pelo primeiro char da String.
	 * Com o identificador sabemos qual o número que está multiplicando o código.
	 * Dividimos por este número e teremos o inteiro decodificado.
	 * @param str String de onde pegará o char para usar na decodificação, deve ser a mesma que foi usada na codificação.
	 * @param id inteiro que será decodificado.
	 * @return o valor inteiro decodificado.
	 * @attention Esta função é usada somente pelo conversor ao traduzir um arquivo XML.
	 */
	public static int decrypt(String str, int id){
		int r,primeirochar, randnum = 1, idAlt;
		primeirochar = str.codePointAt(0); // pega o código do primeiro char do enunciado
		r = id%3;
		switch(r)
		{
		case 0:
			randnum = 47;
			break;
		case 1:
			randnum = 29;
			break;
		case 2:
			randnum = 71;
			break;
		}
		idAlt = (((id/3)/randnum)/primeirochar)-1;
		return idAlt;
	}

	/** 
	 * Salva uma String num arquivo.
	 * Lança NullPointerException se o nome do arquivo ou a string forem null.
	 * É usado nas chamadas de carregar ou salvar arquivos.
	 * @param filename String com o caminho do arquivo para salvar
	 * @param string String a ser salva no arquivo
	 * @return true se salvou com sucesso e false caso não tenha sido salvo.
	 */
	public static boolean saveStringToFile(String filename, String string){
		boolean saved = false;
		BufferedWriter bw = null;
		if(string == null || filename == null){
			throw new NullPointerException("Null String");
		}
		
		try{
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"));
			try{
				bw.write(string);
				saved = true;
			}
			finally{
				bw.close();
			}
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
		return saved;
	}
	
	
	/**
	 * Lê o conteúdo de um arquivo.
	 * Lança NullPointerException se o nome do arquivo for null.
	 * É usado nas chamadas de carregar ou salvar arquivos.
	 * @param filename Arquivo a ser lido
	 * @return A string do conteúdo do arquivo.
	 */
	public static String getStringFromFile(String filename){
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		if(filename == null){
			throw new NullPointerException("Null String");
		}
		
		try{
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filename),"UTF-8"));
			try{
				String s;
				while((s = br.readLine()) != null)
				{
					sb.append(s);
					sb.append("\n");
				}
			}
			finally{
				br.close();
			}
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		return sb.toString();
	}
}
