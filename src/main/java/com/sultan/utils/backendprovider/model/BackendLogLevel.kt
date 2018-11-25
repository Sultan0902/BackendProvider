package com.sultan.utils.backendprovider.model

enum class BackendLogLevel {
    /** No logs.  */
    NONE,
    /**
     * Logs request and response lines.
     *
     *
     * Example:
     * <pre>`--> POST /greeting http/1.1 (3-byte body)
     *
     * <-- 200 OK (22ms, 6-byte body)
    `</pre> *
     */
    BASIC,
    /**
     * Logs request and response lines and their respective headers.
     *
     *
     * Example:
     * <pre>`--> POST /greeting http/1.1
     * Host: example.com
     * Content-Type: plain/text
     * Content-Length: 3
     * --> END POST
     *
     * <-- 200 OK (22ms)
     * Content-Type: plain/text
     * Content-Length: 6
     * <-- END HTTP
    `</pre> *
     */
    HEADERS,
    /**
     * Logs request and response lines and their respective headers and bodies (if present).
     *
     *
     * Example:
     * <pre>`--> POST /greeting http/1.1
     * Host: example.com
     * Content-Type: plain/text
     * Content-Length: 3
     *
     * Hi?
     * --> END POST
     *
     * <-- 200 OK (22ms)
     * Content-Type: plain/text
     * Content-Length: 6
     *
     * Hello!
     * <-- END HTTP
    `</pre> *
     */
    BODY
}
