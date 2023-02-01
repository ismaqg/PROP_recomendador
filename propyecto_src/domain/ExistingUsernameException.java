/** @file ExistingUsernameException.java
 *  @brief Contiene la excepción ExistingUsername
 */

package fxsrc.propyecto.domain;

/** @class ExistingUsernameException
 * @brief Excepción lanzada cuando en un intento de cambio de nombre de usuario o de SignUp, ya existe el nombre de usuario que se está intentando utilizar
 */
public class ExistingUsernameException extends Exception {
    public ExistingUsernameException() { super(); }
    public ExistingUsernameException(String message) { super(message); }
    public ExistingUsernameException(String message, Throwable cause) { super(message, cause); }
    public ExistingUsernameException(Throwable cause) { super(cause); }
}
