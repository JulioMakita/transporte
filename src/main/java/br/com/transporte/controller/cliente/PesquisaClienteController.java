package br.com.transporte.controller.cliente;

import static br.com.transporte.enumeration.EnumPath.CADASTRO_CLIENTE;
import static br.com.transporte.enumeration.EnumPath.HOME;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.juliomakita.fxcomponents.control.mask.CpfField;
import br.com.juliomakita.fxcomponents.control.util.DocumentUtil;
import br.com.transporte.annotation.IgnoreBinding;
import br.com.transporte.enumeration.EnumSexo;
import br.com.transporte.helper.DialogHelper;
import br.com.transporte.interfaces.PesquisaScreen;
import br.com.transporte.model.Cliente;
import br.com.transporte.relatorio.RelatorioContrato;
import br.com.transporte.service.ClienteService;
import br.com.transporte.util.AbstractTableView;
import br.com.transporte.util.ControllerUtils;
import br.com.transporte.util.FieldUtil;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import jfxtras.labs.scene.control.BeanPathAdapter;

public class PesquisaClienteController extends AbstractTableView<Cliente> implements PesquisaScreen{
	
	@IgnoreBinding
	private static final Logger LOG = LoggerFactory.getLogger(PesquisaClienteController.class);
	
	@IgnoreBinding
	private BeanPathAdapter<Cliente> beanPathAdapter;
	
	@IgnoreBinding
	private ApplicationContext context;
	
	@IgnoreBinding
	private Cliente cliente;
	
	@IgnoreBinding
	private ClienteService clienteService;
	
	@FXML 
	private TextField nome;
	
	@FXML 
	private TextField email;
	
	@FXML 
	private ComboBox<EnumSexo> sexo;
	
	@FXML 
	private TextField rg;
	
	@FXML 
	private CpfField cpf;
	
	@FXML 
	@IgnoreBinding 
	private TableView<Cliente> clienteTable =  new TableView<Cliente>();
	
	@FXML 
	@IgnoreBinding 
	private TableColumn<Cliente, String> gradeNome;
	
	@FXML 
	@IgnoreBinding 
	private TableColumn<Cliente, String> gradeEmail;
	
	@FXML 
	@IgnoreBinding 
	private TableColumn<Cliente, String> gradeSexo;
	
	@FXML 
	@IgnoreBinding
	private TableColumn<Cliente, String> gradeRg;
	
	@FXML 
	@IgnoreBinding
	private TableColumn<Cliente, String> gradeCpf;
	
	@IgnoreBinding
	private IntegerProperty index = new SimpleIntegerProperty();
	
	@IgnoreBinding
	private ObservableList<Cliente> listaCliente;
	
	@PostConstruct
	public void initialize() {
		FieldUtil.upperCaseTextField(nome, rg);
		this.cliente = new Cliente();
		this.context = new ClassPathXmlApplicationContext("classpath:/spring/applicationContext.xml");
		this.clienteService = this.context.getBean(ClienteService.class);
		this.limit = new SimpleIntegerProperty(9);
		bind();
		this.sexo.getSelectionModel().select(0);
		this.sexo.setItems(FXCollections.observableArrayList(EnumSexo.values()));
		pesquisar();
	}
	
	@Override
	public void abrirDialogCadastrar(ActionEvent event) {
		try {
			DialogHelper.loadStageFromPage(event, getClass(), CADASTRO_CLIENTE.getPath(), 1024, 600);
		} catch (final IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public void editar(ActionEvent event) {
		
		final Cliente clienteSelected = clienteTable.getSelectionModel().getSelectedItem();
		
		if(clienteSelected == null){
			DialogHelper.alertInformation("", "", "Selecione um Cliente");
			return;
		}
		
		try {
			FXMLLoader loader = DialogHelper.loadStageFromPage(event, getClass(), CADASTRO_CLIENTE.getPath(), 1024, 600);
			CadastroClienteController controller = loader.<CadastroClienteController>getController();
			controller.setBeanPathAdapter(ControllerUtils.bindBidirectional(controller, clienteSelected));	
		} catch (final IOException e) {
			LOG.error(e.getMessage(), e);
		} catch (final IllegalArgumentException | IllegalAccessException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	public void pesquisar(){
		
		this.index.set(-1);
		
		List<Cliente> list = clienteService.findGrid((Cliente) beanPathAdapter.getBean());
		
		this.listaCliente = FXCollections.observableArrayList(list);
		
		listenerPagination(listaCliente, clienteTable);
		
		listenerLimit(listaCliente, clienteTable);

		preencheDataTable(this.listaCliente);
		
		init(listaCliente, clienteTable);
	}
	
	public void excluir(){
		
		int i = this.index.get();
		
		final Cliente selectedItem = clienteTable.getSelectionModel().getSelectedItem();
		
		if(selectedItem == null){
			DialogHelper.alertInformation("", "", "Selecione um Cliente");
			return;
		}
		
		Alert alert = DialogHelper.confirmDialog("", "", "Deseja Excluir ?");
		
		Optional<ButtonType> result = alert.showAndWait();
		
		if (result.get() == ButtonType.OK){
			alert.close();
			this.listaCliente.remove(i);
			this.clienteService.delete(selectedItem);
			this.clienteTable.getSelectionModel().clearSelection();
			this.clienteTable.setItems(FXCollections.observableArrayList(listaCliente));
			DialogHelper.alertInformation("", "", "Excluido com sucesso!");
		} else {
		   alert.close();
		}
	}
	
	public void retornar(final ActionEvent event){	
		try {
			DialogHelper.loadStageFromPage(event, getClass(), HOME.getPath(), 800, 450);
		} catch (final IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	private void bind() {
		try {
			this.beanPathAdapter = ControllerUtils.bindBidirectional(this, cliente);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			LOG.error("Erro ao realizar bind dos campos CadastroClienteController " + e.getMessage());
		}
	}
	
	public void preencheDataTable(final List<Cliente> lista){
		gradeNome.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nome"));
        gradeEmail.setCellValueFactory(new PropertyValueFactory<Cliente, String>("email"));
        gradeSexo.setCellValueFactory(new PropertyValueFactory<Cliente, String>("sexo"));
        gradeRg.setCellValueFactory(new PropertyValueFactory<Cliente, String>("rg"));        
        gradeCpf.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Cliente, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<Cliente, String> value) {
				SimpleStringProperty property = new SimpleStringProperty();
				property.setValue(StringUtils.isNoneBlank(value.getValue().getCpf()) ? 
						DocumentUtil.formatCpf(value.getValue().getCpf()) : EMPTY);
				return property;
			}
		});
        
        clienteTable.setItems(FXCollections.observableArrayList(lista));
        
        clienteTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				index.set(listaCliente.indexOf(newValue));
			}
		});
	}
	
	public void gerarContrato(){
		
		final Cliente clienteSelected = clienteTable.getSelectionModel().getSelectedItem();

		if(clienteSelected == null){
			DialogHelper.alertInformation("", "", "Selecione um Cliente");
			return;
		}

		Alert alert = DialogHelper.confirmDialog("", "", "Deseja Gerar o Contrato ?");
		
		Optional<ButtonType> result = alert.showAndWait();
		
		if (result.get() == ButtonType.OK){
			RelatorioContrato.geraContrato(clienteSelected);
			DialogHelper.alertInformation("", "", " Contrato gerado com sucesso \n Verifique na pasta contrato");
		}else{
			alert.close();
		}
	}
	

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public List<Cliente> getListaCliente() {
		return this.listaCliente;
	}
	
	public TableView<Cliente> getClienteTable() {
		return clienteTable;
	}
}
