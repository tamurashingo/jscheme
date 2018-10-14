package com.github.tamurashingo.jscheme;

public class JSchemeRuntimeException extends RuntimeException {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    public JSchemeRuntimeException() {
        super();
    }

    public JSchemeRuntimeException(String message) {
        super(message);
    }

    public JSchemeRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public JSchemeRuntimeException(Throwable cause) {
        super(cause);
    }
}

