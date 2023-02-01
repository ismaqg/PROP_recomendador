/**
 * @file HomeController.java
 * @brief Contiene la clase HomeController.
 */
package fxsrc.propyecto.presentation;

import fxsrc.propyecto.domain.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

/** \class HomeController
 * \brief Clase encargada del menú principal de la aplicación.
 *
 * Gestiona las acciones que se pueden hacer desde el menú principal y el display de las películas con mayor puntuación.
 */
public class HomeController implements Initializable {

    @FXML
    /**\brief \a Las imagenes Image contienen las imagenes de los cinco items con mayor puntuación. */
    private ImageView image1, image2, image3, image4, image5;

    @FXML
    /**\brief \a searchBar es un campo donde se introducirá el nombre del item buscado. */
    private TextField searchBar;

    @FXML
    /**\brief \a Los Title contienen el título de los cinco mejores items. */
    private Label title1, title2, title3, title4, title5;

    @FXML
    /**\brief \a errorLabel contiene el mensaje de error. */
    private Label errorLabel;

    @FXML
    /**\brief \a Los Rating contienen la puntuación de los cinco mejores items. */
    private Label rating1, rating2, rating3, rating4, rating5;

    /**\brief \a top contiene los cinco items con mayor puntuación. */
    private ArrayList<Item> top = DataManager.GetInstance().GetTopRatedItems(5);

    /**\brief Funcion que se llama al crearse la escena.
     *
     * \pre Cierto.
     * \param URL location parámetro necesario de la función ajeno al funcionamiento de HomeController.
     * \param ResourceBundle resources parámetro necesario de la función ajeno al funcionamiento de HomeController.
     * \post Se llama a la función Init.
     */
    public void initialize(URL location, ResourceBundle resources) {
        Init();
    }

    /**\brief Inicialización de parámetros necesarios.
     *
     * \pre Cierto.
     * \post Se llama a la función topItems.*/
    public void Init() {
        try {
            TopItems();
            TextFields.bindAutoCompletion(searchBar, DataManager.GetInstance().GetAllItemsNames());
        }
        catch (Exception e) {
        }
    }

    /**\brief Gestiona el display de los items con mejor puntuación.
     *
     * \pre Existen items con puntuación.
     * \post Se envía la información de los mejores items a la escena de home-view mediante SceneManager.
     */
    public void TopItems() {
        Label[] title = {title1, title2, title3, title4, title5};
        Label[] rating = {rating1, rating2, rating3, rating4, rating5};
        ImageView[] image = {image1, image2, image3, image4, image5};

        for(int i=0; i<5; i++) {
            title[i].setText(top.get(i).GetName());
            rating[i].setText(Float.toString(top.get(i).GetScore()));
            if(top.get(i).GetImage() != null) image[i].setImage(top.get(i).GetImage());
        }
    }

    /**\brief Gestiona el clic del botón search.
     *
     * \pre Existen items con puntuación.
     * @throws IOException
     * @throws AttValDoesNotExistException
     * \post Se envía al usuario a la pantalla del item buscado mediante SceneManager.
     */
    public void Search() throws IOException, AttValDoesNotExistException {
        String name = searchBar.getText();
        try{
            SceneManager.GetInstance().ChangeSceneToItemInfo(DataManager.GetInstance().GetItemByNameLC(name).GetName());
        }
        catch (Exception e){
            errorLabel.setText("El item introducido no existe en el sistema.");
        }
    }

    /**\brief Gestiona el clic del botón Recomiéndame.
     *
     * \pre El usuario clica el botón de Recomiéndame.
     * @throws IOException
     * @throws NoExistingActiveUserException
     * \post Se envía al usuario a la escena de RecommendMe o getStarted mediante SceneManager.
     */
    public void RecommendMe() throws IOException, NoExistingActiveUserException {
        boolean HasStarted = UserManager.GetInstance().GetHasStarted();
        if(HasStarted) {
            SceneManager.GetInstance().ChangeSceneToRecommendMe();
        }
        else if(UserManager.GetInstance().GetCurrentUserRatingsList().size() > 4){
            UserManager.GetInstance().SetHasStarted(true);
            SceneManager.GetInstance().ChangeSceneToRecommendMe();
        }
        else {
            SceneManager.GetInstance().ChangeSceneToGetStarted();
        }
    }

