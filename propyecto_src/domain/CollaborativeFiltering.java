/** @file CollaborativeFiltering.java
 *  @brief Contiene la clase CollaborativeFiltering
 */

package fxsrc.propyecto.domain;

import java.util.*;

/** @class CollaborativeFiltering
 *   @brief Clase encargada de recibir peticiones de recomendación basándose en el CollaborativeFiltering. Hereda de la clase \a Recommender
 *
 *   Ofrece operaciones de recomendación de items, basadas en agrupación de usuarios similares mediante el algoritmo Kmeans (donde la K es un parámetro en la construcción de la clase) y la posterior recomendación de items basada en los gustos de los usuarios similares haciendo uso de SlopeOne.
 */
public class CollaborativeFiltering extends Recommender{

    /** @brief \a K es la cantidad de agrupaciones de usuarios distintas que queremos.
     */
    private int K;

    /** @brief \a dataset indica la cantidad de usuarios del dataset que utilizamos (en el caso de series, puede ser 250, 750, 2250)
     */
    private static final int dataset = 750;

    /** @brief \a Kgroups son las K agrupaciones de usuarios similares distintas que tenemos.
     */
    private ArrayList<ArrayList<Integer>> Kgroups; //Vector of K positions, which each position has the group of UsersID which have similar likes.

    /** @brief \a KgroupsEmpty indica si \a Kgroups está vacío o no, para saber si se debe calcular o ya se ha calculado.
     */
    private boolean KgroupsEmpty; //porque no puedo usar el metodo Kgroups.IsEmpty porque en la constructora lo inicializo con k posiciones (que contendran basura), asi que siempre daria que no está vacio

    /** @brief \a ratingsMap guarda, para cada usuario (representado por su UserID), todas sus valoraciones (en forma de vector de \a Ratings) existentes en el sistema.
     */
    private HashMap<Integer, ArrayList<Rating>> ratingsMap; //clave = userID. Valor = vector con items valorados por ese user con la valoración. Lo pongo como variable global porque lo uso en Kmeans y SlopeOne

    /** @brief \a centroids almacena los K centroides, uno por cada agrupación de usuarios. Un centroide representa el centro de gustos dentro de un grupo de usuarios similares
     */
    private ArrayList<ArrayList<Rating>> centroids;

    /** @brief \a maxRatingValue almacena el valor máximo que puede dar un user a un item
     */
    private static final float maxRatingValue = DataManager.GetInstance().GetMaxRating();

    /** @brief Constructora de CollaborativeFiltering.
     *
     * \pre \a K >= 1
     * @param k es la K deseada para Kmeans
     * \post Se crea una instancia de la clase CollaborativeFiltering, lista para dar recomendaciones utilizando K grupos distintos para ello.
     */
    public CollaborativeFiltering(int k) {
        K = k;
        Kgroups = new ArrayList<ArrayList<Integer>>(k);
        for (int i = 0; i < k; i++) Kgroups.add(new ArrayList<Integer>()); //necesario para que tenga el size que quiero. Porque el constructor le da initialcapacity pero si luego intento indexar elemento da error. FINAL SIZE= K
        KgroupsEmpty = true;
        ratingsMap = new HashMap<Integer, ArrayList<Rating>>();
        centroids = new ArrayList<ArrayList<Rating>>(); //cada centroide emula un user. Pero como su userID nos da igual por eso solamente damos un vector de pares de peli+valoracion

    }

    /** @brief Retorna la distancia entre 2 usuarios
     *
     * La fórmula utilizada para calcular la distancia es la raiz cuadrada de la suma de los cuadrados de las diferencias de notas entre esos dos items para un cierto user. Solo se tienen en cuenta aquellos items que hayan valorado ambos.
     *
     * \pre Los argumentos representan las listas de valoraciones de 2 usuarios, donde cualquiera de esos usuarios podría ser un centroide o no.
     * @param itemsAndScoresUser1 son los ratings de uno de los dos users de los que se quiere ver su distancia
     * @param itemsAndScoresUser1 son los ratings del otro user
     * \post Retorna la distancia entre 2 usuarios. Esa distancia puede llegar a ser un número muy alto en caso de que no haya ninguna similitud entre los usuarios.
     */
    private double DistanceBetweenUsers(ArrayList<Rating> itemsAndScoresUser1, ArrayList<Rating> itemsAndScoresUser2) {
        double distance = 999999999.0; //No pongo MAX_DOUBLE porque en caso de no tener ningun item en común no funcionaría el "if(distanceToCentroidOfClusterJ < distanceToCentroidOfNearestCluster)" porque ese distanceToCentroidOfNearestCluster vale MAX_DOUBLE, entonces
        //aqui tampoco puedo poner MAX_DOUBLE-1 porque es tan enorme el valor de MAX_DOUBLE (*10^308) que poner un -1 obviamente lo desprecia y se sigue codificando como MAX_DOUBLE

        double sumatorio = 0;
        boolean itemsInCommon = false;
        for(int i = 0; i < itemsAndScoresUser1.size(); i++){
            for(int j = 0; j < itemsAndScoresUser2.size(); j++){
                if(itemsAndScoresUser1.get(i).GetItemID() == itemsAndScoresUser2.get(j).GetItemID()){
                    sumatorio += Math.pow((itemsAndScoresUser1.get(i).GetRating() - itemsAndScoresUser2.get(j).GetRating()), 2);
                    itemsInCommon = true;
                    break;
                }
            }
        }
        if(itemsInCommon)
            distance = Math.sqrt(sumatorio);
        //else{ // OJO, ENTRA CHORROCIENTAS VECES A ESTE ELSE... Supongo que sera normal dados todos los items que hay y que cada user tampoco valora demasiados items
        //    System.out.println("DEBUG: Se ha llamado a DistanceBetweenUsers entre 2 tios sin pelis en comun. Por tanto su distancia se queda como 999999999.0");
        //Si no tiene nada en comun, la distancia será 999999999
        //}

        return distance;
    }



