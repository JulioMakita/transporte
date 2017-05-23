package br.com.transporte.controller.contrato;

import static br.com.transporte.enumeration.EnumPath.PESQUISA_ITINERARIO_CONTRATO;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

import br.com.juliomakita.fxcomponents.control.mask.HoraField;
import br.com.transporte.annotation.IgnoreBinding;
import br.com.transporte.controller.cliente.CadastroClienteController;
import br.com.transporte.helper.DialogHelper;
import br.com.transporte.model.ItinerarioContrato;
import br.com.transporte.service.ItinerarioContratoService;
import br.com.transporte.util.FieldUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;

@Named
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CadastroItinerarioContratoController {

	private static final Logger LOG = LoggerFactory.getLogger(CadastroItinerarioContratoController.class);
	
	private ApplicationContext context;
	
	private ItinerarioContratoService itinerarioContratoService;
	
	private CadastroClienteController cadastroClienteController;
	
	private ItinerarioContrato itinerarioContrato;
	
	@FXML
	private TextField localSaida;
	
	@FXML
	private HoraField horaSaida;
	
	@FXML
	private HoraField horaRetorno;
	
	@FXML
	private TextField localRetorno;
	
	@IgnoreBinding
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
	
	@IgnoreBinding
	private StringBuilder sb = new StringBuilder();
	
	@PostConstruct
	public void initialize() {	
		FieldUtil.upperCaseTextField(localSaida, localRetorno);
		context = new ClassPathXmlApplicationContext("classpath:/spring/applicationContext.xml");
		itinerarioContratoService = context.getBean(ItinerarioContratoService.class);
	}
	
	public void cadastrar(){
		
		ItinerarioContrato itinerarioContrato = getItinerarioContrato(new ItinerarioContrato());
		
		if(validaItinerario(itinerarioContrato)){
			DialogHelper.alertInformation("", "", this.sb.toString());
			this.sb.setLength(0);
			return;
		}
		
		if(this.itinerarioContrato != null){
			this.itinerarioContrato = this.itinerarioContratoService.update(itinerarioContrato);
			DialogHelper.alertInformation("", "", "Itinerário atualizado com sucesso!");
		}else{
			this.itinerarioContrato = this.itinerarioContratoService.save(itinerarioContrato);
			DialogHelper.alertInformation("", "", "Itinerário cadastrado com sucesso!");
		}
		clear();
	}
	
	private ItinerarioContrato getItinerarioContrato(ItinerarioContrato itinerarioContrato) {

		itinerarioContrato.setLocalSaida(localSaida.getText());
		itinerarioContrato.setLocalRetorno(localRetorno.getText());
		
		if(horaSaida != null && !horaSaida.getText().equals("__:__")){
			itinerarioContrato.setHoraSaida(LocalTime.parse(horaSaida.getText(), formatter));
		}
		
		if(horaRetorno != null && !horaRetorno.getText().equals("__:__")){
			itinerarioContrato.setHoraRetorno(LocalTime.parse(horaRetorno.getText(), formatter));
		}

		return itinerarioContrato;
	}
	
	private boolean validaItinerario(final ItinerarioContrato itinerarioContrato){
		
		boolean erroCampos = false;
		sb.append("Os seguintes campos são obrigatórios:\n");
		
		if(localSaida == null || StringUtils.isBlank(localSaida.getText())){
			sb.append("Local Saída \n");
			erroCampos = true;
		}
		
		if(horaSaida == null || horaSaida.getText().equals("__:__")){
			sb.append("Hora Saida \n");
			erroCampos = true;
		}
		
		if(localRetorno == null || StringUtils.isBlank(localRetorno.getText())){
			sb.append("Local Retorno \n");
			erroCampos = true;
		}
		
		if(horaRetorno == null || horaRetorno.getText().equals("__:__")){
			sb.append("Hora Retorno \n");
			erroCampos = true;
		}

		return erroCampos;
	}

	public void retornarDialogPesquisa(final ActionEvent event){
		try {
			FXMLLoader loader = DialogHelper.loadStageFromPage(event, getClass(), PESQUISA_ITINERARIO_CONTRATO.getPath(), 600, 400);
			PesquisaItinerarioContratoController controller =  loader.<PesquisaItinerarioContratoController>getController();
			controller.setCadastroClienteController(this.cadastroClienteController);
			List<ItinerarioContrato> listaItinerario = controller.getListaItinerario();
			listaItinerario.clear();
			listaItinerario.add(this.itinerarioContrato);
			controller.listenerPagination(listaItinerario, controller.getItinerarioContratoTable());
			controller.listenerPagination(listaItinerario, controller.getItinerarioContratoTable());
			controller.preencheDataTable(controller.getListaItinerario());
			controller.init(listaItinerario, controller.getItinerarioContratoTable());
		} catch (final IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	private void clear(){
		this.localSaida.clear();
		this.horaSaida.clear();
		this.localRetorno.clear();
		this.horaRetorno.clear();
		this.sb.setLength(0);
	}
	
	public void setCadastroClienteController(CadastroClienteController cadastroClienteController) {
		this.cadastroClienteController = cadastroClienteController;
	}
	
	public void setHoraSaida(String horaSaida) {
		this.horaSaida.setText(horaSaida);
	}
	
	public void setHoraRetorno(String horaRetorno) {
		this.horaRetorno.setText(horaRetorno);
	}
	
	public void setLocalSaida(String localSaida) {
		this.localSaida.setText(localSaida);
	}
	
	public void setLocalRetorno(String localRetorno) {
		this.localRetorno.setText(localRetorno);
	}
	
	public void setItinerarioContrato(ItinerarioContrato itinerarioContrato) {
		this.itinerarioContrato = itinerarioContrato;
	}
}
