package br.com.transporte.controller.motorista;

import static br.com.transporte.enumeration.EnumPath.CADASTRO_MOTORISTA;
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
import br.com.transporte.model.Motorista;
import br.com.transporte.service.MotoristaService;
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

public class PesquisaMotoristaController extends AbstractTableView<Motorista> implements PesquisaScreen{
	
	private static final Logger LOG = LoggerFactory.getLogger(PesquisaMotoristaController.class);
	
	@IgnoreBinding
	private ApplicationContext context;
	
	@IgnoreBinding
	private MotoristaService motoristaService;
	
	@IgnoreBinding
	private BeanPathAdapter<Motorista> beanPathAdapter;
	
	@IgnoreBinding
	private Motorista motorista;

	@FXML 
	private TextField nome;
	
	@FXML 
	private TextField cnh;
	
	@FXML 
	private ComboBox<EnumSexo> sexo;
	
	@FXML 
	private TextField rg;
	
	@FXML 
	private CpfField cpf;	
	
	@FXML 
	@IgnoreBinding
	private TableView<Motorista> motoristaTable =  new TableView<Motorista>();
	
	@FXML 
	@IgnoreBinding
	private TableColumn<Motorista, String> gradeNome;
	
	@FXML 
	@IgnoreBinding
	private TableColumn<Motorista, String> gradeCnh;
	
	@FXML 
	@IgnoreBinding
	private TableColumn<Motorista, String> gradeSexo;
	
	@FXML 
	@IgnoreBinding
	private TableColumn<Motorista, String> gradeRg;
	
	@FXML 
	@IgnoreBinding
	private TableColumn<Motorista, String> gradeCpf;
	
	@IgnoreBinding
	private ObservableList<Motorista> listaMotorista;
	
	@IgnoreBinding
	private IntegerProperty index = new SimpleIntegerProperty();
	
	@PostConstruct
	public void initialize() {
		FieldUtil.upperCaseTextField(nome, cnh, rg);
		this.motorista = new Motorista();
		context = new ClassPathXmlApplicationContext("classpath:/spring/applicationContext.xml");
		motoristaService = context.getBean(MotoristaService.class);
		bind();
		this.sexo.getSelectionModel().select(0);
		this.sexo.setItems(FXCollections.observableArrayList(EnumSexo.values()));
		this.limit = new SimpleIntegerProperty(9);
		pesquisar();
	}
	
	@Override
	public void abrirDialogCadastrar(final ActionEvent event){
		try {
			DialogHelper.loadStageFromPage(event, getClass(), CADASTRO_MOTORISTA.getPath(), 1024, 600);
		} catch (final IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	@Override
	public void pesquisar(){
		
		index.set(-1);
		
		List<Motorista> list = motoristaService.findGrid((Motorista) beanPathAdapter.getBean());
		
		this.listaMotorista = FXCollections.observableArrayList(list);

		listenerPagination(this.listaMotorista, motoristaTable);
		
		listenerLimit(this.listaMotorista, motoristaTable);
		
        preencheDataTable(this.listaMotorista);
        
        init(this.listaMotorista, motoristaTable);
	}
	
	@Override
	public void editar(final ActionEvent event){
		
		final Motorista selectedItem = this.motoristaTable.getSelectionModel().getSelectedItem();
		
		if(selectedItem == null){
			DialogHelper.alertInformation("", "", "Selecione um Motorista");
			return;
		}
		
		try {
			FXMLLoader loader = DialogHelper.loadStageFromPage(event, getClass(), CADASTRO_MOTORISTA.getPath(), 1024, 600);
			CadastroMotoristaController controller = loader.<CadastroMotoristaController>getController();
			controller.setBeanPathAdapter(ControllerUtils.bindBidirectional(controller, selectedItem));	
		} catch (final IOException e) {
			LOG.error(e.getMessage(), e);
		} catch (final IllegalArgumentException | IllegalAccessException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	@Override
	public void excluir(){
		
		int i = this.index.get();

		final Motorista selectedItem = this.motoristaTable.getSelectionModel().getSelectedItem();
		
		if(selectedItem == null){
			DialogHelper.alertInformation("", "", "Selecione um Motorista");
			return;
		}
		
		Alert alert = DialogHelper.confirmDialog("", "", "Deseja Excluir ?");
		
		Optional<ButtonType> result = alert.showAndWait();
		
		if (result.get() == ButtonType.OK){
			alert.close();
			this.listaMotorista.remove(i);
			this.motoristaService.delete(selectedItem);
			this.motoristaTable.getSelectionModel().clearSelection();
			this.motoristaTable.setItems(FXCollections.observableArrayList(listaMotorista));
			DialogHelper.alertInformation("Exclusão de Motorista", "", "Excluido com sucesso!");
		} else {
		   alert.close();
		}
	}
	
	@Override
	public void retornar(final ActionEvent event){	
		try {
			DialogHelper.loadStageFromPage(event, getClass(), HOME.getPath(), 800, 450);
		} catch (final IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	public void preencheDataTable(final List<Motorista> list){
		gradeNome.setCellValueFactory(new PropertyValueFactory<Motorista, String>("nome"));
	    gradeCnh.setCellValueFactory(new PropertyValueFactory<Motorista, String>("cnh"));
        gradeSexo.setCellValueFactory(new PropertyValueFactory<Motorista, String>("sexo"));
        gradeRg.setCellValueFactory(new PropertyValueFactory<Motorista, String>("rg"));
        gradeCpf.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Motorista, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<Motorista, String> value) {
				SimpleStringProperty property = new SimpleStringProperty();
				property.setValue(StringUtils.isNoneBlank(value.getValue().getCpf()) ? 
						DocumentUtil.formatCpf(value.getValue().getCpf()) : EMPTY);
				return property;
			}
		});
        
        motoristaTable.setItems(FXCollections.observableArrayList(list));
        
        motoristaTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				index.set(listaMotorista.indexOf(newValue));
			}
		});
	}
	
	private void bind() {
		try {
			this.beanPathAdapter = ControllerUtils.bindBidirectional(this, this.motorista);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			LOG.error("Erro ao realizar bind dos campos CadastroClienteController " + e.getMessage());
		}
	}
	
	public List<Motorista> getListaMotorista() {
		return listaMotorista;
	}
	
	public TableView<Motorista> getMotoristaTable() {
		return motoristaTable;
	}
}
