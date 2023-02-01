/** @file UserActual.java
 *  @brief Contiene la clase UserActual
 */

package fxsrc.propyecto.domain;

import fxsrc.propyecto.data.Data;

import java.util.ArrayList;

/** @class UserActual
 *   @brief Clase encargada de representar al Active User del sistema. Hereda los atributos de User y se le añade su nombre de usuario y contraseña.
 *
 *   Solo hay 1 usuario activo simultáneamente, como máximo. Ofrece getters y setters de sus atributos
 */
public class UserActual extends User{

    /** @brief \a username es el nombre de usuario de acceso al sistema del usuario activo. Igual que el userID, es único.
     */
    private String username;

    /** @brief \a email es el correo electrónico del usuario activo. Igual que el userID, es único.
     */
    private String email;

    /** @brief \a password es la contraseña que el usuario activo utiliza para acceder al sistema.
     */
    private String password;

    /** @brief \a security es la respuesta a una pregunta de seguridad para recuperar la contraseña
     */
    private String security;

    /** @brief \a hasStarted indica si el usuario ya ha añadido valoraciones de items al sistema o no
     */
    private boolean hasStarted;

    /** @brief \a favouriteIDs representa los ID de aquellos items que el user haya decidido guardarse en favorito (puede guardar en favorito cualquier item que exista en el sistema, lo haya valorado o no)
     */
    private ArrayList<Integer> favouriteIDs;

    /** @brief \a likedRecommendedIDs representa los ID de aquellas recomendaciones que, cuando se le hayan dado al user, el user haya decidido almacenarlas para consultarlas de nuevo en otro momento
     */
    private ArrayList<Integer> likedRecommendedIDs;


    /** @brief Constructora de UserActual por defecto
     *
     * \pre <em>Cierto</em>
     * \post Se crea una instancia de UserActual vacía
     */
    public UserActual() {
        super();
        favouriteIDs = new ArrayList<>();
        likedRecommendedIDs = new ArrayList<>();
    }

    /** @brief Constructora de UserActual a través de su ID, su nombre de usuario, su email, el rememberMe y la contraseña.
     *
     * Es la constructora a utilizar cuando el usuario es nuevo (Es decir, acaba de hacer SignUp)
     *
     * \pre \a userID es válido (positivo y representa a un user existente) y \a username y \a password también y \a ratingsList también
     * @param userID es el ID del usuario que queremos instanciar
     * @param username es el nombre de usuario del usuario que queremos instanciar
     * @param email es el correo electronico del usuario que queremos instanciar
     * @param password es la contraseña del usuario que queremos instanciar
     * @param security es la respuesta a una pregunta de seguridad para recuperar la contraseña
     * \post Se crea una instancia de UserActual con todos sus atributos
     */
    public UserActual(int userID, String username, String email, String password, String security) {
        super(userID); //ya pone ratingslist vacía
        this.username = username;
        this.password = password;
        this.email = email;
        this.security = security;
        this.hasStarted = false;
        favouriteIDs = new ArrayList<>();
        likedRecommendedIDs = new ArrayList<>();
    }

    /** @brief Constructora de UserActual a través de su ID, su nombre de usuario, su email, el rememberMe y la contraseña.
     *
     * Es la constructora a utilizar cuando el usuario hace SignIn
     *
     * \pre \a userID es válido (positivo y representa a un user existente) y \a username y \a password también y \a ratingsList también
     * @param userID es el ID del usuario que queremos instanciar
     * @param username es el nombre de usuario del usuario que queremos instanciar
     * @param email es el correo electronico del usuario que queremos instanciar
     * @param hasStarted hasStarted indica si el usuario ya ha añadido valoraciones de items al sistema o no
     * @param password es la contraseña del usuario que queremos instanciar
     * @param security es la respuesta a una pregunta de seguridad para recuperar la contraseña
     * @param ratingsList contiene toda la lista de valoraciones de ese usuario a items del sistema que haya llegado a valorar
     * @param favouriteIDs representa los ID de aquellos items que el user haya decidido guardarse en favorito (puede guardar en favorito cualquier item que exista en el sistema, lo haya valorado o no)
     * @param likedRecommendedIDs representa los ID de aquellas recomendaciones que, cuando se le hayan dado al user, el user haya decidido almacenarlas para consultarlas de nuevo en otro momento
     * \post Se crea una instancia de UserActual con todos sus atributos
     */
    public UserActual(int userID, String username, String email, boolean hasStarted, String password, String security, ArrayList<Rating> ratingsList, ArrayList<Integer> favouriteIDs, ArrayList<Integer> likedRecommendedIDs) {
        super(userID, ratingsList);
        this.username = username;
        this.password = password;
        this.email = email;
        this.security = security;
        this.favouriteIDs = favouriteIDs;
        this.likedRecommendedIDs = likedRecommendedIDs;
        this.hasStarted = hasStarted;
    }

    /** @brief Getter del nombre de usuario de un UserActual.
     *
     * \pre <em>Cierto</em>
     * \post retorna el nombre de usuario del objeto implícito
     */
    public String GetUsername() {
        return username;
    }

    /** @brief Setter del nombre de usuario de un UserActual.
     *
     * \pre El nombre de usuario que se quiere poner no pertenece a ningún otro usuario y ya se ha producido el cambio de nombre en el sistema
     * @param username es el nuevo nombre de usuario que le queremos dar al usuario
     * \post Se actualiza el nombre de usuario del UserActual
     */
    public void SetUsername(String username) {
        this.username = username;
    }

