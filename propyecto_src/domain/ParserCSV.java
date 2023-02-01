/** @file ParserCSV.java
 *  @brief Contiene la clase ParserCSV
 */

package fxsrc.propyecto.domain;

import fxsrc.propyecto.data.DataRating;
import fxsrc.propyecto.enums.*;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Locale;

/** \class ParserCSV
 *   \brief Clase que sirve para parsear un CSV
 *
 *   Contiene funciones útiles para parser un CSV a cualquier Item o a Rating
 *   */
public class ParserCSV {

    /** \brief \a attributeName Corresponde al nombre del atributo
     */
    private static ParserCSV instance;

    /** \brief Constructor del ParserCSV con nombre
     *
     * \pre <em>Cierto</em>
     * \post Crea LA instancia de ParserCSV
     */
    private ParserCSV() {

    }

    /** \brief Devuelve la instancia de ParserCSV
     *
     * \pre <em>Cierto</em>
     * \post Si no existe la instancia de ParserCSV, la crea y luego la devuelve, sino solo la devuelve
     */
    public static ParserCSV GetInstance() {
        if(instance == null) {
            instance = new ParserCSV();
        }
        return instance;
    }

    /** \brief Devuelve la instancia de DataManager
     *
     * \param String csvElement La linea de CSV a separar en celdas
     * \pre \a csvElement es un String en formato CSV correcto
     * \post Devuelve un array de String separado por cada celda del fichero
     */
    public String[] CutCSVInParts(String csvElement) {
        ArrayList<String> result = new ArrayList<String>();
        String tempString = new String();
        boolean quote = false;
        for(int i = 0; i < csvElement.length(); ++i) {
            if(csvElement.charAt(i) == '"') {
                quote = !quote;
                tempString += csvElement.charAt(i);
            }
            else if(csvElement.charAt(i) == ',' && !quote) {
                result.add(tempString);
                tempString = "";
            }
            else {
                tempString += csvElement.charAt(i);
            }
        }
        result.add(tempString);
        return result.toArray(new String[0]);
    }

    /** \brief Devuelve el tipo de dato que es el valor pasado por parámetro
     *
     * \param String[] itemValue Corresponde al valor que deseamos saber su tipo
     * \pre itemValue tiene almenos un elemento
     * \post Devuelve el tipo de dato del valor pasado por parámetro
     */
    public ItemTypes GetItemType(String[] itemValue) {

        if(itemValue[0].toUpperCase().equals("TRUE") || itemValue[0].toUpperCase().equals("FALSE")) {
            return ItemTypes.BOOLEAN;
        }
        try{
            int temp = Integer.parseInt(itemValue[0]);
            return ItemTypes.INT;
        }
        catch(Exception e){

        }
        try{
            float temp = Float.parseFloat(itemValue[0]);
            return ItemTypes.FLOAT;
        }
        catch(Exception e){

        }
        return ItemTypes.STRING;
    }

    /** \brief Parsea una línea CSV a un Item
     *
     * \param String attributesNames[] Corresponde al nombre de todos los atributos que tendrá el Item
     * \param String csvElement Corresponde a la línea de CSV que se desea parsear a Item
     * \pre El número de atributos que tiene el Item que corresponde a a \a csvElement es igual al de \a attributesNames
     * \post Parsea una línea de CSV a Item
     */
    public Item ParseItem(String attributesNames[], String csvElement) {
        Item result = new Item();
        String[] parts = CutCSVInParts(csvElement);
        for(int i = 0; i < parts.length; ++i) {
            String[] temp;
            temp = parts[i].split(";");
            ItemTypes itemType = GetItemType(temp);

            if(itemType == ItemTypes.BOOLEAN) {
                ArrayList<Boolean> tempArrayBool = new ArrayList<Boolean>();
                for(int j = 0; j < temp.length; ++j) {
                    tempArrayBool.add(temp[j].toUpperCase().equals("TRUE"));
                }
                ItemAttribute<Boolean> itemArrayBool = new ItemAttribute<Boolean>(attributesNames[i], tempArrayBool);
                result.AddAttribute(itemArrayBool);
            }
            else if(itemType == ItemTypes.INT) {
                ArrayList<Integer> tempArrayInt = new ArrayList<Integer>();
                for(int j = 0; j < temp.length; ++j) {
                    tempArrayInt.add(Integer.parseInt(temp[j]));
                }
                ItemAttribute<Integer> itemArrayInt = new ItemAttribute<Integer>(attributesNames[i], tempArrayInt);
                if(attributesNames[i].toUpperCase().equals("ID")) {
                    result.SetItemID(Integer.parseInt(temp[0]));
                }
                result.AddAttribute(itemArrayInt);
            }
            else if(itemType == ItemTypes.FLOAT) {
                ArrayList<Float> tempArrayFloat = new ArrayList<Float>();
                for(int j = 0; j < temp.length; ++j) {
                    tempArrayFloat.add(Float.parseFloat(temp[j]));
                }
                ItemAttribute<Float> itemArrayFloat = new ItemAttribute<Float>(attributesNames[i], tempArrayFloat);
                if(attributesNames[i].toUpperCase().contains("SCORE") || attributesNames[i].toUpperCase().contains("VOTE_AVERAGE")) {
                    result.SetScore(tempArrayFloat.get(0));
                }
                result.AddAttribute(itemArrayFloat);
            }
            else if(itemType == ItemTypes.STRING) {
                ArrayList<String> tempArrayString = new ArrayList<String>();
                for(int j = 0; j < temp.length; ++j) {
                    tempArrayString.add(temp[j]);
                }
                ItemAttribute<String> itemArrayString = new ItemAttribute<String>(attributesNames[i], tempArrayString);
                try {
                    if (itemArrayString.GetSize() == 1 && itemArrayString.GetAttributeValue(0).toUpperCase().contains("HTTP") &&
                            (itemArrayString.GetAttributeValue(0).toUpperCase().contains("PNG") ||
                            itemArrayString.GetAttributeValue(0).toUpperCase().contains("JPG")) ||
                            itemArrayString.GetAttributeValue(0).toUpperCase().contains("JPEG"))
                    {
                        result.SetImagePath(itemArrayString.GetAttributeValue(0));
                    }
                }
                catch(Exception | AttValDoesNotExistException e) {

                }
                result.AddAttribute(itemArrayString);
            }
        }
        return result;
    }