    // IMPORTANTE: Es completamente necesario pasar ese userID como Integer y no como int, ya que en el .remove(userID) interpretara ese userID como indice si lo hemos pasado como int o lo interpretara como contenido si lo hemos pasado como Integer
    /** @brief Mueve a un user del grupo de Kgroups en el que se encuentre a otro grupo, cuyo índice es \a indexDestinationCluster
     *
     * \pre indexDestinationCluster >= 0 y < K. Y userID es el userID de un usuario existente en el sistema. Es completamente necesario que userID sea un Integer y no un int.
     * @param indexDestinationCluster es el índice que representa el cluster al que queremos mover a un usuario
     * @param userID es el ID del usuario que queremos mover de cluster
     * \post El usuario con ese \a userID sale del grupo de usuarios en el que estaba y se mueve al grupo destino.
     */
    private void ChangeUserBetweenClusters(int indexDestinationCluster, Integer userID){ //100% NECESARIO QUE userID SEA INTEGER Y NO INT.
        //sabemos el destinationCluster pero no donde se encuentra ahora mismo así que vamos a buscarlo...
        for(int i = 0; i < K; i++) {
            ArrayList<Integer> clusterI = Kgroups.get(i);
            if(clusterI.contains(userID)){
                clusterI.remove(userID);
                //Kgroups.set(i, clusterI); //NO necesaria, ya que clusterI apunta a Kgroups (comprobado)
                if(Kgroups.get(i).contains(userID)) System.out.println("ALERT: Linea60 Si esto se ha impreso tienes un problema, porque te crees que has borrado un userID de un cluster y realmente eso no ha tenido efecto en la matriz de Kgroups");
                ArrayList<Integer> destinationCluster = Kgroups.get(indexDestinationCluster);
                destinationCluster.add(userID);
                Kgroups.set(indexDestinationCluster, destinationCluster);
                // TODO: Cuando el recomendador funcione, probar a cambiar estas 3 lineas de arriba por "Kgroups.get(indexDestinationCluster).add(userID)" y ver si sigue funcionando
                if(!Kgroups.get(indexDestinationCluster).contains(userID)) System.out.println("ALERT: Linea65 Si esto se ha impreso tienes un problema, porque te crees que has añadido un userID de un cluster y realmente eso no ha tenido efecto en la matriz de Kgroups");
                return;
            }
        }
    }


    //private void DeepCopyArrayListsOfRatings(ArrayList<Rating> source, ArrayList<Rating> destination) {
    //    Iterator<Rating> it = source.iterator();
    //    while (it.hasNext()) {
    //        Rating element = it.next();
    //        Rating newElement = new Rating(element.GetUserID(), element.GetItemID(), element.GetRating()); //la intenté hacer templatizada pero esto de aquí no sé como hacerlo si es templatizada
    //        destination.add(newElement);
    //   }
    //}


