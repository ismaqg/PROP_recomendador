/** @file DataAlgorithm.java
 *  @brief Contiene la clase DataAlgorithm
 */

package fxsrc.propyecto.data;

import fxsrc.propyecto.domain.ParserCSV;
import fxsrc.propyecto.domain.Rating;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

/** \class DataAlgorithm
 *   \brief Clase que contiene los datos de los cálculos del algoritmo KMeans.
 *
 *   Ofrece operaciones para que el DataManager pueda acceder a información precargada del algoritmo KMeans y para poder guardar una nueva configuración de éste.
 */
public class DataAlgorithm extends Data {

    /** \brief Correpsonde a los kGroups del algoritmo KMeans
     */

    private ArrayList<ArrayList<Integer>> dataKgroups;
    /** \brief Correpsonde a los Centroids del algoritmo KMeans
     */

    private ArrayList<ArrayList<Rating>> centroids;

    /** \brief Correpsonde a la k del algoritmo KMeans
     */
    private int k;

    /** \brief Correpsonde al dataset del algoritmo KMeans
     */
    private int dataset;

    /** \brief Indica si el dataset es correcto o no
     */
    private boolean isDirty;

    /** \brief Función que devuelve si el dataset es correcto o no
     *
     * \pre <em>Cierto</em>
     * \post Devuelve si el dataset cargado es correcto o no
     */
    public boolean IsDirty() {
        return isDirty;
    }

    /** \brief Función que asigna si el dataset es correcto o no
     *
     * \param boolean dirty Corresponde a si el dataset cargado es correcto o no
     * \pre <em>Cierto</em>
     * \post Asigna si el dataset es correcto o no
     */
    public void SetDirty(boolean dirty) {
        isDirty = dirty;
    }



    /** \brief Función para inicializar la información del algoritmo KMeans
     *
     * \pre <em>Cierto</em>
     * \post Se incializa \a dataKgroups y \a centroids y se pone a 0 \a dataset y \a k
     */
    @Override
    public void Init() {
        dataKgroups = new ArrayList<>();
        centroids = new ArrayList<>();
        dataset = 0;
        k = 0;
    }

    /** \brief Función para inicializar los conjuntos de datos.
     *
     * \pre \a dataKgroups no es null.
     * \pre \a centroids no es null.
     * \post Se resetea los conjuntos de datos y se ponen a 0 \a dataset y \a k
     */
    @Override
    public void ClearData() {
        dataKgroups.clear();
        centroids.clear();
        dataset = 0;
        k = 0;
    }

    /** \brief Parsea una linea CSV a KGroup
     * \param String[] attributesNames Corresponde al nombre de los atributos
     * \param String lineRead Corresponde a la linea CSV que hace referencia al KGroup deseado
     * \pre \a attributesNames tiene el tamaño exacto de parametros que tenga el Item y \a lineRead no es vacío.
     * \pre Ningun conjunto es null
     * \post Se parsean los atributos de KGroup y finalmente se añade al conjunto.
     */
    @Override
    public void ParseAndAddData(String[] attributesNames, String lineRead) {
        dataKgroups.add(ParserCSV.GetInstance().ParseKGroup(lineRead));
    }

    /** \brief Funcion que devuelve si hay una línea más por guardar
     * \pre systemUsers no es null
     * \post Devuelve si hay alguna línea más por guardar
     */
    @Override
    protected boolean HasNextLine() {
        return currentLine < dataKgroups.size();
    }

    /** \brief Funcion que devuelve la siguiente línea a guardar
     * \pre usersArray no es null
     * \post Devuelve la siguiente línea a guardar
     */
    @Override
    protected String GetNextLine() {
        String temp = ParserCSV.GetInstance().ParseKGroupToCSV(dataKgroups.get(currentLine));
        currentLine++;
        return temp;
    }

