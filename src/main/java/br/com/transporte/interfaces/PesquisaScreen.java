package br.com.transporte.interfaces;

import javafx.event.ActionEvent;

public interface PesquisaScreen {
	
	void abrirDialogCadastrar(final ActionEvent event);

	void pesquisar();
	
	void editar(final ActionEvent event);
	
	void excluir();
	
	void retornar(final ActionEvent event);
}
