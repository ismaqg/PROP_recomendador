/**
 * @file AlgorithmTester.java
 * @brief Contiene la clase AlgorithmTester y gestiona I/O desde Terminal.
 */

package fxsrc.propyecto.domain;

import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

/** \class AlgorithmTester
 *   \brief Clase utilizada para testear los algoritmos desde la terminal
 *
 *   Ofrece al usuario distintas herramientras para ejecutar los algoritmos
 *   */
public class AlgorithmTester {

    //MainWindow DEPRECATED: NEW APPLICATION CLASS IS SceneManager

    /**\brief Lee los textos e interpreta el input.
     * \pre Cierto
     * @throws FileNotFoundException
     * \post Se llama al algoritmo escogido por el usuario mediante terminal.
     */
    public static void ReadAndExecuteAlgorithms() throws FileNotFoundException {
        boolean first = true;

        while(true){
            if (first){
                System.out.print("Escribe 0 para salir del programa.\nEscribe 1 si quieres probar CollaborativeFiltering, 2 si quieres probar ContentBasedFiltering o 3 el formato híbrido: ");
            }
            else{
                System.out.println();
                System.out.print("Algoritmo realizado con éxito.\nEscribe 0 para salir del programa.\nEscribe 1 si quieres probar CollaborativeFiltering, 2 si quieres probar ContentBasedFiltering o 3 el formato híbrido: ");
            }
            Scanner sc = new Scanner(System.in).useLocale(Locale.US);
            int choice = -1;
            try {
                choice = sc.nextInt();
            }
            catch (Exception e){}
            if(choice == 1){
                CF();
                if (first) first = false;
            }
            else if (choice == 2){
                CBF();
                if (first) first = false;
            }
            else if (choice == 3){
                Hybrid();
                if (first) first = false;
            }
            else if (choice == 0){
                System.exit(0);
            }
            else{
                System.out.println("Por favor, introduce un valor válido");
                first = true;
            }
        }
    }