    /** \brief Función que modifica los datos del algoritmo de KMeans a guardar
     *
     * \param int newK Corresponde a la nueva K
     * \param int newDataset Corresponde al nuevo dataset
     * \param ArrayList<ArrayList<Intger>> kmeans Corresponde al nuevo kmeans
     * \param ArrayList<ArrayList<Rating>> cent Corresponde a los nuevos centroids
     * \post Función que modifica los datos del algoritmo de KMeans a guardar y después los guarda en formato CSV
     */
    public void SetKGroups(int newK, int newDataset, ArrayList<ArrayList<Integer>> kmeans, ArrayList<ArrayList<Rating>> cent) {
        k = newK;
        dataset = newDataset;
        attributesNames = "" + k + "," + newDataset;
        dataKgroups = kmeans;
        centroids = cent;
        filePath = "res/algorithm/kmeans.csv";
        isDirty = false;
        StoreCSV();
    }

    /** \brief Función que devuelve los KGroup guardados si los parametros corresponden con la inforación almacenada
     * \param int newK correpsonde a la k del algoritmo KMeans deseado
     * \param int newDataset correpsonde al dataset del algoritmo KMeans deseado
     * \pre Cierto
     * \post Devuelve los KGroup guardados si los paramtros corresponden con la información almacenada, sino devuelve null
     */
    public ArrayList<ArrayList<Integer>> GetKGroups(int newK, int newDataset) {
        if(isDirty) {
            return null;
        }
        if(k == newK && dataset == newDataset) {
            return dataKgroups;
        }

        LoadCSV("res/algorithm/kmeans.csv");
        if(dataKgroups.isEmpty()) { //el fichero no existe
            return null;
        }

        String temp = "" + newK + "," + newDataset;
        if(attributesNames.equals(temp)) {
            return dataKgroups;
        }
        else {
            return null; //el fichero no coincide con el kmeans que nos han pedido
        }

    }

    /** \brief Función que devuelve los centroides guardados
     * \pre Cierto
     * \post Devuelve los centroides guardados.
     */
    public ArrayList<ArrayList<Rating>> GetCentroids() {
        return centroids;
    }

    /** \brief Lee un fichero CSV y trata sus campos para parsearlo y convertirlo en datos usables para el programa
     * \param String path Corresponde al path del fichero que se desea leer
     * \pre <em>Cierto</em>
     * \post Carga el fichero CSV y los parsea a los tipos de datos que se deseen (a saber en ParseAndAddData)
     */
    @Override
    public void LoadCSV(String path) {
        try { //fichero de kgroup
            File tempFile = new File(path);
            Scanner myReader = new Scanner(tempFile);
            ClearData();
            filePath = path;
            //leo la primera linea sin usarla ya que no me interesa al ser el nombre de los campos
            String line = myReader.nextLine();
            attributesNames = line;
            String[] attributesNames = line.split(",");
            k = Integer.parseInt(attributesNames[0]);
            dataset = Integer.parseInt(attributesNames[1]);

            line = myReader.nextLine();
            centroids = ParserCSV.GetInstance().ParseCentroids(line);
            while (myReader.hasNextLine()) {
                line = ReadNextLineCSV(myReader);
                ParseAndAddData(attributesNames, line);
            }
            myReader.close();
        } catch (FileNotFoundException e) {

        }
    }

    /** \brief Guarda toda la información en un fichero CSV con nombre \a path
     * \param String path Corresponde al path del fichero que se desea guardar
     * \pre <em>Cierto</em>
     * \post Parsea la información a CSV y lo guarda en el fichero CSV con nombre \a path borrando el contenido que tenía antes
     */
    @Override
    public void StoreCSV(String path) {
        try {
            currentLine = 0;
            FileWriter fw = new FileWriter(path, false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(attributesNames);
            bw.newLine();
            bw.write(ParserCSV.GetInstance().ParseCentroidsToCSV(centroids));
            bw.newLine();
            while (HasNextLine()) {
                bw.write(GetNextLine());
                bw.newLine();
            }
            bw.close();
        }
        catch (Exception e) {

        }
    }
}