package br.com.transporte.controller.cliente;

import static br.com.transporte.enumeration.EnumPath.PESQUISA_CLIENTE;
import static br.com.transporte.enumeration.EnumPath.PESQUISA_ITINERARIO_CONTRATO;
import static br.com.transporte.enumeration.EnumPath.PESQUISA_PARCELA_CONTRATO;
import static br.com.transporte.enumeration.EnumPath.PESQUISA_VALOR_CONTRATO;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.juliomakita.fxcomponents.control.mask.CelularField;
import br.com.juliomakita.fxcomponents.control.mask.CepField;
import br.com.juliomakita.fxcomponents.control.mask.CpfField;
import br.com.juliomakita.fxcomponents.control.mask.DateTimeField;
import br.com.juliomakita.fxcomponents.control.mask.EmailField;
import br.com.juliomakita.fxcomponents.control.mask.TelefoneField;
import br.com.transporte.annotation.BeanAttribute;
import br.com.transporte.annotation.IgnoreBinding;
import br.com.transporte.controller.contrato.PesquisaItinerarioContratoController;
import br.com.transporte.controller.contrato.PesquisaParcelaContratoController;
import br.com.transporte.controller.contrato.PesquisaValorContratoController;
import br.com.transporte.enumeration.EnumSexo;
import br.com.transporte.helper.DialogHelper;
import br.com.transporte.model.Cliente;
import br.com.transporte.model.Endereco;
import br.com.transporte.model.ItinerarioContrato;
import br.com.transporte.model.ParcelaContrato;
import br.com.transporte.model.ValorContrato;
import br.com.transporte.service.ClienteService;
import br.com.transporte.util.ControllerUtils;
import br.com.transporte.util.FieldUtil;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import jfxtras.labs.scene.control.BeanPathAdapter;

