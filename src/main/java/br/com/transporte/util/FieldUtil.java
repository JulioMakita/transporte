package br.com.transporte.util;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FieldUtil {

	public static String removeCaracteresMask(String text){
		
		if(text != null) {
			text = text.replace(".", EMPTY).replace("-", EMPTY).replace("(", EMPTY).replace(")", EMPTY).replace("_", EMPTY);
		}
		
		return text;
	}
	
	public static void campoNumero(final TextField field){
		field.textProperty().addListener((observable, oldValue, newValue) -> {
		    if(!newValue.matches("[0-9]*")){
		    	field.setText(oldValue);
		    }
		});
	}
	
	public static void maxLenght(final TextField field, final Integer limit){
		field.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue()) {
                    if (field.getText().length() >= limit) {
                    	field.setText(field.getText().substring(0, limit));
                    }
                }
            }
        });
	}
	
	public static void upperCaseTextField(final TextField... fields){
		for(TextField field : fields){
			field.textProperty().addListener((ov, oldValue, newValue) -> {
				field.setText(newValue.toUpperCase());
			});
		}
	}
	
	public static void upperCaseTextArea(final TextArea... fields){
		for(TextArea field : fields){
			field.textProperty().addListener((ov, oldValue, newValue) -> {
				field.setText(newValue.toUpperCase());
			});
		}
	}
}
