package br.com.transporte.controller.motorista;

import static br.com.transporte.enumeration.EnumPath.PESQUISA_MOTORISTA;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Named;

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
import br.com.juliomakita.fxcomponents.control.mask.TelefoneField;
import br.com.transporte.annotation.BeanAttribute;
import br.com.transporte.annotation.IgnoreBinding;
import br.com.transporte.enumeration.EnumSexo;
import br.com.transporte.helper.DialogHelper;
import br.com.transporte.model.Motorista;
import br.com.transporte.service.MotoristaService;
import br.com.transporte.util.ControllerUtils;
import br.com.transporte.util.FieldUtil;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import jfxtras.labs.scene.control.BeanPathAdapter;

@Named
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CadastroMotoristaController {
	
	private static final Logger LOG = LoggerFactory.getLogger(CadastroMotoristaController.class);
	private static final String ENDERECO = "endereco";

	@IgnoreBinding
	private MotoristaService motoristaService;
	
	@IgnoreBinding
	private BeanPathAdapter<Motorista> beanPathAdapter;
	
	private ApplicationContext context;
	
	@FXML 
	private TextField nome;
	
	@FXML 
	private DateTimeField dataNascimento;
	
	@FXML 
	private ComboBox<EnumSexo> sexo;
	
	@FXML 
	private TextField cnh;

	@FXML 
	private TextField rg;
	
	@FXML 
	private CpfField cpf;
	
	@FXML 
	private TelefoneField telResidencial;
	
	@FXML 
	private CelularField telCelular;
	
	@FXML 
	@BeanAttribute(type=ENDERECO)
	private TextField endereco;
	
	@FXML 
	@BeanAttribute(type=ENDERECO)
	private TextField numero;
	
	@FXML 
	@BeanAttribute(type=ENDERECO)
	private TextField complemento;
	
	@FXML 
	@BeanAttribute(type=ENDERECO)
	private CepField cep;
	
	@FXML 
	@BeanAttribute(type=ENDERECO)
	private TextField cidade;
	
	@FXML 
	@BeanAttribute(type=ENDERECO)
	private TextField estado;
	
	@FXML 
	@BeanAttribute(type=ENDERECO)
	private TextField bairro;
	
	@IgnoreBinding
	private Motorista motorista;
	
	@PostConstruct
	public void initialize() {
		FieldUtil.upperCaseTextField(nome, cnh, rg, endereco, complemento, numero, cidade, estado, bairro);
		this.motorista = new Motorista();
		bind();
		this.context = new ClassPathXmlApplicationContext("classpath:/spring/applicationContext.xml");
		this.motoristaService = this.context.getBean(MotoristaService.class);
		this.sexo.getSelectionModel().select(1);
		this.sexo.setItems(FXCollections.observableArrayList(EnumSexo.MASCULINO, EnumSexo.FEMININO));
	}
	
	@FXML
	public void cadastrar(){
		
		Motorista motorista = (Motorista)beanPathAdapter.getBean();
		
		if(motorista.getId() != null){
			this.motorista = this.motoristaService.update(motorista);
			DialogHelper.alertInformation("", "", "Motorista alterado com sucesso!");
		}else{
			this.motorista = this.motoristaService.save(motorista);
			DialogHelper.alertInformation("", "", "Motorista cadastrado com sucesso!");
		}

		try {
			ControllerUtils.clearFields(this);
			this.sexo.setItems(FXCollections.observableArrayList(EnumSexo.MASCULINO, EnumSexo.FEMININO));
			this.sexo.getSelectionModel().select(0);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	public void retornar(ActionEvent event) throws IOException{
		try {
			FXMLLoader loader = DialogHelper.loadScreen(event, getClass(), PESQUISA_MOTORISTA.getPath(), 1024, 600);
			PesquisaMotoristaController controller = loader.<PesquisaMotoristaController>getController();
			if(this.motorista != null && this.motorista.getId() != null){
				List<Motorista> listaMotorista = controller.getListaMotorista();
				listaMotorista.clear();
				listaMotorista.add(this.motoristaService.findOne(this.motorista.getId()));
				controller.listenerPagination(listaMotorista, controller.getMotoristaTable());
				controller.listenerPagination(listaMotorista, controller.getMotoristaTable());
				controller.preencheDataTable(controller.getListaMotorista());
				controller.init(listaMotorista, controller.getMotoristaTable());
			}
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	public void bind() {
		try {
			this.beanPathAdapter = ControllerUtils.bindBidirectional(this, this.motorista);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			LOG.error("Erro ao realizar bind dos campos CadastroClienteController " + e.getMessage());
		}
	}
	
	public void setBeanPathAdapter(final BeanPathAdapter<Motorista> beanPathAdapter) {
		this.beanPathAdapter = beanPathAdapter;
	}
}