@Named
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CadastroClienteController {
	
	private static final Logger LOG = LoggerFactory.getLogger(CadastroClienteController.class);
	private static final String ENDERECO = "endereco";
	private static final String CONTRATO = "contrato";
	private static final String VALOR_CONTRATO = "contrato.valorContrato";
	private static final String PARCELA_CONTRATO = "contrato.parcelaContrato";
	private static final String ITINERARIO_CONTRATO = "contrato.itinerarioContrato";
	
	@IgnoreBinding
	private BeanPathAdapter<Cliente> beanPathAdapter;
	
	@IgnoreBinding
	private ApplicationContext context;
	
	@IgnoreBinding
	private ClienteService clienteService;
	
	@FXML 
	@IgnoreBinding
	private PesquisaValorContratoController pesquisaValorContratoController;
	
	@FXML
	private DateTimeField dataNascimento;
	
	@FXML 
	private TextField nome;
	
	@FXML 
	private ComboBox<EnumSexo> sexo;

	@FXML
	private EmailField email;
	
	@FXML 
	private TextField rg;
	
	@FXML 
	private CpfField cpf;
	
	@FXML 
	private TelefoneField telResidencial;
	
	@FXML 
	private CelularField telCelular;
	
	@FXML
	@BeanAttribute(type=CONTRATO)
	private TextArea observacao;
	
	@FXML 
	@BeanAttribute(type=ENDERECO)
	private TextField endereco;
	
	@FXML 
	@BeanAttribute(type=ENDERECO)
	private TextField complemento;
	
	@FXML 
	@BeanAttribute(type=ENDERECO)
	private CepField cep;
	
	@FXML 
	@BeanAttribute(type=ENDERECO)
	private TextField bairro;
	
	@FXML 
	@BeanAttribute(type=ENDERECO)
	private TextField cidade;
	
	@FXML 
	@BeanAttribute(type=ENDERECO)
	private TextField estado;
	
	@FXML 
	@BeanAttribute(type=ENDERECO)
	private TextField numero;
	
	@FXML
	@BeanAttribute(type=CONTRATO)
	private DateTimeField vigenciaInicio;
	
	@FXML
	@BeanAttribute(type=CONTRATO)
	private DateTimeField vigenciaFim;
	
	@FXML
	@BeanAttribute(type=VALOR_CONTRATO)
	private TextField valorContrato;
	
	@FXML
	@BeanAttribute(type=VALOR_CONTRATO)
	private TextField valorContratoExtenso;
	
	@FXML
	@BeanAttribute(type=PARCELA_CONTRATO)
	private TextField valorMensalParcela;
	
	@FXML
	@BeanAttribute(type=PARCELA_CONTRATO)
	private TextField valorExtensoParcela;
	
	@FXML
	@BeanAttribute(type=PARCELA_CONTRATO)
	private TextField quantidadeParcela;
	
	@FXML
	@BeanAttribute(type=ITINERARIO_CONTRATO)
	private TextField localSaida;
	
	@FXML
	@BeanAttribute(type=ITINERARIO_CONTRATO)
	private TextField horaSaida;
	
	@FXML
	@BeanAttribute(type=ITINERARIO_CONTRATO)
	private TextField localRetorno;
	
	@FXML 
	@BeanAttribute(type=ITINERARIO_CONTRATO)
	private TextField horaRetorno;
	
	@IgnoreBinding
	private Cliente cliente;
	
	@IgnoreBinding
	private ValorContrato valorContratoBean;
	
	@IgnoreBinding
	private ParcelaContrato parcelaContratoBean;
	
	@IgnoreBinding
	private ItinerarioContrato itinerarioContratoBean;
	
	@IgnoreBinding
	private StringBuilder sb = new StringBuilder();
	
	@PostConstruct
	public void initialize() {
		FieldUtil.upperCaseTextField(nome, rg , endereco, complemento, numero, bairro, cidade, estado);
		FieldUtil.upperCaseTextArea(observacao);
		this.cliente = new Cliente();
		this.cliente.setEndereco(new Endereco());
		bind();
		this.context = new ClassPathXmlApplicationContext("classpath:/spring/applicationContext.xml");
		this.clienteService = this.context.getBean(ClienteService.class);
		this.sexo.getSelectionModel().select(1);
		this.sexo.setItems(FXCollections.observableArrayList(EnumSexo.MASCULINO, EnumSexo.FEMININO));
	}
	
	public void cadastrar(){
		
		Cliente cliente = (Cliente)beanPathAdapter.getBean();
		
		if(validaCliente(cliente)){
			DialogHelper.alertInformation("", "", sb.toString());
			this.sb.setLength(0);
			return;
		}

		if(cliente.getId() != null){
			
			if(parcelaContratoBean != null && parcelaContratoBean.getId() != null){
				cliente.getContrato().setParcelaContrato(parcelaContratoBean);
			}
			
			if(valorContratoBean != null && valorContratoBean.getId() != null){
				cliente.getContrato().setValorContrato(valorContratoBean);
			}
			
			if(itinerarioContratoBean != null && itinerarioContratoBean.getId() != null){
				cliente.getContrato().setItinerarioContrato(itinerarioContratoBean);
			}

			this.cliente = this.clienteService.update(cliente);
			DialogHelper.alertInformation("", "", "Cliente alterado com sucesso!");
		}else{
			if(existeErrorContrato(cliente)) return;
			this.cliente =  this.clienteService.save(cliente);
			DialogHelper.alertInformation("", "", "Cliente cadastrado com sucesso!");
		}
		
		clearFields();
	}

	public void retornar(ActionEvent event) {
		try {
			FXMLLoader loader = DialogHelper.loadScreen(event, getClass(), PESQUISA_CLIENTE.getPath(), 1024, 600);
			PesquisaClienteController controller = loader.<PesquisaClienteController>getController();
			if(this.cliente != null && this.cliente.getId() != null){
				List<Cliente> listaCliente = controller.getListaCliente();
				listaCliente.clear();
				listaCliente.add(this.clienteService.findOne(this.cliente.getId()));
				controller.listenerPagination(listaCliente, controller.getClienteTable());
				controller.listenerPagination(listaCliente, controller.getClienteTable());
				controller.preencheDataTable(controller.getListaCliente());
				controller.init(listaCliente, controller.getClienteTable());
			}
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	public void dialogParcelaContrato(final ActionEvent event){	
		try {
			FXMLLoader loader = DialogHelper.newStage(getClass(), PESQUISA_PARCELA_CONTRATO.getPath(), "Parcela Contrato");
			PesquisaParcelaContratoController controller =  loader.<PesquisaParcelaContratoController>getController();
	        controller.setCadastroClienteController(this);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	public void dialogValorContrato(final ActionEvent event){	
		try {
			FXMLLoader loader = DialogHelper.newStage(getClass(), PESQUISA_VALOR_CONTRATO.getPath(), "Valor Contrato");
			PesquisaValorContratoController controller =  loader.<PesquisaValorContratoController>getController();
	        controller.setCadastroClienteController(this);	
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	public void dialogItinerarioContrato(final ActionEvent event){	
		try {
			FXMLLoader loader = DialogHelper.newStage(getClass(), PESQUISA_ITINERARIO_CONTRATO.getPath(), "Itinerario Contrato");
			PesquisaItinerarioContratoController controller =  loader.<PesquisaItinerarioContratoController>getController();
	        controller.setCadastroClienteController(this);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	public void bind() {
		try {
			this.beanPathAdapter = ControllerUtils.bindBidirectional(this, cliente);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			LOG.error("Erro ao realizar bind dos campos CadastroClienteController " + e.getMessage());
		}
	}
	
	private boolean existeErrorContrato(final Cliente cliente){
		
		if(this.valorContratoBean != null){
			cliente.getContrato().setValorContrato(this.valorContratoBean);
		}else{
			DialogHelper.alertInformation("", "", "Selecione um valor de contrato");
			return true;
		}
		
		if(this.parcelaContratoBean != null){
			cliente.getContrato().setParcelaContrato(this.parcelaContratoBean);
		}else{
			DialogHelper.alertInformation("", "", "Selecione uma parcela de contrato");
			return true;
		}
		
		if(this.itinerarioContratoBean != null){
			cliente.getContrato().setItinerarioContrato(this.itinerarioContratoBean);
		}else{
			DialogHelper.alertInformation("", "", "Selecione um itinerário de contrato");
			return true;
		}
		
		return false;
	}
	
	private void clearFields() {
		try {
			this.valorContratoBean = null;
			this.parcelaContratoBean = null;
			this.itinerarioContratoBean = null;
			ControllerUtils.clearFields(this);
			this.sexo.setItems(FXCollections.observableArrayList(EnumSexo.MASCULINO, EnumSexo.FEMININO));
			this.sexo.getSelectionModel().select(0);
			this.sb.setLength(0);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	private boolean validaCliente(final Cliente cliente){
		
		boolean erroCampos = false;
		sb.append("Os seguintes campos são obrigatórios:\n");
		
		if(cliente.getNome() == null || StringUtils.isBlank(cliente.getNome())){
			sb.append("Nome \n");
			erroCampos = true;
		}
		
		if(cliente.getCpf() == null || StringUtils.isBlank(cliente.getCpf())){
			sb.append("CPF \n");
			erroCampos = true;
		}
		
		if(cliente.getDataNascimento() == null){
			sb.append("Data de Nascimento \n");
			erroCampos = true;
		}
		
		if(cliente.getRg() == null || StringUtils.isBlank(cliente.getRg())){
			sb.append("RG \n");
			erroCampos = true;
		}
		
		if(cliente.getCpf() == null || StringUtils.isBlank(cliente.getCpf())){
			sb.append("CPF \n");
			erroCampos = true;
		}
		
		if(cliente.getEndereco().getEndereco() == null || StringUtils.isBlank(cliente.getEndereco().getEndereco())){
			sb.append("Endereço \n");
			erroCampos = true;
		}
		
		if(cliente.getEndereco().getNumero()== null || StringUtils.isBlank(cliente.getEndereco().getNumero())){
			sb.append("Número do endereço \n");
			erroCampos = true;
		}
		
		if(cliente.getEndereco().getBairro()== null || StringUtils.isBlank(cliente.getEndereco().getBairro())){
			sb.append("Bairro \n");
			erroCampos = true;
		}
		
		if(cliente.getEndereco().getCidade()== null || StringUtils.isBlank(cliente.getEndereco().getCidade())){
			sb.append("Cidade \n");
			erroCampos = true;
		}
		
		if(cliente.getEndereco().getEstado()== null || StringUtils.isBlank(cliente.getEndereco().getEstado())){
			sb.append("Estado \n");
			erroCampos = true;
		}
		
		if(cliente.getEndereco().getCep()== null || StringUtils.isBlank(cliente.getEndereco().getCep())){
			sb.append("Cep \n");
			erroCampos = true;
		}
		
		if(cliente.getContrato().getVigenciaInicio()== null){
			sb.append("Vigência Inicio \n");
			erroCampos = true;
		}
		
		if(cliente.getContrato().getVigenciaFim()== null){
			sb.append("Vigência Término \n");
			erroCampos = true;
		}
		
		return erroCampos;
	}
	public void setValorContrato(BigDecimal valorContrato) {
		this.valorContrato.setText(valorContrato.toString());
	}
	
	public void setValorContratoExtenso(String valorContratoExtenso) {
		this.valorContratoExtenso.setText(valorContratoExtenso);
	}
	
	public void setQuantidadeParcela(String quantidadeParcela) {
		this.quantidadeParcela.setText(quantidadeParcela);
	}
	
	public void setValorMensalParcela(String valorMensalParcela) {
		this.valorMensalParcela.setText(valorMensalParcela);
	}
	
	public void setValorExtensoParcela(String valorExtensoParcela) {
		this.valorExtensoParcela.setText(valorExtensoParcela);
	}

	public void setValorContrato(TextField valorContrato) {
		this.valorContrato = valorContrato;
	}

	public void setValorContratoExtenso(TextField valorContratoExtenso) {
		this.valorContratoExtenso = valorContratoExtenso;
	}

	public void setValorContratoBean(ValorContrato valorContratoBean) {
		this.valorContratoBean = valorContratoBean;
	}
	
	public void setItinerarioContratoBean(ItinerarioContrato itinerarioContratoBean) {
		this.itinerarioContratoBean = itinerarioContratoBean;
	}
	
	public void setParcelaContratoBean(ParcelaContrato parcelaContratoBean) {
		this.parcelaContratoBean = parcelaContratoBean;
	}

	public void setLocalSaida(String localSaida) {
		this.localSaida.setText(localSaida);
	}

	public void setHoraSaida(String horaSaida) {
		this.horaSaida.setText(horaSaida);
	}
	
	public void setLocalRetorno(String localRetorno) {
		this.localRetorno.setText(localRetorno);
	}

	public void setHoraRetorno(String horaRetorno) {
		this.horaRetorno.setText(horaRetorno);
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public void setBeanPathAdapter(BeanPathAdapter<Cliente> beanPathAdapter) {
		this.beanPathAdapter = beanPathAdapter;
	}
}
