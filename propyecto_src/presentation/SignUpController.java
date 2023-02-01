/**
 * @file SignUpController.java
 * @brief Contiene la clase SignUpController.
 */

package fxsrc.propyecto.presentation;

import fxsrc.propyecto.domain.DataManager;
import fxsrc.propyecto.domain.SceneManager;
import fxsrc.propyecto.domain.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

/** \class SignUpController
 * \brief Clase encargada de registrar a un usuario nuevo con sus credenciales.
 *
 * Gestiona el registro de usuarios controlando el fichero de escena signUp-view.fxml
 */
public class SignUpController {
    @FXML
    /**\brief \a errorLabel es un texto de error que aparece en caso de que el usuario haga algo mal. */
    private Label errorLabel;
    @FXML
    /**\brief \a usernameField, \a mailField y \a answerField son los campos de texto a rellenar por el usuario. Deben tener valores adecuados y formateados correctamente. */
    private TextField usernameField, mailField, answerField;
    @FXML
    /**\brief \a passwordField y \a confirmPasswordField son los campos de contraseña a rellenar por el usuario. confirmPasswordField debe tener el mismo contenido que passwordField. */
    private PasswordField passwordField, confirmPasswordField;


    /**\brief Gestiona el clic del botón SignUp.
     *
     * \pre El usuario clica el botón de SignUp.
     * @throws IOException
     * \param ActionEvent e Evento de clic de botón SignUp.
     * \post Se comprueban las credenciales introducidas por el usuario.
     */
    public void UserSignUp(ActionEvent e) throws IOException {
        CheckCredentials();
    }

    /**\brief Gestiona el clic del botón LogIn.
     *
     * \pre El usuario clica el botón de LogIn.
     * @throws IOException
     * \param ActionEvent e Evento de clic de botón LogIn.
     * \post Se envía al usuario a la escena de LogIn mediante SceneManager.
     */
    public void UserLogIn(ActionEvent e) throws IOException {
        SceneManager.GetInstance().ChangeSceneToLogIn();
    }

    /**\brief Gestiona la comprobación de credenciales.
     *
     * \pre Cierto.
     * @throws IOException
     * \post Se modifica el valor de errorLabel en caso de tener algún error, o se envia al usuario a EditProfile mediante SceneManager si no tiene errores.
     */
    private void CheckCredentials() throws IOException {
        if (CheckUsername(usernameField.getText())
                && CheckPassword(passwordField.getText(), confirmPasswordField.getText())
                && CheckMail(mailField.getText())
                && CheckSecurity(answerField.getText())){
            try {
                UserManager.GetInstance().SignUp(usernameField.getText(), passwordField.getText(), mailField.getText(), answerField.getText());
                SceneManager.GetInstance().ChangeSceneToHome();
            }
            catch (Exception e){

            }
        }
    }

    /**\brief Gestiona la comprobación de la respuesta de seguridad.
     *
     * \pre Cierto.
     * \param String text Texto introducido por el usuario en el campo de pregunta de seguridad.
     * \post Se modifica el valor de errorLabel en caso de tener algún error, o se retorna true si no tiene errores.
     * \returns boolean
     */
    private boolean CheckSecurity(String text) {
        boolean valid = true;
        if (text.isEmpty()){
            errorLabel.setText("Por favor rellena todos los campos.");
            valid = false;
        }
        else if(text.contains(",") || text.contains(";") || text.contains("|") || text.contains("/") || text.contains("\\")) {
            errorLabel.setText("La respuesta de seguridad no puede contener comas.");
            valid = false;
        }
        return valid;
    }

    /**\brief Gestiona la comprobación del nombre de usuario.
     *
     * \pre Cierto.
     * \param String text Texto introducido por el usuario en el campo de nombre de usuario.
     * \post Se modifica el valor de errorLabel en caso de tener algún error, o se retorna true si no tiene errores.
     * \returns boolean
     */
    private boolean CheckUsername(String text) {
        boolean valid = false;
        if (text.isEmpty()){
            errorLabel.setText("Por favor rellena todos los campos.");
        }
        else if(text.length() < 3 || text.length() > 12){
            errorLabel.setText("El usuario debe estar comprendido entre 3 y 12 caracteres.");
        }
        else if(DataManager.GetInstance().UserExists(text)){
            errorLabel.setText("Usuario no disponible.");
        }
        else if(text.contains(",") || text.contains(";") || text.contains("|") || text.contains("/") || text.contains("\\")) errorLabel.setText("El nombre de usuario no puede contener comas.");
        else valid = true;
        return valid;
    }

    /**\brief Gestiona la comprobación de la dirección de mail.
     *
     * \pre Cierto.
     * \param String text Texto introducido por el usuario en el campo de la dirección de mail.
     * \post Se modifica el valor de errorLabel en caso de tener algún error, o se retorna true si no tiene errores.
     * \returns boolean
     */
    private boolean CheckMail(String text) {
        boolean valid = false;
        if (text.isEmpty()){
            errorLabel.setText("Por favor rellena todos los campos.");
        }
        else if(!text.contains("@") || !text.contains(".") || text.contains(",") || text.contains(";") || text.contains("|") || text.contains("/") || text.contains("\\")){
            errorLabel.setText("El campo de mail debe tener un formato válido.");
        }

        else valid = true;
        return valid;
    }

    /**\brief Gestiona la comprobación de la contraseña.
     *
     * \pre Cierto.
     * \param String text Texto introducido por el usuario en el campo de la contraseña.
     * \post Se modifica el valor de errorLabel en caso de tener algún error, o se retorna true si no tiene errores.
     * \returns boolean
     */
    private boolean CheckPassword(String pass, String confirmPass) {
        boolean valid = false;
        if (pass.isEmpty() || confirmPass.isEmpty()){
            errorLabel.setText("Por favor rellena todos los campos.");
        }
        else if(pass.length() < 5 || pass.length() > 14){
            errorLabel.setText("La contraseña debe estar comprimida entre 5 y 14 caracteres.");
            passwordField.clear();
            confirmPasswordField.clear();

        }
        else if(pass.contains(",") || pass.contains(";") || pass.contains("|") || pass.contains("/") || pass.contains("\\")) errorLabel.setText("La contraseña no puede contener comas.");
        else if (!confirmPasswordField.getText().equals(passwordField.getText())){
            errorLabel.setText("Las contraseñas no coinciden.");
            passwordField.clear();
            confirmPasswordField.clear();
        }
        else if (pass.toLowerCase().equals(pass)){
            errorLabel.setText("La contraseña debe tener al menos una letra mayúscula.");
            passwordField.clear();
            confirmPasswordField.clear();
        }
        else valid = true;
        return valid;
    }


}