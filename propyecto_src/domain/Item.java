/** @file Item.java
 *  @brief Contiene la clase Item
 */
package fxsrc.propyecto.domain;

import javafx.scene.image.Image;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

/** \class Item
 *   \brief Clase que contiene los datos de un Item
 *
 *   Ofrece operaciones para añadir atributos al Item, y para devolver algunos o todos los atributos que éste tenga
 *   */
public class Item {
    /** \brief \a attribute es un array de ItemAttribute que hace referencia a todos los atributos del Item
     */
    private ArrayList<ItemAttribute>    attribute;

    /** \brief \a name hace referencia al nombre del Item
     */
    private String name = "";
    /** \brief \a description hace referencia a la descripción del Item
     */
    private String description = "";
    /** \brief \a categories hace referencia a las categorias del Item
     */
    private ArrayList<ItemAttribute> categories;
    /** \brief \a score hace referencia a la puntuación del Item
     */
    private float score = 0;
    /** \brief \a imagePath hace referencia al link de la imagen del Item en internet
     */
    private String imagePath;

    /** \brief \a itemID hace referencia a la id del Item
     */
    private int                         itemID;

    /** \brief Constructor por defecto de Item
     *
     * \pre <em>Cierto</em>
     * \post Crea una instancia de Item con \a attribute vacío
     */
    public Item() {
        attribute = new ArrayList<ItemAttribute>();
        categories = new ArrayList<>();
    }

    /** \brief Constructor de Item
     *
     * \param int itemID Corresponde a la id deseada del Item a crear
     * \pre \a itemID >= 0
     * \post Crea una instancia de Item con \attribute vacio y se asigna la \itemID con la ID pasada por parámetro
     */
    public Item(int itemID) {
        this.itemID = itemID;
        attribute = new ArrayList<ItemAttribute>();
        categories = new ArrayList<>();
    }

    /** \brief Constructor de Item
     *
     * \param ArrayList<ItemAttribute> attribute Corresponde a los atributos del Item a crear
     * \param int itemID Corresponde a la id deseada del Item a crear
     * \pre \a itemID >= 0
     * \post Crea una instancia de Item con \attribute igual a \a attribute y se asigna la \itemID con la ID pasada por parámetro
     */
    public Item(ArrayList<ItemAttribute> attribute, int itemID) {
        for(int i = 0; i < attribute.size(); ++i) {
            AddAttribute(attribute.get(i));
        }
        this.itemID = itemID;
    }

    /** \brief Añade un atributo a \a attribute
     *
     * \param ItemAttribute attribute Corresponde al atributo a añadir
     * \pre \a attribute no es null
     * \post Añade el atributo \attribute a la lista \a attribute
     */
    public void AddAttribute(ItemAttribute attribute) {
        this.attribute.add(attribute);
        if(name.equals("") && (attribute.GetAttributeName().toUpperCase().contains("NAME") || attribute.GetAttributeName().toUpperCase().contains("TITLE"))) {
            try {
                name = attribute.GetAttributeValue(0).toString();
            }
            catch (Exception | AttValDoesNotExistException e) {

            }
        }
        try {
            if(description.equals("") && (attribute.GetAllAttributeValues().size() == 1 && attribute.GetAttributeValue(0).toString().length() > 60)) {
                description = attribute.GetAllAttributeValues().get(0).toString();
            }
        }
        catch (AttValDoesNotExistException e) {
            e.printStackTrace();
        }
        if(attribute.GetSize() > 1) {
            categories.add(attribute);
        }
    }

    public ArrayList<ItemAttribute> GetCategories() {
        return categories;
    }

    /** \brief Devuelve el nombre del Item
     *
     * \pre <em>Cierto</em>
     * \post Devuelve el nombre del Item
     */
    public String GetName() {
        return name;
    }

    /** \brief Devuelve la descripción del Item
     *
     * \pre <em>Cierto</em>
     * \post Devuelve la descripción del Item
     */
    public String GetDescription() {
        return description;
    }

