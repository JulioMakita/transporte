package br.com.transporte.util;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.juliomakita.fxcomponents.control.AbstractTextField;
import br.com.juliomakita.fxcomponents.control.AbstractValidationTextField;
import br.com.juliomakita.fxcomponents.control.annotation.DisableVirtualKeyboard;
import br.com.juliomakita.fxcomponents.validation.interfaces.DisableableVirtualKeyboard;
import br.com.juliomakita.fxcomponents.validation.interfaces.ValidatableField;
import br.com.juliomakita.fxcomponents.validation.interfaces.ValuableField;
import br.com.transporte.annotation.BeanAttribute;
import br.com.transporte.annotation.IgnoreBinding;
import br.com.transporte.annotation.IgnoreLoadComboBox;
import br.com.transporte.annotation.IgnoreValidation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import jfxtras.labs.scene.control.BeanPathAdapter;
import jfxtras.labs.scene.control.BeanPathAdapter.FieldPathValue;

public class ControllerUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ControllerUtils.class);
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <C, B> BeanPathAdapter<B> bindBidirectional(C controller, B bean) throws IllegalArgumentException, IllegalAccessException {

		BeanPathAdapter<B> beanPathAdapter = new BeanPathAdapter<B>(bean);
		
		for (Field field : controller.getClass().getDeclaredFields()) {

			// Somente FXML
			if (field.isAnnotationPresent(FXML.class)) {
				
				String beanName = EMPTY;

				field.setAccessible(true);
				
				if(field.isAnnotationPresent(BeanAttribute.class)){
					String type = field.getAnnotation(BeanAttribute.class).type();
					beanName = type + "." + field.getName();
				}else{
					beanName = field.getName();
				}

				if (javafx.scene.control.ComboBox.class.isAssignableFrom(field.getType())) {

					ParameterizedType aType = (ParameterizedType) field.getGenericType();

					Class<?> type = (Class<?>) aType.getActualTypeArguments()[0];

					ComboBox comboBox = (ComboBox) field.get(controller);

					if (type.isEnum() && !field.isAnnotationPresent(IgnoreLoadComboBox.class)) {
						loadComboBoxValues(comboBox, type);
					}

					if (!field.isAnnotationPresent(IgnoreBinding.class)) {

						beanPathAdapter.bindBidirectional(beanName, comboBox.valueProperty(), type);
					}

				} else if (!field.isAnnotationPresent(IgnoreBinding.class)) {
	
					if (ValuableField.class.isAssignableFrom(field.getType())) {

						Class<?> type = (Class<?>) ((ParameterizedType) field.getType().getGenericSuperclass()).getActualTypeArguments()[0];

						beanPathAdapter.bindBidirectional(beanName, ((ValuableField) field.get(controller)).valueProperty(), type);

					} else if (javafx.scene.control.TextField.class.isAssignableFrom(field.getType())) {

						beanPathAdapter.bindBidirectional(beanName, ((javafx.scene.control.TextField) field.get(controller)).textProperty());

					} else if (CheckBox.class.isAssignableFrom(field.getType())) {

						beanPathAdapter.bindBidirectional(beanName, ((CheckBox) field.get(controller)).selectedProperty());

					} else if (Text.class.isAssignableFrom(field.getType())) {

						beanPathAdapter.bindBidirectional(beanName, ((Text) field.get(controller)).textProperty());

					} else if (ListView.class.isAssignableFrom(field.getType())) {

						beanPathAdapter.bindContentBidirectional(beanName, null, bean.getClass(), ((ListView) field.get(controller)).getItems(), String.class, null, null);

					} else if (TextArea.class.isAssignableFrom(field.getType())) {

						beanPathAdapter.bindBidirectional(beanName, ((javafx.scene.control.TextArea) field.get(controller)).textProperty());

					}

				}

				//loadDisableVirtualKeyboard(controller, field);

				//loadSizeValitadions(field, controller, bean);

				//loadValitadions(field, controller, bean);

				if (LOGGER.isDebugEnabled()) {
					beanPathAdapter.fieldPathValueProperty().addListener(new ChangeListener<FieldPathValue>() {
						@Override
						public void changed(final ObservableValue<? extends FieldPathValue> observable, final FieldPathValue oldValue, final FieldPathValue newValue) {
							LOGGER.debug("Value changed from: " + oldValue + " to: " + newValue);
						}
					});
				}
			}
		}

		return beanPathAdapter;
	}
	
	public static <C> void clearFields(C controller) throws IllegalArgumentException, IllegalAccessException {

		for (Field field : controller.getClass().getDeclaredFields()) {

			field.setAccessible(true);

			if (ValidatableField.class.isAssignableFrom(field.getType())) {

				ValidatableField<?> validatableField = (ValidatableField<?>) field.get(controller);
				validatableField.clearValueSkipValidation();

			} else if (javafx.scene.control.TextField.class.isAssignableFrom(field.getType())) {

				javafx.scene.control.TextField textField = (javafx.scene.control.TextField) field.get(controller);
				textField.clear();

			} else if (CheckBox.class.isAssignableFrom(field.getType())) {

				CheckBox checkBox = (CheckBox) field.get(controller);
				checkBox.setSelected(false);

			} else if (ComboBox.class.isAssignableFrom(field.getType())) {

				ComboBox<?> comboBox = (ComboBox<?>) field.get(controller);
				comboBox.getItems().clear();
				//comboBox.clearValueSkipValidation();

			} else if (ListView.class.isAssignableFrom(field.getType())) {

				ListView<?> listView = (ListView<?>) field.get(controller);
				listView.getItems().clear();

			} else if (ImageView.class.isAssignableFrom(field.getType())) {

				ImageView imageView = (ImageView) field.get(controller);
				imageView.setImage(null);

			} else if (TextArea.class.isAssignableFrom(field.getType())) {

				TextArea textArea = (TextArea) field.get(controller);
				textArea.clear();

			}
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void loadComboBoxValues(javafx.scene.control.ComboBox comboBox, Class<?> type) {
		if (comboBox.getItems().isEmpty()) {
			comboBox.getItems().addAll(type.getEnumConstants());
		}
	}
	
	private static <C> void loadDisableVirtualKeyboard(C controller, Field field) throws IllegalAccessException {
		if (field.isAnnotationPresent(DisableVirtualKeyboard.class)) {
			if (field.get(controller) instanceof DisableableVirtualKeyboard) {
				DisableableVirtualKeyboard disableableVirtualKeyboard = (DisableableVirtualKeyboard) field.get(controller);
				disableableVirtualKeyboard.setDisableVirtualKeyboard(true);
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private static <C, B> void loadValitadions(Field field, C controller, B bean) throws IllegalArgumentException, IllegalAccessException {

		if (!(controller.getClass().isAnnotationPresent(IgnoreValidation.class) || field.isAnnotationPresent(IgnoreValidation.class))) {

			if (ValidatableField.class.isAssignableFrom(field.getType())) {

				Annotation[] annotations;
				try {

					final String[] fieldNames = field.getName().split("\\" + BeanPathAdapter.PATH_SEPARATOR);
					final boolean isField = fieldNames.length == 1;
					final String pkey = isField ? fieldNames[0] : fieldNames[1];

					if (isField) {

						annotations = bean.getClass().getDeclaredField(pkey).getAnnotations();

						for (Annotation annotation : annotations) {
							if (annotation instanceof NotNull || annotation instanceof NotBlank) {
								((ValidatableField) field.get(controller)).setRequired(true);
							}

							if (annotation instanceof Size) {
								Size size = (Size) annotation;
								if (field.get(controller) instanceof AbstractValidationTextField<?>) {
									AbstractValidationTextField<?> textField = (AbstractValidationTextField<?>) field.get(controller);
									if (size.min() > 0) {
										textField.setMinSize(size.min());
									}
								}
							}

						}

					} else {

						final String pClass = fieldNames[0];
						final Field fieldClass = bean.getClass().getDeclaredField(pClass).getType().getDeclaredField(pkey);
						final Class beanClass = bean.getClass().getDeclaredField(pClass).getType();

						loadValitadions(fieldClass, controller, beanClass);

					}

				} catch (NoSuchFieldException | SecurityException e) {
					LOGGER.error(e.getMessage(),e);
				}
			}
		}
	}
	
	private static <C, B> void loadSizeValitadions(Field field, C controller, B bean) throws IllegalArgumentException, IllegalAccessException {

		if (AbstractTextField.class.isAssignableFrom(field.getType())) {
			Annotation[] annotations;
			try {

				final String[] fieldNames = field.getName().split("\\" + BeanPathAdapter.PATH_SEPARATOR);
				final boolean isField = fieldNames.length == 1;
				final String pkey = isField ? fieldNames[0] : fieldNames[1];

				if (isField) {

					annotations = bean.getClass().getDeclaredField(pkey).getAnnotations();

					for (Annotation annotation : annotations) {
						if (annotation instanceof Size) {
							Size size = (Size) annotation;
							if (field.get(controller) instanceof AbstractTextField<?>) {
								AbstractTextField<?> textField = (AbstractTextField<?>) field.get(controller);
								if (size.max() > 0) {
									textField.setSize(size.max());
								}
							}
						}
					}
				}
			} catch (NoSuchFieldException | SecurityException e) {
				LOGGER.error(e.getMessage(),e);
			}
		}
	}
}
