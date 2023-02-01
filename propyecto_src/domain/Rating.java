/** @file Rating.java
 *  @brief Contiene la clase Rating
 */
package fxsrc.propyecto.domain;

/** \class Rating
 *   \brief Clase que contiene los datos de un Rating
 *
 *   Ofrece Setters y Getters de los atributos de Rating
 *   */
public class Rating {

    /** \brief \a userID Corresponde a la ID del User que ha realizado la valoración
     */
    private int userID;
    /** \brief \a itemID Corresponde a la ID del Item que ha siado valorado
     */
    private int itemID;

    /** \brief \a rating Corresponde al valor de la valorción
     */
    private float rating;

    /** \brief Constructor por defecto de Rating
     *
     * \pre <em>Cierto</em>
     * \post Crea una instancia de Rating
     */
    public Rating(){}

    /** \brief Constructor por defecto de Rating
     *
     * \param int userID Corresponde a la ID del User que ha valorado la valoración
     * \param int itemID Corresponde a la ID del Item que ha sido valorado
     * \param float rating Corresponde al valor de la valoración
     * \pre <em>Cierto</em>
     * \post Crea una instancia de Rating modificando los respectivos valores pasados por parámetro
     */
    public Rating(int userID, int itemID, float rating) {
        this.userID = userID;
        this.itemID = itemID;
        this.rating = rating;
    }

    /** \brief Devuelve el ID del User que ha realizado la valoración
     *
     * \pre <em>Cierto</em>
     * \post Devuelve el ID del User que ha realizado la valoración
     */
    public int GetUserID() {
        return userID;
    }


    /** \brief Asigna el nuevo valor del ID del User valorador
     *
     * \param int userID corresponde al nuevo valor del ID del User
     * \pre <em>Cierto</em>
     * \post Asigna el nuevo valor de la ID del User valorador
     */
    public void SetUserID(int userID) {
        this.userID = userID;
    }


    /** \brief Devuelve el ID del Item valorado
     *
     * \pre <em>Cierto</em>
     * \post Devuelve el ID del Item valorado
     */
    public int GetItemID() {
        return itemID;
    }


    /** \brief Asigna el nuevo valor del ID del Item valorado
     *
     * \param int itemID corresponde al nuevo valor del ID del Item
     * \pre <em>Cierto</em>
     * \post Asigna el nuevo valor de la ID del Item valorado
     */
    public void SetItemID(int itemID) {
        this.itemID = itemID;
    }


    /** \brief Devuelve el valor de la valoración
     *
     * \pre <em>Cierto</em>
     * \post Devuelve el valor de la valoración
     */
    public float GetRating() {
        return rating;
    }

    /** \brief Asigna el nuevo valor de la valoración
     *
     * \param float rating Corresponde al nuevo valor de la valoración
     * \pre <em>Cierto</em>
     * \post Asigna el nuevo valor de la valoración
     */
    public void SetRating(float rating) {
        this.rating = rating;
    }

    /** \brief Compara dos Rating
     *
     * \param Rating emp Corresponde al Rating a comparar
     * \pre \a emp no es null
     * \post Devuelve true si son el mismo Rating y false en caso contrario
     */
    public boolean Equals(Rating emp) {
        return (emp != null && this.itemID == emp.itemID && this.userID == emp.userID && this.rating == emp.rating);
    }
}