    /** @brief Getter de la contraseña de un UserActual.
     *
     * \pre <em>Cierto</em>
     * \post retorna el nombre de usuario del objeto implícito
     */
    public String GetPassword() {
        return password;
    }

    /** @brief Setter de la contraseña de un UserActual.
     *
     * \pre La contraseña es válida y ya se ha producido el cambio en el sistema de la contraseña de este usuario
     * @param password es la nueva contraseña que le queremos asignar al usuario
     * \post Se actualiza la contraseña de UserActual
     */
    public void SetPassword(String password) {
        this.password = password;
    }

    /** @brief Getter del correo electronico del UserActual
     *
     * \pre <em>Cierto</em>
     * \post Retorna el correo electronico del UserActual
     */
    public String GetEmail() {
        return email;
    }

    /** @brief Setter del correo electronico del UserActual
     *
     * \pre email valido
     * @param email es el correo electronico que le queremos asignar al UserActual
     * \post se asigna \a email como el correo electronico del userActual
     */
    public void SetEmail(String email) {
        this.email = email;
    }

    /** @brief Setter de security
     *
     * \pre security valido
     * @param security es la respuesta a una pregunta de seguridad para recuperar la contraseña
     * \post se actualiza security
     */
    public void SetSecurity(String security) {
        this.security = security;
    }

    /** @brief Getter de security del userActual
     *
     * \pre <em>Cierto</em>
     * \post retorna security
     */
    public String GetSecurity() {return security;}

    /** @brief Getter de hasStarted del user actual.
     *
     * \pre <em>Cierto</em>
     * \post retorna hasStarted
     */
    public boolean GetStarted() {
        return hasStarted;
    }

    /** @brief Setter de hasStarted
     *
     * \pre hasStarted booleano valido
     * @param hasStarted indica si el usuario ya ha añadido valoraciones de items al sistema o no
     * \post se actualiza el valor de hasStarted
     */
    public void SetStarted(boolean hasStarted) {
        this.hasStarted = hasStarted;
    }

    /** @brief Sirve para poder añadir un nuevo itemID que ha recomendado el sistema al usuario activo a la lista de recomendaciones que el usuario se ha guardado para poder consultar más tarde
     *
     * \pre itemID es el ID de un item que el sistema le ha recomendado al usuario activo
     * @param itemID es un item que se le ha recomendado al usuario y quiere añadirlo en la lista de items que el sistema le ha recomendado y quiere guardar para poder consultar más tarde
     * \post se añade itemID a la lista de likedRecommendedIDs
     */
    public void AddLikedRecommendation(int itemID) {
        likedRecommendedIDs.add(itemID);
    }

    /** @brief Elimina un item de la lista de aquellos items de los que le había recomendado el sistema que se había guardado el usuario
     *
     * \pre itemID se encuentra en likedRecommendedIDs
     * @param itemID es el itemID en cuestion
     * \post se elimina el itemID de likedRecommendedIDs
     */
    public void RemoveLikedRecommendation(Integer itemID) {
        likedRecommendedIDs.remove(itemID);
    }

    /** @brief Setter de likedRecommendations
     *
     * \pre likedRecommendationsIDs valido
     * @param likedRecommendationsIDs representa el nuevo vector "likedRecommendations" (ver en definición del atributo qué es) del usuario activo
     * \post se actualizan las likedRecommendationsIDs del usuario activo
     */
    public void SetLikedRecommendations(ArrayList<Integer> likedRecommendationsIDs) {
        this.likedRecommendedIDs = likedRecommendationsIDs;
    }

    /** @brief Getter de likedRecommendations
     *
     * \pre <em>Cierto</em>
     * \post retorna las likedRecommendations del usuario activo
     */
    public ArrayList<Item> GetLikedRecommendations() {
        ArrayList<Item> temp = new ArrayList<>();
        for(int i = 0; i < likedRecommendedIDs.size(); ++i) {
            temp.add(DataManager.GetInstance().GetItemByID(likedRecommendedIDs.get(i)));
        }
        return temp;
    }

    /** @brief Añade un item a la lista de favoritos del usuario activo
     *
     * \pre <em>Cierto</em>
     * @param itemID es el itemID a añadir a la lista de favoritos
     * \post Añade un item a la lista de favoritos del usuario activo
     */
    public void AddFavourite(Integer itemID) {
        favouriteIDs.add(itemID);
    }

    /** @brief Borra un item de la lista de favoritos del usuario activo
     *
     * \pre El item existe en su lista de favoritos
     * @param itemID es el itemID a borrar de la lista de favoritos
     * \post Borra un item de la lista de favoritos del usuario activo
     */
    public void RemoveFavourite(Integer itemID) {
        favouriteIDs.remove(itemID);
    }

    /** @brief Setter de favourites
     *
     * \pre \a favouriteIDs valido
     * @param favouriteIDs representa el nuevo vector favourites (ver en definición del atributo qué es) del usuario activo
     * \post se actualizan las favouritesIDs del usuario activo
     */
    public void SetFavourites(ArrayList<Integer> favouriteIDs) {
        this.favouriteIDs = favouriteIDs;
    }

    /** @brief Getter de favourites
     *
     * \pre <em>Cierto</em>
     * \post retorna los favourites items del usuario activo
     */
    public ArrayList<Item> GetFavourites() {
        ArrayList<Item> temp = new ArrayList<>();
        for(int i = 0; i < favouriteIDs.size(); ++i) {
            temp.add(DataManager.GetInstance().GetItemByID(favouriteIDs.get(i)));
        }
        return temp;
    }

}