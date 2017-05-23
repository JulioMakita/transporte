package br.com.transporte.controller.contrato;

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

import br.com.juliomakita.fxcomponents.control.mask.BigDecimalField;
import br.com.transporte.controller.cliente.CadastroClienteController;
import br.com.transporte.helper.DialogHelper;
import br.com.transporte.model.ValorContrato;
import br.com.transporte.service.ValorContratoService;
import br.com.transporte.util.FieldUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;

@Named
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CadastroValorContratoController {
	
	private static final Logger LOG = LoggerFactory.getLogger(CadastroValorContratoController.class);
	
	private ApplicationContext context;
	
	private ValorContratoService valorContratoService;

	private CadastroClienteController cadastroClienteController;
	
	@FXML
	private BigDecimalField valor;
	
	@FXML
	private TextField valorExtenso;
	
	private ValorContrato valorContrato;
	
	private StringBuilder sb = new StringBuilder();
	
	@PostConstruct
	public void initialize() {
		FieldUtil.upperCaseTextField(valorExtenso);
		this.context = new ClassPathXmlApplicationContext("classpath:/spring/applicationContext.xml");
		valorContratoService = context.getBean(ValorContratoService.class);
	}

	public void retornarDialogPesquisa(final ActionEvent event){
		try {
			FXMLLoader loader = DialogHelper.loadStageFromPage(event, getClass(), PESQUISA_VALOR_CONTRATO.getPath(), 600, 400);
			PesquisaValorContratoController controller =  loader.<PesquisaValorContratoController>getController();
			controller.setCadastroClienteController(this.cadastroClienteController);
			List<ValorContrato> listValor = controller.getListaValor();
			listValor.clear();
			listValor.add(this.valorContrato);
			controller.listenerPagination(listValor, controller.getValorContratoTable());
			controller.listenerPagination(listValor, controller.getValorContratoTable());
			controller.preencheDataTable(controller.getListaValor());
			controller.init(listValor, controller.getValorContratoTable());
		} catch (final IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	public void cadastrar(){
		
		ValorContrato valorContrato = getValorContrato();
		
		if(validaCliente(valorContrato)){
			DialogHelper.alertInformation("", "", this.sb.toString());
			this.sb.setLength(0);
			return;
		}
		
		if(this.valorContrato != null){
			this.valorContrato.setValorContrato(valor.getValue());
			this.valorContrato.setValorContratoExtenso(valorExtenso.getText());
			this.valorContrato = this.valorContratoService.update(valorContrato);
			DialogHelper.alertInformation("Cadastro de Parcela", "", "Parcela atualizado com sucesso!");
		}else{
			this.valorContrato = this.valorContratoService.save(getValorContrato());
			DialogHelper.alertInformation("Cadastro de Parcela", "", "Parcela cadastrado com sucesso!");
		}

		clearFields();
	}
	
	private boolean validaCliente(final ValorContrato valorContrato){
		
		boolean erroCampos = false;
		sb.append("Os seguintes campos são obrigatórios:\n");
		
		if(valor.getValue().compareTo(BigDecimal.ZERO) == 0){
			sb.append("Valor (Precisa ser acima de 0,00)  \n");
			erroCampos = true;
		}
		
		if(valorExtenso == null || StringUtils.isBlank(valorExtenso.getText())){
			sb.append("Valor Extenso  \n");
			erroCampos = true;
		}
		
		return erroCampos;
	}
	
	private ValorContrato getValorContrato(){
		ValorContrato valorContrato = new ValorContrato();
		valorContrato.setValorContrato(valor.getValue());
		valorContrato.setValorContratoExtenso(valorExtenso.getText());
		return valorContrato;
	}
	
	private void clearFields(){
		this.valor.clear();
		this.valorExtenso.clear();
		this.sb.setLength(0);
	}
	
	public void setValorContrato(ValorContrato valorContrato) {
		this.valorContrato = valorContrato;
	}
	
	public void setValor(BigDecimal valor) {
		this.valor.setValue(valor);
	}
	
	public void setValorExtenso(String valorExtenso) {
		this.valorExtenso.setText(valorExtenso);
	}
	
	public void setCadastroClienteController(CadastroClienteController cadastroClienteController) {
		this.cadastroClienteController = cadastroClienteController;
	}
}