    /** @brief Se crean los \a K grupos de usuarios con gustos similares, siendo \a K el atributo de esta clase (se especificó en la constructora)
     *
     * Solo se volverá a computar Kmeans si no estaba ya computado
     *
     * \pre El atributo \a KgrupsEmpty tiene el valor \a true.
     * \post Se crean los grupos de usuarios con gustos similares.
     */
    private void Kmeans() {
        ArrayList<ArrayList<Integer>> temp = DataManager.GetInstance().GetKMeans(K, dataset);
        if(temp != null) {
            Kgroups = temp;
            centroids = DataManager.GetInstance().GetCentroids();
            ratingsMap = DataManager.GetInstance().GetAllRatingsUsersMapDB();
            KgroupsEmpty = false;
        }
        else {
            ratingsMap = DataManager.GetInstance().GetAllRatingsUsersMapDB();
            ArrayList<Integer> usersID = DataManager.GetInstance().GetAllUsersID();

            // INIT OF THE K CENTROIDS CHOOSING K "RANDOM" USERS:
            int numberOfCentroids = 0;
            for (Integer userID : usersID) {
                ArrayList<Rating> ratingsOfOneRandomUser = ratingsMap.get(userID);
                // cuantas mas valoraciones tengan los centroides iniciales, mas exacto sera todo:
                if (ratingsOfOneRandomUser.size() >= 15) {
                    centroids.add(ratingsOfOneRandomUser);
                    ++numberOfCentroids;
                    if(numberOfCentroids == K) break;
                }
            }

            //INICIALIZACION KGROUPS USANDO LOS CENTROIDES RANDOM:
            for (int i = 0; i < usersID.size(); i++) {
                int nearestCluster = -1;
                double distanceToCentroidOfNearestCluster = Double.MAX_VALUE;
                for (int j = 0; j < K; j++) {
                    double distanceToCentroidOfClusterJ = DistanceBetweenUsers(ratingsMap.get(usersID.get(i)), centroids.get(j));
                    if (distanceToCentroidOfClusterJ < distanceToCentroidOfNearestCluster) {
                        distanceToCentroidOfNearestCluster = distanceToCentroidOfClusterJ;
                        nearestCluster = j;
                    }
                }
                if (nearestCluster == -1)
                    System.out.println("ALERT: nearest cluster vale -1. Esta linea pertenece al codigo de inicializacion Kgroup");
                //las siguientes lineas hacen este codigo: Kgroups[nearestCluster].add(usersID.get(i));
                ArrayList<Integer> tmp = Kgroups.get(nearestCluster);
                tmp.add(usersID.get(i));
                Kgroups.set(nearestCluster, tmp);
                if (!Kgroups.get(nearestCluster).contains(usersID.get(i)))
                    System.out.println("ALERT: Si esto se ha impreso tienes un problema, porque te crees que has añadido un userID de un cluster y realmente eso no ha tenido efecto en la matriz de Kgroups. Esta linea está dentro de la parte de inicializacion de Kgroup en el Kmeans");
            }

            //En este punto ya tenemos los clusters hechos a partir de centroides randoms.
            //A continuación vamos a ir reasignando los centroides a "la media de cada grupo" y cambiando a los usuarios de grupo en funcion a sus distancias a esos nuevos centroides,
            //hasta que por fin haya una iteración en la que ningún jugador cambia de grupo (cluster). Ahí diremos que ha convergido

            boolean hasConverged = false;
            int numiters = 0;
            while (!hasConverged) {
                ++numiters;

                //REASIGNACIÓN DE CENTROIDES:
                //cada centroide será un usuario con todas las pelis de su cluster vistas y con la nota media de todas aquellas personas de su cluster que las hayan visto
                for (int i = 0; i < K; i++) {
                    ArrayList<Integer> usersOfOneCluster = Kgroups.get(i);
                    ArrayList<Rating> newCentroid = new ArrayList<Rating>();
                    HashMap<Integer, FrequencyAndSumOfScores> filmAndFrequencyAndSumOfScores = new HashMap<Integer, FrequencyAndSumOfScores>(); //map con clave = itemID y valor = numero de usuarios en ese cluster que han valorado el item y la suma de sus notas (necesarios para la media)
                    //Almacenamos todas las pelis que ha visto gente de ese cluster (y cuantos han visto una cierta peli, para poder hacer la media de las notas:
                    for (int userID : usersOfOneCluster) {
                        ArrayList<Rating> filmsSeenByUser = ratingsMap.get(userID);
                        for (Rating rating : filmsSeenByUser) {
                            if (filmAndFrequencyAndSumOfScores.containsKey(rating.GetItemID())) {
                                FrequencyAndSumOfScores freqAndSumScores = filmAndFrequencyAndSumOfScores.get(rating.GetItemID());
                                ++freqAndSumScores.frequency;
                                freqAndSumScores.scoreSum += rating.GetRating();
                                filmAndFrequencyAndSumOfScores.replace(rating.GetItemID(), freqAndSumScores);
                            } else {
                                FrequencyAndSumOfScores freqAndSumScores = new FrequencyAndSumOfScores(1, rating.GetRating());
                                filmAndFrequencyAndSumOfScores.put(rating.GetItemID(), freqAndSumScores);
                            }
                        }
                    }
                    //Llenamos el centroide de ese cluster con todas las pelis vistas de gente de ese cluster y la respectiva nota media de la gente que la ha visto:
                    for (var entry : filmAndFrequencyAndSumOfScores.entrySet()) {
                        int itemID = entry.getKey();
                        FrequencyAndSumOfScores freqAndSumOfScores = entry.getValue();
                        float score = freqAndSumOfScores.scoreSum / freqAndSumOfScores.frequency;
                        //    if (score > maxRatingValue) score = maxRatingValue;
                        //    else if (score < 0.0f) score = 0.0f; // NO deberia ocurrir nunca, ya que score es igual a una suma de positivos dividido entre el numero de sumandos.
                        Rating itemAndScore = new Rating(-1, itemID, score); //userID=-1 porque no sirve de nada, la info de ese campo sobra.
                        newCentroid.add(itemAndScore);
                    }
                    //Y ahora reemplazamos el centroide de la iter anterior por el nuevo
                    centroids.set(i, newCentroid);
                }

                //CAMBIO DE USUARIOS A SUS NUEVOS CLUSTERS EN FUNCIÓN DE LOS NUEVOS CENTROIDES:
                hasConverged = true; //en el momento que un usuario cambie de grupo, esto se pone a false.
                for (int i = 0; i < usersID.size(); i++) {
                    int nearestCluster = -1;
                    double distanceToCentroidOfNearestCluster = Double.MAX_VALUE;
                    for (int j = 0; j < K; j++) {
                        double distanceToCentroidOfClusterJ = DistanceBetweenUsers(ratingsMap.get(usersID.get(i)), centroids.get(j));
                        if (distanceToCentroidOfClusterJ < distanceToCentroidOfNearestCluster) {
                            distanceToCentroidOfNearestCluster = distanceToCentroidOfClusterJ;
                            nearestCluster = j;
                        }
                    }
                    if (nearestCluster == -1)
                        System.out.println("ALERT: nearest cluster vale -1. Esta linea pertenece al codigo de convergencia");
                    //Buscar si el usuario está en el cluster que nos interesa. Si no esta, lo sacamos del que este y lo ponemos en el que queremos:
                    if (!Kgroups.get(nearestCluster).contains(usersID.get(i))) {
                        hasConverged = false;
                        ChangeUserBetweenClusters(nearestCluster, usersID.get(i));
                    }
                }

            }

            KgroupsEmpty = false;
            DataManager.GetInstance().SetKMeans(K, dataset, Kgroups, centroids);

            /*
            System.out.println("DEBUG: users in ratingsmap: " + ratingsMap.size() + " and number of users: " + usersID.size());
            System.out.println("DEBUG: KMEANS CON K=" + K + " HA TARDADO EN CONVERGER " + numiters + " ITERACIONES");
            for (int i = 0; i < K; i++){
                System.out.println("DEBUG: El cluster " + i + " tiene " + Kgroups.get(i).size() + " usuarios");
            }
             */
        }


    }

