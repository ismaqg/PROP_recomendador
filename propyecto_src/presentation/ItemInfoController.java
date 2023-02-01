/**
 * @file ItemInfoController.java
 * @brief Contiene la clase ItemInfoController.
 */

package fxsrc.propyecto.presentation;

import fxsrc.propyecto.domain.*;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;

/** \class ItemInfoController
 * \brief Clase encargada del menú del item.
 *
 * Gestiona las acciones que se pueden hacer desde la pantalla de ItemInfo.
 */
public class ItemInfoController {

    @FXML
    /**\brief \a Estos labels contienen los datos del ítem. */
    private Label title, rating, attInfo1, userRating;

    @FXML
    /**\brief \a Image contiene la imagen del item, y ratingStar la estrella del rating. */
    private ImageView image, ratingStar;

    @FXML
    /**\brief \a Estos botones contienen los botones de añadir a favoritos, valorar y confirmar valoración. */
    private Button ratingEnter, likeButton, rateButton;

    @FXML
    /**\brief \a ratingField contiene el campo de texto donde se introduce la valoración. */
    private TextField ratingField;

    @FXML
    /**\brief \a description contiene la sinopsis del ítem. */
    private Text description;

    @FXML
    /**\brief \a image1, image2 y image3 contienen las imagenes de los 3 items similares. */
    private ImageView image1, image2, image3;

    @FXML
    /**\brief \a title1, title2 y title3 contienen las títulos de los 3 items similares. */
    private Label title1, title2, title3;

    /**\brief \a itemID contiene la ID del ítem de ItemInfo. */
    private int itemID;

    /**\brief \a item es el item de ItemInfo. */
    private Item item;

    /**\brief \a userRated es la valoración que el usuario le ha puesto al ítem de ItemInfo. */
    private float userRated;

    /**\brief Carga la escena de ItemInfo.
     *
     * \param Nombre del ítem que muestra la escena.
     * \pre Existe un item con nombre itemName.
     * @throws NoExistingActiveUserException
     * @throws AttValDoesNotExistException
     * \post Se carga la escena de ItemInfo con los datos del item itemName.
     */
    public void LoadItemInfo(String itemName) throws AttValDoesNotExistException, NoExistingActiveUserException {

        item = DataManager.GetInstance().GetItemByName(itemName);
        itemID = item.GetItemId();

        InitButton();
        LoadSimilar();

        title.setText(item.GetName());
        rating.setText(Float.toString(item.GetScore()));
        image.setImage(item.GetImage());
        image.setPreserveRatio(false);
        image.setViewport(new Rectangle2D(0,0,225,297));
        description.setText(item.GetDescription());
        if(description.getText().isEmpty()) description.setText("No hay descripción disponible.");

        getGenresName();
    }

    /**\brief Inicializa los diferentes botones de la escena ItemInfo.
     *
     * \pre Cierto.
     * @throws NoExistingActiveUserException
     * \post Se muestran todos los botones por pantalla.
     */
    private void InitButton() throws NoExistingActiveUserException {
        DisableTextField();
        userRated = UserManager.GetInstance().GetItemRating(itemID);
        boolean liked = UserManager.GetInstance().GetFavourites().contains(item);

        if(userRated != -1.0f) {
            userRated = (float) (Math.round(userRated * 100.0) / 100.0);
            userRating.setText(String.valueOf(userRated));
            ratingStar.setVisible(true);
            rateButton.setText("Editar valoración");
        }

        else {
            userRating.setText("No has valorado este ítem.");
            ratingStar.setVisible(false);
            rateButton.setText("Valorar ítem");
        }

        if(liked) {
            ChangeLike(false);
        }
        else {
            ChangeLike(true);
        }
    }

    /**\brief Muestra ítems similares con el de ItemInfo.
     *
     * \pre ItemInfo tiene un ítem.
     * \post Se muestra por pantalla los 3 ítems que más se parecen.
     */
    private void LoadSimilar(){
        ImageView[] images = {image1,image2,image3};
        Label[] title = {title1, title2, title3};
        int nSimilar = 3;
        ContentBasedFiltering cbf = new ContentBasedFiltering(5);
        ArrayList<Item> similar = cbf.GetNSimilarItems(itemID, nSimilar);
        for(int i = 0; i < nSimilar; ++i){
            images[i].setImage(similar.get(i).GetImage());
            title[i].setText(similar.get(i).GetName());
        }
    }

