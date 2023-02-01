/** @file HybridApproachFiltering.java
 *  @brief Contiene la clase HybridApproachFiltering
 */

package fxsrc.propyecto.domain;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;

/** @class HybridApproachFiltering
 *   @brief Clase encargada de recibir peticiones de recomendación basándose en el CollaborativeFiltering compbinado con Content BasedFiltering. Hereda de la clase \a Recommender
 *
 *   Ofrece operaciones de recomendación de items, basadas en los otros 2 métodos de recomendacion, recogiendo así las mejores particularidades de cada uno.
 */
public class HybridApproachFiltering extends Recommender{

    // ATRIBUTOS:
    /** @brief \a collaborativeFiltering es la instancia de CollaborativeFiltering que se utilizará para recomendar
     */
    private CollaborativeFiltering collaborativeFiltering;

    /** @brief \a contentBasedFiltering es la instancia de ContentBasedFiltering que se utilizará para recomendar
     */
    private ContentBasedFiltering contentBasedFiltering;

    /** @brief \a userGroups es la cantidad de agrupaciones que queremos en CollaborativeFiltering. Es lo mismo que la \a K del collaborativeFiltering
     */
    private int userGroups; // es la K del CollaborativeFiltering




    // CONSTANTES:
    /** @brief \a contentBasedK es el valor de K que se utilizará para el collaborative filtering.
     */
    private static final int contentBasedK = 5;

    /** @brief \a COLLAB_RECOMMENDATIONS_THRESHOLD es la cantidad de recomendaciones maximas que se le pediran al collaborative filtering
     */
    private static final int COLLAB_RECOMMENDATIONS_THRESHOLD = Integer.MAX_VALUE; // TODO: CUANDO HAYA TESTEADO EL DCG Y TAL VOLVER A HACER QUE EL VALOR DE COLLAB_RECOMMENDATIONS_THRESHOLD VALGA 50

    /** @brief \a CONTENT_SIMILARITY_THRESHOLD es la similitud (en tanto por cien) entre 2 items a partir del cual, si es menor, desecharemos.
     */
    private static final float CONTENT_SIMILARITY_THRESHOLD = 60.0f;

    /** @brief \a POSITIVE_RATING_THRESHOLD es la nota a partir de la cual consideraremos que la nota a un item indica que le ha gustado
     */
    private static final float POSITIVE_RATING_THRESHOLD = DataManager.GetInstance().GetMaxRating() * 0.8f;



    /** @brief Constructora de HybridApproachFiltering.
     *
     * \pre \a userGroups >= 1
     * @param userGroups es la K deseada para Kmeans
     * \post Se crea una instancia de la clase HybridApproachFiltering, lista para dar recomendaciones utilizando \a userGroups grupos distintos para ello.
     */
    public HybridApproachFiltering(int userGroups) {
        this.userGroups = userGroups;
        collaborativeFiltering = new CollaborativeFiltering(userGroups);
        contentBasedFiltering = new ContentBasedFiltering(contentBasedK);
    }


    /** @brief Función recomendadora que hará la recomendación a un usuario nuevo en base al uso de combinar los otros dos metodos de recomendacion que tenemos.
     *
     * \pre \a newUserID tiene que ser un usuario que NO se encuentre en la base de datos de ratings del sistema, es decir, debe ser un nuevo usuario para el sistema; aparte, debe ser un Integer, no un int. \a numberOfRecommendations > 0. \a ratingsOfThatUser son los ratings de ese usuario nuevo.
     * @param newUserID es el ID del usuario al que queremos recomendar
     * @param ratingsOfThatUser son las valoraciones conocidas de ese usuario
     * @param numberOfRecommendations es el numero de recomendaciones que queremos hacer
     * \post Retorna \a numberOfRecommendations recomendaciones ordenadas por valoracion decreciente. Si \a numberOfRecommendations es mayor a un threshold interno, retornará la cantidad marcada por ese threshold.
     */
    public ArrayList<Integer> Recommend (Integer newUserID, ArrayList<Rating> ratingsOfThatUser, int numberOfRecommendations) {

       // Vemos las recomendaciones para este usuario según el collab:
       ArrayList<Rating> collabFiltRecommendations = collaborativeFiltering.Recommend(newUserID, ratingsOfThatUser, COLLAB_RECOMMENDATIONS_THRESHOLD);

       // Ordenacion decreciente de los datos KNOWN del user según las notas:
       ratingsOfThatUser.sort(new Comparator<Rating>() {
            @Override
            public int compare(Rating o1, Rating o2) {
                return Float.valueOf(o2.GetRating()).compareTo(Float.valueOf(o1.GetRating()));
            }
        });

        return RecommendInternal(collabFiltRecommendations, ratingsOfThatUser, numberOfRecommendations);
    }

    /** @brief Función recomendadora que hará la recomendación a un usuario ya existente en el sistema en base al uso de combinar los otros dos metodos de recomendacion que tenemos.
     *
     * \pre \a ExistantUserIDRecommend tiene que ser un usuario que OBLIGATORIAMENTE se encuentre en la base de datos de ratings del sistema; aparte, debe ser un Integer, no un int. \a numberOfRecommendations > 0.
     * @param ExistantUserIDRecommend es el ID del usuario al que queremos recomendar
     * @param numberOfRecommendations es el numero de recomendaciones que queremos hacer
     * \post Retorna \a numberOfRecommendations recomendaciones ordenadas por valoracion decreciente. Si \a numberOfRecommendations es mayor a un threshold interno, retornará la cantidad marcada por ese threshold.
     */

