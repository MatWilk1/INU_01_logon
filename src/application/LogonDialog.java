package application;

import java.util.ArrayList;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.collections.FXCollections;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class LogonDialog extends Dialog {

	private Dialog<Pair<Environment, String>> dialog;
	private ChoiceBox<Environment> choiceBoxEnv;
	private ComboBox<String> comboBoxUsers;
	private PasswordField passField;
	private ButtonType loginButtonType;
	private ButtonType cancelButtonType;
	private GridPane gridPane;
	private DataBase base;

	public LogonDialog() {
		dialog = new Dialog<>();
		dialog.setTitle("Logowanie");
		dialog.setHeaderText("Logowanie do systemu STYLEman");

		// Stworzenie obiektu bazy aby wygenerowa� zbi�r u�ytkownik�w
		base = new DataBase();

		ArrayList<String> lista = new ArrayList<String>();
		lista.add("aaa");
		lista.add("bbb");
		lista.add("ccc");
		lista.add("dddddddd");

		// Stworzenie etykiet i p�l
		Label labelEnv = new Label("�rodowisko:");
		Label labelUsers = new Label("U�ytkownik:");
		Label labelPassField = new Label("Has�o:");
		choiceBoxEnv = new ChoiceBox<Environment>(FXCollections.observableArrayList(Environment.values()));
		choiceBoxEnv.getSelectionModel().select(Environment.Produkcyjne);
		choiceBoxEnv.setPrefWidth(250);
		comboBoxUsers = new ComboBox<String>();
		comboBoxUsers.setPrefWidth(250);
		comboBoxUsers.setEditable(true);
		comboBoxUsers.setItems(FXCollections.observableArrayList(base.getEnvirUsers(Environment.Produkcyjne)));
		passField = new PasswordField();
		passField.setDisable(true);

		// Stworzenie s�uchaczy
		choiceBoxEnv.valueProperty().addListener((observable, oldVal, newVal) -> choiceBoxEnvChanged(newVal));
		comboBoxUsers.valueProperty().addListener((observable, oldVal, newVal) -> comboBoxUsersChanged(newVal));
		passField.textProperty().addListener((observable, oldVal, newVal) -> passFieldChanged(newVal));

		// Stworzenie GridPane i przypisanie element�w
		gridPane = new GridPane();
		gridPane.setHgap(20);
		gridPane.addRow(0, labelEnv, choiceBoxEnv);
		gridPane.addRow(1, labelUsers, comboBoxUsers);
		gridPane.addRow(2, labelPassField, passField);
		dialog.getDialogPane().setContent(gridPane);

		// Stworzenie przycisk�w
		loginButtonType = new ButtonType("Loguj", ButtonData.OK_DONE);
		cancelButtonType = new ButtonType("Anuluj", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, cancelButtonType);
		dialog.getDialogPane().lookupButton(loginButtonType).setDisable(true);

		// Dodanie obrazka
		dialog.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("Login_64x.png"))));
//		Image image = new Image(ClassLoader.getSystemResourceAsStream("Login_64x.png"));
//		ImageView imageView = new ImageView(image);
//		dialog.setGraphic(imageView);

		// Zwraca par� (�rodowisko, U�ytkownik)
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == loginButtonType) {
				return new Pair<>(choiceBoxEnv.getValue(), comboBoxUsers.getValue());
			} else
				return null;
		});

		Optional<Pair<Environment, String>> result = dialog.showAndWait();

		// Je�li Optional posiada warto�� to wypisuje informacje o �rodowisku i
		// U�ytkowniku na konsoli
		result.ifPresent(res -> {
			System.out.println(
					"�rodowisko = " + choiceBoxEnv.getValue() + ", " + "U�ytkownik = " + comboBoxUsers.getValue());

			// Weryfikuje czy podane has�o jest prawid�owe i wy�wietla odpowiednie okienko z
			// informacj�
			if (base.passwordCheck(comboBoxUsers.getValue(), passField.getText()) == 1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Wynik logowania");
				alert.setHeaderText("Informacje o logowaniu");
				alert.setContentText("Logowanie przebieg�o pomy�lnie.");
				alert.showAndWait();
			} else if (base.passwordCheck(comboBoxUsers.getValue(), passField.getText()) == 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Wynik logowania");
				alert.setHeaderText("Informacje o logowaniu");
				alert.setContentText("Niestety nie uda�o si� zalogowa�. B��dne has�o.");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Wynik logowania");
				alert.setHeaderText("Informacje o logowaniu");
				alert.setContentText("Nie znaleziono takiego u�ytkownika.");
				alert.showAndWait();
			}
		});

	}

	// Definicja poszczeg�lnych s�uchaczy
	public void choiceBoxEnvChanged(Environment newVal) {
		comboBoxUsers.setItems(FXCollections.observableArrayList(base.getEnvirUsers(newVal)));
	}

	public void comboBoxUsersChanged(String newVal) {
		if (newVal != null) {
			passField.setDisable(false);
		} else {
			passField.setDisable(true);
		}
	}

	public void passFieldChanged(String newVal) {
		if (!newVal.isEmpty()) {
			dialog.getDialogPane().lookupButton(loginButtonType).setDisable(false);
		} else {
			dialog.getDialogPane().lookupButton(loginButtonType).setDisable(true);
		}
	}

}