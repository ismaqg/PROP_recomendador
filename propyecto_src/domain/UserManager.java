/** @file UserManager.java
 *  @brief Contiene la clase UserManager
 */

package fxsrc.propyecto.domain;

import java.util.ArrayList;

/** @class UserManager
 *   @brief Clase singleton encargada de recibir peticiones acerca de usuarios y servirlas.
 *
 *   Ofrece operaciones para que el usuario pueda hacer LogIn, LogOut, eliminar cuenta y cambiar su perfil, entre otras.
 *   Será la clase con la que se comunicará toda la capa de presentación relativa a cuestiones del usuario y esta delegará el trabajo que tenga que ver con los datos almacenados de los usuarios a la clase Data Manager.
 */
public class UserManager {

    /** @brief \a instance es la única instancia que existirá en un cierto momento de esta clase singleton
     */
    private static UserManager instance; //UserManager es un SINGLETON

    /** @brief \a activeUser es la instancia del usuario activo en un cierto momento. Nunca habrá 2 usuarios activos simultáneamente
     */
    private UserActual activeUser;
    //Sin DataManager aquí porque Datamanager ES SINGLETON

    /** @brief Constructora de UserManager. Privada porque es singleton.
     *
     * \pre La instancia de esta clase es \a null
     * \post Se crea una instancia de UserManager, la cual aún no tendrá conocimiento de ningún usuario activo.
     */
    private UserManager() {
        activeUser = null;
    }

