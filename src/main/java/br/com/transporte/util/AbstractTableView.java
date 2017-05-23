package br.com.transporte.util;

import java.util.List;

import br.com.transporte.annotation.IgnoreBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;

public abstract class AbstractTableView<T> {
	
	@FXML
	@IgnoreBinding
    protected StackPane pagePanel;
	
	@FXML
	@IgnoreBinding
	protected Pagination pagination;

	@IgnoreBinding
	protected IntegerProperty limit;

	public void listenerPagination(List<T> lista, TableView<T> tableView){
		pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                changeTableView(lista, tableView, newValue.intValue(), limit.get());
            }
        });
	}
	
	public void listenerLimit(List<T> lista, TableView<T> tableView){
		limit.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                changeTableView(lista, tableView, pagination.getCurrentPageIndex(), newValue.intValue());
            }
        });
	}
	
	public void changeTableView(List<T> lista, TableView<T> tableView,  int index, int limit) {
		
        int newIndex = index * limit;

        List<T> list = lista.subList(Math.min(newIndex, lista.size()), Math.min(lista.size(), newIndex + limit));
        tableView.getItems().clear();
        tableView.setItems(null);
        ObservableList<T> l = FXCollections.observableArrayList();
        tableView.setItems(l);
        for (T t : list) {
            l.add(t);
        }
    }
	
	public void init(List<T> lista, TableView<T> tableView) {
        resetPage(lista, tableView);
        pagination.setCurrentPageIndex(0);
        changeTableView(lista, tableView, 0, limit.get());
    }
	
	public void resetPage(List<T> lista, TableView<T> tableView) {
        pagination.setPageCount((int) (Math.ceil(lista.size() * 1.0 / limit.get())));
    }
	
	public Pagination getPagination() {
		return pagination;
	}
	
	public IntegerProperty getLimit() {
		return limit;
	}
}
