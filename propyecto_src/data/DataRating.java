/** @file DataRating.java
 *  @brief Contiene la clase DataRating
 */

package fxsrc.propyecto.data;

import fxsrc.propyecto.domain.*;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

/** \class DataRating
 *   \brief Clase que contiene los datos de todos los Rating del sistema.
 *
 *   Ofrece operaciones para que el DataManager pueda añadir Rating nuevos a partir de un String que sería la representación del Rating a añadir en formato CSV.
 *   También ofrece operaciones para que el DataManager pida todos los Rating, un Rating en concreto a partir de la posición del Array (útil para bucles) o incluso un Rating a partir de sus IDs.
 */
public class DataRating extends Data{

    /** \brief rating maximo (5, 10, 100) sobre el que se están puntuando los items
     */
    static float maxRating;

    /** \brief Son los nombres de las columnas leidas en CSV
     */
    private String[] atNames;

    /** \brief \a ratings es un array de Rating donde se guardan todos los Rating del sistema.
     */
    private ArrayList<Rating> ratings;
    /** \brief HashMap de los Rating del sistema (para buscar un valor concreto)
     */
    private HashMap<Pair<Integer, Integer>, Float> ratingsMap;

    /** \brief HashMap de los Rating del sistema agrupados por usuario (para buscar todos los rating de un User)
     */
    private HashMap<Integer, ArrayList<Rating>> allRatingsUser;

    /** \brief HashMap de los Rating del sistema agrupados por item (para buscar todos los rating de un Item)
     */
    private HashMap<Integer, ArrayList<Rating>> allRatingsItem;

    /** \brief Constructor por defecto de DataRating
     *
     * \pre <em>Cierto</em>
     * \post Se crea una instancia de DataRating y se llama a la constructora por defecto de Data
     */
    public DataRating() {
        super();
    }

    /** \brief Llamada a la carga de CSV de la clase data
     *
     * \param String path Es el path del csv
     * \pre <em>Cierto</em>
     * \post Se llama a la carga de CSV de la clase data  y se le da valor a maxRating
     */
    @Override
    public void LoadCSV(String path) {
        maxRating = 5;
        super.LoadCSV(path);
    }

    /** \brief Función para inicializar \a ratings
     *
     * \pre <em>Cierto</em>
     * \post Se incializa el array \a ratings a un array vacío,
     */
    public void Init() {
        ratings = new ArrayList<Rating>();
        ratingsMap = new HashMap<>();
        allRatingsUser = new HashMap<>();
        allRatingsItem = new HashMap<>();
    }


    /** \brief Función para inicializar el \a ratings.
     *
     * \pre \a ratings no es null.
     * \post Se resetea el array \a ratings.
     */
    public void ClearData() {
        ratings.clear();
        ratingsMap.clear();
        allRatingsItem.clear();
        allRatingsUser.clear();
    }

    /** \brief Parsea una linea CSV a un Rating y lo añade a \a ratings
     * \param String[] attributesNames Corresponde al nombre de los atributos
     * \param String lineRead Corresponde a la linea CSV que hace referencia al Rating deseado
     * \pre \a attributesNames tiene el tamaño exacto de parametros que tenga el Rating y \a lineRead no es vacío.
     * \post Se parsean los atributos de Rating y finalmente se añade a \a ratings.
     */
    public void ParseAndAddData(String attributesNames[], String lineRead) {
        atNames = attributesNames;
        Rating tempRating = ParserCSV.GetInstance().ParseRating(attributesNames, lineRead);
        ratings.add(tempRating);
        if(tempRating.GetRating() > maxRating) {
            if(tempRating.GetRating() > 10) {
                maxRating = 100;
            }
            else if(tempRating.GetRating() > 5) {
                maxRating = 10;
            }
        }

        //añadimos el rating al ratingMap principal
        ratingsMap.put(new Pair(tempRating.GetUserID(), tempRating.GetItemID()), tempRating.GetRating());

        //añadimos el rating a los ratings de cada User
        if(allRatingsUser.containsKey(tempRating.GetUserID())) {
            allRatingsUser.get(tempRating.GetUserID()).add(tempRating);
        }
        else {
            ArrayList<Rating> tempList = new ArrayList<>();
            tempList.add(tempRating);
            allRatingsUser.put(tempRating.GetUserID(), tempList);
        }

        //añadimos el rating a los ratings de cada Item
        if(allRatingsItem.containsKey(tempRating.GetItemID())) {
            allRatingsItem.get(tempRating.GetItemID()).add(tempRating);
        }
        else {
            ArrayList<Rating> tempList = new ArrayList<>();
            tempList.add(tempRating);
            allRatingsItem.put(tempRating.GetItemID(), tempList);
        }
    }

