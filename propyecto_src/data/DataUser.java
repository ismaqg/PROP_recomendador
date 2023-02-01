/** @file DataUser.java
 *  @brief Contiene la clase DataUser
 */

package fxsrc.propyecto.data;

import fxsrc.propyecto.domain.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


/** \class DataUser
 *   \brief Clase que contiene los datos de todos los Usuarios del sistema.
 *
 *   Ofrece operaciones para que el DataManager pueda añadir Usuarios nuevos a partir de un String que sería la representación del Usuario a añadir en formato CSV.
 *   También ofrece operaciones para que el DataManager pida todos los Usuarios, un Usuario en concreto a partir del nombre, o incluso un Usuario a partir de su ID.
 */
public class DataUser extends Data {
    /** \brief HashMap de los usuarios con su username como key
     */
    private HashMap<String, UserActual> systemUsers;
    /** \brief Conjunto de los usuarios para recorridos secuenciales
     */
    private ArrayList<UserActual> usersArray;
    /** \brief HashMap de los usuarios con su id como key
     */
    private HashMap<Integer, UserActual> usersIds;

    /** \brief Constructor por defecto de DataUser
     *
     * \pre <em>Cierto</em>
     * \post Se crea una instancia de DataUser y se llama a la constructora por defecto de Data
     */
    public DataUser () {
        super();
    }

    /** \brief Función para inicializar \a items
     *
     * \pre <em>Cierto</em>
     * \post Se incializa el array \a items a un array vacío,
     */
    public void Init() {
        systemUsers = new HashMap<>();
        usersArray = new ArrayList<>();
        usersIds = new HashMap<>();
    }

    /** \brief Función para inicializar los conjuntos de datos.
     *
     * \pre \a systemUser no es null.
     * \pre \a usersIds no es null.
     * \pre \a usersArray no es null.
     * \post Se resetea los conjuntos de datos
     */
    public void ClearData() {
        systemUsers.clear();
        usersIds.clear();
        usersArray.clear();
    }

    /** \brief Función que devuelve el User con username = \a username
     *
     * \param String username Corresponde al username del usuario deseado
     * \pre \a systemUser no es null.
     * \post Devuelve el User con username = \a username
     */
    public UserActual GetUser(String username) {
        return systemUsers.get(username);
    }

    /** \brief Función que devuelve si existe el user con username = \a username o no
     *
     * \param String username Corresponde al username del usuario a checkear
     * \pre \a systemUser no es null.
     * \post Devuelve si existe el user con username = username o no
     */
    public boolean UserExists(String username) {
        return systemUsers.containsKey(username);
    }

    /** \brief Función que cambia la password del user con username = \a username a \a newPassword
     *
     * \param String username Corresponde al username del usuario a modificar
     * \param String newPassword Corresponde al nuevo password del usario a modificar
     * \pre \a systemUser no es null.
     * \post Cambia la password del user con username = \a username a \a newPassword y lo guarda en el CSV
     */
    public void SetNewPassword(String username, String newPassword) {
        systemUsers.get(username).SetPassword(newPassword);
        StoreCSV();
    }

    /** \brief Función que cambia el email del user con username = \a username a \a newEmail
     *
     * \param String username Corresponde al username del usuario a modificar
     * \param String newEmail Corresponde al nuevo email del usario a modificar
     * \pre \a systemUser no es null.
     * \post Cambia el email del user con username = \a username a \a newEmail y lo guarda en el CSV
     */
    public void SetNewEmail(String username, String newEmail) {
        systemUsers.get(username).SetEmail(newEmail);
        StoreCSV();
    }

    /** \brief Función que cambia el username del user con username = \a username a \a newPassword
     *
     * \param String username Corresponde al username del usuario a modificar
     * \param String newUsername Corresponde al nuevo username del usario a modificar
     * \pre \a systemUser no es null.
     * \post cambia el username del user con username = \a username a \a newUsername y lo guarda en el CSV
     */
    public void SetNewUsername(String username, String newUsername) {
        UserActual temp = systemUsers.get(username);
        systemUsers.remove(username);
        usersArray.remove(temp);
        usersIds.remove(temp.GetUserID());
        temp.SetUsername(newUsername);
        AddUser(temp);
    }

