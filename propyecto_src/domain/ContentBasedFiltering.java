/**
 * @file ContentBasedFiltering.java
 * @brief Contiene la clase ContentBasedFiltering.
 */

package fxsrc.propyecto.domain;

import javafx.util.Pair;
import java.text.DecimalFormat;
import java.util.*;

/** \class ContentBasedFiltering
 * \brief Clase encargada del cálculo de distancias entre dos items mediante el uso del algoritmo <em>k-nearest</em>.
 *
 * Ofrece operaciones de comparación de items según como de cercanos sean sus atributos, y asignando ponderaciones si estos son realmente útiles para el cálculo.
 */
public class ContentBasedFiltering extends Recommender {

    /**\brief \a kValue es el número de items cercanos que queremos recibir respecto un item inicial. */
    private int kValue;

    /**\brief \a itemsSize contiene el tamaño del array \a allItems para evitar el coste computacional de repetirlo. */
    private int itemsSize;

    /**\brief \a ALL_ITEMS_NINETY_PERCENT contiene el valor del 90% del tamaño de \a allItems para evitar el coste computacional de repetirlo.  */
    private double ALL_ITEMS_NINETY_PERCENT;

    /**\brief \a ALL_ITEMS_TWENTY_PERCENT contiene el valor del 20% del tamaño de \a allItems para evitar el coste computacional de repetirlo. */
    private double ALL_ITEMS_TWENTY_PERCENT;

    /**\brief \a ponderations almacena el valor de ponderación de cada atributo. En caso de no ser útil un atributo, contiene un 0 en su columna.*/
    private ArrayList<Float> ponderations = new ArrayList<>();

    /**\brief \a allItems almacena la información de todos los items de la base de datos. Se usa para calcular */
    private ArrayList<Item> allItems;

    /**\brief Constructora de ContentBasedFiltering.
     * \pre \a k >= 1 e \a itemID_ existe en el sistema.
     * \param int k Valor que tomará kValue.
     * \param int itemID_ Valor que tomara itemID.
     * \post Se crea una instancia de ContentBasedFiltering, esperando a que se llame a algún método público.
     */
    public ContentBasedFiltering(int k){
        allItems = DataManager.GetInstance().GetAllItems();
        kValue = k;
        ComputePonderations();
    }

    /**\brief Calcula las ponderaciones mediante un algoritmo que analiza si queremos guardar o no cada atributo.
     *
     * \pre Cierto
     * \post Se llena el array \ponderations con los valores generados.
     */
    private void ComputePonderations() {
        ArrayList<String> attributeValue = new ArrayList<>();
        ArrayList<Integer> attributeCount = new ArrayList<>();
        itemsSize = Math.min(allItems.size(), 1500); //Se coge el valor maximo dado que es innecesario iterar más de 1500 items
        ALL_ITEMS_NINETY_PERCENT = itemsSize * 0.9;
        ALL_ITEMS_TWENTY_PERCENT = itemsSize * 0.2;
        int attributeSize = allItems.get(0).GetNumberOfAttributes();
        for(int i = 0; i < attributeSize; ++i){ //Iterando sobre cada atributo (es decir, las columnas de la matriz)
            attributeValue.clear();
            attributeCount.clear();
            for(int j = 0; j < itemsSize; ++j){ //Iterando sobre cada atributo de cada item (es decir, cada valor de un atributo para _un_ item)
                for(int k = 0; k < allItems.get(j).GetAttribute(i).GetSize(); ++k){ //Iterando sobre cada valor distinto dentro del valor de un atributo (por ejemplo en genre)
                    String name = "";
                    try {
                        name = allItems.get(j).GetAttribute(i).GetAttributeValue(k).toString();
                    }
                    catch (AttValDoesNotExistException e){
                        e.printStackTrace();
                        System.exit(0);
                    }
                    if(attributeValue.contains(name)){
                        int index = attributeValue.indexOf(name);
                        int auxCount = attributeCount.get(index);
                        attributeCount.set(index,++auxCount); //Se tiene ya el atributo y se suma uno
                    }
                    else{
                        attributeValue.add(name); //Se añade el atributo nuevo con 1 en count
                        attributeCount.add(1);
                    }
                }
            }
            if(attributeValue.size() > ALL_ITEMS_NINETY_PERCENT){ //Si mas del 90% de los items son totalmente distintos unos a los otros, no los contamos
                ponderations.add(i, 0.0f);
            }
            else{
                int dispersionCount = 0;
                for(int k = 0; k < attributeValue.size(); ++k){
                    if(attributeCount.get(k) > ALL_ITEMS_TWENTY_PERCENT){
                        ++dispersionCount;
                        if (attributeCount.get(k) > ALL_ITEMS_NINETY_PERCENT){ //Si un valor del atributo contiene el 90% de los counts, son todos demasiado iguales y no los contamos
                            dispersionCount = 0;
                            break;
                        }
                    }
                }
                if(dispersionCount > ALL_ITEMS_TWENTY_PERCENT || dispersionCount <= 0){
                    ponderations.add(i, 0.0f);
                }
                else ponderations.add(i, 1.0f); //Tendremos en cuenta este atributo por tener ponderación >0.
            }
        }
    }

