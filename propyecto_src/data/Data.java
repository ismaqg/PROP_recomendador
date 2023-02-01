/** @file Data.java
 *  @brief Contiene la clase Data
 */

package fxsrc.propyecto.data;

import fxsrc.propyecto.domain.Item;
import fxsrc.propyecto.domain.ParserCSV;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

/** \class Data
 *   \brief Clase abstracta para la gestión de los datos en la Capa de Datos
 *
 *   Ofrece una base para todas las clases de la capa de datos para cargar y guardar sus datos en formato CSV en los ficheros.
 */

public abstract class Data {

    /** \brief Path del fichero
     */
    protected String filePath;

    /** \brief Línea actual de lectura
     */
    protected int currentLine;

    /** \brief Nombre de los atributos
     */
    protected String attributesNames;

    /** \brief Constructor por defecto de Data
     *
     * \pre <em>Cierto</em>
     * \post Crea una instancia de Data y Llama a la función Init para inicializar la informacion de sus clases hijas
     */
    Data(){
        Init();
    }

    /** \brief Función abstracta para inicializar la subclase
     *
     * \pre <em>Cierto</em>
     * \post Hay que redifinarla en las subclases
     */
    public abstract void Init();

    /** \brief Función abstracta para resetear la información de la subclase
     *
     * \pre <em>Cierto</em>
     * \post Hay que redefinirla en las subclases
     */
    public abstract void ClearData();

    /** \brief Funión abstracta que parsea una linea CSV
     * \param String[] attributesNames Corresponde al nombre de los atributos
     * \param String lineRead Corresponde a la linea CSV que hace referencia a los campos del dato deseado
     * \pre \a attributesNames tiene el tamaño exacto de parametros que tenga el Item y \a lineRead no es vacío.
     * \post Hay que redefinirla en las subclases
     */
    public abstract void ParseAndAddData(String attributesNames[], String lineRead);

    /** \brief Lee un fichero CSV y trata sus campos para parsearlo y convertirlo en datos usables para el programa
     * \param String path Corresponde al path del fichero que se desea leer
     * \pre <em>Cierto</em>
     * \post Carga el fichero CSV y los parsea a los tipos de datos que se deseen (a saber en ParseAndAddData)
     */
    public void LoadCSV(String path) {
        try {
            File tempFile = new File(path);
            Scanner myReader = new Scanner(tempFile);
            ClearData();
            filePath = path;
            //leo la primera linea sin usarla ya que no me interesa al ser el nombre de los campos
            String line = myReader.nextLine();
            attributesNames = line;
            String[] attributesNames = line.split(",");
            while (myReader.hasNextLine()) {
                line = ReadNextLineCSV(myReader);
                ParseAndAddData(attributesNames, line);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /** \brief Para obtener la siguiente fila del CSV
     * \param Scanner reader Corresponde al Scanner del fichero que estamos leyendo
     * \pre \a reader no es null
     * \post Lee y devuelve la siguiente fila del fichero CSV (no necesariamente la siguiente linea del fichero)
     */
    public String ReadNextLineCSV(Scanner reader) {
        String temp = "";
        boolean found = false;
        boolean isQuote = false;
        while(!found && reader.hasNextLine()) {
            String currentLine = reader.nextLine();

            for(int i = 0; i < currentLine.length(); ++i) {
                if(currentLine.charAt(i) == '"') {
                    isQuote = !isQuote;
                }
            }
            temp += currentLine;
            found = !isQuote;
        }

        return temp;
    }

    /** \brief Funcion abstracta que devuelve si hay una línea más por guardar
     * \pre Cierto
     * \post Devuelve si hay alguna línea más por guardar
     */
    protected abstract boolean HasNextLine();

    /** \brief Funcion abstracta que devuelve la siguiente línea a guardar
     * \pre Cierto
     * \post Devuelve la siguiente línea a guardar
     */
    protected abstract String GetNextLine();


    /** \brief Guarda toda la información en un fichero CSV con el path por defecto
     * \pre <em>Cierto</em>
     * \post Parsea la información a CSV y lo guarda en el fichero CSV del pathh por defecto borrando el contenido que tenía antes
     */
    public void StoreCSV() {
        StoreCSV(filePath);
    }

    /** \brief Guarda toda la información en un fichero CSV con nombre \a path
     * \param String path Corresponde al path del fichero que se desea guardar
     * \pre <em>Cierto</em>
     * \post Parsea la información a CSV y lo guarda en el fichero CSV con nombre \a path borrando el contenido que tenía antes
     */
    public void StoreCSV(String path) {
        try {
            currentLine = 0;
            FileWriter fw = new FileWriter(path, false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(attributesNames);
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