    /**\brief Gestiona la llamada al algoritmo CollaborativeFiltering
     * \pre Cierto
     * @throws FileNotFoundException
     * \post Llama al algoritmo y escribe por terminal su resultado.
     */
    private static void CF() throws FileNotFoundException {
        //Scanner sc = new Scanner(new File("res/queries/InputQueriesCF_films.txt")).useLocale(Locale.US);
        //Scanner sc = new Scanner(new File("res/queries/InputQueriesCF_anime250.txt")).useLocale(Locale.US);
        Scanner sc = new Scanner(new File("res/queries/InputQueriesCF_anime750.txt")).useLocale(Locale.US);
        // CODIGO PARA UTILIZAR EL CollaborativeFiltering:
        CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering(5);
        int nQueries = sc.nextInt();
        float sumaDCG = 0;
        float sumaIDCG = 0;
        float sumaNDCG = 0;
        for (int i = 0; i < nQueries; i++) {
            int userid = sc.nextInt();
            int lengthknown = sc.nextInt();
            int lengthunknown = sc.nextInt();

            // KNOWN ratings input:
            ArrayList<Rating> ratingsUser = new ArrayList<Rating>();
            for (int j = 0; j < lengthknown; j++) {
                int itemID = sc.nextInt();
                float rating = sc.nextFloat();
                Rating r = new Rating(userid, itemID, rating);
                ratingsUser.add(r);
            }

            // UNKNOWN items input:
            ArrayList<Integer> itemsIDToRecommend = new ArrayList<Integer>();
            for (int j = 0; j < lengthunknown; j++) {
                itemsIDToRecommend.add(sc.nextInt());
            }

            // ASK FOR RECOMMENDATION:
            ArrayList<Rating> recommendations = collaborativeFiltering.Recommend(Integer.valueOf(userid), ratingsUser, Integer.MAX_VALUE);
            ArrayList<Rating> output1aEntrega = new ArrayList<>();

            // PARA LA 1a ENTREGA SOLO QUEREMOS RECOMENDACIONES PARA LOS ITEMS DE UNKNOWN:
            for (Rating rating : recommendations) {
                //if el item de ese itemandscore coincide con alguno de los itemsIDpara recomendar, imprimo por pantalla el item (y su score, por ver yo que salga bien). Y me fijo en que los Q primeros coincidan
                for (int j = 0; j < lengthunknown; j++) {
                    if (itemsIDToRecommend.get(j) == rating.GetItemID()) {
                        output1aEntrega.add(rating);
                        break;
                    }
                }
            }
            //Para cada user escribir:
            System.out.println("UserID\tRecommendedID\tPredicted Rating\tReal Rating");
            ArrayList<Rating> unknowns = DataManager.GetInstance().GetUserUnknownRatings(userid);
            //OUTPUT:
            for(Rating rating : output1aEntrega) {
                DecimalFormat df = new DecimalFormat("#.##");
                if(rating.GetItemID() > 999){
                    System.out.print(userid + "\t\t" + rating.GetItemID() + "\t\t" + df.format(rating.GetRating()) + "\t\t\t\t");
                }
                else if(rating.GetItemID() > 9999){
                    System.out.print(userid + "\t\t" + rating.GetItemID() + "\t" + df.format(rating.GetRating()) + "\t\t\t\t");
                }
                else{
                    System.out.print(userid + "\t\t" + rating.GetItemID() + "\t\t\t" + df.format(rating.GetRating()) + "\t\t\t\t");
                }
                for(int j = 0; j < unknowns.size(); ++j){
                    if(unknowns.get(j).GetItemID() == rating.GetItemID()){
                        System.out.println(df.format(unknowns.get(j).GetRating()));
                    }
                }
            }
            // convertimos ese vector de Ratings en un vector de Integer (itemID) para poder usar las funciones DCG e IDCG:
            ArrayList<Integer> itemsIDoutput1aEntrega = new ArrayList<Integer>();
            for (Rating ratingOutput1aEntrega : output1aEntrega){
                itemsIDoutput1aEntrega.add(ratingOutput1aEntrega.GetItemID());
            }
            float DCG = collaborativeFiltering.ComputeDCG(userid, itemsIDoutput1aEntrega);
            float IDCG = collaborativeFiltering.ComputeIDCG(userid, itemsIDoutput1aEntrega);
            float NDCG = collaborativeFiltering.ComputeNDCG(DCG, IDCG);
            System.out.println("DCG: " + DCG);
            System.out.println("IDCG: " + IDCG);
            System.out.println("NDCG: " + NDCG);
            sumaDCG += DCG;
            sumaIDCG += IDCG;
            sumaNDCG += NDCG;
            System.out.println("*****************************************************");

        }
        float DCGmedio = sumaDCG / nQueries;
        float IDCGmedio = sumaIDCG / nQueries;
        float NDCGmedio = sumaNDCG / nQueries;
        System.out.println("DCG medio: " + DCGmedio);
        System.out.println("IDCG medio: " + IDCGmedio);
        System.out.println("NDCG medio: " + NDCGmedio);
    }

    /**\brief Gestiona la llamada al algoritmo ContentBasedFiltering
     * \pre Cierto
     * @throws FileNotFoundException
     * \post Llama al algoritmo y escribe por terminal su resultado.
     */
    private static void CBF() throws FileNotFoundException {
        //Scanner sc = new Scanner(new File("res/queries/InputQueriesCBF_films.txt"));
        //Scanner sc = new Scanner(new File("res/queries/InputQueriesCBF_anime250.txt"));
        Scanner sc = new Scanner(new File("res/queries/InputQueriesCBF_anime750.txt"));
        int nUsers = sc.nextInt();
        for (int j = 0; j < nUsers; ++j){

            int userID = sc.nextInt();
            int kValue = sc.nextInt();
            System.out.println();

            ArrayList<Rating> toRate = DataManager.GetInstance().GetUserUnknownRatings(userID);
            for(int i = 0; i < toRate.size(); ++i){
                System.out.println("Parecidos para el usuario " + userID + " del item " + toRate.get(i).GetItemID() + ":");
                ContentBasedFiltering cbf = new ContentBasedFiltering(kValue);
                ArrayList<Pair<Integer, Float>> result = cbf.ComputeKNearest(toRate.get(i).GetItemID());
                cbf.PrintKNearest(result);
                System.out.println("****************************************************");
            }
        }
    }

