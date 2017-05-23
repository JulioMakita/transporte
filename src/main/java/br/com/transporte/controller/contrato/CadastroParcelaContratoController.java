
package br.com.transporte.controller.contrato;

import static br.com.transporte.enumeration.EnumPath.PESQUISA_PARCELA_CONTRATO;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.juliomakita.fxcomponents.control.mask.BigDecimalField;
import br.com.transporte.annotation.IgnoreBinding;
import br.com.transporte.controller.cliente.CadastroClienteController;
import br.com.transporte.helper.DialogHelper;
import br.com.transporte.model.ParcelaContrato;
import br.com.transporte.service.ParcelaContratoService;
import br.com.transporte.util.FieldUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;

@Named
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CadastroParcelaContratoController {
	
	private static final Logger LOG = LoggerFactory.getLogger(CadastroParcelaContratoController.class);
	
	private ApplicationContext context;
	
	private ParcelaContratoService parcelaContratoService;
	
	private CadastroClienteController cadastroClienteController;
	
	private ParcelaContrato parcelaContrato;

	@FXML
	private TextField parcela;
	
	@FXML
	private BigDecimalField valorMensal;
	
	@FXML
	private TextField valorExtenso;
	
	@IgnoreBinding
	private StringBuilder sb = new StringBuilder();
	
	@PostConstruct
	public void initialize() {
		context = new ClassPathXmlApplicationContext("classpath:/spring/applicationContext.xml");
		parcelaContratoService = context.getBean(ParcelaContratoService.class);
		FieldUtil.campoNumero(this.parcela);
		FieldUtil.upperCaseTextField(valorExtenso);
	}
	
	public void incluir(){
		
		ParcelaContrato parcelaContrato = getParcelaContrato();
		
		if(validaParcela(parcelaContrato)){
			DialogHelper.alertInformation("", "", this.sb.toString());
			this.sb.setLength(0);
			return;
		}
		
		if(this.parcelaContrato != null){
			this.parcelaContrato.setQuantidadeParcela(parcela.getText().equals(EMPTY) ? 1 :Integer.valueOf(parcela.getText()));
			this.parcelaContrato.setValorMensalParcela(valorMensal.getValue());
			this.parcelaContrato.setValorExtensoParcela(valorExtenso.getText());
			this.parcelaContrato = this.parcelaContratoService.update(this.parcelaContrato);
			DialogHelper.alertInformation("", "", "Parcela atualizado com sucesso!");
		}else{
			this.parcelaContrato = this.parcelaContratoService.save(getParcelaContrato());
			DialogHelper.alertInformation("", "", "Parcela cadastrado com sucesso!");
		}
		clearFields();
	}
	
	private boolean validaParcela(final ParcelaContrato parcelaContrato){
		
		boolean erroCampos = false;
		sb.append("Os seguintes campos são obrigatórios:\n");
		
		if(this.parcela == null || this.parcela.getText().equals(EMPTY) || this.parcela.getText().equals("0")){
			this.sb.append("Parcela (Precisa ser acima de 0) \n");
			erroCampos = true;
		}
		
		if(valorMensal == null || valorMensal.getValue().compareTo(BigDecimal.ZERO) == 0){
			sb.append("Valor Mensal (Precisa ser acima de 0,00)  \n");
			erroCampos = true;
		}
		
		if(valorExtenso == null || EMPTY.equals(valorExtenso.getText())){
			sb.append("Valor Extenso");
			erroCampos = true;
		}
		
		return erroCampos;
	}
	
	public void retornarDialogPesquisa(final ActionEvent event){
		try {
			FXMLLoader loader = DialogHelper.loadStageFromPage(event, getClass(), PESQUISA_PARCELA_CONTRATO.getPath(), 600, 400);
			PesquisaParcelaContratoController controller =  loader.<PesquisaParcelaContratoController>getController();
			controller.setCadastroClienteController(this.cadastroClienteController);
			List<ParcelaContrato> listaParcelaContrato = controller.getListaParcelaContrato();
			listaParcelaContrato.clear();
			listaParcelaContrato.add(this.parcelaContrato);
			controller.listenerPagination(listaParcelaContrato, controller.getParcelaContratoTable());
			controller.listenerPagination(listaParcelaContrato, controller.getParcelaContratoTable());
			controller.preencheParcelaContrato(controller.getListaParcelaContrato());
			controller.init(listaParcelaContrato, controller.getParcelaContratoTable());
		} catch (final IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	private ParcelaContrato getParcelaContrato(){
		ParcelaContrato parcelaContrato = new ParcelaContrato();
		parcelaContrato.setQuantidadeParcela(parcela.getText().equals(EMPTY) ? 1 :Integer.valueOf(parcela.getText()));
		parcelaContrato.setValorMensalParcela(valorMensal.getValue());
		parcelaContrato.setValorExtensoParcela(valorExtenso.getText());
		return parcelaContrato;
	}
	
	private void clearFields(){
		this.parcela.clear();
		this.valorMensal.clear();
		this.valorExtenso.clear();
		this.sb.setLength(0);
	}
	
	public void setParcela(String parcela) {
		this.parcela.setText(parcela);;
	}
	
	public void setValorMensal(BigDecimal valorMensal) {
		this.valorMensal.setValue(valorMensal);
	}
	
	public void setValorExtenso(String valorExtenso) {
		this.valorExtenso.setText(valorExtenso);
	}
	
	public void setParcelaContrato(ParcelaContrato parcelaContrato) {
		this.parcelaContrato = parcelaContrato;
	}
	
	public void setCadastroClienteController(CadastroClienteController cadastroClienteController) {
		this.cadastroClienteController = cadastroClienteController;
	}
}
