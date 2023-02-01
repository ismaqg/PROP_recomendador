/**
 * @file EditProfileController.java
 * @brief Contiene la clase EditProfileController.
 */

package fxsrc.propyecto.presentation;

import fxsrc.propyecto.domain.DataManager;
import fxsrc.propyecto.domain.NoExistingActiveUserException;
import fxsrc.propyecto.domain.SceneManager;
import fxsrc.propyecto.domain.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/** \class EditProfileController
 * \brief Clase encargada del cambio de credenciales del usuario.
 *
 * Gestiona el cambio de credenciales del usuario controlando el fichero de escena editProfile-view.fxml
 */
public class EditProfileController implements Initializable {
    //USERNAME EDIT
    @FXML
    /**\brief \a currentUsername contiene el nombre de usuario actual del usuario loggeado. */
    private Label currentUsername;
    @FXML
    /**\brief \a newUsernameField contiene el nuevo nombre de usuario que se quiere cambiar. */
    private TextField newUsernameField;
    @FXML
    /**\brief \a acceptUsernameButton es el botón que acepta el cambio de nombre de usuario. */
    private Button acceptUsernameButton;
    @FXML
    /**\brief \a cancelUsernameButton es el botón que cancela el cambio de nombre de usuario. */
    private Button cancelUsernameButton;
    @FXML
    /**\brief \a usernameErrorLabel contiene un mensaje de error si el usuario hace algo mal. */
    private Label usernameErrorLabel;

    //EMAIL EDIT
    @FXML
    /**\brief \a currentEmail contiene el mail actual del usuario loggeado. */
    private Label currentEmail;
    @FXML
    /**\brief \a newEmailField contiene el nuevo mail que se quiere cambiar. */
    private TextField newEmailField;
    @FXML
    /**\brief \a acceptEmailButton es el botón que acepta el cambio de mail. */
    private Button acceptEmailButton;
    @FXML
    /**\brief \a cancelEmailButton es el botón que cancela el cambio de mail. */
    private Button cancelEmailButton;
    @FXML
    /**\brief \a emailErrorLabel contiene un mensaje de error si el usuario hace algo mal. */
    private Label emailErrorLabel;

    //PASSWORD EDIT
    @FXML
    /**\brief \a newPasswordField contiene la password que se quiere cambiar. */
    private PasswordField newPasswordField;
    @FXML
    /**\brief \a acceptPasswordButton es el botón que acepta el cambio de password. */
    private Button acceptPasswordButton;
    @FXML
    /**\brief \a cancelPasswordButton es el botón que cancela el cambio de password. */
    private Button cancelPasswordButton;
    @FXML
    /**\brief \a passwordErrorLabel contiene un mensaje de error si el usuario hace algo mal. */
    private Label passwordErrorLabel;
    @FXML
    /**\brief \a passwordFeedbackLabel contiene un mensaje para avisar al usuario del cambio correcto. */
    private Label passwordFeedbackLabel;

    //SECURITY QUESTION EDIT
    @FXML
    /**\brief \a currentSecurity contiene la respuesta de seguridad actual del usuario loggeado. */
    private Label currentSecurity;
    @FXML
    /**\brief \a newSecurityField contiene la nueva respuesta de seguridad que se quiere cambiar. */
    private TextField newSecurityField;
    @FXML
    /**\brief \a acceptSecurityButton es el botón que acepta el cambio de respuesta de seguridad. */
    private Button acceptSecurityButton;
    @FXML
    /**\brief \a cancelSecurityButton es el botón que cancela el cambio de respuesta de seguridad. */
    private Button cancelSecurityButton;
    @FXML
    private Label securityErrorLabel;

    @FXML
    /**\brief \a deleteAccountButton es el botón que hace saltar la alerta para confirmar la alerta de eliminación de cuenta. */
    private Button deleteAccountButton;

