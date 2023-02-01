/** @file AttValDoesNotExistException.java
 *  @brief Contiene la clase AttValDoesNotExistException
 */

package fxsrc.propyecto.domain;

/** @class AttributeValueOutOfRange
 * @brief Excepción lanzada cuando se intenta acceder a un valor de atributo inexistente
 */
public class AttValDoesNotExistException extends Throwable {
    public AttValDoesNotExistException() { super(); }
    public AttValDoesNotExistException(String message) { super(message); }
    public AttValDoesNotExistException(String message, Throwable cause) { super(message, cause); }
    public AttValDoesNotExistException(Throwable cause) { super(cause); }
}
