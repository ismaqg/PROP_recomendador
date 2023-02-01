/** @file DataItem.java
 *  @brief Contiene la clase DataItem
 */

package fxsrc.propyecto.data;
import fxsrc.propyecto.domain.ExistingActiveUserException;
import fxsrc.propyecto.domain.Item;
import fxsrc.propyecto.domain.ItemDoesNotExistException;
import fxsrc.propyecto.domain.ParserCSV;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/** \class DataItem
 *   \brief Clase que contiene los datos de todos los Item del sistema.
 *
 *   Ofrece operaciones para que el DataManager pueda añadir Items nuevos a partir de un String que sería la representación del Item a añadir en formato CSV.
 *   También ofrece operaciones para que el DataManager pida todos los Items, un Item en concreto a partir de la posición del Array (útil para bucles) o incluso un Item a partir de su ID.
 */
public class DataItem extends Data{

    /** \brief \a items es un array de Item donde se guardan todos los Item del sistema.
     */
    private ArrayList<Item> items;
    /** \brief HashMap de los items con su id como key
     */
    private HashMap<Integer, Item> itemsMap;
    /** \brief Lista de los nombres de todos los items
     */
    private ArrayList<String> itemsNames;

    /** \brief HashMap de los items con su id como key
     */
    private HashMap<String, Item> itemsNameMap;

    /** \brief Lista de los items mejor valorados del sistema
     */
    private ArrayList<Item> topRated;

    /** \brief Constructor por defecto de DataItem
     *
     * \pre <em>Cierto</em>
     * \post Crea una instanncia de DataItem y llama a la constructora por defecto de Data
     */
    public DataItem () {
        super();
    }

    /** \brief Función para inicializar \a items
     *
     * \pre <em>Cierto</em>
     * \post Se incializa el array \a items a un array vacío,
     */
    public void Init() {
        items = new ArrayList<Item>();
        itemsMap = new HashMap<>();
        itemsNames = new ArrayList<>();
        itemsNameMap = new HashMap<>();
        topRated = new ArrayList<>();
    }

    /** \brief Función para inicializar el \a items.
     *
     * \pre \a items no es null.
     * \pre \a itemsMap no es null.
     * \pre \a itemsNames no es null.
     * \pre \a itemsNameMap no es null.
     * \post Se resetea el array \a items.
     */
    public void ClearData() {
        items.clear();
        itemsMap.clear();
        itemsNames.clear();
        itemsNameMap.clear();
        topRated.clear();
    }

    /** \brief Parsea una linea CSV a un Item y lo añade a \a items
     * \param String[] attributesNames Corresponde al nombre de los atributos
     * \param String lineRead Corresponde a la linea CSV que hace referencia al Item deseado
     * \pre \a attributesNames tiene el tamaño exacto de parametros que tenga el Item y \a lineRead no es vacío.
     * \post Se parsean los atributos de Item y finalmente se añade a \a items.
     */
    public void ParseAndAddData(String attributesNames[], String lineRead) {
        Item temp = ParserCSV.GetInstance().ParseItem(attributesNames, lineRead);
        items.add(temp);
        itemsMap.put(temp.GetItemId(), temp);
        itemsNames.add(temp.GetName());
        itemsNameMap.put(temp.GetName(), temp);
    }

    /** \brief Debuelve todos los Item dentro de \a items
     *
     * \pre <em>Cierto</em>
     * \post Devuelve todos los Item dentro de \a items.
     */
    public ArrayList<Item> GetAllItems() {
        return items;
    }


    /** \brief Devuelve el Item en la posición \a pos
     *
     * @throws ItemDoesNotExistException
     * \param int pos Corresponde a la posición a la que se quiere acceder del array \a items
     * \pre <em>Cierto</em>
     * \post Devuelve el Item en posición \a pos
     */
    public Item GetItemByPosition(int pos) {
        try {
            return items.get(pos);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
            return null;
        }
    }