    /**\brief Retorna la similitud entre dos strings mediante Jaccard
     *
     * La fórmula utilizada es la división de la union de los caracteres entre la intersección de los mismos.
     *
     * \pre Cierto
     * \param String string1 Primer string a comparar
     * \param String string2 Segundo string a comprar
     * \post Retorna la similitud entre dos strings en un Float comprendido entre 0 y 1 siendo 0 totalmente distintos y 1 iguales.
     */
    private float ComputeJaccard(String string1, String string2) {
        Set<String> intersection = new HashSet<String>();
        Set<String> union = new HashSet<String>();

        int string1Size = string1.length();
        int string2Size = string2.length();
        if (string1Size == 0 || string2Size == 0) {
            return 0.0f;
        }
        boolean finished = false;
        for (int i = 0; i < string1Size; i++) { //Iteramos sobre el primer string
            union.add(String.valueOf(string1.charAt(i))); //Añadimos char iterado a la union
            for (int j = 0; j < string2Size; j++) { //Iteramos sobre el segundo string
                if (string1.charAt(i) == string2.charAt(j)) {
                    intersection.add(String.valueOf(string1.charAt(i))); //Si son iguales, lo añadimos a la interseccion
                }
                if (!finished) {
                    union.add(String.valueOf(string2.charAt(j))); //En caso de estar aun iterando string2, añadimos el char iterado a la union
                }
            }
            finished = true;
        }
        return (float)intersection.size()/(float)union.size();
    }

    /**\brief Retorna la distancia entre dos items dando uso a las ponderaciones.
     * \pre \a itemA e \a itemB existen en el sistema.
     * \param Item itemA Primer item a comparar.
     * \param Item itemB Segundo item a comparar.
     * \post Se retorna un float con la distancia entre los dos items.
     */
    public float CompareItems(Item itemA, Item itemB){ //Compara itemA con itemB
        float countIguales = 0.0f;
        int comparisonCount = 0;
        if(itemA.GetItemId() == itemB.GetItemId()) return -1;         //Si es el mismo item, retornamos -1
        for(int i = 0; i < itemA.GetNumberOfAttributes(); ++i) {      //Iteramos sobre la cantidad de atributos de ambos items
            if (ponderations.get(i) > 0) {                            //Miramos si cogemos o no ese atributo
                ++comparisonCount;                                    //Sumamos uno a atributosValorados
                int sizeAttrA = itemA.GetAttribute(i).GetSize();      //Número de instancias del atributo en A (acción, drama = 2)
                int sizeAttrB = itemB.GetAttribute(i).GetSize();      //Número de instancias del atributo en B (acción, thriller, comedia = 3)
                for (int j = 0; j < sizeAttrA; ++j) {                 //Iteramos sobre acciónA y dramaA
                    for (int k = 0; k < sizeAttrB; ++k) {             //Para cada uno de los anteriores, iteramos sobre acciónB, thrillerB y comediaB
                        try {
                            if (!(itemA.GetAttribute(i).GetAttributeValue(0) instanceof String)) {                                  //Si no es string, comparamamos igualdad
                                if (itemA.GetAttribute(i).GetAttributeValue(j).equals(itemB.GetAttribute(i).GetAttributeValue(k))) {
                                    countIguales += 2.0f / (sizeAttrA + sizeAttrB);
                                }
                            } else {                                                                                                   //Si es string, aplicamos Jaccard
                                float auxiliar = ComputeJaccard(itemA.GetAttribute(i).GetAttributeValue(j).toString(), itemB.GetAttribute(i).GetAttributeValue(k).toString());
                                auxiliar /= (float) sizeAttrA + sizeAttrB;
                                countIguales += auxiliar;
                            }
                        }
                        catch(AttValDoesNotExistException e) {
                            e.printStackTrace();
                            System.exit(0);
                        }
                    }
                }
            }
        }
        return (countIguales/comparisonCount) * 100; //Retornamos porcentaje de similitud.
    }