    /**\brief Funcion que se llama al crearse la escena.
     *
     * \pre Cierto.
     * \param URL location parámetro necesario de la función ajeno al funcionamiento de EditProfileController.
     * \param ResourceBundle resources parámetro necesario de la función ajeno al funcionamiento de EditProfileController.
     * \post Se llama a la función Init.
     */
    public void initialize(URL location, ResourceBundle resources) {
        Init();
    }

    /**\brief Inicialización de parámetros necesarios.
     *
     * \pre Cierto.
     * \post Se actualizan los textos \a currentUsername , \a currentEmail , \a currentSecurity en relación con el usuario actual.
     */
    public void Init() {
        try {
            currentUsername.setText(UserManager.GetInstance().GetCurrentUserUsername());
            currentEmail.setText(UserManager.GetInstance().GetCurrentUserEmail());
            currentSecurity.setText(UserManager.GetInstance().GetCurrentSecurity());
        }
        catch (Exception e) {

        }
    }

    //USERNAME
    /**\brief Gestiona el clic del botón EditUsername.
     *
     * \pre El usuario clica el botón de EditUsername.
     * \post Se activan los elementos necesarios para editar el nombre de usuario.
     */
    public void EditUsername(){
        EnableTextField(newUsernameField);
        EnableButton(acceptUsernameButton);
        EnableButton(cancelUsernameButton);
        acceptUsernameButton.setDefaultButton(true);
        newUsernameField.requestFocus();
        CancelEmail();
        CancelPassword();
        CancelSecurity();
    }

    /**\brief Gestiona el clic del botón UsernameAccept.
     *
     * \pre El usuario clica el botón de UsernameAccept.
     * \post Se gestiona el cambio de nombre de usuario, escribe los errores en usernameErrorLabel (en caso de que los haya).
     */
    public void UsernameAccept() {
        String text = newUsernameField.getText();
        if(text.isEmpty()) CancelUsername();
        else if (text.length() < 3 || text.length() > 12) {
            usernameErrorLabel.setText("El nombre de usuario debe estar entre 3 y 12 caracteres.");
        }
        else if (currentUsername.getText().toLowerCase().equals(text.toLowerCase())){
            usernameErrorLabel.setText("Este ya es tu nombre de usuario. Cancela para mantenerlo igual.");
        }
        else if (DataManager.GetInstance().UserExists(text)) {
            usernameErrorLabel.setText("Ya existe un usuario con ese nombre de usuario.");
        }

        else if(text.contains(",") || text.contains(";") || text.contains("|") || text.contains("/") || text.contains("\\")) usernameErrorLabel.setText("El nombre de usario no puede contener comas.");
        else {
            DisableButton(acceptUsernameButton);
            DisableButton(cancelUsernameButton);
            DisableTextField(newUsernameField);
            usernameErrorLabel.setText("");
            DataManager.GetInstance().SetNewUsername(currentUsername.getText(), text);
            passwordFeedbackLabel.setText("");

            currentUsername.setText(text);
            newUsernameField.clear();

        }
    }

    /**\brief Gestiona el clic del botón CancelUsername.
     *
     * \pre El usuario clica el botón de CancelUsername.
     * \post Se cancela el cambio de nombre de usuario y se vuelven a ocultar los elementos de cambio de nombre de usuario.
     */
    public void CancelUsername(){
        DisableButton(acceptUsernameButton);
        DisableButton(cancelUsernameButton);
        DisableTextField(newUsernameField);
        usernameErrorLabel.setText("");
        newUsernameField.clear();
    }

    //EMAIL
    /**\brief Gestiona el clic del botón EditEmail.
     *
     * \pre El usuario clica el botón de EditEmail.
     * \post Se activan los elementos necesarios para editar el mail.
     */
    public void EditEmail(){
        EnableTextField(newEmailField);
        EnableButton(acceptEmailButton);
        EnableButton(cancelEmailButton);
        acceptEmailButton.setDefaultButton(true);
        newEmailField.requestFocus();
        CancelUsername();
        CancelPassword();
        CancelSecurity();

    }

