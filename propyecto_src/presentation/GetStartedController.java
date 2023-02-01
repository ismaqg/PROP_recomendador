/**
 * @file GetStartedController.java
 * @brief Contiene la clase GetStartedController.
 */

package fxsrc.propyecto.presentation;

import fxsrc.propyecto.domain.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import org.controlsfx.control.textfield.TextFields;


/** \class GetStartedController
 * \brief Clase encargada de añadir las primeras valoraciones del usuario.
 *
 * Gestiona las primeras valoraciones del usuario controlando el fichero de escena getStarted-view.fxml
 */
public class GetStartedController implements Initializable {
    @FXML
    /**\brief \a image1 hasta \a image5 son las imágenes que contendrán los items añadidos por el usuario.*/
    private ImageView image1,image2,image3,image4,image5;
    @FXML
    /**\brief \a title1 hasta \a title5 son los títulos que contendrán los items añadidos por el usuario.*/
    private Label title1,title2,title3,title4,title5;
    @FXML
    /**\brief \a rating1 hasta \a rating5 son las valoraciones que dará el usuario a los items añadidos.*/
    private TextField rating1,rating2,rating3,rating4,rating5;
    @FXML
    /**\brief \a errorLabel es un texto de error que aparece en caso de que el usuario haga algo mal. */
    private Label errorLabel;
    @FXML
    /**\brief \a addItemButton es el botón que usará el usuario para añadir items nuevos tras su búsqueda. */
    private Button addItemButton;
    @FXML
    /**\brief \a searchBarField es la barra de búsqueda que usará el usuario para encontrar items para valorar. */
    private TextField searchBarField;

    /**\brief \a filledPositions es el valor que controla cuantos de los 5 Items por añadir han sido añadidos. */
    private int filledPositions = 0;

    /**\brief \a isFilled es el vector que controla cuáles de los 5 Items por añadir han sido añadidos. */
    private Boolean[] isFilled = {false,false,false,false,false};

    /**\brief \a rates es el vector que guarda que valoraciones se han dado a los items añadidos. */
    private ArrayList<Float> rates = new ArrayList<>();

    /**\brief \a ids es el vector que guarda que IDs tienen los items añadidos. */
    private Set<Integer> ids = new HashSet<>();

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
     * \post Se actualiza el \a addItemButton como botón default para poder pulsarlo con la tecla Enter.
     */
    public void Init() {
        addItemButton.setDefaultButton(true);
        TextFields.bindAutoCompletion(searchBarField, DataManager.GetInstance().GetAllItemsNames());
    }

    /**\brief Gestiona el clic del botón Continue.
     * \pre El usuario clica el botón de Continue.
     * @throws IOException
     * \param ActionEvent e Evento de clic de botón Continue.
     * \post Se controlan los errores (mostrados en errorLabel) y en caso de no haberlos, se envia al usuario a la escena RecommendMe mediante SceneManager.
     */
    public void Continue(ActionEvent actionEvent) throws IOException, NoExistingActiveUserException {

        if(!(filledPositions == 5)) errorLabel.setText("Por favor, añada 5 items con sus valoraciones antes de continuar.");
        else if (!CheckRatings()) errorLabel.setText("Por favor, añada números del 1 al 10 en sus valoraciones.");
        else{
            errorLabel.setText("");
            UserManager.GetInstance().ActiveUserNewRating(DataManager.GetInstance().GetItemByName(title1.getText()).GetItemId(), rates.get(0));
            UserManager.GetInstance().ActiveUserNewRating(DataManager.GetInstance().GetItemByName(title2.getText()).GetItemId(), rates.get(1));
            UserManager.GetInstance().ActiveUserNewRating(DataManager.GetInstance().GetItemByName(title3.getText()).GetItemId(), rates.get(2));
            UserManager.GetInstance().ActiveUserNewRating(DataManager.GetInstance().GetItemByName(title4.getText()).GetItemId(), rates.get(3));
            UserManager.GetInstance().ActiveUserNewRating(DataManager.GetInstance().GetItemByName(title5.getText()).GetItemId(), rates.get(4));
            UserManager.GetInstance().SetHasStarted(true);
            SceneManager.GetInstance().ChangeSceneToRecommendMe();
        }
    }

