/**
 * @file LogInController.java
 * @brief Contiene la clase LogInController.
 */

package fxsrc.propyecto.presentation;

import fxsrc.propyecto.domain.BadCredentialsException;
import fxsrc.propyecto.domain.SceneManager;
import fxsrc.propyecto.domain.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

/** \class LogInController
 * \brief Clase encargada del acceso de un usuario con sus credenciales.
 *
 * Gestiona el acceso de usuarios mediante la base de datos controlando el fichero de escena logIn-view.fxml
 */
public class LogInController {
    @FXML
    /**\brief \a errorLabel es un texto de error que aparece en caso de que el usuario haga algo mal. */
    private Label errorLabel;
    @FXML
    /**\brief \a usernameField es el campo de texto donde el usuario introduce su nombre de usuario. */
    private TextField usernameField;
    @FXML
    /**\brief \a passwordField es el campo de contraseña donde el usuario introduce su contraseña. */
    private PasswordField passwordField;

    /**\brief Gestiona el clic del botón LogIn.
     *
     * \pre El usuario clica el botón de LogIn.
     * @throws IOException
     * \param ActionEvent e Evento de clic de botón LogIn.
     * \post Se comprueban las credenciales introducidas por el usuario.
     */
    public void UserLogIn(ActionEvent e) throws IOException{
        CheckLogin();
    }

    /**\brief Gestiona el clic del botón SignUp.
     *
     * \pre El usuario clica el botón de SignUp.
     * @throws IOException
     * \param ActionEvent e Evento de clic de botón SignUp.
     * \post Se envía al usuario a la escena de SignUp mediante SceneManager.
     */
    public void UserSignUp(ActionEvent e) throws IOException {
        SceneManager.GetInstance().ChangeSceneToSignUp();
    }

    /**\brief Gestiona el clic del botón ForgotPass.
     *
     * \pre El usuario clica el botón de ForgotPass.
     * @throws IOException
     * \param ActionEvent e Evento de clic de botón ForgotPass.
     * \post Se envía al usuario a la escena de ForgotPass mediante SceneManager.
     */
    public void UserForgotPass(ActionEvent e) throws IOException {
        SceneManager.GetInstance().ChangeSceneToForgotPass();
    }

    /**\brief Gestiona la comprobación de credenciales.
     *
     * \pre Cierto.
     * @throws IOException
     * \post Se modifica el valor de errorLabel en caso de tener algún error, o se envia al usuario al Home mediante SceneManager si no tiene errores.
     */
    private void CheckLogin() throws IOException{
        // FIXME: 01/12/2021 añadir comprobación con sistema

        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()){
            errorLabel.setText("Por favor rellena todos los campos.");
        }
        else {
            try {
                UserManager.GetInstance().SignIn(usernameField.getText(), passwordField.getText());
                SceneManager.GetInstance().ChangeSceneToHome();
            } catch (Exception | BadCredentialsException e) {
                errorLabel.setText("Tus datos no coinciden con los registrados en el sistema.\nCompruébalos e intentalo otra vez!");
            }
        }
    }
}