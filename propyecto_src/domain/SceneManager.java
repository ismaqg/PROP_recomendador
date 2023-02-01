/**
 * @file SceneManager.java
 * @brief Contiene la clase SceneManager. Gestiona toda la interfaz gráfica.
 */

package fxsrc.propyecto.domain;


import fxsrc.propyecto.presentation.ItemInfoController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;


/** \class SceneManager
 * \brief Clase singleton encargada de gestionar el inicio de la interfaz y los cambios de escena.
 *
 * Ofrece operaciones de cambio de escena a todos los controladores.
 */
public class SceneManager extends Application {

    /**\brief \a instance es la instancia de la clase Singleton SceneManager. */
    private static SceneManager instance;

    /**\brief \a stg es el Stage principal que se instancia la primera vez. */
    private static Stage stg;

    /**\brief \a sceneStack es una pila de escenas para gestionar el GoBack. */
    private Stack<String> sceneStack;

    /**\brief \a itemStack es una pila de itemNames para gestionar el GoBack. */
    private Stack<String> itemStack;

    /**\brief \a first es un booleano que gestiona el GoBack. */
    private boolean first = true;

    /**\brief Función principal de la clase.
     * \pre Cierto.
     * \param String[] args.
     * \post Se lanza la ventana inicial de la aplicación y posteriormente se permite probar por consola el AlgorithmTester.
     */
    public static void main(String[] args) throws IOException {
        launch(args);
        AlgorithmTester.ReadAndExecuteAlgorithms();
    }