    /**\brief Gestiona el clic del botón EmailAccept.
     *
     * \pre El usuario clica el botón de EmailAccept.
     * \post Se gestiona el cambio de mail, escribe los errores en emailErrorLabel (en caso de que los haya).
     */
    public void EmailAccept() {
        String text = newEmailField.getText();
        if (text.isEmpty()) CancelEmail();
        else if(!text.contains("@") || !text.contains(".") || text.contains(",") || text.contains(";") || text.contains("|") || text.contains("/") || text.contains("\\")){
            emailErrorLabel.setText("El campo debe tener un formato de mail válido.");
        }
        else{
            DisableButton(acceptEmailButton);
            DisableButton(cancelEmailButton);
            DisableTextField(newEmailField);
            emailErrorLabel.setText("");
            currentEmail.setText(text);
            passwordFeedbackLabel.setText("");
            DataManager.GetInstance().SetNewEmail(currentUsername.getText(), text);
            newEmailField.clear();

        }
    }

    /**\brief Gestiona el clic del botón CancelEmail.
     *
     * \pre El usuario clica el botón de CancelEmail.
     * \post Se cancela el cambio de mail y se vuelven a ocultar los elementos de cambio de mail.
     */
    public void CancelEmail(){
        DisableButton(acceptEmailButton);
        DisableButton(cancelEmailButton);
        DisableTextField(newEmailField);
        emailErrorLabel.setText("");
        newEmailField.clear();
    }

    //PASSWORD
    /**\brief Gestiona el clic del botón EditPassword.
     *
     * \pre El usuario clica el botón de EditPassword.
     * \post Se activan los elementos necesarios para editar la contraseña.
     */
    public void EditPassword(){
        EnablePassField(newPasswordField);
        EnableButton(acceptPasswordButton);
        EnableButton(cancelPasswordButton);
        acceptPasswordButton.setDefaultButton(true);
        newPasswordField.requestFocus();
        CancelUsername();
        CancelEmail();
        CancelSecurity();
    }

    /**\brief Gestiona el clic del botón PasswordAccept.
     *
     * \pre El usuario clica el botón de PasswordAccept.
     * \post Se gestiona el cambio de mail, escribe los errores en passwordErrorLabel (en caso de que los haya).
     */
    public void PasswordAccept() {
        String text = newPasswordField.getText();
        if (text.isEmpty()) CancelPassword();
        else if (text.length() < 5 || text.length() > 14){
            passwordErrorLabel.setText("La contraseña debe estar comprimida entre 5 y 14 caracteres.");
            newPasswordField.clear();
        }
        else if (text.toLowerCase().equals(text)){
            passwordErrorLabel.setText("La contraseña debe tener al menos una letra mayúscula.");
            newPasswordField.clear();
        }
        else if(text.contains(",") || text.contains(";") || text.contains("|") || text.contains("/") || text.contains("\\")) passwordErrorLabel.setText("La contraseña no puede contener comas o barras.");
        else{
            DisableButton(acceptPasswordButton);
            DisableButton(cancelPasswordButton);
            DisablePassField(newPasswordField);
            passwordErrorLabel.setText("");
            newPasswordField.clear();
            passwordFeedbackLabel.setText("Contraseña cambiada correctamente.");
            DataManager.GetInstance().SetNewPassword(currentUsername.getText(), text);
        }
    }

    /**\brief Gestiona el clic del botón CancelPassword.
     *
     * \pre El usuario clica el botón de CancelPassword.
     * \post Se cancela el cambio de contraseña y se vuelven a ocultar los elementos de cambio de contraseña.
     */
    public void CancelPassword(){
        DisableButton(acceptPasswordButton);
        DisableButton(cancelPasswordButton);
        DisablePassField(newPasswordField);
        passwordErrorLabel.setText("");
        passwordFeedbackLabel.setText("");
        newPasswordField.clear();
    }