    /**\brief Gestiona la comprobación de valoraciones.
     *
     * \pre Cierto.
     * \post Se retorna Cierto si la valoración añadida es un número entre el 1 y el 5. Se retorna Falso de cualquier otro modo.
     * \returns boolean
     */
    private boolean CheckRatings() {
        TextField[] ratings = {rating1,rating2,rating3,rating4,rating5};
        for(int i = 0; i < isFilled.length; ++i){
            if (ratings[i] == null) {
                return false;
            }
            float temp;
            try {
                temp = Float.parseFloat(ratings[i].getText());
            } catch (NumberFormatException nfe) {
                return false;
            }
            if(temp < 1 || temp > 10) return false;
            rates.add(temp);
        }
        return true;
    }

    /**\brief Gestiona el clic del botón AddItem.
     * \pre El usuario clica el botón de AddItem.
     * \param ActionEvent e Evento de clic de botón AddItem.
     * \post Se añade el Item del buscador con su respectiva Imagen y Título.
     */
    public void AddItem(ActionEvent actionEvent) {
        Label[] titles = {title1,title2,title3,title4,title5};
        ImageView[] images = {image1,image2,image3,image4,image5};
        if(filledPositions == 5) errorLabel.setText("Ya existen 5 items, elimina uno para añadir uno nuevo.");
        else{
            // FIXME: 13/12/2021 Get Item from the autocompleted text field
            for(int i = 0; i < isFilled.length; ++i){
                if (!isFilled[i]) {
                    String title = searchBarField.getText();
                    try{
                        int id = DataManager.GetInstance().GetItemByNameLC(title).GetItemId();
                        if(ids.contains(id)) {
                            errorLabel.setText("No se puede añadir mas de una vez un item.");
                            break;
                        }
                        ids.add(id);
                        errorLabel.setText("");
                        titles[i].setText(DataManager.GetInstance().GetItemByNameLC(title).GetName());
                        images[i].setImage(DataManager.GetInstance().GetItemByNameLC(title).GetImage());
                        isFilled[i] = true;
                        filledPositions++;
                    }
                    catch (Exception e){
                        errorLabel.setText("El item introducido no existe en el sistema.");
                    }
                    break;
                }
            }

        }
    }

    /**\brief Gestiona el clic del botón DeleteItem[1..5].
     * \pre El usuario clica el botón de DeleteItem[1..5].
     * \param Label title Título del Item a eliminar.
     * \param ImageView image Imagen del Item a eliminar.
     * \param int col Columna del Item a eliminar.
     * \post Se elimina el Item correspondiente a la columna pasada por parámetro reasignando al título e imagen los valores por defecto.
     */
    private void DeleteItem(Label title, ImageView image, int col) {
        if(isFilled[col-1]){
            ids.remove(DataManager.GetInstance().GetItemByName(title.getText()).GetItemId());
            title.setText("Item por añadir");
            image.setImage(new Image("NoImage.png"));
            isFilled[col-1] = false;
            if (filledPositions > 0) filledPositions--;
        }
    }
    /**\brief Gestiona el clic del botón DeleteItem1. Véase DeleteItem*/
    public void DeleteItem1(ActionEvent actionEvent) {
        DeleteItem(title1, image1, 1);
    }
    /**\brief Gestiona el clic del botón DeleteItem2. Véase DeleteItem*/
    public void DeleteItem2(ActionEvent actionEvent) {
        DeleteItem(title2, image2, 2);
    }
    /**\brief Gestiona el clic del botón DeleteItem3. Véase DeleteItem*/
    public void DeleteItem3(ActionEvent actionEvent) {
        DeleteItem(title3, image3, 3);
    }
    /**\brief Gestiona el clic del botón DeleteItem4. Véase DeleteItem*/
    public void DeleteItem4(ActionEvent actionEvent) {
        DeleteItem(title4, image4, 4);
    }
    /**\brief Gestiona el clic del botón DeleteItem5. Véase DeleteItem*/
    public void DeleteItem5(ActionEvent actionEvent) {
        DeleteItem(title5, image5, 5);
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