    /**\brief Función de inicio de escena.
     * \pre Cierto.
     * \param Stage firstStage Stage principal en que va a empezar la interfaz.
     * \post Se configura la ventana inicial y se abre la ventana de LogIn.
     */
    @Override
    public void start(Stage firstStage){

        sceneStack = new Stack<>();
        itemStack = new Stack<>();

        try{
            stg = firstStage;
            FXMLLoader loader = new FXMLLoader(new File("./res/views/logIn-view.fxml").toURI().toURL());
            Parent root = loader.load();
            firstStage.setScene(new Scene(root, 600, 400));
            firstStage.setResizable(false);
            firstStage.setTitle("Recomendador");
            sceneStack.push("./res/views/logIn-view.fxml");
            firstStage.show();
            firstStage.setOnCloseRequest(event -> {
                event.consume();
                try {
                    Logout();
                } catch (NoExistingActiveUserException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**\brief Permite salir de la aplicación y la cuenta.
     * \pre Existe una aplicación abierta.
     * @throws NoExistingActiveUserException
     * \post El usuario (si existe) cierra sesión y la aplicación se cierra.
     */
    public void Logout() throws NoExistingActiveUserException {
        Alert closeAlert = new Alert(Alert.AlertType.CONFIRMATION);
        closeAlert.setTitle("Confirmación de salida");
        closeAlert.setHeaderText("Estás a punto de cerrar la aplicación!");
        closeAlert.setContentText("¿Estás seguro de que quieres salir?");
        if(closeAlert.showAndWait().get() == ButtonType.OK){
            stg.close();
            try{
                UserManager.GetInstance().SignOut();
            }
            catch (NoExistingActiveUserException e){
                stg.close();
            }
        }

    }

    /**\brief Permite salir de la aplicación.
     * \pre Existe una aplicación abierta.
     * \post La aplicación se cierra.
     */
    public void Close() {
        stg.close();
    }

    /** \brief Devuelve la instancia de SceneManager
     *
     * \pre Cierto.
     * \post Si no existe la instancia de SceneManager, la crea y luego la devuelve, sino solo la devuelve.
     */
    public static SceneManager GetInstance() {
        if (instance == null) {
            instance = new SceneManager();
            instance.sceneStack = new Stack<>();
            instance.itemStack = new Stack<>();
            instance.sceneStack.push("./res/views/logIn-view.fxml");
        }
        return instance;
    }

    /**\brief Permite cambiar de escena.
     * \pre Existe una aplicación abierta.
     * @throws IOException
     * \param String newScene path relativo a la nueva escena a cambiar.
     * \post Se cambia la escena a la recibida por parámetro.
     */
    public void ChangeScene(String newScene) throws IOException {
        sceneStack.push(newScene);
        FXMLLoader loader = new FXMLLoader(new File(newScene).toURI().toURL());
        Parent root = loader.load();
        stg.getScene().setRoot(root);
    }

    /**\brief Permite cambiar a la escena anterior.
     * \pre Existe una aplicación abierta.
     * @throws IOException
     * \post Se cambia la escena a la anterior guardada en sceneStack.
     */
    public void ChangeSceneToPast() throws IOException{
        sceneStack.pop();
        if(sceneStack.lastElement().equals("./res/views/itemInfo-view.fxml")){
            itemStack.pop();
            try {
                ChangeSceneToItemInfo(itemStack.lastElement());
            } catch (AttValDoesNotExistException e) {
                e.printStackTrace();
            } catch (NoExistingActiveUserException e) {
                e.printStackTrace();
            }
        }
        else ChangeScene(sceneStack.lastElement());
    }

    /**\brief Permite cambiar a la escena SignUp.
     * \pre Existe una aplicación abierta.
     * @throws IOException
     * \post Se cambia la escena a SignUp.
     */
    public void ChangeSceneToSignUp() throws IOException{
        ChangeScene("./res/views/signUp-view.fxml");
    }

    /**\brief Permite cambiar a la escena LogIn.
     * \pre Existe una aplicación abierta.
     * @throws IOException
     * \post Se cambia la escena a LogIn.
     */
    public void ChangeSceneToLogIn() throws IOException{
        ChangeScene("./res/views/logIn-view.fxml");
    }

    /**\brief Permite cambiar a la escena Home.
     * \pre Existe una aplicación abierta.
     * @throws IOException
     * \post Se cambia la escena a Home y se maximiza la escena para obtener mayor tamaño.
     */
    public void ChangeSceneToHome() throws IOException{
        ChangeScene("./res/views/home-view.fxml");
        Maximize();
    }

    /**\brief Permite cambiar a la escena ForgotPass.
     * \pre Existe una aplicación abierta.
     * @throws IOException
     * \post Se cambia la escena a ForgotPass.
     */
    public void ChangeSceneToForgotPass() throws IOException {
        ChangeScene("./res/views/forgotPass-view.fxml");
    }

    /**\brief Permite cambiar a la escena EditProfile.
     * \pre Existe una aplicación abierta.
     * @throws IOException
     * \post Se cambia la escena a EditProfile y se maximiza la escena para obtener mayor tamaño.
     */
    public void ChangeSceneToEditProfile() throws IOException {
        ChangeScene("./res/views/editProfile-view.fxml");
        Maximize();
    }

    /**\brief Permite cambiar a la escena GetStarted.
     * \pre Existe una aplicación abierta.
     * @throws IOException
     * \post Se cambia la escena a GetStarted y se maximiza la escena para obtener mayor tamaño.
     */
    public void ChangeSceneToGetStarted() throws IOException {
        sceneStack.push("./res/views/home-view.fxml");
        FXMLLoader loader = new FXMLLoader(new File("./res/views/getStarted-view.fxml").toURI().toURL());
        Parent root = loader.load();
        stg.getScene().setRoot(root);
        Maximize();
    }

    /**\brief Permite cambiar a la escena FavList.
     * \pre Existe una aplicación abierta.
     * @throws IOException
     * \post Se cambia la escena a FavList y se maximiza la escena para obtener mayor tamaño.
     */
    public void ChangeSceneToFavList() throws IOException {
        ChangeScene("./res/views/likedList-view.fxml");
        Maximize();
    }

    /**\brief Permite cambiar a la escena RecommendList.
     * \pre Existe una aplicación abierta.
     * @throws IOException
     * \post Se cambia la escena a RecommendList y se maximiza la escena para obtener mayor tamaño.
     */
    public void ChangeSceneToRecommendList() throws IOException{
        ChangeScene("./res/views/recommendList-view.fxml");
        Maximize();
    }

    /**\brief Permite cambiar a la escena RecommendMe.
     * \pre Existe una aplicación abierta.
     * @throws IOException
     * \post Se cambia la escena a RecommendMe y se maximiza la escena para obtener mayor tamaño.
     */
    public void ChangeSceneToRecommendMe() throws IOException{
        ChangeScene("./res/views/recommendMe-view.fxml");
        Maximize();
    }

    /**\brief Permite cambiar a la escena ItemInfo.
     * \pre Existe una aplicación abierta.
     * @throws IOException
     * \post Se cambia la escena a ItemInfo y se maximiza la escena para obtener mayor tamaño.
     */
    public void ChangeSceneToItemInfo(String itemName) throws IOException, AttValDoesNotExistException, NoExistingActiveUserException {
        if(!first && !itemStack.lastElement().equals(itemName)){
            sceneStack.push("./res/views/itemInfo-view.fxml");
            itemStack.push(itemName);
        }
        else if(first){
            sceneStack.push("./res/views/itemInfo-view.fxml");
            itemStack.push(itemName);
            first = false;
        }
        FXMLLoader loader = new FXMLLoader(new File("./res/views/itemInfo-view.fxml").toURI().toURL());
        Parent root = loader.load();
        stg.getScene().setRoot(root);
        ItemInfoController itemInfo = loader.getController();
        itemInfo.LoadItemInfo(itemName);

        Maximize();
    }

    /**\brief Permite cambiar el tamaño de la escena a grande.
     * \pre Existe una aplicación abierta.
     * \post Se maximiza la escena para obtener mayor tamaño.
     */
    private void Maximize(){
        stg.setResizable(true);
        stg.setHeight(950);
        stg.setWidth(1650);
        stg.setX(130);
        stg.setY(60);
        stg.setResizable(false);
    }

    /**\brief Permite cambiar el tamaño de la escena a pequeño.
     * \pre Existe una aplicación abierta.
     * \post Se minimiza la escena para obtener menor tamaño.
     */
    private void Minimize() {
        stg.setResizable(true);
        stg.setHeight(450);
        stg.setWidth(650);
        stg.setX(0);
        stg.setY(0);
        stg.setResizable(false);
    }



}