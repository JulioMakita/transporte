package br.com.transporte.controller.veiculo;

import static br.com.transporte.enumeration.EnumPath.CADASTRO_VEICULO;
import static br.com.transporte.enumeration.EnumPath.HOME;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.juliomakita.fxcomponents.control.mask.PlacaVeiculoField;
import br.com.transporte.annotation.IgnoreBinding;
import br.com.transporte.helper.DialogHelper;
import br.com.transporte.interfaces.PesquisaScreen;
import br.com.transporte.model.Veiculo;
import br.com.transporte.service.VeiculoService;
import br.com.transporte.util.AbstractTableView;
import br.com.transporte.util.ControllerUtils;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import jfxtras.labs.scene.control.BeanPathAdapter;

public class PesquisaVeiculoController extends AbstractTableView<Veiculo> implements PesquisaScreen{
	
	private static final Logger LOG = LoggerFactory.getLogger(PesquisaVeiculoController.class);
	
	@IgnoreBinding
	private VeiculoService veiculoService;
	
	@IgnoreBinding
	private BeanPathAdapter<Veiculo> beanPathAdapter;
	
	@IgnoreBinding
	private ApplicationContext context;

	@FXML private TextField marca;
	
	@FXML private TextField modelo;
	
	@FXML private TextField anoFabricacao;
	
	@FXML private PlacaVeiculoField placa;
	
	@FXML private TextField chassi;	
	
	@FXML private TableView<Veiculo> veiculoTable =  new TableView<Veiculo>();
	
	@FXML private TableColumn<Veiculo, String> gradeMarca;
	
	@FXML private TableColumn<Veiculo, String> gradeModelo;
	
	@FXML private TableColumn<Veiculo, String> gradeAno;
	
	@FXML private TableColumn<Veiculo, String> gradePlaca;
	
	@FXML private TableColumn<Veiculo, String> gradeChassi;

	@IgnoreBinding
	private ObservableList<Veiculo> listaVeiculo;
	
	@IgnoreBinding
	private Veiculo veiculo;
	
	@IgnoreBinding
	private IntegerProperty index = new SimpleIntegerProperty();
	
	@PostConstruct
	public void initialize(){
		FieldUtil.campoNumero(this.anoFabricacao);
		FieldUtil.maxLenght(this.anoFabricacao, 4);
		FieldUtil.upperCaseTextField(marca, modelo, chassi, placa);
		this.veiculo = new Veiculo();
		this.context = new ClassPathXmlApplicationContext("classpath:/spring/applicationContext.xml");
		this.veiculoService = this.context.getBean(VeiculoService.class);
		this.limit = new SimpleIntegerProperty(10);
		bind();
		pesquisar();
	}
	
	@Override
	public void retornar(final ActionEvent event){	
		try {
			DialogHelper.loadStageFromPage(event, getClass(), HOME.getPath(), 800, 450);
		} catch (final IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public void abrirDialogCadastrar(ActionEvent event) {
		try {
			DialogHelper.loadStageFromPage(event, getClass(), CADASTRO_VEICULO.getPath(), 1024, 600);
		} catch (final IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	@Override
	public void pesquisar(){
		
		index.set(-1);
		
		List<Veiculo> list = veiculoService.findGrid((Veiculo) beanPathAdapter.getBean());
	
		this.listaVeiculo =  FXCollections.observableArrayList(list);
		
		listenerPagination(listaVeiculo, veiculoTable);
		
		listenerLimit(listaVeiculo, veiculoTable);

		preencheDataTable(this.listaVeiculo);
		
		init(listaVeiculo, veiculoTable);
	}

	public void preencheDataTable(List<Veiculo> lista){
		
		gradeMarca.setCellValueFactory(new PropertyValueFactory<Veiculo, String>("marca"));
	    gradeModelo.setCellValueFactory(new PropertyValueFactory<Veiculo, String>("modelo"));
        gradeAno.setCellValueFactory(new PropertyValueFactory<Veiculo, String>("ano"));
        gradePlaca.setCellValueFactory(new PropertyValueFactory<Veiculo, String>("placa"));
        gradeChassi.setCellValueFactory(new PropertyValueFactory<Veiculo, String>("chassi"));
        
        veiculoTable.setItems(FXCollections.observableArrayList(lista));
        
        veiculoTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				index.set(lista.indexOf(newValue));
			}
		});
	}
	
	public void bind() {
		try {
			this.beanPathAdapter = ControllerUtils.bindBidirectional(this, this.veiculo);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			LOG.error("Erro ao realizar bind dos campos CadastroClienteController " + e.getMessage());
		}
	}
	
	@Override
	public void editar(ActionEvent event) {
		final Veiculo selectedItem = this.veiculoTable.getSelectionModel().getSelectedItem();
		
		if(selectedItem == null){
			DialogHelper.alertInformation("", "", "Selecione um Veiculo");
			return;
		}
		
		try {
			FXMLLoader loader = DialogHelper.loadStageFromPage(event, getClass(), CADASTRO_VEICULO.getPath(), 1024, 600);
			CadastroVeiculoController controller = loader.<CadastroVeiculoController>getController();
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

		final Veiculo selectedItem = this.veiculoTable.getSelectionModel().getSelectedItem();
		
		if(selectedItem == null){
			DialogHelper.alertInformation("", "", "Selecione um Veiculo");
			return;
		}
		
		Alert alert = DialogHelper.confirmDialog("", "", "Deseja Excluir ?");
		
		Optional<ButtonType> result = alert.showAndWait();
		
		if (result.get() == ButtonType.OK){
			alert.close();
			this.listaVeiculo.remove(i);
			this.veiculoService.delete(selectedItem);
			this.veiculoTable.getSelectionModel().clearSelection();
			this.veiculoTable.setItems(FXCollections.observableArrayList(this.listaVeiculo));
			DialogHelper.alertInformation("", "", "Excluido com sucesso!");
		} else {
		   alert.close();
		}
	}
	
	public List<Veiculo> getListaVeiculo() {
		return listaVeiculo;
	}
	
	public TableView<Veiculo> getVeiculoTable() {
		return veiculoTable;
	}
}