    /** \brief Devuelve el atributo con nombre \a name
     *
     * \param String name Corresponde al nombre del atributo deseado
     * \pre <em>Cierto</em>
     * \post Devuelve el atributo con nombre \a name del item
     */
    public ItemAttribute GetAttribute(String name) {
        ItemAttribute temp = null;
        for(int i = 0; i < attribute.size(); ++i) {
            temp = attribute.get(i);
            if(Objects.equals(temp.GetAttributeName(), name)) {
                return temp;
            }
        }
        return null;
    }

    /** \brief Devuelve el atributo en la posicion \a pos
     *
     * \param int pos Corresponde a la posicion del atributo deseado dentro de la lista \a attribute
     * \pre <em>Cierto</em>
     * \post Devuelve el atributo en la posicion \a pos dentro de la lista \a attribute
     */
    public ItemAttribute GetAttribute(int pos) {
        if(pos < 0 || pos >= attribute.size()) {
            return null;
        }
        else {
            return attribute.get(pos);
        }
    }


    /** \brief Devuelve el numero de atributos de \a name
     *
     * \pre <em>Cierto</em>
     * \post Devuelve la cantidad de atributos que tiene el Item
     */
    public int GetNumberOfAttributes() {
        return attribute.size();
    }

    /** \brief Se cambia el valor de \a itemID a \a ID
     *
     * \param int ID Corresponde al valor de la nueva ID del Item
     * \pre <em>Cierto</em>
     * \post Se modifica el valor de \a itemID por \a ID
     */
    public void SetItemID(int ID) {
        itemID = ID;
    }

    /** \brief Devuelve el valor de \a itemID
     *
     * \pre <em>Cierto</em>
     * \post Devuelve el valor de \a itemID
     */
    public int GetItemId() {
        return itemID;
    }

    /** \brief Devuelve el Item en formato CSV
     *
     * \pre <em>Cierto</em>
     * \post Devuelve el Item en formato CSV
     */
    public String ToString() {
        String out = "";
        for(int i = 0; i < attribute.size(); ++i) {
            out += attribute.get(i).ToString();
            if(attribute.size() > i+1) {
                out += ",";
            }
        }
        return out;
    }

    /** \brief Compara dos Item
     *
     * \param Item emp Corresponde al Item a comparar
     * \pre \a emp no es null
     * \post Devuelve true si son el mismo Item y false en caso contrario
     */
    public boolean Equals(Item emp) {
        return (emp != null && this.itemID == emp.itemID);
    }


    /** \brief Se cambia el valor de \a score del Item
     *
     * \param float newScore Corresponde a la nueva puntuación del Item
     * \pre <em>Cierto</em>
     * \post Se modifica el valor de \a score por \a newScore
     */
    public void SetScore(float newScore) {
        score = newScore;
    }

    /** \brief Devuelve el valor de score del Item
     *
     * \pre <em>Cierto</em>
     * \post Devuelve el valor de score del Item
     */
    public float GetScore() {
        return score;
    }

    /** \brief Devuelve el path de la imagen del Item
     *
     * \pre <em>Cierto</em>
     * \post Devuelve el path de la imagen del Item
     */
    public String GetImagePath() {
        return imagePath;
    }

    /** \brief Se cambia el valor del \a imagePath del Item
     *
     * \param String imagePath Corresponde al nuevo path de la imagen del Item
     * \pre <em>Cierto</em>
     * \post Se modifica el valor de \a imagePath del Item por \a imagePath
     */
    public void SetImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /** \brief Devuelve el valor la imagen que esté en el imagePath
     *
     * \pre <em>Cierto</em>
     * \post Devuelve el valor la imagen que esté en el imagePath o null si no existe ninguna imagen en ese path
     */
    public Image GetImage() {
        if(!imagePath.equals("")) {
            return new Image(imagePath);
        }
        else {
            return null;
        }
    }
}