    /** \brief Parsea un Item a una línea CSV
     *
     * \param Item item Corresponde al Item a parsear a CSV
     * \post Parsea un Item a formato CSV
     */
    public String ParseItemToCSV(Item item) {
        return item.ToString();
    }


    /** \brief Parsea una línea CSV a un Rating
     *
     * \param String attributesNames[] Corresponde a los nombres de las columnas de la línea CSV a parsear
     * \param String csvElement Corresponde a la línea de CSV que se desea parsear a Rating
     * \pre \a csvElement es un String en formato CSV correcto para poder parsear a un Rating
     * \post Parsea una línea CSV a un Rating
     */
    public Rating ParseRating(String attributesNames[], String csvElement) {
        Rating result = new Rating();

        String[] parts = CutCSVInParts(csvElement);

        for(int i = 0; i < 3; ++i) {
            if(attributesNames[i].toUpperCase().contains("RAT")) {
                result.SetRating(Float.parseFloat(parts[i]));
            }
            else if(attributesNames[i].toUpperCase().contains("USER")) {
                result.SetUserID(Integer.parseInt(parts[i]));
            }
            else if(attributesNames[i].toUpperCase().contains("ITEM")) {
                result.SetItemID(Integer.parseInt(parts[i]));
            }
        }
        return result;
    }

    /** \brief Parsea un Rating a una línea CSV
     *
     * \param String attributesNames[] Corresponde a los nombres de las columnas de la línea CSV a parsear
     * \param Rating rating Corresponde al Rating a parsear a CSV
     * \pre \a attributesNames tiene exactamente 3 elementos
     * \post Parsea un Rating a formato CSV
     */
    public String ParseRatingToCSV(String attributesNames[], Rating rating){
        String temp = "";
        for(int i = 0; i < 3; ++i) {
            if(attributesNames[i].toUpperCase().contains("RAT")) {
                temp += rating.GetRating();
            }
            else if(attributesNames[i].toUpperCase().contains("USER")) {
                temp += rating.GetUserID();
            }
            else if(attributesNames[i].toUpperCase().contains("ITEM")) {
                temp += rating.GetItemID();
            }
            if(i < 2) {
                temp += ",";
            }
        }
        return temp;
    }

    /** \brief Parsea una línea CSV a un UserActual
     *
     * \param String attributesNames[] Corresponde a los nombres de las columnas de la línea CSV a parsear
     * \param String csvElement Corresponde a la línea de CSV que se desea parsear a Rating
     * \pre \a csvElement es un String en formato CSV correcto para poder parsear a un UserActual
     * \post Parsea una línea CSV a un UserActual
     */
    public UserActual ParseUser(String attributesNames[], String csvElement) {
        UserActual result = new UserActual();
        String[] parts = CutCSVInParts(csvElement);

        for(int i = 0; i < parts.length; ++i) {
            if(attributesNames[i].toUpperCase().contains("USERID")) {
                result.SetUserID(Integer.parseInt(parts[i]));
            }
            else if(attributesNames[i].toUpperCase().contains("USERNAME")) {
                result.SetUsername(parts[i]);
            }
            else if(attributesNames[i].toUpperCase().contains("PASSW")) {
                result.SetPassword(parts[i]);
            }
            else if(attributesNames[i].toUpperCase().contains("EMAIL")) {
                result.SetEmail(parts[i]);
            }
            else if(attributesNames[i].toUpperCase().contains("SECURITY")) {
                result.SetSecurity(parts[i]);
            }
            else if(attributesNames[i].toUpperCase().contains("STARTED")) {
                result.SetStarted(parts[i].toUpperCase().equals("TRUE"));
            }
            else if(attributesNames[i].toUpperCase().contains("FAV")) {
                String[] fav = parts[i].split(";");
                if(!fav[0].equals("")) {
                    for (int j = 0; j < fav.length; ++j) {
                        result.AddFavourite(Integer.parseInt(fav[j]));
                    }
                }
            }
            else if(attributesNames[i].toUpperCase().contains("LIKED")) {
                String[] fav = parts[i].split(";");
                if(!fav[0].equals("")) {
                    for (int j = 0; j < fav.length; ++j) {
                        result.AddLikedRecommendation(Integer.parseInt(fav[j]));
                    }
                }
            }
        }

        return result;
    }

