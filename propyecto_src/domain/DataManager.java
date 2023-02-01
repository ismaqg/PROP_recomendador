/** @file DataManager.java
 *  @brief Contiene la clase DataManager
 */
package fxsrc.propyecto.domain;

import fxsrc.propyecto.data.DataAlgorithm;
import fxsrc.propyecto.data.DataItem;
import fxsrc.propyecto.data.DataRating;
import fxsrc.propyecto.data.DataUser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;


/** \class DataManager
 *   \brief Clase que sirve como puente entre la capa de Dominio y la capa de Datos. Se usa el patrón SINGLETON
 *
 *   Ofrece varias funciones para acceder a los datos de la capa de datos y en general sirve para controlar los datos
 *   */
public class DataManager {

    /** \brief \a DataManager Corresponde a la instancia del singleton DataManager
     */
    private static DataManager instance;

    /** \brief \a items Corresponde a la clase DataItem de la capa de datos
     */
    private DataItem items;
    /** \brief \a ratingsDB Corresponde a la clase DataRating de la capa de datos que hace referencia al conjunto de datos ratings.db.csv
     */
    private DataRating ratingsDB;
    /** \brief \a ratingsTestKnown Corresponde a la clase DataRating de la capa de datos que hace referencia al conjunto de datos ratings.test.known.csv
     */
    private DataRating ratingsTestKnown;
    /** \brief \a ratingsTestUnknown Corresponde a la clase DataRating de la capa de datos que hace referencia al conjunto de datos ratings.test.unknown.csv
     */
    private DataRating ratingsTestUnknown;

    /** \brief \a users Corresponde a la clase DataUser de la capa de datos que hace referencia al conjunto de datos users.csv
     */
    private DataUser users;

    /** \brief \a algorithmData Corresponde a la clase DataAlgorithm de la capa de datos que hace referencia al conjunto de datos kmeans.csv
     */
    private DataAlgorithm algorithmData;

    /** \brief Constructor por defecto de DataManager
     *
     * \pre <em>Cierto</em>
     * \post Se crea LA instancia de DataManager y se cargan todos los datos
     */
    private DataManager() {
        items = new DataItem();
        LoadItems("res/items.csv");

        ratingsDB = new DataRating();
        LoadRating("res/ratings.db.csv", ratingsDB);
        ratingsTestKnown = new DataRating();
        LoadRating("res/ratings.test.known.csv", ratingsTestKnown);
        ratingsTestUnknown = new DataRating();
        LoadRating("res/ratings.test.unknown.csv", ratingsTestUnknown);
        users = new DataUser();
        LoadUsers("res/users/users.csv");

        algorithmData = new DataAlgorithm();

    }

    /** \brief Devuelve la instancia de DataManager
     *
     * \pre <em>Cierto</em>
     * \post Si no existe la instancia de DataManager, la crea y luego la devuelve, sino solo la devuelve
     */
    public static DataManager GetInstance() {
        if (instance == null) {
            instance = new DataManager();
            instance.users.SetRatings();
        }
        return instance;
    }

    /** \brief Se cargan los Item del fichero pasado por parámetro
     *
     * \param String path Corresponde al path del fichero que queremos cargar
     * \pre <em>Cierto</em>
     * \post Se llama a la carga de CSV de \a items
     */
    private void LoadItems(String path) {
        items.LoadCSV(path);
    }

    /** \brief Se cargan los Rating del fichero pasado por parámetro
     *
     * \param String path Corresponde al path del fichero que queremos cargar
     * \param DataRating rating Corresponde al DataRating donde queremos cargar los datos del fichero pasado por parámetro
     * \pre <em>Cierto</em>
     * \post Se llama a la carga de CSV del DataRating pasado por parámetro
     */
    private void LoadRating(String path, DataRating rating) {
        rating.LoadCSV(path);
    }

    /** \brief Función que carga la información de los usuarios
     *
     * \param String path Corresponde al path del archivo a cargar
     * \pre \a users no es null
     * \post Carga la información de los usuarios
     */
    private void LoadUsers(String path) {
        users.LoadCSV(path);
        //users.Print();
    }

