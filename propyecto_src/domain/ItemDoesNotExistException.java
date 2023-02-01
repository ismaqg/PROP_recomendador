/** @file ItemDoesNotExistException.java
 *  @brief Contiene la clase ItemDoesNotExistException
 */
package fxsrc.propyecto.domain;

/** @class ItemDoesNotExistException
 * @brief Excepción lanzada cuando un Item no se ha encontrado en la base de datos.
 */
public class ItemDoesNotExistException extends Exception{
    public ItemDoesNotExistException() { super(); }
    public ItemDoesNotExistException(String message) { super(message); }
    public ItemDoesNotExistException(String message, Throwable cause) { super(message, cause); }
    public ItemDoesNotExistException(Throwable cause) { super(cause); }
}