    /** \brief Parsea un User a una línea CSV
     *
     * \param UserActual Corresponde al UserActual a parsear a CSV
     * \pre Cierto
     * \post Parsea un UserActual a formato CSV
     */
    public String ParseUserToCSV(UserActual user) {
        String ret = user.GetUserID() + "," + user.GetUsername() + "," + user.GetEmail() + "," + user.GetPassword() + "," + user.GetSecurity() + "," + user.GetStarted() + ",";
        ArrayList<Item> liked = user.GetLikedRecommendations();
        for(int i = 0; i < liked.size(); ++i) {
            ret += liked.get(i).GetItemId();
            if(i < liked.size()-1) {
                ret += ";";
            }
        }
        ret += ",";
        ArrayList<Item> fav = user.GetFavourites();
        for(int i = 0; i < fav.size(); ++i) {
            ret += fav.get(i).GetItemId();
            if(i < fav.size()-1) {
                ret += ";";
            }
        }
        return ret;
    }

    /** \brief Parsea una línea CSV a un KGroup
     *
     * \param String csvElement Corresponde a la línea de CSV que se desea parsear a KGroup
     * \pre \a csvElement es un String en formato CSV correcto para poder parsear a un KGroup
     * \post Parsea una línea CSV a un KGroup
     */
    public ArrayList<Integer> ParseKGroup(String csvElement) {
        String[] parts = CutCSVInParts(csvElement);
        ArrayList<Integer> result = new ArrayList<>();
        for(int i = 0; i < parts.length; ++i) {
            try {
                result.add(Integer.parseInt(parts[i]));
            }
            catch(Exception e) {

            }
        }
        return result;
    }

    /** \brief Parsea un KGroup a una línea CSV
     *
     * \param Rating rating Corresponde al KGroup a parsear a CSV
     * \pre Cierto
     * \post Parsea un KGroup a formato CSV
     */
    public String ParseKGroupToCSV(ArrayList<Integer> kgroup) {
        String result = "";
        for(int i = 0; i < kgroup.size(); ++i) {
            result += kgroup.get(i);
            if(i < kgroup.size()-1) {
                result += ",";
            }
        }
        return result;
    }

    /** \brief Parsea una línea CSV a un Centroid
     *
     * \param String csvElement Corresponde a la línea de CSV que se desea parsear a Centroid
     * \pre \a csvElement es un String en formato CSV correcto para poder parsear a un UserActual
     * \post Parsea una línea CSV a un Centroid
     */
    public ArrayList<ArrayList<Rating>> ParseCentroids(String csvElement) {
        ArrayList<ArrayList<Rating>> result = new ArrayList<>();
        String[] parts = CutCSVInParts(csvElement);
        for (int i = 0; i < parts.length; ++i) {
            String[] tempCentroid = parts[i].split(";");
            ArrayList<Rating> c = new ArrayList<>();
            for (int j = 0; j < tempCentroid.length; ++j) {

                String[] temp = tempCentroid[j].split("&");
                try {
                    Rating tempRating = new Rating(-1, Integer.parseInt(temp[0]), Float.parseFloat(temp[1]));
                    c.add(tempRating);
                }
                catch(Exception e) {
                }

            }
            result.add(c);
        }
        return result;
    }

    /** \brief Parsea un Centroid a una línea CSV
     *
     * \param Rating rating Corresponde al Centroid a parsear a CSV
     * \pre Cierto
     * \post Parsea un Centroid a formato CSV
     */
    public String ParseCentroidsToCSV(ArrayList<ArrayList<Rating>> centroids) {
        String result = "";
        for(int i = 0; i < centroids.size(); ++i) {
            for(int j = 0; j < centroids.get(i).size(); ++j) {
                result += centroids.get(i).get(j).GetItemID() + "&" + centroids.get(i).get(j).GetRating();
                if(j < centroids.get(i).size() - 1) {
                    result += ';';
                }
            }
            if(i < centroids.size() - 1) {
                result += ",";
            }
        }
        return result;
    }

}