    /** @brief Retorna el grupo de usuarios (representados por usersID) con los gustos más similares al usuario representado por el \a userID que hay como parametro, incluyendo a ese usuario.
     * Se utiliza cuando se quiere saber los usuarios más cercanos a un usuario que ya esté presente en alguno de los K grupos.
     *
     * \pre UserID está contenido en algún grupo de Kgroups.
     * @param userID es el ID del usuario del cual queremos saber los demás usuarios del sistema con afinidad a él
     * \post Retorna el grupo de usuarios (representados por usersID) con los gustos más similares al usuario representado por el \a userID que hay como parametro
     */
    private ArrayList<Integer> UsersIDofSimilarUsers(int userID) {
        for(ArrayList<Integer> innerList : Kgroups) {
            if (innerList.contains(userID)) return innerList;
        }
        return null;
    }

    /** @brief Retorna el rating de un cierto usuario a un cierto item, -1.0 si no lo había valorado
     *
     * \pre \a userID representa a un usuario válido contenido en la base de datos de \ratings que hay en el sistema e itemID representa a un ítem válido
     * @param userID es el ID del usuario del que queremos saber su nota para un item
     * @param itemID representa ese item
     * \post Retorna el rating de un cierto usuario a un cierto item, -1.0 si no lo había valorado
     */
    private float RatingOfOneUserToAnItem(int userID, int itemID) {
        ArrayList<Rating> itemsRatedByOneUser = ratingsMap.get(userID);
        if(itemsRatedByOneUser == null) System.out.println("ALERT: EFECTIVAMENTE RETORNA NULL");
        for (Rating rating: itemsRatedByOneUser) {
            if (rating.GetItemID() == itemID) return rating.GetRating();
        }
        return -1.0f;
    }

    /** @brief Retorna un booleano diciendo si un cierto item fue valorado por el usuario al cual estamos intentando predecir sus gustos.
     *
     * \pre \a item es un ítem válido y \a itemsAndScoreOfMainUser es la lista de valoraciones del usuario a quien estamos intentando predecir sus gustos
     * @param item es el item que nos interesa saber si fue valorado
     * @param itemsAndScoreOfMainUser es el vector de valoraciones en el que queremos ver si existe ese item
     * \post Retorna \a true si \a item está presente en \a itemsAndScoreOfMainUser. Retorna \a false en caso contrario.
     */
    private boolean WasThatItemRatedByMainUser(Item item, ArrayList<Rating> itemsAndScoreOfMainUser){
        // nota: recuerda que la clase item contiene el itemID y todos los atributos del item mientras que ItemAndScore contiene el itemID a secas y su score.
        for(Rating itemRated: itemsAndScoreOfMainUser){
            if(item.GetItemId() == itemRated.GetItemID()) return true;
        }
        return false;
    }