    //SECURITY QUESTION
    /**\brief Gestiona el clic del botón EditSecurity.
     *
     * \pre El usuario clica el botón de EditSecurity.
     * \post Se activan los elementos necesarios para editar la respuesta de seguridad.
     */
    public void EditSecurity(){
        EnableTextField(newSecurityField);
        EnableButton(acceptSecurityButton);
        EnableButton(cancelSecurityButton);
        acceptSecurityButton.setDefaultButton(true);
        newSecurityField.requestFocus();
        CancelUsername();
        CancelEmail();
        CancelPassword();
    }

    /**\brief Gestiona el clic del botón SecurityAccept.
     *
     * \pre El usuario clica el botón de SecurityAccept.
     * \post Se gestiona el cambio de respuesta de seguridad.
     */
    public void SecurityAccept() {
        String text = newSecurityField.getText();
        if (text.isEmpty() || text.equals(DataManager.GetInstance().GetSecurity(currentUsername.getText()))) CancelSecurity();
        else if(text.contains(",") || text.contains(";") || text.contains("|") || text.contains("/") || text.contains("\\")) securityErrorLabel.setText("La respuesta de seguridad no puede contener comas.");
        else{
            DisableButton(acceptSecurityButton);
            DisableButton(cancelSecurityButton);
            DisableTextField(newSecurityField);
            currentSecurity.setText(text);
            DataManager.GetInstance().SetNewSecurity(currentUsername.getText(), text);
            passwordFeedbackLabel.setText("");
            newSecurityField.clear();
        }
    }

    /**\brief Gestiona el clic del botón CancelSecurity.
     *
     * \pre El usuario clica el botón de CancelSecurity.
     * \post Se cancela el cambio de respuesta de seguridad y se vuelven a ocultar los elementos de cambio de respuesta de seguridad.
     */
    public void CancelSecurity(){
        DisableButton(acceptSecurityButton);
        DisableButton(cancelSecurityButton);
        DisableTextField(newSecurityField);
        newSecurityField.clear();
    }


    //TEXT FIELD
    /**\brief Gestiona la activación de TextFields.
     *
     * \param TextField field Campo de texto a activar.
     * \pre El usuario accede a cambiar algún dato.
     * \post Se muestra visible y editable el TextField pasado por parámetro.
     */
    private void EnableTextField (TextField field) {
        field.setVisible(true);
        field.setEditable(true);
    }

    /**\brief Gestiona la desactivación de TextFields.
     *
     * \param TextField field Campo de texto a desactivar.
     * \pre El usuario cancela o acepta el cambio algún dato.
     * \post Se quita la propiedad de edición y visibilidad del TextField pasado por parámetro.
     */
    private void DisableTextField (TextField field) {
        field.setVisible(false);
        field.setEditable(false);
    }

    //PASSWORD FIELD
    /**\brief Gestiona la activación de PasswordFields.
     *
     * \param PasswordField field Campo de contraseña a activar.
     * \pre El usuario accede a cambiar algún dato.
     * \post Se muestra visible y editable el PasswordField pasado por parámetro.
     */
    private void EnablePassField (PasswordField field) {
        field.setVisible(true);
        field.setEditable(true);
    }

    /**\brief Gestiona la desactivación de PasswordFields.
     *
     * \param PasswordField field Campo de contraseña a desactivar.
     * \pre El usuario cancela o acepta el cambio algún dato.
     * \post Se quita la propiedad de edición y visibilidad del PasswordField pasado por parámetro.
     */
    private void DisablePassField (PasswordField field) {
        field.setVisible(false);
        field.setEditable(false);
    }

    //BUTTON
    /**\brief Gestiona la activación de Buttons.
     *
     * \param Button button Botón a activar.
     * \pre El usuario accede a cambiar algún dato.
     * \post Se muestra activado y visible el Button pasado por parámetro.
     */
    private void EnableButton(Button button) {
        button.setDisable(false);
        button.setOpacity(1);
    }

