/** @file NoExistingActiveUserException.java
 *  @brief Contiene la excepción NoExistingActiveUserException
 */

package fxsrc.propyecto.domain;

/** @class NoExistingActiveUserException
 * @brief Excepción lanzada cuando se está ejecutando una función que solo debería ejecutarse existiendo un usuario activo, y no lo hay.
 */
public class NoExistingActiveUserException extends Exception{
    public NoExistingActiveUserException() { super(); }
    public NoExistingActiveUserException(String message) { super(message); }
    public NoExistingActiveUserException(String message, Throwable cause) { super(message, cause); }
    public NoExistingActiveUserException(Throwable cause) { super(cause); }
}