    /** @brief Hace las predicciones para el usuario principal (identificado por \a mainUserID) a través del algoritmo SlopeOne.
     *
     * \pre El vector de \a SimilarUsersID no contiene el \a mainUserID, el cual es un ID válido. El vector \a itemsRatedByMainUser contiene todas las valoraciones que ha realizado el usuario sobre el que vamos a intentar predecir. \a numberOfRecommendations > 0
     * @param SimilarUsersID son los ID de los usuarios similares al mainUser
     * @param mainUserID es el ID del mainUser (que es el usuario para el que estamos prediciendo valoraciones)
     * @param numberOfRecommendations es el numero de recomendaciones que nos interesa hacerle a mainUser
     * @param itemsRatedByMainUser son las valoraciones de mainUser
     * \post Retorna \a numberOfRecommendations recomendaciones ordenadas por valoracion decreciente. Si \a numberOfRecommendations es mayor a la cantidad de ítems en el sistema sobre los cuales se pueden hacer predicciones, retornará ese número de predicciones.
     */
    private ArrayList<Rating> SlopeOne(ArrayList<Integer> SimilarUsersID, int mainUserID, int numberOfRecommendations, ArrayList<Rating> itemsRatedByMainUser) {

        int numberOfItemsRatedByMainUser = itemsRatedByMainUser.size();

        // rellenamos la matriz ratingsOfSimilarUsersToRatedItemsOfMainUser:
        int numberOfSimilarUsers = SimilarUsersID.size();
        ArrayList<ArrayList<Float>> ratingsOfSimilarUsersToRatedItemsOfMainUser = new ArrayList<ArrayList<Float>>(); //SIZE FINAL= numberOfSimilarUsers
        for (int i = 0; i < numberOfSimilarUsers; i++) {
            ArrayList<Float> ratingOfOneUserToTheItemsRatedByMainUser = new ArrayList<Float>(); //SIZE FINAL= numberOfItemsRatedByMainUser
            int userID = SimilarUsersID.get(i);
            // bucle (var induccion j) sobre las pelis vistas por el mainUser. Si esa peli tambien la ha visto el user "i", pongo en la posicion j del vector
            // RatingOfOneUserToTheItemsRatedByMainUser la nota que le habia dado el user "i". Si no, pongo en esa posición un -1:
            for(int j = 0; j < numberOfItemsRatedByMainUser; j++) {
                // vemos la nota que le había puesto un usuario afin al mainuser a uno de los items (j) valorados por mainUser (-1.0 si no hay valoracion)
                float rating = RatingOfOneUserToAnItem(userID, itemsRatedByMainUser.get(j).GetItemID());
                // y eso lo ponemos en el vector de valoraciones de un cierto user (i) a las pelis vistas por el mainUser:
                ratingOfOneUserToTheItemsRatedByMainUser.add(rating);
            }
            // cuando tenemos correcto el vector de valoraciones de un cierto user (i) a las pelis vistas por el mainUser, lo ponemos en el vector de vectores donde cada posicion representaba A UN CIERTO USER AFIN (donde la info que guardamos de ese user afin son sus ratings a las pelis vistas por el main user)
            ratingsOfSimilarUsersToRatedItemsOfMainUser.add(ratingOfOneUserToTheItemsRatedByMainUser);
        }

        // vector con predicciones (o sea, vector de los UnratedItems). Contiene todos los items que NO ha valorado el user con la nota predicha (-1 para aquellos que o aún no contienen nada válido o que no se ha podido predecir porque por ejemplo ningun usuario mas del cluster lo ha valorado:
        // ESTE VECTOR PODRIA HABER SIDO LLAMADO itemsUnratedByMainUser !!!!!!!
        ArrayList<Rating> predictions = new ArrayList<Rating>(); // EQUIVALE A ItemsUnratedByMainUser. Solo que su valoracion es -1 cuando actua como ItemsUnrated o es un cierto real entre 0 y 5 cuando actua de prediccion
        ArrayList<Item> itemsInTheSystem = DataManager.GetInstance().GetAllItems();
        int numberOfItemsInTheSystem = itemsInTheSystem.size();
        for (Item item : itemsInTheSystem){
            if(!WasThatItemRatedByMainUser(item, itemsRatedByMainUser)){
                Rating itemToPredictScore = new Rating(mainUserID, item.GetItemId(), -1.0f);
                predictions.add(itemToPredictScore);
            }
        }

        // rellenamos la matriz ratingsOfSimilarUsersToUnratedItemsOfMainUser:
        int numberItemsToPredict = predictions.size();
        ArrayList<ArrayList<Float>> ratingsOfSimilarUsersToUnratedItemsOfMainUser = new ArrayList<ArrayList<Float>>(); //SIZE FINAL=numberItemsToPredict
        for(int i = 0; i < numberOfSimilarUsers; i++) {
            ArrayList<Float> ratingOfOneUserToTheItemsUnratedByMainUser = new ArrayList<Float>(); //SIZE FINAL= numberItemsToPredict
            int userID = SimilarUsersID.get(i);
            for(int j = 0; j < numberItemsToPredict; j++) {
                float rating = RatingOfOneUserToAnItem(userID, predictions.get(j).GetItemID()); //-1.0 si no hay rating. Y RECUERDA QUE EL VECTOR PREDICTIONS CONTIENE LOS ItemsUnratedByMainUser
                ratingOfOneUserToTheItemsUnratedByMainUser.add(rating);
            }
            ratingsOfSimilarUsersToUnratedItemsOfMainUser.add(ratingOfOneUserToTheItemsUnratedByMainUser);
        }


        if(numberOfItemsInTheSystem != numberItemsToPredict + numberOfItemsRatedByMainUser) System.out.println("ALERT: numberOfItemsInTheSystem != numberItemsToPredict + numberOfItemsRatedByMainUser, y debería ser igual. En concreto numberOfItemsInTheSystem = " + numberOfItemsInTheSystem + ", numberItemsToPredict = " + numberItemsToPredict + ", numberOfItemsRatedByMainUser = " + numberOfItemsRatedByMainUser);

        // En este punto tenemos las matrices de ratingsOfSimilarUsersToRATEDItemsOfMainUser y ratingsOfSimilarUsersToUNRATEDItemsOfMainUser, con las notas de cada uno de esos users a cada uno de esos items.
        // La gracia está en que los users representados por cada fila en esas matrices son el mismo en uno y otro.


        //para cada uno de los items a predecir vemos sus desviaciones con los demas items que habia valorado el user. Y con ello podemos calcular las predicciones:
        for (int i = 0; i < numberItemsToPredict; i++) {
            ArrayList<Float> ratingJplusDeviationIandJ = new ArrayList<Float>(); //Almacena esto: lo siguiente: rating main user sobre item j + desviacion entre i y j (pero solamente si esa desviacion era calculable. Si no, no se ocupara ninguna posicion de este vector). Por ende, la cardinalidad de este vector será <= numberofitemsratedbymainuser
            // Calculamos la desviacion del item a predecir (i) con los otros items (j) y almacenamos en el cada posicion del vector ratingJplusDeviationIandJ, que en la linea de arriba está explicado lo que almacena.
            for (int j = 0; j < numberOfItemsRatedByMainUser; j++) {
                // Ahora, para la desviacion de un cierto item a predecir (i) con otro cierto item (j) de los usados para calcular la desviacion (que son los que habia valorado el user) debemos ver la resta de las valoraciones de los users entre un item y otro. Y así luego calcular la media para tener la desviacion entre ambos items:
                int numberOfUsersRatingItemIandItemJ = 0;
                float sumOfDifferences = 0;
                //Para TODOS los similarUsers que hayan rateado I y J tengo que hacer la resta de esos ratings (ratingI-ratingJ) e irlo sumando a la variable Sum esa e ir haciendo number++.
                for (int k = 0; k < numberOfSimilarUsers; k++){
                    float ratingOfUserKToItemI = ratingsOfSimilarUsersToUnratedItemsOfMainUser.get(k).get(i);
                    float ratingOfUserKToItemJ = ratingsOfSimilarUsersToRatedItemsOfMainUser.get(k).get(j);
                    // item i es uno de los items a predecir e item j es uno de los items que ya habia valorado el mainuser.
                    if(ratingOfUserKToItemI != -1.0f && ratingOfUserKToItemJ != -1.0f) {
                        sumOfDifferences += (ratingOfUserKToItemI - ratingOfUserKToItemJ);
                        numberOfUsersRatingItemIandItemJ++;
                    }
                }
                //Con los datos recogidos, calculo desviacion entre item i e item j:
                float mediaDesviacionesDeLosUsersParaIyJ;
                if (numberOfUsersRatingItemIandItemJ != 0) mediaDesviacionesDeLosUsersParaIyJ = sumOfDifferences / (float)numberOfUsersRatingItemIandItemJ;
                else mediaDesviacionesDeLosUsersParaIyJ = Float.MIN_VALUE; // si la media de las diferencias vale Float.MIN_VALUE es que no existe ningun SimilarUser que haya valorado tanto el item I a predecir como el J.

                //sumo la nota de mainUser sobre el item j y la desviacion entre el item a predecir i y el item j, y todos esos resultados los almaceno en el vector ratingJplusDeviationIandJ:
                if (mediaDesviacionesDeLosUsersParaIyJ != Float.MIN_VALUE) {
                    ratingJplusDeviationIandJ.add(itemsRatedByMainUser.get(j).GetRating() + mediaDesviacionesDeLosUsersParaIyJ);
                }
            }

            // Calculamos el valor predicho de i en base a hacer SUMATORIO(valoracion main user a item j + desviacion entre i y j) / num elementos validos en el sumatorio de arriba. (si la desviacion entre i y j es MIN_VALUE, eso no participara en el sumatorio, porque significa que no se ha podido obtener un dato bueno de esa desviacion).
            // Ese sumatorio lo haremos sumando los elementos del vector ratingJplusDeviationIandJ, y el denominador sera su cardinalidad
            float predictedRatingI = 0.0f;
            if(ratingJplusDeviationIandJ.size() == 0) {
                predictedRatingI = Float.MIN_VALUE; // No podemos predecir el item I porque no habia ningun usuario que hubiese votado tanto el item I como alguno de los items que habia votado el mainUser
                //System.out.println("DEBUG (esto puede ocurrir): Tenemos una prediccion imposible de realizar");
            }
            else {
                float sumaDePrediccionesDeIenBaseAUnJ = 0.0f;
                for(Float prediccionDeIenBaseAUnJ : ratingJplusDeviationIandJ){
                    sumaDePrediccionesDeIenBaseAUnJ += prediccionDeIenBaseAUnJ;
                }
                predictedRatingI = sumaDePrediccionesDeIenBaseAUnJ / ratingJplusDeviationIandJ.size();
            }

        /*
            if(predictedRatingI > 100.f) System.out.println("ALERT: Ojo que un valor predicho supera el valor 100 (cuando no deberia superar mucho el 5)...");
            if(predictedRatingI > maxRatingValue) predictedRatingI = maxRatingValue;
            else if (predictedRatingI < 0.0f && predictedRatingI > Float.MIN_VALUE){ //antes de realizarse este if, predictedRatingI podia valer MIN_FLOAT si era imposible de predecir o nevativa a secas si el valor predicho era negativo (o sea, una basura)
                predictedRatingI = 0.0f;
                System.out.println("DEBUG (esto puede ocurrir): La prediccion se ha podido realizar pero es una prediccion tan mala que vale negativa");
            }
        */

            //las predicciones al vector de predicciones:

            Rating predictionItemI = predictions.get(i);
            predictionItemI.SetRating(predictedRatingI);
            predictions.set(i, predictionItemI);
            //NOTA: Las columnas de la matriz ratingsOfSimilarUsersToUnratedItemsOfMainUser y del vector predictions representan los mismos itemsID!

        }

        // Ordenamos las predicciones DE FORMA DECRECIENTE: (las imposibles de predecir valen MIN_FLOAT)
        predictions.sort(new Comparator<Rating>() {
            @Override
            public int compare(Rating o1, Rating o2) {
                return Float.valueOf(o2.GetRating()).compareTo(Float.valueOf(o1.GetRating()));
            }
        });

        // coger las predicciones que seran recomendaciones. Eso seran las numberOfPredictions del vector "predicciones" mas altas, salvo que ese numero sea menor al numero de predicciones distintas a -1.0 que entonces retornaria las distinas a -1 y ya:
        ArrayList<Rating> recommendation = new ArrayList<Rating>();
        numberOfRecommendations = Math.min(numberOfRecommendations, predictions.size());
        for(int i = 0; i < numberOfRecommendations; i++){
            Rating recommendationI = predictions.get(i);
            // Hemos podido predecir por encima del tope o por debajo el minimo, así que en ese caso ponemos esos valores como toca:
            if(recommendationI.GetRating() > maxRatingValue*10.0f) System.out.println("ALERT: Ojo que un valor predicho supera por mucho al maximo valor de prediccion que debemos dar...");
            if(recommendationI.GetRating() > maxRatingValue) recommendationI.SetRating(maxRatingValue);
            else if (recommendationI.GetRating() < 0.0f && recommendationI.GetRating() > Float.MIN_VALUE){ //antes de realizarse este if, predictedRatingI podia valer MIN_FLOAT si era imposible de predecir o negativa a secas si el valor predicho era negativo (o sea, una basura)
                recommendationI.SetRating(0.0f);
                System.out.println("DEBUG (esto puede ocurrir): La prediccion se ha podido realizar pero es una prediccion tan mala que vale negativa");
            }
            // en este punto las notas de las prediccioines van entre 0.0 y el valor max de nota salvo para las que no se ha podido predecir, que vale MIN_FLOAT
            // IMPORTANTE: La segunda condicion del if es completamente necesaria porque no sé por qué si vale MIN_FLOAT lo interpreta como >=0.0f
            if (recommendationI.GetRating() >= 0.0f && recommendationI.GetRating() != Float.MIN_VALUE) { //IMPORTANTE: Nota que la primera condicion del if es inútil, pero si en el futuro quiero decir (lo que esté por debajo del 2.0 no lo recomiendes) pues sí que me servirá (y cambiaría 0.0 por 2.0).
                recommendation.add(recommendationI);
            }
            else break; // va ordenado de mayor a menor. En el momento en que llegamos al primer valor de prediccion a partir del cual ya no lo queremos contar como recomendacion, paramos. (Si admitimos hasta el 0.0, pararemos en el primer elemento NO PREDECIBLE (valor MIN_FLOAT) que nos encontremos)
        }

        return recommendation; //el subset de las "numberOfRecomendations" predicciones con nota mas alta
    }

