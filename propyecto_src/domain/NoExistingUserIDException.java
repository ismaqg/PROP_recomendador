/** @file NoExistingUserIDException.java
 *  @brief Contiene la excepción NoExistingUserIDException
 */

package fxsrc.propyecto.domain;

/** @class NoExistingUserIDException
 * @brief Excepción lanzada cuando se está ejecutando una función acerca de un usuario, dado su UserID, y este no existe en el sistema.
 */
public class NoExistingUserIDException extends Exception{
    public NoExistingUserIDException() { super(); }
    public NoExistingUserIDException(String message) { super(message); }
    public NoExistingUserIDException(String message, Throwable cause) { super(message, cause); }
    public NoExistingUserIDException(Throwable cause) { super(cause); }
}
