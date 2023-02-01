/** @file BadCredentialsException.java
 *  @brief Contiene la excepción BadCredentials
 */

package fxsrc.propyecto.domain;

/** @class BadCredentialsException
 * @brief Excepción lanzada cuando los credenciales de acceso de un usuario (nombre de usuario y contraseña) no son correctos
 */
public class BadCredentialsException extends Throwable {
    public BadCredentialsException() { super(); }
    public BadCredentialsException(String message) { super(message); }
    public BadCredentialsException(String message, Throwable cause) { super(message, cause); }
    public BadCredentialsException(Throwable cause) { super(cause); }
}