    // IMPORTANTE: Es completamente necesario pasar ese userID como Integer y no como int, ya que en el .remove(userID) interpretara ese userID como indice si lo hemos pasado como int o lo interpretara como contenido si lo hemos pasado como Integer
    /** @brief Función recomendadora que hará la recomendación a \ExistantUserIDRecommend (un usuario que existiese previamente en el dataset que se nos dio en la asignatura) en base a Kmeans+SlopeOne.
     *
     * \pre \a ExistantUserIDRecommend tiene que ser un usuario que se encuentre en el dataset que se nos proporcionó en la asignatura, es decir, no puede ser un usuario que hayamos creado a partir de nuestra interfaz grafica aparte, debe ser un Integer, no un int. \a numberOfRecommendations > 0
     * @param ExistantUserIDRecommend es el ID del usuario al que queremos recomendar
     * @param numberOfRecommendations es el número de recomendaciones que queremos hacer
     * \post Retorna \a numberOfRecommendations recomendaciones ordenadas por valoración decreciente. Si \a numberOfRecommendations es mayor a la cantidad de ítems en el sistema sobre los cuales se pueden hacer predicciones, retornará ese número de predicciones.
     */
    public ArrayList<Rating> Recommend(Integer ExistantUserIDRecommend, int numberOfRecommendations) throws NoExistingUserIDException {
        ArrayList<Rating> recommendation = new ArrayList<Rating>();
        if(KgroupsEmpty){ // Es extremadamente probable (almenos en la 1a entrega) que nos den un set de users con sus valoraciones y simplemente pidan recomendaciones
            // para varios users. Por tanto, seria una tonteria computar el Kmeans de nuevo en cada una de esas peticiones si no cambian ni los users ni los ratings.
            Kmeans();
        }
        /* TODO: el if de arriba lo tengo que cambiar por algo de este estilo:
        Kgroups = data manager get kgroups con k=10
        if(kgroups es null) {
            Kmeans(); //esto ya pide dentro ratingsmap y computa kgroups y centroids
            mandarle Kgroups y centroids a data manager;
        }
        else {
            pedir centroids y ratingsmap a datamanager;
        }
         */
        ArrayList<Integer> usersIDofSimilarUsers = UsersIDofSimilarUsers(ExistantUserIDRecommend);
        if (usersIDofSimilarUsers == null) throw new NoExistingUserIDException();
        usersIDofSimilarUsers.remove(ExistantUserIDRecommend);
        recommendation = SlopeOne(usersIDofSimilarUsers, ExistantUserIDRecommend, numberOfRecommendations, ratingsMap.get(ExistantUserIDRecommend));

        //OJOOOO: El array de usersIDofSimilarUsers usalo de solo lectura, que si no realmente estaras modificando un grupo del Kgroups (recuerda que java funciona por referencias). Aunque creo que realmente no lo necesitas modificar para nada

        return recommendation;
    }

