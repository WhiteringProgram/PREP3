	package controller;
	
	import java.io.BufferedReader;
	import java.io.BufferedWriter;
	import java.io.File;
	import java.io.FileWriter;
	import java.io.FileReader;
	import java.io.IOException;
	import java.io.InputStreamReader;
	import java.io.OutputStreamWriter;
	import java.net.HttpURLConnection;
	import java.net.MalformedURLException;
	import java.net.ProtocolException;
	import java.net.URL;

	
	public class ControladorDeArquivos {
	
	//Vari�veis
	private HttpURLConnection conexao ;
	private OutputStreamWriter escritor;
	private BufferedReader buffer;
	private StringBuilder geraString;
	private String linha;
	private URL endere�o;
	private String str = "";
	
	//M�todo construtor
	public ControladorDeArquivos() {
		super ();
	}
	
	//Getters e setters para manipula��o de vari�veis privadas com foco em encapsulamento
	public void setConexao(HttpURLConnection conexao) {
		this.conexao = conexao;
	}
	
	public HttpURLConnection getConexao() {
		return conexao;
	}
	
	public void setEscritor (OutputStreamWriter escritor) {
		this.escritor = escritor;
	}
	
	public OutputStreamWriter getEscritor() {
		return escritor;
	}
	
	public void setBuffer(BufferedReader buffer) {
		this.buffer = buffer;
	}
	
	public BufferedReader getBuffer() {
		return buffer;
	}
	
	public void setGeraString(StringBuilder geraString) {
		this.geraString = geraString;
	}
	
	public StringBuilder getGeraString() {
		return geraString;
	}
	
	public void setLinha(String linha) {
		this.linha = linha;
	}
	
	public String getLinha() {
		return linha;
	}
	
	public void setEndere�o(URL endere�o) {
		this.endere�o = endere�o;
	}
	
	public URL getEndere�o() {
		return endere�o;
	}
	
	public void setStr(String str) {
		this.str = str;
	}
	
	public String getStr() {
		return str;
	}


	//Extrai dados do website mencionado pelo exerc�cio a fim de transcrev�-los ao arquivo
	public void dadosArquivo() {
	//Obten��o dos dados do endere�o e tratamento de poss�veis erros
		try {
		endere�o = new URL("https://wikimedia.org/api/rest_v1"
		+ "/metrics/pageviews/per-article/en.wikipedia"
		+ "/all-access/all-agents/Tiger_King/daily/20210901/20210930");
		setConexao(null);
		//Aceita o endere�o do protocolo HTTP, abrindo uma conex�o
		setConexao((HttpURLConnection)endere�o.openConnection());
		getConexao().setRequestMethod("GET");
		getConexao().setDoOutput(true);
		getConexao().setReadTimeout(1000);
		getConexao().connect();
		buffer = new BufferedReader(new InputStreamReader(getConexao().getInputStream()));
		geraString = new StringBuilder();
		
		while ((linha = buffer.readLine()) != null) {
		geraString.append(linha + '\n');
		}
		str += geraString;
		str.toString(); //Transforma os dados obtidos em uma String
		} catch (MalformedURLException e) {
		e.printStackTrace();
		} catch (ProtocolException e) {
		e.printStackTrace();
		} catch (IOException e) {
		e.printStackTrace();
		}
		finally {
		//Desconecta da URL inserida
		getConexao().disconnect();
		//Anula os valores para limpar a mem�ria
		setBuffer(null);
		setGeraString(null);
		setEscritor(null);
		setConexao(null);
			}
		}
	
	//Obt�m a parte esperada do arquivo (visualiza��es e data). Al�m disso, esse m�todo tamb�m retira os elementos repetidos
	public void filtraArquivo () {
		//Os separadores comuns do conte�do da p�gina s�o o ponto e v�rgula e a v�rgula
		//Um replace � necess�rio para substituir as chaves e os colchetes por espa�os vazios
		str = str.replaceAll("[\\[\\](){}\"]",""); /* Remove as pontua��o especificada (barras, colchetes, par�nteses, chaves, aspas duplas etc.)
		isso facilita a leitura do conte�do obtido pelo algoritmo*/
		str = str.replaceAll("items:","");
		//Substitui os itens desnecess�rios por espa�os em branco
		str = str.replaceAll("project:en.wikipedia,article:Tiger_King,granularity:daily,", ""); 
		str = str.replaceAll(",access:all-access,agent:all-agents,"," |");
		str = str.replaceAll("00", "");
		str = str.replace("14", "1400").replaceAll("23", "2300").replaceAll("15", "1500").replaceAll("150016", "1516").replaceAll("140044", "1444").
				replaceAll("150046", "1546").replaceAll("140094", "1494").replaceAll("140089", "1489").replaceAll("2021091400", "20210914").
				replaceAll("2021091500", "20210915").replaceAll("2021092300", "20210923").replaceAll("140084", "1484").replaceAll("140069", "1469").
				replaceAll("150099", "1599").replaceAll("140071", "1471").replaceAll("150021", "1521");
		String [] linhas = new String [str.length()];
			for (int i = 0; i < linhas.length; i++) {
			linhas = str.split(",");
			System.out.println(linhas[i]);
			}
    }
	
	//Transcreve os dados do website ao arquivo e faz a leitura
	public void controleDeArquivo () {
		File arquivo = new File ("C:\\TEMP");
		try {
		
		//Escrita de Arquivo
		if (!arquivo.exists()) {
		arquivo.mkdirs();
		}
		arquivo = new File ("C:\\TEMP\\wiki.txt");
		if (!arquivo.exists()) {
		arquivo.createNewFile();
		}
		FileWriter fw = new FileWriter (arquivo, true);
		BufferedWriter bw = new BufferedWriter (fw);
		bw.write(str);
		bw.newLine();
		bw.close();
		fw.close();
		//Leitura do arquivo
		FileReader fr = new FileReader (arquivo);
		BufferedReader br = new BufferedReader(fr);
		while (br.ready()) {
			linha = br.readLine();
		}
		fr.read();
		br.read();
		br.close();
		fr.close();
		} catch (IOException ex) {
		ex.printStackTrace();
		}
	}
}