    /** \brief Devuelve todos los Item del sistema
     *
     * \pre <em>Cierto</em>
     * \post Devuelve todos los Item del sistema
     */
    public ArrayList<Item> GetAllItems() {
        return items.GetAllItems();
    }

    /** \brief Devuelve todos los Rating correspondientes al ratings.db.csv
     *
     * \pre <em>Cierto</em>
     * \post Devuelve todos los Rating correspondientes al ratings.db.csv
     */
    public ArrayList<Rating> GetAllRatingsDB() {
        return ratingsDB.GetAllRatings();
    }


    /** \brief Devuelve todos los Rating correspondientes al ratings.test.known.csv
     *
     * \pre <em>Cierto</em>
     * \post Devuelve todos los Rating correspondientes al ratings.test.known.csv
     */
    public ArrayList<Rating> GetAllRatingsTestKnown() {
        return ratingsTestKnown.GetAllRatings();
    }


    /** \brief Devuelve todos los Rating correspondientes al ratings.test.unknown.csv
     *
     * \pre <em>Cierto</em>
     * \post Devuelve todos los Rating correspondientes al ratings.test.unknown.csv
     */
    public ArrayList<Rating> GetAllRatingsTestUnknown() {
        return ratingsTestUnknown.GetAllRatings();
    }


    /** \brief Devuelve el Item en la posicion \a i
     *
     * \param int pos Corresponde a la posición del Item deseado dentro del array \a items
     * \pre <em>Cierto</em>
     * \post Devuelve el Item en la posición \a i
     */
    public Item GetItemByArrayPosition(int pos) {
        return items.GetItemByPosition(pos);
    }

    /** \brief Devuelve el Item con ID = \a id
     *
     * \param int id Corresponde al ID del Item deseado \a items
     * \pre <em>Cierto</em>
     * \post Devuelve el Item con ID = \a id
     */
    public Item GetItemByID(int id) {
        try {
            return items.GetItemByID(id);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
            return null;
        }
    }

    /** \brief Devuelve el Item con con nombre \a name
     *
     * \param String name Corresponde al nombre del Item deseado \a items
     * \pre \a items no es null
     * \post Devuelve el Item con con nombre \a name (no es case sensitive)
     */
    public Item GetItemByNameLC(String name) {
        return items.GetItemByNameLC(name);
    }


    /** \brief Devuelve el valor del Rating con ID de User \a userID y ID de Item \a itemID dentro
     *
     * \param int userID Corresponde a la ID del User que ha hecho la valoración
     * \param int itemID Corresponde a la ID del Item que ha sido valorado
     * \pre <em>Cierto</em>
     * \post Devuelve el valor del Rating con ID de User \a userID y ID de Item \a itemID dentro de DB
     */
    public float GetRatingValueDB(int userID, int itemID)  {
        try {
            return ratingsDB.GetRatingValueByID(userID, itemID);
        }
        catch (RatingDoesNotExistException e){
            e.printStackTrace();
            System.exit(0);
            return -1;
        }
    }

    /** \brief Devuelve el valor del Rating con ID de User \a userID y ID de Item \a itemID
     *
     * \param int userID Corresponde a la ID del User que ha hecho la valoración
     * \param int itemID Corresponde a la ID del Item que ha sido valorado
     * \pre <em>Cierto</em>
     * \post Devuelve el valor del Rating con ID de User \a userID y ID de Item \a itemID dentro de Known
     */
    public float GetRatingValueTestKnown(int userID, int itemID) {
        try {
            return ratingsTestKnown.GetRatingValueByID(userID, itemID);
        }
        catch(RatingDoesNotExistException e) {
            e.printStackTrace();
            System.exit(0);
            return -1;
        }

    }