    /**\brief Devuelve los géneros que tiene un ítem.
     *
     * \pre Existe un ítem con géneros.
     * @throws AttValDoesNotExistException
     * \post Se muestra en pantalla los géneros.
     */
    public void getGenresName() throws AttValDoesNotExistException {
        ArrayList<ItemAttribute> itemAtt = item.GetCategories();
        String value = "";
        if (itemAtt.get(0).GetSize() <= 1) {
            value = (String) itemAtt.get(0).GetAttributeValue(0);
        }
        else {
            for(int i=0; i<itemAtt.get(0).GetSize(); i++){
                value += itemAtt.get(0).GetAttributeValue(i);
                if(i < itemAtt.get(0).GetSize() - 1) {
                    value += ", ";
                }
            }
        }
        value += ".";
        attInfo1.setText(value);
    }

    /**\brief Gestiona la desactivación de Buttons.
     *
     * \param Button button Botón a desactivar.
     * \pre El usuario accede a cambiar algún dato.
     * \post Se cambia a desactivado e invisible el Button pasado por parámetro.
     */
    private void DisableButton(Button button) {
        button.setDisable(true);
        button.setVisible(false);
        button.setDefaultButton(false);
    }

    /**\brief Gestiona la activación de TextFields.
     *
     * \pre El usuario accede a cambiar algún dato.
     * \post Se muestra visible y editable el TextField pasado por parámetro.
     */
    private void EnableTextField () {
        ratingField.setVisible(true);
        ratingField.setEditable(true);
        ratingEnter.setDisable(false);
        ratingEnter.setVisible(true);
        ratingEnter.setDefaultButton(true);

    }

    /**\brief Gestiona la desactivación de TextFields.
     *
     * \pre El usuario cancela o acepta el cambio algún dato.
     * \post Se quita la propiedad de edición y visibilidad del TextField pasado por parámetro.
     */
    private void DisableTextField () {
        ratingField.setVisible(false);
        ratingField.setEditable(false);

        ratingEnter.setDisable(true);
        ratingEnter.setVisible(false);
        ratingEnter.setDefaultButton(false);

    }

