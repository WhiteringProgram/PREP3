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
	
	//Variáveis
	private HttpURLConnection conexao ;
	private OutputStreamWriter escritor;
	private BufferedReader buffer;
	private StringBuilder geraString;
	private String linha;
	private URL endereço;
	private String str = "";
	
	//Método construtor
	public ControladorDeArquivos() {
		super ();
	}
	
	//Getters e setters para manipulação de variáveis privadas com foco em encapsulamento
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
	
	public void setEndereço(URL endereço) {
		this.endereço = endereço;
	}
	
	public URL getEndereço() {
		return endereço;
	}
	
	public void setStr(String str) {
		this.str = str;
	}
	
	public String getStr() {
		return str;
	}


	//Extrai dados do website mencionado pelo exercício a fim de transcrevê-los ao arquivo
	public void dadosArquivo() {
	//Obtenção dos dados do endereço e tratamento de possíveis erros
		try {
		endereço = new URL("https://wikimedia.org/api/rest_v1"
		+ "/metrics/pageviews/per-article/en.wikipedia"
		+ "/all-access/all-agents/Tiger_King/daily/20210901/20210930");
		setConexao(null);
		//Aceita o endereço do protocolo HTTP, abrindo uma conexão
		setConexao((HttpURLConnection)endereço.openConnection());
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
		//Anula os valores para limpar a memória
		setBuffer(null);
		setGeraString(null);
		setEscritor(null);
		setConexao(null);
			}
		}
	
	//Obtém a parte esperada do arquivo (visualizações e data). Além disso, esse método também retira os elementos repetidos
	public void filtraArquivo () {
		//Os separadores comuns do conteúdo da página são o ponto e vírgula e a vírgula
		//Um replace é necessário para substituir as chaves e os colchetes por espaços vazios
		str = str.replaceAll("[\\[\\](){}\"]",""); /* Remove as pontuação especificada (barras, colchetes, parênteses, chaves, aspas duplas etc.)
		isso facilita a leitura do conteúdo obtido pelo algoritmo*/
		str = str.replaceAll("items:","");
		//Substitui os itens desnecessários por espaços em branco
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