    /**\brief Gestiona la llamada al algoritmo HybridApproach
     * \pre Cierto
     * @throws FileNotFoundException
     * \post Llama al algoritmo y escribe por terminal su resultado.
     */
    private static void Hybrid() throws FileNotFoundException {
        //Scanner sc = new Scanner(new File("res/queries/InputQueriesCF_films.txt")).useLocale(Locale.US);
        //Scanner sc = new Scanner(new File("res/queries/InputQueriesCF_anime250.txt")).useLocale(Locale.US);
        Scanner sc = new Scanner(new File("res/queries/InputQueriesCF_anime750.txt")).useLocale(Locale.US);
        HybridApproachFiltering hybridApproachFiltering = new HybridApproachFiltering(5);
        int nQueries = sc.nextInt();
        float sumaDCG = 0;
        float sumaIDCG = 0;
        float sumaNDCG = 0;
        for (int i = 0; i < nQueries; i++) {
            int userid = sc.nextInt();
            int lengthknown = sc.nextInt();
            int lengthunknown = sc.nextInt();

            // KNOWN ratings input:
            ArrayList<Rating> ratingsUser = new ArrayList<Rating>();
            for (int j = 0; j < lengthknown; j++) {
                int itemID = sc.nextInt();
                float rating = sc.nextFloat();
                Rating r = new Rating(userid, itemID, rating);
                ratingsUser.add(r);
            }

            // UNKNOWN items input:
            ArrayList<Integer> itemsIDToRecommend = new ArrayList<Integer>();
            for (int j = 0; j < lengthunknown; j++) {
                itemsIDToRecommend.add(sc.nextInt());
            }

            // ASK FOR RECOMMENDATION:
            ArrayList<Integer> recommendations = hybridApproachFiltering.Recommend(Integer.valueOf(userid), ratingsUser, Integer.MAX_VALUE);
            ArrayList<Integer> output1aEntrega = new ArrayList<>();

            // SOLO QUEREMOS RECOMENDACIONES PARA LOS ITEMS DE UNKNOWN:
            for (Integer itemIDRecomendadoPorAlgoritmo : recommendations) {
                for (Integer itemIDToRecommend : itemsIDToRecommend) {
                    if (itemIDToRecommend.compareTo(itemIDRecomendadoPorAlgoritmo) == 0) {
                        output1aEntrega.add(itemIDToRecommend);
                        break;
                    }
                }
            }
            //Para cada user escribir:
            System.out.println("UserID\tRecommendedID");
            ArrayList<Rating> unknowns = DataManager.GetInstance().GetUserUnknownRatings(userid);
            //OUTPUT:
            for(Integer IDrecomendacion : output1aEntrega) {
                System.out.println(userid + "\t\t" + IDrecomendacion);
            }
            float DCG = hybridApproachFiltering.ComputeDCG(userid, output1aEntrega);
            float IDCG = hybridApproachFiltering.ComputeIDCG(userid, output1aEntrega);
            float NDCG = hybridApproachFiltering.ComputeNDCG(DCG, IDCG);
            System.out.println("DCG: " + DCG);
            System.out.println("IDCG: " + IDCG);
            System.out.println("NDCG: " + NDCG);
            sumaDCG += DCG;
            sumaIDCG += IDCG;
            sumaNDCG += NDCG;
            System.out.println("*****************************************************");

        }
        float DCGmedio = sumaDCG / nQueries;
        float IDCGmedio = sumaIDCG / nQueries;
        float NDCGmedio = sumaNDCG / nQueries;
        System.out.println("DCG medio: " + DCGmedio);
        System.out.println("IDCG medio: " + IDCGmedio);
        System.out.println("NDCG medio: " + NDCGmedio);
    }
}