    /** \brief Devuelve todos los Rating dentro de \a ratings
     *
     * \pre <em>Cierto</em>
     * \post Devuelve todos los Rating dentro de \a ratigns.
     */
    public ArrayList<Rating> GetAllRatings() {
        return ratings;
    }

    /** \brief Devuelve todos los Rating agrupados por usuario
     *
     * \pre <em>Cierto</em>
     * \post Devuelve todos los Rating agrupados por usuario
     */
    public HashMap<Integer, ArrayList<Rating>> GetAllRatingsUsersMap() {
        return allRatingsUser;
    }

    /** \brief Devuelve todos los Ratings
     *
     * \pre <em>Cierto</em>
     * \post Devuelve todos los Ratings
     */
    public HashMap<Pair<Integer, Integer>, Float> GetAllRatingsMap() {
        return ratingsMap;
    }

    /** \brief Devuelve el valor de Rating con id de usuario \a userID y id de item \itemID
     *
     * @throws RatingDoesNotExistException
     *
     * \param int userID Corresponde a la ID del usuario del rating deseado
     * \param int itemID Corresponde a la ID del item del rating deseado
     * \pre <em>Cierto</em>
     * \post Devuelve el valor Rating con id de usuario \a userID y id de item \itemID
     */
    public float GetRatingValueByID(int userID, int itemID) throws RatingDoesNotExistException {
        Float temp = ratingsMap.get(new Pair<>(userID, itemID));
        if(temp == null) {
            throw new RatingDoesNotExistException();
        }
        else {
            return temp;
        }
    }

    /** \brief Devuelve todos los Rating guardados en \a ratings
     *
     * \pre <em>Cierto</em>
     * \post Devuelve todos los Rating guardados en \a ratings
     */
    public ArrayList<Integer> GetAllUsersID() {
        ArrayList<Integer> usersID = new ArrayList<Integer>();
        for (Rating rating : ratings) {
            boolean found = false;
            for(int i = 0; i < usersID.size(); ++i) {
                if(rating.GetUserID() == usersID.get(i)) {
                    found = true;
                    break;
                }
            }
            if(!found) {
                usersID.add(rating.GetUserID());
            }
        }
        return usersID;
    }

    /** \brief Devuelve el valor de todos los Rating que el usuario con id \a userID ha realizado
     *
     * @throws RatingDoesNotExistException
     *
     * \param int userID Corresponde a la ID del usuario deseado
     * \pre <em>Cierto</em>
     * \post Devuelve el valor de todos los Rating que el usuario con id \a userID ha realizado
     */
    public ArrayList<Rating> GetUserRatings(int userID) throws NoExistingUserIDException{

        ArrayList<Rating> temp = allRatingsUser.get(userID);

        if(temp == null || temp.isEmpty()) {
            throw new NoExistingUserIDException();
        }
        return temp;
    }

    /** \brief Devuelve el valor de todos los Rating que tiene el item con id \a itemID
     *
     * @throws ItemDoesNotExistException
     *
     * \param int itemID Corresponde a la ID del item deseado
     * \pre <em>Cierto</em>
     * \post Devuelve el valor de todos los Rating que tiene el item con id \a itemID
     */
    public ArrayList<Rating> GetItemRatings(int itemID) throws ItemDoesNotExistException{
        ArrayList<Rating> temp = allRatingsUser.get(itemID);

        if(temp.isEmpty()) {
            throw new ItemDoesNotExistException();
        }
        return temp;
    }

    /** \brief Devuelve el valor sobre que están los ratings puntuados (5,10,100)
     *
     *
     * \pre <em>Cierto</em>
     * \post Devuelve el valor sobre que están los ratings puntuados (5,10,100)
     */
    public static float GetMaxRating() {
        return maxRating;
    }

    /** \brief Devuelve el valor sobre que están los ratings puntuados (5,10,100)
     *
     *
     * \pre <em>Cierto</em>
     * \post Devuelve el valor sobre que están los ratings puntuados (5,10,100)
     */
    protected boolean HasNextLine() {
        return currentLine < ratings.size();
    }

    /** \brief Funcion que devuelve la siguiente línea a guardar
     * \pre Cierto
     * \post Devuelve la siguiente línea a guardar
     */
    protected String GetNextLine() {
        String temp = ParserCSV.GetInstance().ParseRatingToCSV(atNames, ratings.get(currentLine));
        currentLine++;
        return temp;
    }