    /** @brief Retorna la única instancia existente en el sistema de UserManager
     *
     * \pre <em>Cierto</em>
     * \post Retorna la única instancia existente en el sistema de UserManager
     */
    public static UserManager GetInstance() {
        if(instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    /** @brief Conecta al usuario con ese \a username y \a password en el sistema
     *
     * @throws ExistingActiveUserException
     * @throws BadCredentialsException
     *
     * \pre \a username y \a password son Strings válidos.
     * @param username es el nombre de usuario de quien está haciendo SignIn
     * @param password es la contraseña de quien está haciendo SignIn
     * \post Conecta al usuario con ese \a username y \a password en el sistema, si es que existían, y este usuario será el usuario activo. Si \a rememberMe es \a true, el sistema recordará su inicio de sesión y no se lo pedirá al usuario la próxima vez que acceda a la aplicación.
     */
    public void SignIn(String username, String password) throws ExistingActiveUserException, BadCredentialsException {
        if(activeUser != null) throw new ExistingActiveUserException();
        boolean correct = DataManager.GetInstance().CheckCredentials(username, password);
        if (!correct) throw new BadCredentialsException();

        activeUser = DataManager.GetInstance().SignIn(username);
    }

    /** @brief Da de alta al usuario con ese \a username y \a password en el sistema. Implícitamente hace un SignIn después de dar de alta.
     *
     * @throws ExistingActiveUserException
     * @throws ExistingUsernameException
     *
     * \pre \a username y \a password son Strings válidos. rememberMe indica si el usuario quiere que se recuerde su inicio de sesión al cerrar la aplicación para que no se le vuelva a pedir.
     * @param username es el nombre de usuario de quien está haciendo SignIn
     * @param password es la contraseña de quien está haciendo SignIn
     * @param email es el correo electronico del usuario
     * @param security es la respuesta a una pregunta de seguridad para recuperar la contraseña
     * \post Conecta al usuario con ese \a username y \a password en el sistema, si es que existían, y este usuario será el usuario activo. Si \a rememberMe es \a true, el sistema recordará su inicio de sesión y no se lo pedirá al usuario la próxima vez que acceda a la aplicación.
     */
    public void SignUp(String username, String password, String email, String security) throws ExistingActiveUserException, ExistingUsernameException {
        if(activeUser != null)  throw new ExistingActiveUserException();
        if(DataManager.GetInstance().UserExists(username)) throw new ExistingUsernameException();

        activeUser = DataManager.GetInstance().SignUp(username, password, email, security);
    }

    /** @brief Desconecta al usuario activo del sistema, haciendo que no haya ningún usuario activo usándolo.
     *
     * @throws NoExistingActiveUserException
     *
     * \pre <em>Cierto</em>.
     * \post Desconecta al usuario activo del sistema
     */
    public void SignOut() throws NoExistingActiveUserException {
        if(activeUser == null) throw new NoExistingActiveUserException();
        activeUser = null; // al desreferenciarse el último puntero a este objeto de activeUser que se creó (que lo creó el dATAmANAGER
        // en su metodo getNewUser), ya es como hacer delete del objeto (visto en internet). El problema estaría
        // si en algún otro lado aún se tiene guardada la referencia a este usuario
    }

    /** @brief Desconecta al usuario activo del sistema, haciendo que no haya ningún usuario activo usándolo, y se borran todos sus datos de la Base de Datos de la aplicación.
     *
     * @throws NoExistingActiveUserException
     *
     * \pre <em>Cierto</em>.
     * \post Desconecta al usuario activo del sistema y se borran todos sus datos de la Base de Datos de la aplicación.
     */
    public void DeleteCurrentAccount() throws NoExistingActiveUserException {
        if(activeUser == null) throw new NoExistingActiveUserException();
        DataManager.GetInstance().DeleteAccount(activeUser.GetUsername());
        activeUser = null;
    }

    /** @brief El usuario activo cambia su contraseña
     *
     * @throws NoExistingActiveUserException
     *
     * \pre <em>Cierto</em>.
     * @param newPassword nueva contraseña que se quiere que tenga el usuario activo
     * \post El sistema guarda la nueva contraseña de inicio de sesión del usuario activo.
     */
    public void ChangePassword(String newPassword) throws NoExistingActiveUserException {
        if(activeUser == null) throw new NoExistingActiveUserException();
        DataManager.GetInstance().SetNewPassword(activeUser.GetUsername(), newPassword);
        activeUser.SetPassword(newPassword);
    }

    /** @brief El usuario activo cambia su nombre de usuario, si es que el que quiere no está ya en uso
     *
     * @throws NoExistingActiveUserException
     * @throws ExistingUsernameException
     *
     * \pre <em>Cierto</em>.
     * @param newUsername nuevo nombre de usuario que se quiere que tenga el usuario activo
     * \post El sistema guarda el nuevo nombre de usuario del usuario activo, si es que quiere un nombre que no se está utilizando ya.
     */
    public void ChangeUsername(String newUsername) throws NoExistingActiveUserException, ExistingUsernameException {
        if(activeUser == null)  throw new NoExistingActiveUserException();
        if (DataManager.GetInstance().UserExists(newUsername)) throw new ExistingUsernameException();
        DataManager.GetInstance().SetNewUsername(activeUser.GetUsername(), newUsername);
        activeUser.SetUsername(newUsername);
    }

    /** @brief Devuelve el UserID del usuario activo
     *
     * @throws NoExistingActiveUserException
     *
     * \pre <em>Cierto</em>.
     * \post Devuelve el UserID del usuario activo.
     */
    public int GetCurrentUserID() throws NoExistingActiveUserException {
        if(activeUser == null) throw new NoExistingActiveUserException();
        return activeUser.GetUserID();
    }

    /** @brief Devuelve la RatingsList del usuario activo
     *
     * @throws NoExistingActiveUserException
     *
     * \pre <em>Cierto</em>
     * \post Devuelve la RatingsList del usuario activo
     */
    public ArrayList<Rating> GetCurrentUserRatingsList() throws NoExistingActiveUserException {
        if(activeUser == null) throw new NoExistingActiveUserException();
        return activeUser.GetRatingsList();
    }

    /** @brief Devuelve el nombre de usuario del usuario activo
     *
     * @throws NoExistingActiveUserException
     *
     * \pre <em>Cierto</em>.
     * \post Devuelve el nombre de usuario del usuario activo.
     */
    public String GetCurrentUserUsername() throws NoExistingActiveUserException {
        if(activeUser == null) throw new NoExistingActiveUserException();
        return activeUser.GetUsername();
    }

    /** @brief Devuelve el correo electronico del usuario activo
     *
     * @throws NoExistingActiveUserException
     *
     * \pre <em>Cierto</em>.
     * \post Devuelve el email del usuario activo.
     */
    public String GetCurrentUserEmail() throws NoExistingActiveUserException {
        if(activeUser == null) throw new NoExistingActiveUserException();
        return activeUser.GetEmail();
    }

    /** @brief Devuelve la respuesta a la pregunta de seguridad del usuario activo
     *
     * @throws NoExistingActiveUserException
     *
     * \pre <em>Cierto</em>
     * \post Devuelve la respuesta a la pregunta de seguridad del usuario activo
     */
    public String GetCurrentSecurity() throws NoExistingActiveUserException {
        if(activeUser == null) throw new NoExistingActiveUserException();
        return activeUser.GetSecurity();

    }

    /** @brief Añade una nueva valoración a un cierto item que valora por primera vez el usuario activo
     *
     * \pre El usuario NO había valorado ese item jamás
     * @param itemID es el ID del item que está valorando
     * @param rating es la nota a ese item
     * @throws NoExistingActiveUserException
     * \post Se almacena la información de esa nueva valoración
     */
    public void ActiveUserNewRating(int itemID, float rating) throws NoExistingActiveUserException {
        if(activeUser == null) throw new NoExistingActiveUserException();
        Rating r = new Rating(activeUser.GetUserID(), itemID, rating);
        activeUser.AddToRatingsList(r);
        DataManager.GetInstance().AddNewRating(r); //ese r ya almacena la información de quien es el user que está haciendo la valoracion (así como el item y la nota)
        DataManager.GetInstance().StoreUsersInfo();
    }

    /** @brief Retorna un booleano indicando si el usuario ya ha añadido valoraciones de items al sistema o no
     *
     * @throws NoExistingActiveUserException
     *
     * \pre <em>Cierto</em>
     * \post Retorna un booleano indicando si el usuario ya ha añadido valoraciones de items al sistema o no
     */
    public boolean GetHasStarted() throws NoExistingActiveUserException {
        if(activeUser == null) throw new NoExistingActiveUserException();
        return activeUser.GetStarted();
    }

    /** @brief Retorna la nota que le ha puesto el usuario activo a \a itemID (o -1 si no lo ha valorado)
     *
     * @throws NoExistingActiveUserException
     *
     * \pre itemID corresponde al ID de un item existente en el sistema
     * @param itemID
     * \post Retorna la nota que le ha puesto el usuario activo a \a itemID (o -1 si no lo ha valorado)
     */
    public float GetItemRating(int itemID) throws NoExistingActiveUserException {
        if(activeUser == null) throw new NoExistingActiveUserException();
        for (Rating r : activeUser.GetRatingsList()) {
            if(r.GetItemID() == itemID) return r.GetRating();
        }
        return -1.0f;
    }

    /** @brief Sirve para poder añadir un nuevo itemID que ha recomendado el sistema al usuario activo a la lista de recomendaciones que el usuario se ha guardado para poder consultar más tarde
     *
     * \pre itemID es el ID de un item que el sistema le ha recomendado al usuario activo
     * @param itemID es un item que se le ha recomendado al usuario y quiere añadirlo en la lista de items que el sistema le ha recomendado y quiere guardar para poder consultar más tarde
     * \post se añade itemID a la lista de likedRecommendedIDs del usuario activo
     */
    public void AddLikedRecommendation(int itemID) {
        activeUser.AddLikedRecommendation(itemID);
        DataManager.GetInstance().StoreUsersInfo();
    }

    /** @brief Elimina un item de la lista de aquellos items de los que le había recomendado el sistema que se había guardado el usuario
     *
     * \pre itemID se encuentra en likedRecommendedIDs
     * @param itemID es el itemID en cuestion
     * \post se elimina el itemID de likedRecommendedIDs
     */
    public void RemoveFromLikedRecommendations(int itemID) {
        activeUser.RemoveLikedRecommendation(Integer.valueOf(itemID));
        DataManager.GetInstance().StoreUsersInfo();
        //DataManager.GetInstance().RemoveFromLikedRecommendations(activeUser.GetUserID(), itemID); <- innecesaria porque el store users lo hace implicitamente (porque guarda la info de los usuarios creados por nosotros)
    }

    /** @brief Setter de likedRecommendations del usuario activo
     *
     * \pre likedRecommendationsIDs valido
     * @param likedRecommendationsIDs representa el nuevo vector "likedRecommendations" (ver en definición del atributo de UserActual.java qué es) del usuario activo
     * \post se actualizan las likedRecommendationsIDs del usuario activo
     */
    public void SetLikedRecommendations(ArrayList<Integer> likedRecommendationsIDs) {
        activeUser.SetLikedRecommendations(likedRecommendationsIDs);
        DataManager.GetInstance().StoreUsersInfo();
    }

    /** @brief Getter de likedRecommendations
     *
     * \pre <em>Cierto</em>
     * \post retorna las likedRecommendations del usuario activo
     */
    public ArrayList<Item> GetLikedRecommendations() {
        return activeUser.GetLikedRecommendations();
    }

    /** @brief Añade un item a la lista de favoritos del usuario activo
     *
     * \pre <em>Cierto</em>
     * @param itemID es el itemID a añadir a la lista de favoritos
     * \post Añade un item a la lista de favoritos del usuario activo
     */
    public void AddFavourite(int itemID) {
        activeUser.AddFavourite(itemID);
        DataManager.GetInstance().StoreUsersInfo();
    }

    /** @brief Borra un item de la lista de favoritos del usuario activo
     *
     * \pre el item existe en la lista de favoritos
     * @param itemID es el itemID a borrar de la lista de favoritos
     * \post Borra un item a la lista de favoritos del usuario activo
     */
    public void RemoveFavourite(int itemID){
        activeUser.RemoveFavourite(itemID);
        DataManager.GetInstance().StoreUsersInfo();
    }

    /** @brief Setter de favourites del usuario activo
     *
     * \pre \a favouriteIDs valido
     * @param favouriteIDs representa el nuevo vector favourites (ver en definición del atributo en UserActual.java) del usuario activo
     * \post se actualizan las favouritesIDs del usuario activo
     */
    public void SetFavourites(ArrayList<Integer> favouriteIDs) {
        activeUser.SetFavourites(favouriteIDs);
        DataManager.GetInstance().StoreUsersInfo();
    }

    /** @brief Getter de favourites del usuario activo
     *
     * \pre <em>Cierto</em>
     * \post retorna los favourites items del usuario activo
     */
    public ArrayList<Item> GetFavourites() {
        return activeUser.GetFavourites();
    }

    /** @brief Setter del atributo hasStarted de UserActual
     *
     * \pre hasStarted booleano valido
     * @param hasStarted indica si el usuario ya ha añadido valoraciones de items al sistema o no
     * \post se actualiza el valor de hasStarted del active user.
     */
    public void SetHasStarted(boolean hasStarted) {
        activeUser.SetStarted(hasStarted);
        DataManager.GetInstance().StoreUsersInfo();
    }

    /** @brief Para modificar la nota de un cierto item del active user
     *
     * \pre el active user ha valorado ese itemID
     * @param itemID es el itemID en cuestion
     * @param newRating es la nueva nota a ese item
     * \post se modifica la nota que el usuario activo le ha dado a ese itemID
     */
    public void ModifyItemRating(int itemID, float newRating) {
        activeUser.ModifyItemRating(itemID, newRating);
        Rating newR = new Rating(activeUser.GetUserID(), itemID, newRating);
        DataManager.GetInstance().ModifyItemRating(newR); //newR ya contiene el user y el item para el que se está dando el nuevo rating
    }

    /** @brief Para eliminar una valoracion del usuario activo
     *
     * \pre el active user ha valorado ese itemID
     * @param itemID es el itemID en cuestion
     * \post se elimina ese item de la lista de peliculas valoradas por el usuario activo
     */
    public void DeleteRatedItem(int itemID) {
        for(Rating r: activeUser.GetRatingsList()){
            if(r.GetItemID() == itemID){
                activeUser.RemoveFromRatingsList(r);
                break;
            }
        }
        DataManager.GetInstance().DeleteRatedItem(activeUser.GetUserID(), itemID);
    }



}