    /** \brief Devuelve el valor del Rating con ID de User \a userID y ID de Item \a itemID
     *
     * \param int userID Corresponde a la ID del User que ha hecho la valoración
     * \param int itemID Corresponde a la ID del Item que ha sido valorado
     * \pre <em>Cierto</em>
     * \post Devuelve el valor del Rating con ID de User \a userID y ID de Item \a itemID dentro de Unknown
     */
    public float GetRatingValueTestUnknown(int userID, int itemID) {
        try {
            return ratingsTestUnknown.GetRatingValueByID(userID, itemID);
        }
        catch (RatingDoesNotExistException e){
            e.printStackTrace();
            System.exit(0);
            return -1;
        }

    }

    /** \brief Devuelve el valor de Security con ID de User \a userID y ID de Item \a itemID
     *
     * \param String username Corresponde al nombre del User
     * \pre <em>Cierto</em>
     * \post Devuelve la respuesta de seguridad del usuario pasado por parámetro
     */
    public String GetSecurity(String username) {
        return users.GetUser(username).GetSecurity();
    }

    /** \brief Comprueba si existe un User con username y password
     *
     * \param String username Corresponde al nombre del User
     * \param String password Corresponde a la contraseña del User
     * \pre <em>Cierto</em>
     * \post Devuelve si existe o no un User con el username y password pasados por parámetro
     */
    public boolean CheckCredentials(String username, String password) {
        return users.CheckCredentials(username, password);
    }

    /** \brief Loggea al usuario con nombre \a username
     *
     * \param String username Corresponde al nombre del User
     * \param boolean rememberMe Corresponde a si se desea iniciar sesion automaticamente al inciar la aplicacion
     * \pre Existe User con username = \a username
     * \post Devuelve el UserActual con los datos del User con nombre username
     */
    public UserActual SignIn(String username) {
        users.Print();
        return users.GetUser(username);
    }

    /** \brief Comprueba si existe un User con username
     *
     * \param String username Corresponde al nombre del User
     * \pre <em>Cierto</em>
     * \post Devuelve si existe o no un User con el username pasado por parámetro
     */
    public boolean UserExists(String username) {
        return users.UserExists(username);
    }

    /** \brief Crea un User con toda la información pasada por parámetro
     *
     * \param String username Corresponde al nombre del User
     * \param String password Corresponde a la contraseña del User
     * \param boolean rememberMe Corresponde a si se desea iniciar sesion automaticamente al inciar la aplicacion
     * \pre No existe User con username = \a username
     * \post Crea un User con toda la información pasada por parámetro
     */
    public UserActual SignUp(String username, String password, String email, String security) {
        int userID = users.GetFreeID();
        UserActual newUser = new UserActual(userID, username, email, password, security);
        users.AddUser(newUser);
        return newUser;
    }

    /** \brief Elimina el User con nombre \a username
     *
     * \param String username Corresponde al nombre del User
     * \pre Existe User con username = \a username
     * \post Se elimina el User con nombre \a username
     */
    public void DeleteAccount(String getUsername) {
        ratingsDB.DeleteRatingsOfUser(users.GetUser(getUsername).GetUserID());
        users.DeleteUser(getUsername);
    }

    /** \brief Se modifica la contraseña del User pasado por parámetro
     *
     * \param String getUsername Corresponde al nombre del User
     * \param String password Corresponde a la nueva contraseña del User
     * \pre Existe User con username = \a username
     * \post Se modifica la contraseña del User por la nueva pasada por parámetro
     */
    public void SetNewPassword(String getUsername, String newPassword) {
        users.SetNewPassword(getUsername, newPassword);
    }

    /** \brief Se modifica el nombre del User pasado por parámetro
     *
     * \param String getusername Corresponde al nombre (original) del User
     * \param String newUsername Corresponde al nuevo nombre del User
     * \pre Existe User con username = \a username
     * \post Se modifica el nombre del User por el nuevo pasado por parámetro
     */
    public void SetNewUsername(String getUsername, String newUsername) {
        users.SetNewUsername(getUsername, newUsername);
    }