    /** \brief Funcion que añade un Rating a las diferentes listas
     * \param Rating tempRating corresponde al Rating a añadir
     * \pre ratings, ratingsMap, allRatingsItem y allRatingsUser no son null
     * \post Añade \a tempRating a todas las listas de almacenamiento
     */
    public void AddRating(Rating tempRating) {
        ratings.add(tempRating);
        if(tempRating.GetRating() > maxRating) {
            if(tempRating.GetRating() > 10) {
                maxRating = 100;
            }
            else if(tempRating.GetRating() > 5) {
                maxRating = 10;
            }
        }

        //añadimos el rating al ratingMap principal
        ratingsMap.put(new Pair(tempRating.GetUserID(), tempRating.GetItemID()), tempRating.GetRating());

        //añadimos el rating a los ratings de cada User
        if(allRatingsUser.containsKey(tempRating.GetUserID())) {
            allRatingsUser.get(tempRating.GetUserID()).add(tempRating);
        }
        else {
            ArrayList<Rating> tempList = new ArrayList<>();
            tempList.add(tempRating);
            allRatingsUser.put(tempRating.GetUserID(), tempList);
        }

        //añadimos el rating a los ratings de cada Item
        if(allRatingsItem.containsKey(tempRating.GetItemID())) {
            allRatingsItem.get(tempRating.GetItemID()).add(tempRating);
        }
        else {
            ArrayList<Rating> tempList = new ArrayList<>();
            tempList.add(tempRating);
            allRatingsItem.put(tempRating.GetItemID(), tempList);
        }
        StoreCSV();
    }

    /** \brief Funcion que modifica un Rating en las diferentes listas
     * \param Rating tempRating corresponde al Rating a modificar
     * \pre ratings, ratingsMap, allRatingsItem y allRatingsUser no son null
     * \post Modifica \a tempRating en todas las listas de almacenamiento
     */
    public void ModifyItemRating(Rating r) {
        //ratingsMap.get(new Pair(r.GetUserID(), r.GetItemID())) = r.GetRating();
        DeleteRating(r.GetUserID(), r.GetItemID());
        AddRating(r);
    }

    /** \brief Funcion que elimina un Rating de las diferentes listas
     * \param int userID Corresponde al ID del usuario del Rating a eliminar
     * \param int itemID Corresponde al ID del item del Rating a eliminar
     * \pre ratings, ratingsMap, allRatingsItem y allRatingsUser no son null
     * \pre \a userID corresponde a la ID de un usuario existente
     * \pre \a itemID corresponde a la ID de un Item existente
     * \post Elimina \a tempRating de todas las listas de almacenamiento
     */
    public void DeleteRating(int userID, int itemID) {
        ratingsMap.remove(new Pair(userID, itemID));

        for(int i = 0; i < ratings.size(); ++i) {
            if(ratings.get(i).GetUserID() == userID && ratings.get(i).GetItemID() == itemID) {
                ratings.remove(i);
                break;
            }
        }

        for(int i = 0; i < allRatingsItem.get(itemID).size(); ++i) {
            if(allRatingsItem.get(itemID).get(i).GetUserID() == userID && allRatingsItem.get(itemID).get(i).GetItemID() == itemID) {
                allRatingsItem.get(itemID).remove(i);
                break;
            }
        }

        for(int i = 0; i < allRatingsUser.get(userID).size(); ++i) {
            if(allRatingsUser.get(userID).get(i).GetUserID() == userID && allRatingsUser.get(userID).get(i).GetItemID() == itemID) {
                allRatingsUser.get(userID).remove(i);
                break;
            }
        }

        StoreCSV();
    }

    /** \brief Funcion que elimina todos los Rating de un usuario en las diferentes listas
     * \param int userID Corresponde al ID del usuario que queremos eliminar todos sus Rating
     * \pre ratings, ratingsMap, allRatingsItem y allRatingsUser no son null
     * \pre \a userID corresponde a la ID de un usuario existente
     * \post Elimina \a tempRating de todas las listas de almacenamiento
     */
    public void DeleteRatingsOfUser(int userID){

        try {
            ArrayList<Rating> temp = GetUserRatings(userID);
            for(int i = 0; i < temp.size(); ++i) {
                DeleteRating(userID, temp.get(i).GetItemID());
            }
        } catch (NoExistingUserIDException e) {
            e.printStackTrace();
        }

    }
}