    /**\brief Retorna el ArrayList de entrada ordenado.
     *
     * \pre \a array contiene valores.
     * \param <ArrayList<Pair<Integer,Float>> array Array de entrada para ser ordenado.
     * \post Se retorna el ArrayList de entrada ordenado primero por el Rating y luego por el ID
     */
    private ArrayList<Pair<Integer, Float>> SortArray(ArrayList<Pair<Integer, Float>> array) {
        Collections.sort(array, new Comparator<Pair<Integer, Float>>() {
            @Override
            public int compare(Pair<Integer, Float> p1, Pair<Integer, Float> p2) {
                if(p1.getValue().equals(p2.getValue())){
                    if (p1.getKey() < p2.getKey()) return 1;
                    return -1;
                }
                if(p1.getValue() > p2.getValue()) return -1;
                return 1;
            }
        });
        return array;
    }

    /**\brief Imprime por pantalla los \a kValue elementos con mayor cercanía al item identificado por \a itemID.
     * \pre Cierto
     * \param ArrayList<Pair<Integer, Float>> finalList Lista a imprimir.
     * \post Se escriben por pantalla los \a kValue elementos con porcentaje de similitud mayor al item identificado por \a itemID.
     */
    public void PrintKNearest(ArrayList<Pair<Integer, Float>> finalList) {
        boolean stop = false;
        int enough = 0;
        for(int i = 0; i < finalList.size() && !stop; ++i){ //Iteramos sobre la lista ordenada de Items similares
            DecimalFormat df = new DecimalFormat("##.##");
            enough++;
            System.out.println("Parecido con " + finalList.get(i).getKey() + ": " + df.format(Math.min(finalList.get(i).getValue(),100.0f)) + "%"); //Los imprimimos con un formato agradable
            if(enough >= kValue) stop = true; //Paramos de imprimir cuando ya hemos impreso k valores
        }
    }

    /**\brief Llena y retorna el Array computado.
     * \pre Cierto
     * \post Llena y retorna el Array ordenado de los \a kValue elementos con mayor cercanía al item identificado por \a itemID.
     */
    public ArrayList<Pair<Integer, Float>> ComputeKNearest(int itemID){
        ArrayList<Pair<Integer, Float>> finalList = new ArrayList<>();
        for(int i = 0; i < allItems.size(); ++i){ //Iteramos sobre todos los items del sistema
            float similitud = CompareItems(DataManager.GetInstance().GetItemByID(itemID), allItems.get(i)); //Obtenemos su similitud con el pasado por parametro
            if (similitud != -1){ //Si no es el mismo
                finalList.add(new Pair<>(allItems.get(i).GetItemId(), similitud)); //Lo añadimos a la lista
            }
        }
        finalList = SortArray(finalList); //Ordenamos la lista
        finalList.subList(kValue, finalList.size()).clear(); //Hacemos una sublista con los k valores necesarios.
        return finalList;
    }

    /**\brief Devuelve una lista con los N items más parecidos al item del parámetro
     * \param int itemID Es el identificador del item base
     * \param int nItems Es el número de items que devolvera la función
     * \pre Cierto
     * \post Devuelve una lista con los N items más parecidos al item del parámetro
     */
    public ArrayList<Item> GetNSimilarItems(int itemID, int nItems){
        ArrayList<Pair<Integer,Float>> temp = ComputeKNearest(itemID);
        temp.subList(nItems, temp.size()).clear();
        ArrayList<Item> result = new ArrayList<>();
        for(int i = 0; i < temp.size(); ++i){
            result.add(DataManager.GetInstance().GetItemByID(temp.get(i).getKey()));
        }
        return result;
    }
}