    /** \brief Se modifica el email del User pasado por parámetro
     *
     * \param String getusername Corresponde al nombre del User
     * \param String newEmail Corresponde al nuevo email del User
     * \pre Existe User con username = \a username
     * \post Se modifica el email del User por el nuevo pasado por parámetro
     */
    public void SetNewEmail(String getUsername, String newEmail) {
        users.SetNewEmail(getUsername, newEmail);
    }

    /** \brief Devuelve las IDs de todos los User del sistema
     *
     * \pre <em>Cierto</em>
     * \post Devuelve las IDs de todos los User del sistema
     */
    public ArrayList<Integer> GetAllUsersID() {
        return ratingsDB.GetAllUsersID();
    }

    /** \brief Se modifica la respuesta de seguridad del User pasado por parámetro
     *
     * \param String getusername Corresponde al nombre del User
     * \param String newSecurity Corresponde a la nueva respuesta de seguridad
     * \pre Existe User con username = \a username
     * \post Se modifica la respuesta de seguridad del User por el nuevo pasado por parámetro
     */
    public void SetNewSecurity(String getUsername, String newSecurity) {
        users.SetNewSecurity(getUsername, newSecurity);
    }


    /** \brief Devuelve todos los Rating dentro del conjunto de DataRating pasado por parámetro
     *
     * \pre <em>Cierto</em>
     * \post Devuelve todos los Rating dentro del conjunto de DataRating pasado por parámetro
     */
    private ArrayList<Rating> GetRatingValue(int userID, DataRating dr) {
        try {
            return dr.GetUserRatings(userID);
        }
        catch (NoExistingUserIDException e){
            e.printStackTrace();
            return null;
        }
    }

    /** \brief Devuelve todos los Rating que ha hecho un user con userID = userID dentro del conjunto ratingsTestUnknown
     *
     * \pre <em>Cierto</em>
     * \post Devuelve todos los Rating que ha hecho un user con userID = userID dentro del conjunto \a ratingsTestUnknown
     */
    public ArrayList<Rating> GetUserUnknownRatings(int userID) {
        return GetRatingValue(userID, ratingsTestUnknown);
    }

    /** \brief Devuelve todos los Rating que ha hecho un user con userID = userID dentro del conjunto ratingsDB
     *
     * \pre <em>Cierto</em>
     * \post Devuelve todos los Rating que ha hecho un user con userID = userID dentro del conjunto \a ratingsDB
     */
    public ArrayList<Rating> GetRatingsValueDB(int userID) {
        return GetRatingValue(userID, ratingsDB);
    }

    /** \brief Devuelve todos los Rating dentro del conjunto \a dr clasificados por usuario
     *
     * \pre <em>Cierto</em>
     * \post Devuelve todos los Rating dentro del conjunto \a dr clasificados por usuario
     */
    private HashMap<Integer, ArrayList<Rating>> GetAllRatingsUsersMap(DataRating dr) {
        return dr.GetAllRatingsUsersMap();
    }

    /** \brief Devuelve todos los Rating dentro del conjunto \a ratingsDB clasificados por usuario
     *
     * \pre <em>Cierto</em>
     * \post Devuelve todos los Rating dentro del conjunto \a ratingsDB clasificados por usuario
     */
    public HashMap<Integer, ArrayList<Rating>> GetAllRatingsUsersMapDB() {
        return GetAllRatingsUsersMap(ratingsDB);
    }

    /** \brief Devuelve la informacion almacenada para el algoritmo KMeans
     *
     * \pre int k Corresponde a la k que se quiere usar para el KMeans
     * \pre int dataset Corresponde al dataset que se quiere usar para el KMeans
     * \pre \a algorithmData no es null
     * \post Devuelve la informacion almacenada para el algoritmo KMeans si es el que corresponde a los parametros
     */
    public ArrayList<ArrayList<Integer>> GetKMeans(int k, int dataset) {
        return algorithmData.GetKGroups(k, dataset);
    }

