package br.com.transporte.controller.contrato;

import static br.com.transporte.enumeration.EnumPath.CADASTRO_VALOR_CONTRATO;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.juliomakita.fxcomponents.control.mask.BigDecimalField;
import br.com.juliomakita.fxcomponents.control.util.BigDecimalUtil;
import br.com.transporte.controller.cliente.CadastroClienteController;
import br.com.transporte.helper.DialogHelper;
import br.com.transporte.interfaces.PesquisaScreen;
import br.com.transporte.model.ValorContrato;
import br.com.transporte.service.ValorContratoService;
import br.com.transporte.util.AbstractTableView;
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
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

@Named
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PesquisaValorContratoController extends AbstractTableView<ValorContrato> implements PesquisaScreen{

	private static final Logger LOG = LoggerFactory.getLogger(PesquisaValorContratoController.class);
	
	private static final String MENSAGEM_EXCLUSAO = "Exclusão de Valor";
	
	@FXML
	private CadastroClienteController cadastroClienteController;
	
	private ApplicationContext context;
	
	private ValorContratoService valorContratoService;
	
	@FXML
	private BigDecimalField valor;
	
	@FXML
	private TextField valorExtenso;
	
	@FXML 
	private TableView<ValorContrato> valorContratoTable =  new TableView<ValorContrato>();
	
	@FXML 
	private TableColumn<ValorContrato, String> gradeValor;
	
	@FXML 
	private TableColumn<ValorContrato, String> gradeValorExtenso;
	
	private ObservableList<ValorContrato> listaValor;
	
	private IntegerProperty index = new SimpleIntegerProperty();
	
	private BigDecimalUtil util = new BigDecimalUtil();
	
	@PostConstruct
	public void initialize() {
		FieldUtil.upperCaseTextField(valorExtenso);
		this.context = new ClassPathXmlApplicationContext("classpath:/spring/applicationContext.xml");
		this.valorContratoService = this.context.getBean(ValorContratoService.class);
		this.limit = new SimpleIntegerProperty(6);
		pesquisar();
	}

	@Override
	public void abrirDialogCadastrar(final ActionEvent event){
		try {
			FXMLLoader loader = DialogHelper.loadStageFromPage(event, getClass(), CADASTRO_VALOR_CONTRATO.getPath(), 600, 400);
			CadastroValorContratoController controller =  loader.<CadastroValorContratoController>getController();
			controller.setCadastroClienteController(this.cadastroClienteController);
		} catch (final IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public void pesquisar() {
		
		index.set(-1);
		
		List<ValorContrato> list = this.valorContratoService.findGrid(getValorContrato());
		
		this.listaValor = FXCollections.observableArrayList(list);
		
		listenerPagination(this.listaValor, valorContratoTable);
		
		listenerLimit(this.listaValor, valorContratoTable);

		preencheDataTable(this.listaValor);
		
		init(this.listaValor, valorContratoTable);
	}
	
	public void preencheDataTable(final List<ValorContrato> lista){
		gradeValor.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ValorContrato, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<ValorContrato, String> value) {
				SimpleStringProperty property = new SimpleStringProperty();
				property.setValue(util.format(value.getValue().getValorContrato()));
				return property;
			}
		});
		
		gradeValorExtenso.setCellValueFactory(new PropertyValueFactory<ValorContrato, String>("valorContratoExtenso"));
        
		valorContratoTable.setItems(FXCollections.observableArrayList(lista));
        
		valorContratoTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				index.set(lista.indexOf(newValue));
			}
		});
	}

	@Override
	public void editar(final ActionEvent event) {
		
		final ValorContrato selectedItem = this.valorContratoTable.getSelectionModel().getSelectedItem();
		
		if(selectedItem == null){
			DialogHelper.alertInformation("", "", "Selecione um Valor");
			return;
		}

		try {
			FXMLLoader loader = DialogHelper.loadStageFromPage(event, getClass(), CADASTRO_VALOR_CONTRATO.getPath(), 600, 400);
			CadastroValorContratoController controller =  loader.<CadastroValorContratoController>getController();
	        controller.setValorContrato(selectedItem);
	        controller.setValor(selectedItem.getValorContrato());
	        controller.setValorExtenso(selectedItem.getValorContratoExtenso());
	        controller.setCadastroClienteController(this.cadastroClienteController);
			
		} catch (final IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public void excluir() {
		int i = this.index.get();

		final ValorContrato selectedItem = this.valorContratoTable.getSelectionModel().getSelectedItem();
		
		if(selectedItem == null){
			DialogHelper.alertInformation(MENSAGEM_EXCLUSAO, "", "Selecione um Valor");
			return;
		}
		
		Alert alert = DialogHelper.confirmDialog(MENSAGEM_EXCLUSAO, "", "Deseja Excluir ?");
		
		Optional<ButtonType> result = alert.showAndWait();
		
		if (result.get() == ButtonType.OK){
			alert.close();
			this.listaValor.remove(i);
			this.valorContratoService.delete(selectedItem);
			this.valorContratoTable.getSelectionModel().clearSelection();
			this.valorContratoTable.setItems(FXCollections.observableArrayList(this.listaValor));
			DialogHelper.alertInformation(MENSAGEM_EXCLUSAO, "", "Excluido com sucesso!");
		} else {
		   alert.close();
		}
		
	}
	
	@Override
	public void retornar(final ActionEvent event){	
		Stage appStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		appStage.close();
	}
	
	private ValorContrato getValorContrato(){
		ValorContrato valorContrato = new ValorContrato();
		valorContrato.setValorContrato((valor.getValue() == null || valor.getValue().compareTo(BigDecimal.ZERO) == 0) ? null : valor.getValue());
		valorContrato.setValorContratoExtenso(valorExtenso.getText());
		return valorContrato;
	}

	public void selecionar(final ActionEvent event) {
		
		final ValorContrato selectedItem = this.valorContratoTable.getSelectionModel().getSelectedItem();
		
		if(selectedItem == null){
			DialogHelper.alertInformation("", "", "Selecione um Valor");
			return;
		}
		
		Stage appStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		
		cadastroClienteController.setValorContratoExtenso(selectedItem.getValorContratoExtenso());
		cadastroClienteController.setValorContrato(selectedItem.getValorContrato());
		cadastroClienteController.setValorContratoBean(selectedItem);
		
		appStage.close();
	}
	
	public void setCadastroClienteController(CadastroClienteController cadastroClienteController) {
		this.cadastroClienteController = cadastroClienteController;
	}
	
	public List<ValorContrato> getListaValor() {
		return this.listaValor;
	}
	
	public TableView<ValorContrato> getValorContratoTable() {
		return valorContratoTable;
	}
}
