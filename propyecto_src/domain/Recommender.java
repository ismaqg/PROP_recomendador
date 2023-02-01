/** @file Recommender.java
 *  @brief Contiene la clase Recommender, de la que heredan nuestras 3 formas de recomendación
 */

package fxsrc.propyecto.domain;

import java.util.ArrayList;
import java.util.Comparator;

/** @class Recommender
 *   @brief Clase abstracta padre de nuestros 3 recomendadores
 *
 *   Ofrece operaciones de cálculos de calidad de recomendación utilizando Discounted Cumulative Gain.
 */
public abstract class Recommender {
    //no tiene atributo de DataManager ni de UserManager porque SON SINGLETON

    /** @brief Computa el log 2 de un entero
     *
     * Se basa en que log2(N) = log10(N) / log10(2). Java no trae nativamente la funcion log2 en la clase Math
     *
     * \pre N > 0
     * @param N el entero del cual queremos hacer log2
     * \post Retorna log2(2).
     */
    private static double Log2(int N) {
        return (Math.log(N) / Math.log(2));
    }

    /** @brief Retorna el índice de un vector de itemsID en el que se encuentra un rating con el itemID especificado, o MAX_VALUE si no está
     *
     * \pre <em>Cierto</em>
     * @param predicted vector de itemsID en el que se va a buscar
     * @param itemID es el itemID que se quiere buscar
     * \post Retorna el índice en el que está un rating con ese itemID. Si itemID no está presente, retorna MAX_VALUE
     */
    private Integer GetIndexGivenItemID(ArrayList<Integer> predicted, int itemID){
        for(Integer x : predicted) {
            if (x == itemID) return Integer.valueOf(x);
        }
        return Integer.MAX_VALUE; // puede ocurrir que el itemID no esté, ya que itemID es un elemento del vector unknownRatings para un cierto user y el vector predicted es un SUBCONJUNTO de ese otro vector comentado.
        // en ese caso retornamos MAX_VALUE porque a la hora de ordenar tenemos este valor en cuenta y nos interesa que se coloque el último dentro de todos los items con misma nota para que tenga menos peso en el cálculo del IDCG.
    }

    /** @brief Computa el DCG de la predicción hecha sobre los items de unknown del usuario
     *
     * \pre \a userID representa el ID de un usuario presente en el file de UNKNOWN del sistema y \a predicted son los ID de las predicciones de los items presentes en UNKNOWN que más le van a gustar ya \b ordenados
     * @param userID es el ID del usuario para el que se va a computar
     * @param predicted vector de itemsID de los items predichos que le gustarán
     * \post Retorna el DCG.
     */
    public float ComputeDCG(int userID, ArrayList<Integer> predicted) {
        float DCG = 0.0f;
        int alertCounter = 0;
        ArrayList<Rating> unknownRatings = DataManager.GetInstance().GetUserUnknownRatings(userID);

        int Q = predicted.size();
        for(int i = 0; i < Q; i++){
            int predictedItemIDinPositionI = predicted.get(i);
            for(Rating unknownRating : unknownRatings){
                if (unknownRating.GetItemID() == predictedItemIDinPositionI){
                    DCG += (   (Math.pow(2.0, unknownRating.GetRating()) - 1) / Log2(i+2)   ); //+2 en lugar de +1 porque el bucle for empieza en i=0 y no en i=1
                    alertCounter++;
                    break;
                }
            }
        }
        if(alertCounter != Q) System.out.println("ALERT: Al computar el DCG no hemos pasado un vector de predicteds que contiene itemsID presentes en los UNKNOWN items para ese user");

        return DCG;
    }

    /** @brief Computa el IDCG para un usuario con los mejores Q elementos de unknown
     *
     * Para ello se ordenan los items de unknown en funcion del rating de unknown que tienen de forma decreciente. En caso de mismo rating, ordeno primero aquel que yo recomiende antes (es decir, aquel con indice menor en el vector predicted)
     *
     * \pre \a userID representa el ID de un usuario presente en el file de UNKNOWN del sistema y \a predicted son los ID de las predicciones de los items presentes en UNKNOWN que más le van a gustar ya \b ordenados
     * @param userID es el ID del usuario para el que se va a computar
     * @param predicted vector de itemsID de los items predichos que le gustarán
     * \post Retorna el IDCG.
     */
    public float ComputeIDCG(int userID, ArrayList<Integer> predicted) {
        float IDCG = 0.0f;
        ArrayList<Rating> unknownRatings = DataManager.GetInstance().GetUserUnknownRatings(userID);
        // Ordenamos DE FORMA DECRECIENTE, necesario para el bucle de más abajo. En caso de 2 items de unknown con mismo rating, ordeno primero aquel que yo haya dicho que se recomienda con más prioridad
        unknownRatings.sort(new Comparator<Rating>() {
            @Override
            public int compare(Rating o1, Rating o2) {
                if(Float.valueOf(o2.GetRating()).compareTo(Float.valueOf(o1.GetRating())) == 0){ //en caso de 2 items de unknown con mismo rating en unknown...
                    Integer indexOfO1 = GetIndexGivenItemID(predicted, o1.GetItemID());
                    Integer indexOfO2 = GetIndexGivenItemID(predicted, o2.GetItemID());
                    // como predicted está ordenado, el índice que sea menor significa que yo lo estoy recomendando con mas prioridad. Si alguno de esos items no se encontraba en nuestras predicciones le damos indice = MAX_VALUE
                    // porque así se pondrá lo mas a la derecha posible despues de ordenar dentro de todos los items con nota igual, y de esta forma tendrá menos peso en el cálculo del IDCG
                    return indexOfO1.compareTo(indexOfO2); // ordeno antes al que recomiendo antes.
                }
                else return Float.valueOf(o2.GetRating()).compareTo(Float.valueOf(o1.GetRating()));
            }
        });

        int Q = predicted.size();
        for(int i = 0; i < Q; i++){
            IDCG += (   (Math.pow(2.0, unknownRatings.get(i).GetRating()) - 1) / Log2(i+2)   ); //+2 en lugar de +1 porque el bucle for empieza en i=0 y no en i=1
        }

        return IDCG;
    }

    /** @brief Computa el NDCG, comprendido entre 0 y 1. Cuanto más cercano a 1, mejor predicción.
     *
     * \pre \a DCG y \a IDCG deben haber salido de utilizar previamente los métodos ComputeIDCG y ComputeDCG sobre un mismo usuario.
     * @param DCG es el valor de DCG que ha retornado la funcion ComputeDCG para un cierto usuario X
     * @param IDCG es el valor de DCG que ha retornado la funcion ComputeDCG para un cierto usuario X
     * \post Retorna el NDCG.
     */
    public float ComputeNDCG(float DCG, float IDCG) {
        return DCG / IDCG;
    }
}