    /** \brief Asigna la informacion almacenada para el algoritmo KMeans
     *
     * \pre int k Corresponde a la k que se quiere usar para el KMeans
     * \pre int dataset Corresponde al dataset que se quiere usar para el KMeans
     * \pre ArrayList<ArrayList<Integer>> kgroups Corresponde a la información de los grupos del kmeans
     * \pre ArrayList<ArrayList<Rating>> centroids Corresponde a los centroides
     * \pre \a algorithmData no es null
     * \post Asigna la informacion almacenada para el algoritmo KMeans si es el que corresponde a los parametros
     */
    public void SetKMeans(int k, int dataset, ArrayList<ArrayList<Integer>> kgroups, ArrayList<ArrayList<Rating>> centroids) {
        algorithmData.SetKGroups(k, dataset, kgroups, centroids);
    }

    /** \brief Devuelve la informacion de los Centroids almacenada para algoritmo KMeans
     *
     * \pre Previamente se ha comprobado que el KMeans que se está cargando es el correcto
     * \post Devuelve la informacion de los Centroids almacenada para algoritmo KMeans
     */
    public ArrayList<ArrayList<Rating>> GetCentroids() {
        return algorithmData.GetCentroids();
    }

    /** \brief Devuelve los nombres de todos los Item
     * \pre items no es null
     * \post Devuelve el Item con nombre = \a name. Devuelve null si no lo encuentra
     */
    public ArrayList<String> GetAllItemsNames() {
        return items.GetAllItemsNames();
    }

    /** \brief Devuelve el Item con nombre = \a name
     * \param String name Corresponde al nombre del Item deseado
     * \pre <em>Cierto</em>
     * \post Devuelve el Item con nombre = \a name. Devuelve null si no lo encuentra
     */
    public Item GetItemByName(String name) {
        try {
            return items.GetItemByName(name);
        }
        catch (ItemDoesNotExistException e) {
            e.printStackTrace();
            return null;
        }
    }

    /** \brief Devuelve el maximo valor sobre el que los ratings se están puntuando (5,10,100)
     * \pre <em>Cierto</em>
     * \post Devuelve el maximo valor sobre el que los ratings se están puntuando (5,10,100)
     */
    public float GetMaxRating() {
        return DataRating.GetMaxRating();
    }

    /** \brief Devuelve los \a top Items mejor puntuados
     * \param int top Corresponde a la cantidad de Items deseados
     * \pre <em>Cierto</em>
     * \post Devuelve los \a top Items mejor puntuados
     */
    public ArrayList<Item> GetTopRatedItems(int top){
        return items.GetTopRated(top);
    }

    /** \brief Añade un nuevo Rating al conjunto
     * \param Rating r Corresponde al rating a añadir
     * \pre <em>Cierto</em>
     * \post Añade un nuevo Rating al conjunto
     */
    public void AddNewRating(Rating r) {
        ratingsDB.AddRating(r);
        algorithmData.SetDirty(true);
    }

    /** \brief Función que devuelve todos los Rating de un usuario
     * \param int userID corresponde la ID del usuario del cual queremmos obtener todos sus rating
     * \pre <em>Cierto</em>
     * \post Devuelve todos los Rating de un usuario
     */
    public ArrayList<Rating> GetUserRatings(int userID) {
        try {
            return ratingsDB.GetUserRatings(userID);
        }
        catch (Exception e) {
            return new ArrayList<Rating>();
        }
    }

    /** \brief Función que alamacena la información de los usuarios
     * \pre \a users no es null
     * \post Almacena la información de los usuarios en CSV (util para cuando se modifica la información del user actual)
     */
    public void StoreUsersInfo() {
        users.StoreCSV();
    }

    /** \brief Función que modifica un Rating
     * \param Rating r Corresponde al rating a modificar
     * \pre ratingsDB no es null
     * \post Modifica un Rating
     */
    public void ModifyItemRating(Rating r) {
        ratingsDB.ModifyItemRating(r);
    }

    /** \brief Función que elimina un Rating
     * \param Rating r Corresponde al rating a eliminar
     * \pre ratingsDB no es null
     * \post Elimina un Rating
     */
    public void DeleteRatedItem(int userID, int itemID) {
        ratingsDB.DeleteRating(userID, itemID);
    }
}