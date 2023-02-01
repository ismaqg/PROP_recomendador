/**
 * @file ForgotPassController.java
 * @brief Contiene la clase ForgetPassController.
 */


package fxsrc.propyecto.presentation;

import fxsrc.propyecto.domain.DataManager;
import fxsrc.propyecto.domain.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

/** \class ForgotPassController
 * \brief Clase encargada de la generacion de contraseña en caso de perdida.
 *
 * Cambia la contraseña de un usuario a traves de la base de datos controlando el fichero de escena forgotPass-view.fxml
 */
public class ForgotPassController {
    @FXML
    /**\brief \a userField es el campo de texto donde el usuario introduce su nombre de usuario, y answerField donde introduce la respuesta a la pregunta de seguridad. */
    private TextField userField, answerField;

    @FXML
    /**\brief \a errorLabel es un texto de error que aparece en caso de que el usuario haga algo mal, y passLabel donde muestra la nueva contraseña generada. */
    private Label errorLabel, passLabel;

    /**\brief \a newPass es un string que contiene la nueva contraseña generada por generatePass. */
    private String newPass = generatePass();

    /**\brief Gestiona el clic del botón Enviar.
     *
     * \pre El usuario clica el botón de Enviar.
     * @throws IOException
     * \post Se comprueban las credencialesla respuesta de la pregunta de seguridad introducida por el usuario, y modifica los labels acorde a la situación.
     */
    public void send() throws IOException {
        if(userField.getText().isEmpty() || answerField.getText().isEmpty()){
            passLabel.setText("");
            errorLabel.setText("Por favor rellene los campos obligatorios.");
        }
        else if(!(DataManager.GetInstance().UserExists(userField.getText()))){
            errorLabel.setText("El usuario no coincide con ningún usuario existente.");
            passLabel.setText("");
        }
        else if(!answerField.getText().equals(DataManager.GetInstance().GetSecurity(userField.getText()))){
            errorLabel.setText("Respuesta incorrecta. Inténtelo de nuevo.");
            passLabel.setText("");
        }
        else{
            DataManager.GetInstance().SetNewPassword(userField.getText(), newPass);
            passLabel.setText("Correcto. Tu nueva contraseña es: " + newPass);
            errorLabel.setText("");
        }
    }

    /**\brief Gestiona el clic del botón LogIn.
     *
     * \pre El usuario clica el botón de LogIn.
     * @throws IOException
     * \post Se envía al usuario a la escena de LogIn mediante SceneManager.
     */
    public void goToLogIn() throws IOException {
        SceneManager.GetInstance().ChangeSceneToLogIn();
    }

    /**\brief Gestiona la creación de la nueva contraseña.
     *
     * \pre Cierto.
     * \post Retorna una contraseña en formato de String.
     */
    public String generatePass() {
        int s = (int) (100 + (Math.random() * (899)));
        return "A" + s + "Z";
    }

    /**\brief Gestiona el clic del botón Atrás.
     *
     * \pre El usuario clica el botón de Atrás.
     * @throws IOException
     * \post Se envía al usuario a la escena anterior mediante SceneManager.
     */
    public void GoBack() throws IOException {
        SceneManager.GetInstance().ChangeSceneToPast();
    }

}