    /** \brief Función que comprueba si existe un User con username = \a username y password = \a password
     *
     * \param String username Corresponde al username del UserActual a checkear
     * \param String password Corresponde a la password del UserActual a checkear
     * \pre \a systemUser no es null.
     * \post Devuelve si existe un User con username = \a username y password = \a password o no
     */
    public boolean CheckCredentials(String username, String password) {
        if(!systemUsers.containsKey(username)) {
            return false;
        }
        else {
            if(systemUsers.get(username).GetPassword().equals(password)) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    /** \brief Función que devuelve la primera ID disponible para un nuevo User
     *
     * \pre Cierto
     * \post Devuelve la primera ID disponible para un nuevo User
     */
    public int GetFreeID() {
        boolean found = false;
        int temp = -1;
        while(!found) {
            temp--;
            if(!usersIds.containsKey(temp)) {
                found = true;
            }
        }
        return  temp;
    }


    /** \brief Función que Añade un nuevo usuario a los conjuntos
     *
     * \param UserActual user Corresponde al UserActual a añadir a los conjuntos
     * \pre \a systemUser no es null.
     * \pre \a usersArray no es null.
     * \pre \a usersIds no es null.
     * \post Añade un nuevo usuario los conjuntos
     */
    public void AddUser(UserActual user) {
        usersArray.add(user);
        systemUsers.put(user.GetUsername(), user);
        usersIds.put(user.GetUserID(), user);
        StoreCSV();
        //escribirlo en la base de datos
    }

    /** \brief Parsea una linea CSV a un UserActual y lo añade a \a systemUsers
     * \param String[] attributesNames Corresponde al nombre de los atributos
     * \param String lineRead Corresponde a la linea CSV que hace referencia al Item deseado
     * \pre \a attributesNames tiene el tamaño exacto de parametros que tenga el Item y \a lineRead no es vacío.
     * \pre Ningun conjunto es null
     * \post Se parsean los atributos de Item y finalmente se añade a \a items.
     */
    public void ParseAndAddData(String attributesNames[], String lineRead) {
        UserActual temp = ParserCSV.GetInstance().ParseUser(attributesNames, lineRead);
        /*ArrayList<Rating> ratings = DataManager.GetInstance().GetUserRatings(temp.GetUserID());
        temp.SetRatingsList(ratings);*/
        usersArray.add(temp);
        systemUsers.put(temp.GetUsername(), temp);
        usersIds.put(temp.GetUserID(), temp);
    }

    /** \brief Funcion que imprime la informacion de todos los usuarios
     * \pre systemUsers no es null
     * \post Se imprime la informacion de todos los usuarios
     */
    public void Print() {
        System.out.println(systemUsers.toString());
    }

    /** \brief Funcion que asigna todos los ratings de un usuario que obtiene de la base de datos
     * \pre systemUsers no es null
     * \pre DataManager ya ha sido instanciado
     * \post Se asignan todos los ratings de un usuario que obtiene de la base de datos
     */
    public void SetRatings() {
        for(int i = 0; i < usersArray.size(); ++i) {
            ArrayList<Rating> ratings = DataManager.GetInstance().GetUserRatings(usersArray.get(i).GetUserID());
            usersArray.get(i).SetRatingsList(ratings);
        }
    }

    /** \brief Funcion que devuelve si hay una línea más por guardar
     * \pre systemUsers no es null
     * \post Devuelve si hay alguna línea más por guardar
     */
    protected boolean HasNextLine() {
        return currentLine < systemUsers.size();
    }

    /** \brief Funcion que devuelve la siguiente línea a guardar
     * \pre usersArray no es null
     * \post Devuelve la siguiente línea a guardar
     */
    protected String GetNextLine() {
        String temp = ParserCSV.GetInstance().ParseUserToCSV(usersArray.get(currentLine));
        currentLine++;
        return temp;
    }

    /** \brief Función que cambia la respuesta de la pregunta de seguridad del user con username = \a username a \a newSecurity
     *
     * \param String username Corresponde al username del usuario a modificar
     * \param String newUsername Corresponde al nuevo username del usario a modificar
     * \pre \a systemUser no es null.
     * \post cambia la respuesta de la pregunta de seguridad del user con username = \a username a \a newSecurity y lo guarda en el CSV
     */
    public void SetNewSecurity(String username, String newSecurity) {
        systemUsers.get(username).SetSecurity(newSecurity);
        StoreCSV();
    }

    /** \brief Función que elimina el usuario con username = \a getUsername
     *
     * \param String username Corresponde al username del usuario a eliminar
     * \pre \a systemUser no es null.
     * \post Elimina el usuario con username = \a getUsername
     */
    public void DeleteUser(String getUsername) {
        UserActual temp = systemUsers.get(getUsername);

        for(int i = 0; i < usersArray.size(); ++i) {
            if(usersArray.get(i).GetUsername().equals(getUsername)) {
                usersArray.remove(i);
                break;
            }
        }
        systemUsers.remove(getUsername);

        usersIds.remove(temp.GetUserID());

        StoreCSV();
    }
}
