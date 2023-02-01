/**
 * @file RecommendMeController.java
 * @brief Contiene la clase RecommendMeController.
 */

package fxsrc.propyecto.presentation;

import fxsrc.propyecto.domain.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/** \class RecommendMeController
 * \brief Clase encargada de recomendar items al usuario.
 *
 * Gestiona las recomendaciones del usuario controlando el fichero de escena recommendMe-view.fxml
 */
public class RecommendMeController implements Initializable {


    @FXML
    /**\brief moreRecommendButton es el boton para pedir mas recomendaciones.*/
    private Button moreRecommendButton;
    @FXML
    /**\brief \a image1 hasta \a image5 son las imágenes que contendrán los items recomendados al usuario.*/
    private ImageView image1,image2,image3,image4,image5;
    @FXML
    /**\brief \a title1 hasta \a title5 son los títulos que contendrán los items recomendados al usuario.*/
    private Label title1,title2,title3,title4,title5;
    @FXML
    /**\brief \a feedbackLabel es un texto de confirmación para el usuario. */
    private Label feedbackLabel;
    @FXML
    /**\brief \a errorLabel es un texto de error que aparece en caso de que el usuario haga algo mal. */
    private Label errorLabel;

    /**\brief \a list es la lista de recomendaciones que recibe el usuario. */
    private ArrayList<Integer> list = new ArrayList<>();

    /**\brief \a currentRecommendation es la recomendación en la que se encuentra el usuario (de todas las que se le dan). */
    private int currentRecommendation = 0;

    /**\brief Funcion que se llama al crearse la escena.
     *
     * \pre Cierto.
     * \param URL location parámetro necesario de la función ajeno al funcionamiento de EditProfileController.
     * \param ResourceBundle resources parámetro necesario de la función ajeno al funcionamiento de EditProfileController.
     * \post Se llama a la función Init.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ShowRecommendations();
        } catch (NoExistingActiveUserException e) {
            e.printStackTrace();
        }
    }


    /**\brief Muestra al usuario las recomendaciones más afines a él.
     *
     * \pre Cierto.
     * @throws NoExistingActiveUserException
     * \post Muestra al usuario las recomendaciones más afines a él.
     */
    @FXML
    private void ShowRecommendations() throws NoExistingActiveUserException {
        Label[] titles = {title1,title2,title3,title4,title5};
        ImageView[] images = {image1,image2,image3,image4,image5};
        if(currentRecommendation == 0){
            HybridApproachFiltering ha = new HybridApproachFiltering(3);
            int userid = UserManager.GetInstance().GetCurrentUserID();
            list = ha.Recommend(userid,UserManager.GetInstance().GetCurrentUserRatingsList(), 25);
        }
        for(int i = 0; i < 5; ++i){
            Item temp = DataManager.GetInstance().GetItemByID(list.get(i+currentRecommendation));
            titles[i].setText(temp.GetName());
            images[i].setImage(temp.GetImage());
        }
        currentRecommendation += 5;
        if(currentRecommendation >= 25) {
            errorLabel.setText("Valora más ítems para recibir nuevas recomendaciones");
            moreRecommendButton.setDisable(true);
            moreRecommendButton.setVisible(false);
            currentRecommendation = 0;
        }
    }

    /**\brief Gestiona el clic del boton Guardar Recomendaciones.
     * \pre Cierto.
     * @throws NoExistingActiveUserException
     * \post Guarda las recomendaciones que se muestran ese momento en pantalla.
     */
    public void SaveRecommendations() throws NoExistingActiveUserException {
        ArrayList<Item> recList = UserManager.GetInstance().GetLikedRecommendations();
        for (int i = currentRecommendation-5; i < currentRecommendation; ++i){
            if (!recList.contains(DataManager.GetInstance().GetItemByID(list.get(i)))){
                UserManager.GetInstance().AddLikedRecommendation(list.get(i));
            }
        }
        feedbackLabel.setText("Se han guardado tus recomendaciones! Consultalas en la lista del menu lateral.");
    }


    /**\brief Gestiona el clic del botón LogOut.
     *
     * \pre El usuario clica el botón de LogOut.
     * @throws NoExistingActiveUserException
     * \param ActionEvent e Evento de clic de botón LogOut.
     * \post El usuario sale del sistema y se cierra la aplicación.
     */
    public void LogOut(MouseEvent mouseEvent) throws NoExistingActiveUserException {
        SceneManager.GetInstance().Logout();
    }

    /**\brief Gestiona el clic del botón Home.
     *
     * \pre El usuario clica el botón de Home.
     * @throws IOException
     * \param ActionEvent e Evento de clic de botón Home.
     * \post Se envía al usuario a la escena de Home mediante SceneManager.
     */
    public void GoToHome(MouseEvent mouseEvent) throws IOException {
        SceneManager.GetInstance().ChangeSceneToHome();
    }

    /**\brief Gestiona el clic del botón Profile.
     *
     * \pre El usuario clica el botón de Profile.
     * @throws IOException
     * \param ActionEvent e Evento de clic de botón Profile.
     * \post Se envía al usuario a la escena de EditProfile mediante SceneManager.
     */
    public void GoToProfile(MouseEvent mouseEvent) throws IOException {
        SceneManager.GetInstance().ChangeSceneToEditProfile();
    }

    /**\brief Gestiona el clic del botón Fav.
     *
     * \pre El usuario clica el botón de Fav.
     * @throws IOException
     * \param ActionEvent e Evento de clic de botón Fav.
     * \post Se envía al usuario a la escena de FavList mediante SceneManager.
     */
    public void GoToFav(MouseEvent mouseEvent) throws IOException {
        SceneManager.GetInstance().ChangeSceneToFavList();
    }

    /**\brief Gestiona el clic del botón Recommend.
     *
     * \pre El usuario clica el botón de Recommend.
     * @throws IOException
     * \param ActionEvent e Evento de clic de botón Recommend.
     * \post Se envía al usuario a la escena de RecommendList mediante SceneManager.
     */
    public void GoToRecommend(MouseEvent mouseEvent) throws IOException {
        SceneManager.GetInstance().ChangeSceneToRecommendList();
    }


    public void GoToItem1(MouseEvent mouseEvent) throws IOException, AttValDoesNotExistException, NoExistingActiveUserException {
        SceneManager.GetInstance().ChangeSceneToItemInfo(title1.getText());
    }
    public void GoToItem2(MouseEvent mouseEvent) throws IOException, AttValDoesNotExistException, NoExistingActiveUserException {
        SceneManager.GetInstance().ChangeSceneToItemInfo(title2.getText());
    }
    public void GoToItem3(MouseEvent mouseEvent) throws IOException, AttValDoesNotExistException, NoExistingActiveUserException {
        SceneManager.GetInstance().ChangeSceneToItemInfo(title3.getText());
    }
    public void GoToItem4(MouseEvent mouseEvent) throws IOException, AttValDoesNotExistException, NoExistingActiveUserException {
        SceneManager.GetInstance().ChangeSceneToItemInfo(title4.getText());
    }
    public void GoToItem5(MouseEvent mouseEvent) throws IOException, AttValDoesNotExistException, NoExistingActiveUserException {
        SceneManager.GetInstance().ChangeSceneToItemInfo(title5.getText());
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