    /**\brief Gestiona el clic del botón Profile.
     *
     * \pre El usuario clica el botón de Profile.
     * @throws IOException
     * \post Se envía al usuario a la escena de EditProfile mediante SceneManager.
     */
    public void GoToProfile() throws IOException {
        SceneManager.GetInstance().ChangeSceneToEditProfile();
    }

    /**\brief Gestiona el clic del botón Fav.
     *
     * \pre El usuario clica el botón de Fav.
     * @throws IOException
     * \post Se envía al usuario a la escena de FavList mediante SceneManager.
     */
    public void GoToFav() throws IOException {
        SceneManager.GetInstance().ChangeSceneToFavList();

    }

    /**\brief Gestiona el clic del botón Recommend.
     *
     * \pre El usuario clica el botón de Recommend.
     * @throws IOException
     * \post Se envía al usuario a la escena de RecommendList mediante SceneManager.
     */
    public void GoToRecommend() throws IOException {
        SceneManager.GetInstance().ChangeSceneToRecommendList();
    }

    /**\brief Gestiona el clic del botón LogOut.
     *
     * \pre El usuario clica el botón de LogOut.
     * @throws NoExistingActiveUserException
     * @throws IOException
     * \post El usuario sale del sistema y se cierra la aplicación.
     */
    public void LogOut() throws NoExistingActiveUserException, IOException {
        SceneManager.GetInstance().Logout();
    }

    /**\brief Gestiona el clic de las imagenes de los items.
     *
     * \pre El usuario clica cualquier imagen de un item.
     * @throws NoExistingActiveUserException
     * @throws IOException
     * @throws AttValDoesNotExistException
     * \post Se envía al usuario a la escena del Item mediante SceneManager.
     */
    public void GoToItem1() throws IOException, AttValDoesNotExistException, NoExistingActiveUserException {
        SceneManager.GetInstance().ChangeSceneToItemInfo(top.get(0).GetName());
    }

    /**\brief Gestiona el clic de las imagenes de los items.
     *
     * \pre El usuario clica cualquier imagen de un item.
     * @throws NoExistingActiveUserException
     * @throws IOException
     * @throws AttValDoesNotExistException
     * \post Se envía al usuario a la escena del Item mediante SceneManager.
     */
    public void GoToItem2() throws IOException, AttValDoesNotExistException, NoExistingActiveUserException {
        SceneManager.GetInstance().ChangeSceneToItemInfo(top.get(1).GetName());
    }

    /**\brief Gestiona el clic de las imagenes de los items.
     *
     * \pre El usuario clica cualquier imagen de un item.
     * @throws NoExistingActiveUserException
     * @throws IOException
     * @throws AttValDoesNotExistException
     * \post Se envía al usuario a la escena del Item mediante SceneManager.
     */
    public void GoToItem3() throws IOException, AttValDoesNotExistException, NoExistingActiveUserException {
        SceneManager.GetInstance().ChangeSceneToItemInfo(top.get(2).GetName());
    }

    /**\brief Gestiona el clic de las imagenes de los items.
     *
     * \pre El usuario clica cualquier imagen de un item.
     * @throws NoExistingActiveUserException
     * @throws IOException
     * @throws AttValDoesNotExistException
     * \post Se envía al usuario a la escena del Item mediante SceneManager.
     */
    public void GoToItem4() throws IOException, AttValDoesNotExistException, NoExistingActiveUserException {
        SceneManager.GetInstance().ChangeSceneToItemInfo(top.get(3).GetName());
    }

    /**\brief Gestiona el clic de las imagenes de los items.
     *
     * \pre El usuario clica cualquier imagen de un item.
     * @throws NoExistingActiveUserException
     * @throws IOException
     * @throws AttValDoesNotExistException
     * \post Se envía al usuario a la escena del Item mediante SceneManager.
     */
    public void GoToItem5() throws IOException, AttValDoesNotExistException, NoExistingActiveUserException {
        SceneManager.GetInstance().ChangeSceneToItemInfo(top.get(4).GetName());
    }
}
