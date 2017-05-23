package br.com.transporte.controller.contrato;

import static br.com.transporte.enumeration.EnumPath.CADASTRO_PARCELA_CONTRATO;
import static org.apache.commons.lang3.StringUtils.EMPTY;

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
import br.com.transporte.model.ParcelaContrato;
import br.com.transporte.service.ParcelaContratoService;
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
public class PesquisaParcelaContratoController extends AbstractTableView<ParcelaContrato> implements PesquisaScreen{
	
	private static final Logger LOG = LoggerFactory.getLogger(PesquisaParcelaContratoController.class);
	
	private ApplicationContext context;
	
	@FXML
	private ParcelaContratoService parcelaContratoService;
	
	@FXML
	private CadastroClienteController cadastroClienteController;
	
	@FXML
	private TextField parcela;
	
	@FXML
	private BigDecimalField valorMensal;
	
	@FXML 
	private TableView<ParcelaContrato> parcelaContratoTable =  new TableView<ParcelaContrato>();
	
	@FXML 
	private TableColumn<ParcelaContrato, String> gradeParcela;
	
	@FXML 
	private TableColumn<ParcelaContrato, String> gradeValorMensal;
	
	@FXML 
	private TableColumn<ParcelaContrato, String> gradeValorExtenso;
	
	private ObservableList<ParcelaContrato> listaParcelaContrato;
	
	private IntegerProperty index = new SimpleIntegerProperty();
	
	private BigDecimalUtil util = new BigDecimalUtil();
	
	@PostConstruct
	public void initialize() {
		this.context = new ClassPathXmlApplicationContext("classpath:/spring/applicationContext.xml");
		parcelaContratoService = this.context.getBean(ParcelaContratoService.class);
		FieldUtil.campoNumero(this.parcela);
		this.limit = new SimpleIntegerProperty(6);
		pesquisar();
	}
	
	@Override
	public void pesquisar(){
		
		index.set(-1);
		
		List<ParcelaContrato> list = this.parcelaContratoService.findGrid(getParcelaContrato());
		
		this.listaParcelaContrato =  FXCollections.observableArrayList(list);
		
		listenerPagination(this.listaParcelaContrato, parcelaContratoTable);
		
		listenerLimit(this.listaParcelaContrato, parcelaContratoTable);

		preencheParcelaContrato(this.listaParcelaContrato);
		
		preencheParcelaContrato(this.listaParcelaContrato);
		
		init(this.listaParcelaContrato, parcelaContratoTable);
	}
	
	public void preencheParcelaContrato(List<ParcelaContrato> lista){
		
		gradeParcela.setCellValueFactory(new PropertyValueFactory<ParcelaContrato, String>("quantidadeParcela"));
		gradeValorExtenso.setCellValueFactory(new PropertyValueFactory<ParcelaContrato, String>("valorExtensoParcela"));

		gradeValorMensal.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ParcelaContrato, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<ParcelaContrato, String> value) {
				SimpleStringProperty property = new SimpleStringProperty();
				if(value != null && value.getValue() != null){
					property.setValue(util.format(value.getValue().getValorMensalParcela()));
				}
				return property;
			}
		});
        
		parcelaContratoTable.setItems(FXCollections.observableArrayList(lista));
        
		parcelaContratoTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				index.set(lista.indexOf(newValue));
			}
		});
	}
	
	@Override
	public void editar(final ActionEvent event){

		final ParcelaContrato selectedItem = this.parcelaContratoTable.getSelectionModel().getSelectedItem();
		
		if(selectedItem == null){
			DialogHelper.alertInformation("Edição de Parcela", "", "Selecione uma Parcela");
			return;
		}
		
		try {
			FXMLLoader loader = DialogHelper.loadStageFromPage(event, getClass(), CADASTRO_PARCELA_CONTRATO.getPath(), 600, 400);
			CadastroParcelaContratoController controller =  loader.<CadastroParcelaContratoController>getController();
	        controller.setParcelaContrato(selectedItem);
	        controller.setParcela(selectedItem.getQuantidadeParcela().toString());
	        controller.setValorMensal(selectedItem.getValorMensalParcela());
	        controller.setValorExtenso(selectedItem.getValorExtensoParcela());
	        controller.setCadastroClienteController(this.cadastroClienteController);
			
		} catch (final IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	@Override
	public void excluir(){
		
		int i = this.index.get();
		
		final ParcelaContrato selectedItem = this.parcelaContratoTable.getSelectionModel().getSelectedItem();
		
		if(selectedItem == null){
			DialogHelper.alertInformation("Exclusão de Parcela", "", "Selecione uma Parcela");
			return;
		}

		Alert alert = DialogHelper.confirmDialog("Exclusão de Parcela", "", "Deseja Excluir ?");
		
		Optional<ButtonType> result = alert.showAndWait();
		
		if (result.get() == ButtonType.OK){
			alert.close();
			this.listaParcelaContrato.remove(i);
			this.parcelaContratoService.delete(selectedItem);
			this.parcelaContratoTable.getSelectionModel().clearSelection();
			this.parcelaContratoTable.setItems(FXCollections.observableArrayList(listaParcelaContrato));
			DialogHelper.alertInformation("Exclusão de Parcela", "", "Excluido com sucesso!");
		} else {
		   alert.close();
		}
	}
	
	@Override
	public void retornar(final ActionEvent event){	
		Stage appStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		appStage.close();
	}
	
	@Override
	public void abrirDialogCadastrar(final ActionEvent event){
		try {
			FXMLLoader loader = DialogHelper.loadStageFromPage(event, getClass(), CADASTRO_PARCELA_CONTRATO.getPath(), 600, 400);
			CadastroParcelaContratoController controller =  loader.<CadastroParcelaContratoController>getController();
	        controller.setCadastroClienteController(this.cadastroClienteController);
		} catch (final IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	private ParcelaContrato getParcelaContrato(){
		ParcelaContrato parcelaContrato = new ParcelaContrato();
		parcelaContrato.setQuantidadeParcela(parcela.getText().equals(EMPTY) ? null :  Integer.valueOf(parcela.getText()));
		parcelaContrato.setValorMensalParcela((valorMensal.getValue() == null || valorMensal.getValue().compareTo(BigDecimal.ZERO) == 0) ? null : valorMensal.getValue());

		return parcelaContrato;
	}

	public void selecionar(final ActionEvent event) {

		final ParcelaContrato selectedItem = this.parcelaContratoTable.getSelectionModel().getSelectedItem();
		
		if(selectedItem == null){
			DialogHelper.alertInformation("", "", "Selecione um Valor");
			return;
		}
		
		Stage appStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		
		cadastroClienteController.setQuantidadeParcela(String.valueOf(selectedItem.getQuantidadeParcela()));
		cadastroClienteController.setValorMensalParcela(selectedItem.getValorMensalParcela().toString());
		cadastroClienteController.setValorExtensoParcela(selectedItem.getValorExtensoParcela());
		cadastroClienteController.setParcelaContratoBean(selectedItem);
		
		appStage.close();
	}
	
	public void setCadastroClienteController(CadastroClienteController cadastroClienteController) {
		this.cadastroClienteController = cadastroClienteController;
	}
	
	public List<ParcelaContrato> getListaParcelaContrato() {
		return listaParcelaContrato;
	}
	
	public TableView<ParcelaContrato> getParcelaContratoTable() {
		return parcelaContratoTable;
	}
}