    /**\brief Gestiona el clic del botón Home.
     *
     * \pre El usuario clica el botón de Home.
     * @throws IOException
     * \post Se envía al usuario a la escena de Home mediante SceneManager.
     */
    public void GoToHome() throws IOException {
        SceneManager.GetInstance().ChangeSceneToHome();
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
     * \post El usuario sale del sistema y se cierra la aplicación.
     */
    public void LogOut() throws NoExistingActiveUserException, IOException {
        SceneManager.GetInstance().Logout();
    }

    /**\brief El usuario le da a confirmar valoración.
     *
     * \pre El usuario clica el botón de confirmar.
     * \post El usuario añade su valoración a la base de datos.
     */
    public void RateItem() {

        userRating.setVisible(false);
        EnableTextField();

        if(userRated != -1.0f) {
            ratingEnter.setOnAction(e-> EditRating());
        }
        else{
            ratingEnter.setOnAction(e-> {
                try {
                    ConfirmRating();
                } catch (NoExistingActiveUserException ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

    /**\brief El usuario añade una nueva valoración.
     *
     * \pre No existe valoración.
     * @throws NoExistingActiveUserException
     * \post El usuario añade su valoración a la base de datos.
     */
    public void ConfirmRating() throws NoExistingActiveUserException {
        String rating = ratingField.getText();

        if(CheckInput(rating)) {

            userRated = Float.parseFloat(rating);
            UserManager.GetInstance().ActiveUserNewRating(itemID, userRated);
            userRated = (float) (Math.round(userRated * 100.0) / 100.0);
            userRating.setText(String.valueOf(userRated));

            DisableTextField();

            userRating.setVisible(true);
            rateButton.setText("Editar valoración");
            ratingStar.setVisible(true);
            DisableButton(ratingEnter);
        }
        else {
            //FIXME posar label error
            System.out.println("error de new");
        }
    }
    /**\brief Se comprueba si el input es una valoración válida.
     *
     * \pre El usuario introduce una valoración.
     * \param String rating Contiene el rating introducido por el usuario
     * \post Retorna true si el valor es un float entre 0 y 10.
     */
    public Boolean CheckInput(String rating) {
        if (rating == null) {
            return false;
        }
        float temp;
        try {
            temp = Float.parseFloat(rating);
        } catch (NumberFormatException nfe) {
            return false;
        }
        if(temp < 1 || temp > 10) return false;

        return true;
    }

    /**\brief Se edita la valoración de un ítem.
     *
     * \pre El usuario clica el botón de confirmar valoración.
     * \post El usuario edita su valoración.
     */
    public void EditRating() {
        String rating = ratingField.getText();

        if(CheckInput(rating)) {
            userRated = Float.parseFloat(rating);
            UserManager.GetInstance().ModifyItemRating(itemID, userRated);
            userRated = (float) (Math.round(userRated * 100.0) / 100.0);
            userRating.setText(String.valueOf(userRated));

            DisableTextField();

            userRating.setVisible(true);
            rateButton.setText("Editar valoración");
            DisableButton(ratingEnter);
        }
        else {
            System.out.println("error de edit");
        }
    }

    /**\brief Se elimina la valoración de un ítem.
     *
     * \pre El usuario clica el botón de Eliminar valoración.
     * \post Se elimina la valoración.
     */
    public void DeleteRating() {
        if(userRated != -1.0f) {
            UserManager.GetInstance().DeleteRatedItem(itemID);
            userRated = -1.0f;

            userRating.setText("Este ítem no tiene valoración.");
            ratingStar.setVisible(false);
            rateButton.setText("Valorar ítem");
        }
    }
    /**\brief Añade un ítem a la lista de favoritos.
     *
     * \pre El usuario le da al botón de "Me gusta".
     * \post El ítem se añade a la lista de favoritos.
     */
    public void LikeItem() {
        UserManager.GetInstance().AddFavourite(itemID);
        ChangeLike(false);
    }

    /**\brief Quita un ítem a la lista de favoritos.
     *
     * \pre El usuario le da al botón de "No me gusta".
     * \post El ítem se elimina de la lista de favoritos.
     */
    public void DislikeItem() {
        UserManager.GetInstance().RemoveFavourite(itemID);
        ChangeLike(true);
    }

    /**\brief Cambia la funcionalidad del botón "Me gusta".
     *
     * \param Boolean like es true si queremos cambiar a like y false si cambiamos a dislike.
     * \pre Entra un boolean like por parámetro.
     * \post El botón cambia segun el booleano introducido.
     */
    private void ChangeLike(boolean like) {
        if(like) {
            likeButton.setOnAction(e-> LikeItem());
            likeButton.setText("Me gusta");
        }
        else{
            likeButton.setOnAction(e-> DislikeItem());
            likeButton.setText("Ya no me gusta");
        }
    }

    /**\brief Gestiona el clic de las imagenes de los items.
     *
     * \pre El usuario clica cualquier imagen de un item.
     * @throws NoExistingActiveUserException
     * @throws IOException
     * @throws AttValDoesNotExistException
     * \post Se envía al usuario a la escena del Item mediante SceneManager.
     */
    public void GoToItem1() throws NoExistingActiveUserException, IOException, AttValDoesNotExistException {
        SceneManager.GetInstance().ChangeSceneToItemInfo(title1.getText());
    }

    /**\brief Gestiona el clic de las imagenes de los items.
     *
     * \pre El usuario clica cualquier imagen de un item.
     * @throws NoExistingActiveUserException
     * @throws IOException
     * @throws AttValDoesNotExistException
     * \post Se envía al usuario a la escena del Item mediante SceneManager.
     */
    public void GoToItem2() throws NoExistingActiveUserException, IOException, AttValDoesNotExistException {
        SceneManager.GetInstance().ChangeSceneToItemInfo(title2.getText());
    }

    /**\brief Gestiona el clic de las imagenes de los items.
     *
     * \pre El usuario clica cualquier imagen de un item.
     * @throws NoExistingActiveUserException
     * @throws IOException
     * @throws AttValDoesNotExistException
     * \post Se envía al usuario a la escena del Item mediante SceneManager.
     */
    public void GoToItem3() throws NoExistingActiveUserException, IOException, AttValDoesNotExistException {
        SceneManager.GetInstance().ChangeSceneToItemInfo(title3.getText());
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