    /** @brief Función recomendadora que hará la recomendación a un usuario que hemos creado a través de la interfaz gráfica (en lugar de ser un user preexistente en el dataset que nos dieron en la asignatura) en base a Kmeans+SlopeOne.
     *
     * \pre \a newUserID tiene que ser un usuario que NO se encuentre en la base de datos de ratings que se nos entregó como dataset en la asignatura; aparte, debe ser un Integer, no un int. \a numberOfRecommendations > 0. \a ratingsOfThatUser son los ratings de ese usuario nuevo.
     * @param newUserID es el ID del usuario al que queremos recomendar
     * @param ratingsOfThatUser son las valoraciones conocidas de ese usuario
     * @param numberOfRecommendations es el numero de recomendaciones que queremos hacer
     * \post Retorna \a numberOfRecommendations recomendaciones ordenadas por valoracion decreciente. Si \a numberOfRecommendations es mayor a la cantidad de ítems en el sistema sobre los cuales se pueden hacer predicciones, retornará ese número de predicciones.
     */
    public ArrayList<Rating> Recommend (Integer newUserID, ArrayList<Rating> ratingsOfThatUser, int numberOfRecommendations) {
        ArrayList<Rating> recommendation = new ArrayList<Rating>();
        if(KgroupsEmpty){
            Kmeans();
        }

        //Busca el grupo de usuarios más afín al newUserID:
        int indexCloserCentroid = -1;
        double distanceToCloserCentroid = Double.MAX_VALUE;
        for (int i = 0; i < K; i++) {
            double distance = DistanceBetweenUsers(ratingsOfThatUser, centroids.get(i));
            if (distance < distanceToCloserCentroid) {
                distanceToCloserCentroid = distance;
                indexCloserCentroid = i;
            }
        }
        if(indexCloserCentroid == -1) System.out.println("ALERT: NO HE ENCONTRADO EL GRUPO DE USUARIOS MÁS AFIN AL USUARIO");
        ArrayList<Integer> usersIDofSimilarUsers = Kgroups.get(indexCloserCentroid);
        recommendation = SlopeOne(usersIDofSimilarUsers, newUserID, numberOfRecommendations, ratingsOfThatUser);
        return recommendation;
    }

    /** @class FrequencyAndSumOfScores
     *   @brief Clase privada que actua de par de int y float
     *
     *   Se utiliza simplemente como un par de int y float, representando la cantidad de valoraciones a un item y la suma de esas valoraciones, respectivamente, usada simplemente para facilitar la tarea a algunas funciones del CollaborativeFiltering y facilitar la comprensión del código gracias a los nombres de sus atributos.
     */
    private class FrequencyAndSumOfScores{
        protected int frequency;
        protected float scoreSum;
        protected FrequencyAndSumOfScores(int frequency, float scoreSum) {
            this.frequency = frequency;
            this.scoreSum = scoreSum;
        }
    }
}