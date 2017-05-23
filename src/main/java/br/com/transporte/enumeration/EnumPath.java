package br.com.transporte.enumeration;

public enum EnumPath {

	HOME("/fxml/home/index.fxml"),
	CADASTRO_CLIENTE("/fxml/cliente/cadastroCliente.fxml"),
	PESQUISA_CLIENTE("/fxml/cliente/pesquisaCliente.fxml"),
	PESQUISA_PARCELA_CONTRATO("/fxml/contrato/pesquisaParcelaContrato.fxml"),
	PESQUISA_VALOR_CONTRATO("/fxml/contrato/pesquisaValorContrato.fxml"),
	PESQUISA_ITINERARIO_CONTRATO("/fxml/contrato/pesquisaItinerarioContrato.fxml"),
	CADASTRO_VALOR_CONTRATO("/fxml/contrato/cadastroValorContrato.fxml"),
	CADASTRO_PARCELA_CONTRATO("/fxml/contrato/cadastroParcelaContrato.fxml"),
	CADASTRO_ITINERARIO_CONTRATO("/fxml/contrato/cadastroItinerarioContrato.fxml"),
	PESQUISA_MOTORISTA("/fxml/motorista/pesquisaMotorista.fxml"),
	CADASTRO_MOTORISTA("/fxml/motorista/cadastroMotorista.fxml"),
	PESQUISA_VEICULO("/fxml/veiculo/pesquisaVeiculo.fxml"),
	CADASTRO_VEICULO("/fxml/veiculo/cadastroVeiculo.fxml");
	
	private String path;
	
	private EnumPath(final String path){
		this.path = path;
	}
	
	public String getPath() {
		return path;
	}
}