    /**\brief Gestiona la desactivación de Buttons.
     *
     * \param Button button Botón a desactivar.
     * \pre El usuario accede a cambiar algún dato.
     * \post Se cambia a desactivado e invisible el Button pasado por parámetro.
     */
    private void DisableButton(Button button) {
        button.setDisable(true);
        button.setOpacity(0);
    }

    /**\brief Gestiona el clic del botón Home.
     *
     * \pre El usuario clica el botón de Home.
     * @throws IOException
     * \param MouseEvent mouseEvent Evento de clic de botón Home.
     * \post Se envía al usuario a la escena de Home mediante SceneManager.
     */
    public void GoToHome(MouseEvent mouseEvent) throws IOException {
        SceneManager.GetInstance().ChangeSceneToHome();
    }

    /**\brief Gestiona el clic del botón Profile.
     *
     * \pre El usuario clica el botón de Profile.
     * @throws IOException
     * \param MouseEvent mouseEvent Evento de clic de botón Profile.
     * \post Se envía al usuario a la escena de EditProfile mediante SceneManager.
     */
    public void goToProfile(MouseEvent mouseEvent) throws IOException {
        SceneManager.GetInstance().ChangeSceneToEditProfile();
    }

    /**\brief Gestiona el clic del botón Fav.
     *
     * \pre El usuario clica el botón de Fav.
     * @throws IOException
     * \param MouseEvent mouseEvent Evento de clic de botón Fav.
     * \post Se envía al usuario a la escena de FavList mediante SceneManager.
     */
    public void goToFav(MouseEvent mouseEvent) throws IOException {
        SceneManager.GetInstance().ChangeSceneToFavList();
    }

    /**\brief Gestiona el clic del botón Recommend.
     *
     * \pre El usuario clica el botón de Recommend.
     * @throws IOException
     * \param MouseEvent mouseEvent Evento de clic de botón Recommend.
     * \post Se envía al usuario a la escena de RecommendList mediante SceneManager.
     */
    public void goToRecommend(MouseEvent mouseEvent) throws IOException {
        SceneManager.GetInstance().ChangeSceneToRecommendList();
    }

    /**\brief Gestiona el clic del botón LogOut.
     *
     * \pre El usuario clica el botón de LogOut.
     * @throws NoExistingActiveUserException
     * \param MouseEvent mouseEvent Evento de clic de botón LogOut.
     * \post El usuario sale del sistema y se cierra la aplicación.
     */
    public void logOut(MouseEvent mouseEvent) throws NoExistingActiveUserException {
        SceneManager.GetInstance().Logout();
    }

    /**\brief Gestiona el clic del botón EliminarCuenta.
     *
     * \pre El usuario clica el botón de EliminarCuenta.
     * \param ActionEvent actionEvent Evento de clic de botón EliminarCuenta.
     * \post El usuario sale del sistema y se elimina su cuenta de la base de datos.
     */
    public void DeleteAccount(ActionEvent actionEvent) {
        Alert deleteAccount = new Alert(Alert.AlertType.CONFIRMATION);
        deleteAccount.setTitle("Eliminación de cuenta");
        deleteAccount.setHeaderText("Estás a punto de eliminar tu cuenta!");
        deleteAccount.setContentText("¿Estás seguro de que quieres eliminarla?\nPerderás el acceso.");
        if(deleteAccount.showAndWait().get() == ButtonType.OK){
            try{
                UserManager.GetInstance().DeleteCurrentAccount();
                SceneManager.GetInstance().Close();
            }
            catch (NoExistingActiveUserException e){

            }
        }
    }

    /**\brief Gestiona el clic del botón Atrás.
     *
     * \pre El usuario clica el botón de Atrás.
     * @throws IOException
     * \param ActionEvent e Evento de clic de botón Atrás.
     * \post Se envía al usuario a la escena anterior mediante SceneManager.
     */
    public void GoBack(ActionEvent actionEvent) throws IOException {
        SceneManager.GetInstance().ChangeSceneToPast();
    }
}