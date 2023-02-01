/** @file ExistingActiveUserException.java
 *  @brief Contiene la excepción ExistingActiveUserException
 */

package fxsrc.propyecto.domain;

/** @class ExistingActiveUserException
 * @brief Excepción lanzada cuando se está ejecutando una función que solo debería ejecutarse en ausencia de usuario activo, y ya hay uno.
 */
public class ExistingActiveUserException extends Exception {
    public ExistingActiveUserException() { super(); }
    public ExistingActiveUserException(String message) { super(message); }
    public ExistingActiveUserException(String message, Throwable cause) { super(message, cause); }
    public ExistingActiveUserException(Throwable cause) { super(cause); }
}
