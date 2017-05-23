package br.com.transporte.controller.veiculo;

import static br.com.transporte.enumeration.EnumPath.PESQUISA_VEICULO;

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

import br.com.juliomakita.fxcomponents.control.mask.PlacaVeiculoField;
import br.com.transporte.annotation.IgnoreBinding;
import br.com.transporte.helper.DialogHelper;
import br.com.transporte.model.Veiculo;
import br.com.transporte.service.VeiculoService;
import br.com.transporte.util.ControllerUtils;
import br.com.transporte.util.FieldUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import jfxtras.labs.scene.control.BeanPathAdapter;

@Named
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CadastroVeiculoController {
	
	private static final Logger LOG = LoggerFactory.getLogger(CadastroVeiculoController.class);
	
	@IgnoreBinding
	private BeanPathAdapter<Veiculo> beanPathAdapter;
	
	@IgnoreBinding
	private VeiculoService veiculoService;
	
	@IgnoreBinding
	private ApplicationContext context;
	
	@FXML private TextField marca;
	
	@FXML private TextField modelo;
	
	@FXML private TextField anoFabricacao;
	
	@FXML private PlacaVeiculoField placa;
	
	@FXML private TextField chassi;	
	
	@FXML private TextField renavam;	
	
	@IgnoreBinding
	private Veiculo veiculo;
	
	@PostConstruct
	public void initialize(){
		FieldUtil.campoNumero(this.anoFabricacao);
		FieldUtil.maxLenght(this.anoFabricacao, 4);
		FieldUtil.upperCaseTextField(marca, modelo, chassi, placa, renavam);
		this.veiculo = new Veiculo();
		this.context = new ClassPathXmlApplicationContext("classpath:/spring/applicationContext.xml");
		this.veiculoService = this.context.getBean(VeiculoService.class);
		bind();
	}
	
	public void cadastrar(){
		
		Veiculo veiculo = (Veiculo)this.beanPathAdapter.getBean();
		
		if(veiculo.getId() != null){
			this.veiculo = this.veiculoService.update(veiculo);
			DialogHelper.alertInformation("", "", "Veiculo alterado com sucesso!");
		}else{
			this.veiculo = this.veiculoService.save(veiculo);
			DialogHelper.alertInformation("", "", "Veiculo cadastrado com sucesso!");
		}
		
		clearFields();
		
	}

	public void retornar(ActionEvent event) {
		
		try {
			FXMLLoader loader = DialogHelper.loadScreen(event, getClass(), PESQUISA_VEICULO.getPath(), 1024, 600);
			PesquisaVeiculoController controller = loader.<PesquisaVeiculoController>getController();
			if(this.veiculo != null && this.veiculo.getId() != null){
				List<Veiculo> listaVeiculo = controller.getListaVeiculo();
				listaVeiculo.clear();
				listaVeiculo.add(this.veiculoService.findOne(this.veiculo.getId()));
				controller.listenerPagination(listaVeiculo, controller.getVeiculoTable());
				controller.listenerPagination(listaVeiculo, controller.getVeiculoTable());
				controller.preencheDataTable(controller.getListaVeiculo());
				controller.init(listaVeiculo, controller.getVeiculoTable());
			}
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	public void clearFields(){
		try {
			ControllerUtils.clearFields(this);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	public void bind() {
		try {
			this.beanPathAdapter = ControllerUtils.bindBidirectional(this, this.veiculo);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			LOG.error("Erro ao realizar bind dos campos CadastroClienteController " + e.getMessage());
		}
	}
	
	public void setBeanPathAdapter(final BeanPathAdapter<Veiculo> beanPathAdapter) {
		this.beanPathAdapter = beanPathAdapter;
	}
}
