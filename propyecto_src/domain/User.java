/** @file User.java
 *  @brief Contiene la clase User
 */

package fxsrc.propyecto.domain;

import java.util.ArrayList;

/** @class User
 *   @brief Clase encargada de representar a un cierto usuario, a través de su ID y sus gustos
 *
 *   Ofrece getters y setters de sus atributos
 */
public class User {

    /** @brief \a userID representa de forma única a un usuario.
     */
    private int userID;

    /** @brief \a ratingsList contiene toda la lista de valoraciones de ese usuario a items del sistema que haya llegado a valorar.
     */
    private ArrayList<Rating> ratingsList;

    /** @brief Constructora de User por defecto
     *
     * \pre <em>Cierto</em>
     * \post Se crea una instancia de User vacía
     */
    public User() {
        ratingsList = new ArrayList<Rating>();
    }

    /** @brief Constructora de User a través de su ID.
     *
     * \pre \a userID es válido (positivo y representa a un user existente)
     * @param userID es el ID del usuario que queremos instanciar
     * \post Se crea una instancia de User con ese \a userID y sin valoraciones hechas
     */
    public User(int userID) {
        this.userID = userID;
        ratingsList = new ArrayList<Rating>();
    }

    /** @brief Constructora de User a través de su ID y sus valoraciones.
     *
     * \pre \a userID es válido (positivo y representa a un user existente) y \a ratingsList son sus valoraciones reales
     * @param userID es el ID del usuario que queremos instanciar
     * @param ratingsList son las valoraciones del usuario que queremos instanciar
     * \post Se crea una instancia de User con ese \a userID y sus valoraciones
     */
    public User(int userID, ArrayList<Rating> ratingsList) {
        this.userID = userID;
        this.ratingsList = ratingsList;
    }

    /** @brief Getter del UserID de un Usuario.
     *
     * \pre <em>Cierto</em>
     * \post retorna el UserID del objeto implícito
     */
    public int GetUserID() {
        return userID;
    }

    /** @brief Setter del UserID de un Usuario.
     *
     * \pre \a userID es válido y representa de forma única al objeto implícito y es existente en el sistema
     * @param userID es el nuevo ID que le queremos dar a una instancia del usuario
     * \post se cambia el \a userID del objeto implícito por el nuevo userID
     */
    public void SetUserID(int userID) {
        this.userID = userID;
    }

    /** @brief Getter de la lista de valoraciones de un Usuario.
     *
     * \pre <em>Cierto</em>
     * \post retorna la lista de valoraciones del objeto implícito
     */
    public ArrayList<Rating> GetRatingsList() {
        return ratingsList;
    }

    /** @brief Setter de la lista de valoraciones de un Usuario.
     *
     * \pre \a la lista de valoraciones es correcta y representa las valoraciones del objeto implícito
     * @param ratingsList es la nueva lista de valoraciones que le queremos dar a una instancia del usuario
     * \post se cambia el \a ratingsList del objeto implícito por el nuevo ratingsList
     */
    public void SetRatingsList(ArrayList<Rating> ratingsList) {
        this.ratingsList = ratingsList;
    }

    /** Añade un nuevo rating al vector ratingsList
     *
     * \pre \a r es un rating válido
     * @param r es el rating a añadir
     * \post \a r es añadido a la ratinggList del user
     */
    public void AddToRatingsList(Rating r) {
        this.ratingsList.add(r);
    }

    /** Elimina un rating al vector ratingsList, si existe
     *
     * \pre \a r es un rating válido
     * @param r es el rating a añadir
     * \post \a r es borrado a la ratingList del user. Si no existe, no hace nada.
     */
    public void RemoveFromRatingsList(Rating r) {
        this.ratingsList.remove(r);
    }

    /** @brief Para modificar la nota de un cierto item de la ratingsList del user
     *
     * \pre el item existe en la ratings list
     * @param itemID es el itemID en cuestion
     * @param newRating es la nueva nota a ese item
     * \post se modifica la nota que el usuario le ha dado a ese itemID
     */
    public void ModifyItemRating(int itemID, float newRating) {
        for(Rating r : ratingsList){
            if(r.GetItemID() == itemID) r.SetRating(newRating);
        }
    }

}
