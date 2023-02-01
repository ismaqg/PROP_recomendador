/** @file ItemAttribute.java
 *  @brief Contiene la clase ItemAttribute
 */
package fxsrc.propyecto.domain;


import java.util.ArrayList;

/** \class ItemAttribute
 *   \brief Clase que contiene los datos necesarios para un atributo de un Item
 *
 *   Es una clase generica que usa tipos genericos para poder tener atributos de cualquier tipo. Ofrece Varias funciones útiles para añadir valores al vector de atributos, getters y setters
 *   */
public class ItemAttribute <T>{

    /** \brief \a attributeName Corresponde al nombre del atributo
     */
    private String          attributeName;

    /** \brief \a attributeValue Corresponde a los valores del atributo (puede ser uno o varios)
     */
    private ArrayList<T>    attributeValue;

    /** \brief Constructor del ItemAttribute con nombre
     *
     * \param String attributeName Corresponde al nombre del atributo
     * \pre <em>Cierto</em>
     * \post Crea una instancia de ItemAttribute y asigna el nombre del atributo a \a attributeName
     */
    public ItemAttribute(String attributeName) {
        this.attributeName = attributeName;
        this.attributeValue = new ArrayList<T>();
    }

    /** \brief Constructor del ItemAttribute con nombre y los valores del atributo
     *
     * \param String attributeName Corresponde al nombre del atributo
     * \param ArrayList<T> attributeValue Corresponde a los valores del atributo
     * \pre <em>Cierto</em>
     * \post Crea una instancia de ItemAttribute y asigna el nombre del atributo a \a attributeName y los valores del atributo
     */
    public ItemAttribute(String attributeName, ArrayList<T> attributeValue) {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    /** \brief Getter del nombre del atributo
     *
     * \pre <em>Cierto</em>
     * \post Devuelve el nombre del atributo
     */
    public String GetAttributeName() {
        return attributeName;
    }

    /** \brief Setter del nombre del atributo
     *
     * \param String attributeName Corresponde al nombre del atributo
     * \pre <em>Cierto</em>
     * \post Asigna el nuevo valor del nombre del atributo
     */
    public void SetAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    /** \brief Getter de los valores del atributo
     *
     * \pre <em>Cierto</em>
     * \post Devuelve los valores del atributo
     */
    public ArrayList<T> GetAllAttributeValues() {
        return attributeValue;
    }

    /** \brief Getter de el valor en la posicion \a i del atributo
     *
     * \param int i Corresponde a la posición que queremos acceder del atributo
     * \pre <em>Cierto</em>
     * \post Devuelve el valor en la posicion \a i del atributo
     */
    public T GetAttributeValue(int i) throws AttValDoesNotExistException {
        if(i < 0 || i >= attributeValue.size()) {
            throw new AttValDoesNotExistException();
        }
        return attributeValue.get(i);
    }

    /** \brief Getter de la cantidad de valores que tiene el atributo
     *
     * \pre <em>Cierto</em>
     * \post Devuelve el tamaño del array \a attributeValue
     */
    public int GetSize() {
        return attributeValue.size();
    }

    /** \brief Compara este ItemAttribute con otro y devuelve si son equivalentes o no
     *
     * \param ItemAttribute(T) other Corresponde al ItemAttribute con el que se quiere la comparación
     * \pre <em>Cierto</em>
     * \post Compara dos ItemAttribute y devuelve si son equivalentes o no
     */
    public boolean Equals(ItemAttribute<T> other) {
        for(int i = 0; i < other.GetSize(); ++i) {
            try {
                if (!other.GetAttributeValue(i).equals(this.GetAttributeValue(i))) {
                    return false;
                }
            }
            catch (AttValDoesNotExistException e) {
                e.printStackTrace();
                System.exit(0);
                return false;
            }
        }
        return true;
    }

    /** \brief Devuelve si el atributo contiene el valor pasado por parametro
     *
     * \param T otherValue Corresponde al valor que se desea buscar
     * \pre <em>Cierto</em>
     * \post Debuelve si el atributo contiene el valor pasado por paramtro o no
     */
    public boolean Contains(T otherValue) {
        for(int i = 0; i < attributeValue.size(); ++i) {
            if(attributeValue.get(i).equals(otherValue)) {
                return true;
            }
        }
        return false;
    }

    /** \brief Devuelve el tipo de clase que es el atributo
     *
     * \pre <em>Cierto</em>
     * \post Devuelve el tipo de clase que es el atributo
     */
    public Class GetClass() {
        return attributeValue.getClass();
    }

    /** \brief Asigna los valores del atributo
     *
     * \param ArrayList<T> newValue Corresponde al nuevo valor a asignar en el atributo
     * \pre <em>Cierto</em>
     * \post Asigna los nuevos valores al atributo
     */
    public void SetAttributeValue(ArrayList<T> newValue) {
        this.attributeValue = newValue;
    }

    /** \brief Añade un valor a la lista de valores del atributo
     *
     * \param T newValue Corresponde al valor a añadir
     * \pre <em>Cierto</em>
     * \post Se añade un valor a la lista de valores del atributo
     */
    public void AddAttributeValue(T newValue) {
        this.attributeValue.add(newValue);
    }

    /** \brief Devuelve los valores del atributo en formato CSV
     *
     * \pre <em>Cierto</em>
     * \post Devuelve los valores del atributo en formato CSV
     */
    public String ToString() {
        String ret = "";
        for(int i = 0; i < attributeValue.size(); ++i) {
            ret += attributeValue.get(i);
            if(attributeValue.size() > i + 1) {
                ret += ";";
            }
        }
        return ret;
    }
}
