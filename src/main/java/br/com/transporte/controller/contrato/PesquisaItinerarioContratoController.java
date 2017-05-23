package br.com.transporte.controller.contrato;

import static br.com.transporte.enumeration.EnumPath.CADASTRO_ITINERARIO_CONTRATO;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

import br.com.juliomakita.fxcomponents.control.mask.HoraField;
import br.com.transporte.controller.cliente.CadastroClienteController;
import br.com.transporte.helper.DialogHelper;
import br.com.transporte.interfaces.PesquisaScreen;
import br.com.transporte.model.ItinerarioContrato;
import br.com.transporte.service.ItinerarioContratoService;
import br.com.transporte.util.AbstractTableView;
import br.com.transporte.util.FieldUtil;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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

@Named
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PesquisaItinerarioContratoController extends AbstractTableView<ItinerarioContrato> implements PesquisaScreen{
	
	private static final Logger LOG = LoggerFactory.getLogger(PesquisaItinerarioContratoController.class);
	
	private static final String MENSAGEM_EXCLUSAO = "Exclusão de Itinerário";
	
	private ApplicationContext context;
	
	private ItinerarioContratoService itinerarioContratoService;
	
	@FXML
	private CadastroClienteController cadastroClienteController;
	
	@FXML
	private TextField localSaida;
	
	@FXML
	private HoraField horaSaida;
	
	@FXML
	private TextField localRetorno;
	
	@FXML
	private HoraField horaRetorno;
	
	@FXML 
	private TableView<ItinerarioContrato> itinerarioContratoTable =  new TableView<ItinerarioContrato>();
	
	@FXML 
	private TableColumn<ItinerarioContrato, String> gradeLocalSaida;
	
	@FXML 
	private TableColumn<ItinerarioContrato, String> gradeHoraSaida;
	
	@FXML 
	private TableColumn<ItinerarioContrato, String> gradeLocalRetorno;
	
	@FXML 
	private TableColumn<ItinerarioContrato, String> gradeHoraRetorno;
	
	private ObservableList<ItinerarioContrato> listaItinerario;
	
	private IntegerProperty index = new SimpleIntegerProperty();
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
	
	@PostConstruct
	public void initialize() {
		FieldUtil.upperCaseTextField(localSaida, localRetorno);
		this.context = new ClassPathXmlApplicationContext("classpath:/spring/applicationContext.xml");
		this.itinerarioContratoService = this.context.getBean(ItinerarioContratoService.class);
		this.limit = new SimpleIntegerProperty(4);
		pesquisar();
	}
	
	@Override
	public void pesquisar(){
		
		index.set(-1);
		
		List<ItinerarioContrato> list = this.itinerarioContratoService.findGrid(getItinerarioContrato());
		
		this.listaItinerario = FXCollections.observableArrayList(list);
		
		listenerPagination(listaItinerario, itinerarioContratoTable);
		
		listenerLimit(listaItinerario, itinerarioContratoTable);

		preencheDataTable(this.listaItinerario);
		
		init(listaItinerario, itinerarioContratoTable);
	}
	
	public void preencheDataTable(final List<ItinerarioContrato> lista){
		
		gradeLocalSaida.setCellValueFactory(new PropertyValueFactory<ItinerarioContrato, String>("localSaida"));
		gradeHoraSaida.setCellValueFactory(new PropertyValueFactory<ItinerarioContrato, String>("horaSaida"));
		gradeLocalRetorno.setCellValueFactory(new PropertyValueFactory<ItinerarioContrato, String>("localRetorno"));
		gradeHoraRetorno.setCellValueFactory(new PropertyValueFactory<ItinerarioContrato, String>("horaRetorno"));
        
		itinerarioContratoTable.setItems(FXCollections.observableArrayList(lista));
        
		itinerarioContratoTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				index.set(lista.indexOf(newValue));
			}
		});
	}
	
	@Override
	public void excluir(){
		
		int i = this.index.get();

		final ItinerarioContrato selectedItem = this.itinerarioContratoTable.getSelectionModel().getSelectedItem();
		
		if(selectedItem == null){
			DialogHelper.alertInformation(MENSAGEM_EXCLUSAO, "", "Selecione um Itinerário");
			return;
		}
		
		Alert alert = DialogHelper.confirmDialog(MENSAGEM_EXCLUSAO, "", "Deseja Excluir ?");
		
		Optional<ButtonType> result = alert.showAndWait();
		
		if (result.get() == ButtonType.OK){
			alert.close();
			this.listaItinerario.remove(i);
			this.itinerarioContratoService.delete(selectedItem);
			this.itinerarioContratoTable.getSelectionModel().clearSelection();
			this.itinerarioContratoTable.setItems(FXCollections.observableArrayList(this.listaItinerario));
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
	
	@Override
	public void abrirDialogCadastrar(final ActionEvent event){
		try {
			FXMLLoader loader = DialogHelper.loadStageFromPage(event, getClass(), CADASTRO_ITINERARIO_CONTRATO.getPath(), 600, 400);
			CadastroItinerarioContratoController controller =  loader.<CadastroItinerarioContratoController>getController();
			controller.setCadastroClienteController(this.cadastroClienteController);
		} catch (final IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	private ItinerarioContrato getItinerarioContrato() {
		
		ItinerarioContrato itinerarioContrato = new ItinerarioContrato();
		itinerarioContrato.setLocalSaida(localSaida.getText());
		itinerarioContrato.setLocalRetorno(localRetorno.getText());
		
		if(horaSaida.getText() != null && !horaSaida.getText().equals("__:__")){
			itinerarioContrato.setHoraSaida(LocalTime.parse(horaSaida.getText(), formatter));
		}
		
		if(horaRetorno.getText() != null && !horaRetorno.getText().equals("__:__")){
			itinerarioContrato.setHoraRetorno(LocalTime.parse(horaRetorno.getText(), formatter));
		}

		return itinerarioContrato;
	}

	@Override
	public void editar(final ActionEvent event) {
		
		final ItinerarioContrato selectedItem = this.itinerarioContratoTable.getSelectionModel().getSelectedItem();
		
		if(selectedItem == null){
			DialogHelper.alertInformation("", "", "Selecione um Valor");
			return;
		}
		
		try {
			FXMLLoader loader = DialogHelper.loadStageFromPage(event, getClass(), CADASTRO_ITINERARIO_CONTRATO.getPath(), 600, 400);
			CadastroItinerarioContratoController controller =  loader.<CadastroItinerarioContratoController>getController();
			controller.setCadastroClienteController(this.cadastroClienteController);
			controller.setHoraSaida(formatter.format(selectedItem.getHoraSaida()));
			controller.setHoraRetorno(formatter.format(selectedItem.getHoraRetorno()));
			controller.setLocalSaida(selectedItem.getLocalSaida());
			controller.setLocalRetorno(selectedItem.getLocalRetorno());
			controller.setItinerarioContrato(selectedItem);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	public void selecionar(final ActionEvent event) {
		
		final ItinerarioContrato selectedItem = this.itinerarioContratoTable.getSelectionModel().getSelectedItem();
		
		if(selectedItem == null){
			DialogHelper.alertInformation("", "", "Selecione um Valor");
			return;
		}
		
		Stage appStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		
		cadastroClienteController.setLocalSaida(selectedItem.getLocalSaida());
		cadastroClienteController.setHoraSaida(formatter.format(selectedItem.getHoraSaida()));
		cadastroClienteController.setLocalRetorno(selectedItem.getLocalRetorno());
		cadastroClienteController.setHoraRetorno(formatter.format(selectedItem.getHoraRetorno()));
		cadastroClienteController.setItinerarioContratoBean(selectedItem);
		
		appStage.close();
		
	}
	
	public void setCadastroClienteController(CadastroClienteController cadastroClienteController) {
		this.cadastroClienteController = cadastroClienteController;
	}
	
	public List<ItinerarioContrato> getListaItinerario() {
		return listaItinerario;
	}
	
	public TableView<ItinerarioContrato> getItinerarioContratoTable() {
		return itinerarioContratoTable;
	}
}
