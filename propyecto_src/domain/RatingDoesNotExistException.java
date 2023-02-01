/** @file RatingDoesNotExistException.java
 *  @brief Contiene la clase RatingDoesNotExistException
 */
package fxsrc.propyecto.domain;

/** @class ExistingActiveUserException
 * @brief Excepción lanzada cuando un Rating no se ha encontrado en la base de datos.
 */
public class RatingDoesNotExistException extends Exception{
    public RatingDoesNotExistException() { super(); }
    public RatingDoesNotExistException(String message) { super(message); }
    public RatingDoesNotExistException(String message, Throwable cause) { super(message, cause); }
    public RatingDoesNotExistException(Throwable cause) { super(cause); }
}