    public Item GetItemByNameLC(String s) {
        for(int i = 0; i < items.size(); ++i) {
            if(items.get(i).GetName().toLowerCase().equals(s.toLowerCase())) {
                return items.get(i);
            }
        }
        return null;
    }

    /** \brief Devuelve el Item con ID \a id
     *
     * @throws ItemDoesNotExistException
     *
     * \param int id Corresponde a la ID del Item deseado
     * \pre <em>Cierto</em>
     * \post Devuelve el Item con ID = \a id
     */
    public Item GetItemByID(int id) throws ItemDoesNotExistException {
        Item temp = itemsMap.get(id);
        if(temp == null) {
            throw new ItemDoesNotExistException();
        }
        else {
            return temp;
        }
    }

    /** \brief Devuelve el Item con nombre \a name
     *
     * @throws ItemDoesNotExistException
     *
     * \param String name Corresponde al nombre de un Item
     * \pre <em>Cierto</em>
     * \post Devuelve el Item con nombre = name
     */
    public Item GetItemByName(String name) throws ItemDoesNotExistException {
        Item temp = itemsNameMap.get(name);
        if(temp == null) {
            throw  new ItemDoesNotExistException();
        }
        else {
            return temp;
        }
    }

    /** \brief Funcion que devuelve si hay una línea más por guardar
     * \pre \a items no es null
     * \post Devuelve si hay alguna línea más por guardar
     */
    protected boolean HasNextLine() {
        return currentLine < items.size();
    }

    /** \brief Funcion que devuelve la siguiente línea a guardar
     * \pre Cierto
     * \post Devuelve la siguiente línea a guardar
     */
    protected String GetNextLine() {
        String temp = ParserCSV.GetInstance().ParseItemToCSV((items.get(currentLine)));
        currentLine++;
        return temp;
    }

    /** \brief Devuelve los nombres de todos los Item
     *
     *
     * \pre <em>Cierto</em>
     * \post Devuelve los nombres de todos los Item
     */
    public ArrayList<String> GetAllItemsNames() {
        return itemsNames;
    }

    /** \brief Devuelve la puntuación del Item con id = \a id
     *
     * @throws ItemDoesNotExistException
     *
     * \param int id Corresponde a la ID del Item deseado
     * \pre <em>Cierto</em>
     * \post Devuelve la puntuación del Item con ID = \a id
     */
    public float GetScore(int id) throws ItemDoesNotExistException {
        return GetItemByID(id).GetScore();
    }

    /** \brief Devuelve la puntuación del Item con nombre = \a name
     *
     * @throws ItemDoesNotExistException
     *
     * \param String name Corresponde al nombre del Item deseado
     * \pre <em>Cierto</em>
     * \post Devuelve la puntuación del Item con nombre = \a name
     */
    public float GetScore(String name) throws ItemDoesNotExistException {
        return GetItemByName(name).GetScore();
    }

    /** \brief Devuelve los \a r Items mejor punutados del sistema
     *
     * @throws ItemDoesNotExistException
     *
     * \param int r Corresponde a la cantidad de Items que queremos
     * \pre <em>Cierto</em>
     * \post Devuelve los \a r Items mejor punutados del sistema
     */
    public ArrayList<Item> GetTopRated(int r) {
        if(topRated.size() == r) {
            return topRated;
        }
        ArrayList<Item> temp = items;

        for(int i = 0; i < temp.size(); ++i) {
            for(int j = 0; j < temp.size()-1; ++j) {
                if(temp.get(j).GetScore() < temp.get(j+1).GetScore()) {
                    Item tempItem = temp.get(j);
                    temp.set(j, temp.get(j+1));
                    temp.set(j+1, tempItem);
                }
            }
        }

        for(int i = 0; i < r; ++i) {
            topRated.add(temp.get(i));
        }

        return topRated;
    }
}
