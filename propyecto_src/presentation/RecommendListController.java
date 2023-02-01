package fxsrc.propyecto.presentation;

import fxsrc.propyecto.domain.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;



public class RecommendListController implements Initializable {
    @FXML
    /**\brief \a scrollPane es el ScrollPane que permite desplazarse verticalmente a través de la lista de items añadidos. */
    private ScrollPane scrollPane;

    /**\brief \a grid es el GridPane que muestra la lista de items añadidos. */
    private GridPane grid;

    /**\brief \a list es el array que controla la lista de items añadidos. */
    private ArrayList<Item> list;
    @Override

    /**\brief Funcion que se llama al crearse la escena.
     *
     * \pre Cierto.
     * \param URL location parámetro necesario de la función ajeno al funcionamiento de EditProfileController.
     * \param ResourceBundle resources parámetro necesario de la función ajeno al funcionamiento de EditProfileController.
     * \post Se llama a la función Init.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Init();
    }

    /**\brief Inicialización de parámetros necesarios.
     *
     * \pre Cierto.
     * \post Se crea la Grid que contendra todas las recomendaciones guardadas.
     */
    private void Init() {
        list = UserManager.GetInstance().GetLikedRecommendations();
        grid = new GridPane();
        Font font = Font.font("System", FontWeight.BOLD, 20);
        for(int i = 0; i < list.size(); ++i){
            VBox vBox = new VBox();

            Button deleteButton = new Button("Eliminar");
            int finalI = i;
            deleteButton.setOnAction(e->DeleteItem(finalI, list.get(finalI).GetItemId()));
            deleteButton.setFont(font);
            deleteButton.setPrefHeight(46.0);
            deleteButton.setPrefWidth(230.0);
            deleteButton.setTextFill(Paint.valueOf("#e40000"));

            Label title = new Label(list.get(i).GetName());
            title.setAlignment(Pos.TOP_CENTER);
            title.setPrefSize(230, 100);
            title.setWrapText(true);
            title.setFont(font);

            ImageView image = new ImageView(list.get(i).GetImage());

            image.setPreserveRatio(false);
            image.setViewport(new Rectangle2D(0,0,225,297));

            image.setOnMouseClicked(e-> {
                try {
                    GoToItem(title.getText());
                } catch (NoExistingActiveUserException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (AttValDoesNotExistException ex) {
                    ex.printStackTrace();
                }
            });

            vBox.getChildren().add(image);
            vBox.getChildren().add(title);
            vBox.getChildren().add(deleteButton);
            grid.add(vBox,i%5,i/5);
        }

        grid.setPrefWidth(scrollPane.getPrefWidth());
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(25);
        grid.setVgap(25);
        scrollPane.setVmax(grid.getChildren().size()/5);
        scrollPane.setContent(grid);

    }

    /**\brief Eliminación de Items de la grid.
     *
     * \pre Existen Items en la grid.
     * \post Se elimina el item con el ID pasado por parámetro.
     */
    private void DeleteItem(int i, int id) {
        grid.getChildren().remove(i);
        UserManager.GetInstance().RemoveFromLikedRecommendations(id);
        Init();
    }

    /**\brief Gestiona el clic de las imagenes de los items.
     *
     * \pre El usuario clica cualquier imagen de un item.
     * @throws NoExistingActiveUserException
     * @throws IOException
     * @throws AttValDoesNotExistException
     * \param String name Nombre del item a visitar.
     * \post Se envía al usuario a la escena del Item mediante SceneManager.
     */
    private void GoToItem(String name) throws NoExistingActiveUserException, IOException, AttValDoesNotExistException {
        SceneManager.GetInstance().ChangeSceneToItemInfo(name);
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