    public ArrayList<Integer> Recommend(Integer ExistantUserIDRecommend, int numberOfRecommendations) throws NoExistingUserIDException {
        // IMPORTANTE: Aquí NO llamar a la otra funcion Recommend porque entonces volvera a llamar a collaborative Filtering (con un usuario que existia) y cosas de esas

        // Vemos las recomendaciones para este usuario según el collab:
        ArrayList<Rating> collabFiltRecommendations = collaborativeFiltering.Recommend(ExistantUserIDRecommend, COLLAB_RECOMMENDATIONS_THRESHOLD);

        // Le pedimos a DataManager las valoraciones (KNOWN) de este user:
        ArrayList<Rating> ratingsOfThatUser = DataManager.GetInstance().GetRatingsValueDB(ExistantUserIDRecommend);
        // Ordenamos los ratings de ese user decrecientemente por nota:
        ratingsOfThatUser.sort(new Comparator<Rating>() {
            @Override
            public int compare(Rating o1, Rating o2) {
                return Float.valueOf(o2.GetRating()).compareTo(Float.valueOf(o1.GetRating()));
            }
        });

        return RecommendInternal(collabFiltRecommendations, ratingsOfThatUser, numberOfRecommendations);

    }



    /** @brief Función recomendadora que hará el trabajo en común que tienen los otros métodos \a Recommend de esta clase
     *
     * \pre \a ratingsofthatuser tiene que estar ORDENADO decrecientemente por nota, \a numberOfRecommendations > 0
     * @param collabFiltRecommendations tiene que ser un vector que nos haya retornado la funcion de recomendar del CollaborativeFiltering para el mismo usuario al que estamos intentando recomendar
     * @param ratingsOfThatUser son los ratings conocidos del usuario al que estamos intentando recomendar
     * @param numberOfRecommendations es el numero de recomendaciones que queremos hacer
     * \post Retorna \a numberOfRecommendations recomendaciones ordenadas por predicción de a cuáles le dará mas nota finalmente (las primeras posiciones seran a las que hemos predicho mayor nota). Si \a numberOfRecommendations es mayor a un threshold interno, retornará la cantidad marcada por ese threshold.
     */
    private ArrayList<Integer> RecommendInternal(ArrayList<Rating> collabFiltRecommendations, ArrayList<Rating> ratingsOfThatUser, int numberOfRecommendations) {

        // Declaro el vector que retornaré con las recomendaciones:
        ArrayList<Integer> recommendations = new ArrayList<Integer>();

        // cogemos todas las valoraciones >= POSITIVE_RATING_THRESHOLD dentro de los ratings KNOWN de ese usuario:
        ArrayList<Rating> userPositiveRatings = new ArrayList<Rating>();
        for (Rating rating : ratingsOfThatUser) {
            if(rating.GetRating() >= POSITIVE_RATING_THRESHOLD){
                userPositiveRatings.add(rating);
            }
            else break;
        }

        // para cada uno de los items de userPositiveRatings, cojo los items más parecidos (con parecido mayor al threshold establecido):
        ArrayList<Pair<Integer, Float>> similarToPositiveRatedItems = new ArrayList<Pair<Integer, Float>>();
        for (Rating positiveRatedItem : userPositiveRatings){
            ArrayList<Pair<Integer, Float>> similarToOneItem = contentBasedFiltering.ComputeKNearest(positiveRatedItem.GetItemID()); //retorna ordenado!
            // descarto todos los que tengan similitud menor al threshold establecido:
            for(int i = 0; i < similarToOneItem.size(); i++){
                if(similarToOneItem.get(i).getValue() <= CONTENT_SIMILARITY_THRESHOLD) {
                    similarToOneItem.subList(i, similarToOneItem.size()).clear();
                    break;
                }
            }
            // y el resto los concateno en similarToPositiveRatedItems:
            similarToPositiveRatedItems.addAll(similarToOneItem);
        }

        // de esos items que tengo, solo me quedo con aquellos presentes en lo que me había recomendado el CollaborativeFiltering:
        for(Rating itemCollabRecommendation : collabFiltRecommendations) {
            if (numberOfRecommendations == recommendations.size()) break;
            for (Pair<Integer, Float> itemCBF : similarToPositiveRatedItems) {
                if (itemCollabRecommendation.GetItemID() == itemCBF.getKey()) {
                    recommendations.add(itemCollabRecommendation.GetItemID()); break;
                }
            }
        }

        // System.out.println("*********DEBUG: RECOMENDACIONES COGIDAS POR EL HIBRIDO: " + recommendations.size() + " **********");

        // si tengo menos recomendaciones de lo que pedian, añado tambien como recomendacion los primeros elementos retornados por el collaborativeFiltering (que no estuviesen ya en el vector recommendations).
        // En caso de que ese numberOfRecommendations sea mas grande que las recomendaciones que soy capaz de dar (que realmente coincide con el valor de COLLAB_RECOMMENDATION_THRESHOLD) pues numberOfRecommendations = COLLAB_RECOMMENDATION_THRESHOLD
        if (recommendations.size() < numberOfRecommendations){
            for (Rating itemCollabRecommendation : collabFiltRecommendations) {
                if(!recommendations.contains(itemCollabRecommendation.GetItemID()))
                    recommendations.add(itemCollabRecommendation.GetItemID());
                if (recommendations.size() == numberOfRecommendations) break;
            }
        }

        return recommendations;
    }

}
