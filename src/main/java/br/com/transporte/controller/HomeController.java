package br.com.transporte.controller;

import static br.com.transporte.enumeration.EnumPath.PESQUISA_CLIENTE;
import static br.com.transporte.enumeration.EnumPath.PESQUISA_MOTORISTA;
import static br.com.transporte.enumeration.EnumPath.PESQUISA_VEICULO;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.transporte.helper.DialogHelper;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class HomeController {
	
	private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);
	
	public void pesquisaCliente(final ActionEvent event) {		
		try {
			DialogHelper.loadScreen(event, getClass(), PESQUISA_CLIENTE.getPath(), 1024, 600);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	public void pesquisaMotorista(final ActionEvent event) {	
		try {
			DialogHelper.loadScreen(event, getClass(), PESQUISA_MOTORISTA.getPath(), 1024, 600);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	public void pesquisaVeiculo(final ActionEvent event) {	
		try {
			DialogHelper.loadScreen(event, getClass(), PESQUISA_VEICULO.getPath(), 1024, 600);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	public void sair(final ActionEvent event){
		Stage appStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		appStage.close();
	}
}
