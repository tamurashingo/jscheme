package com.github.tamurashingo.jscheme;

public class JSchemeException extends Exception {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    
    public JSchemeException() {
        super();
    }
    
    public JSchemeException(String message) {
        super(message);
    }
    
    public JSchemeException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public JSchemeException(Throwable cause) {
        super(cause);
    }
}
