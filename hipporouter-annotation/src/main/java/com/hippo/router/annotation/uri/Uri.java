package com.hippo.router.annotation.uri;

import java.net.MalformedURLException;

/**
 * Created by Kevin on 2016/12/29.
 */

public abstract class Uri {
    private Uri() {}

    /**
     * Gets the scheme of this URI. Example: "http"
     *
     * @return the scheme or null if this is a relative URI
     */
    public abstract String getScheme();

    /**
     * Gets the encoded host from the authority for this URI. For example,
     * if the authority is "bob@google.com", this method will return
     * "google.com".
     *
     * @return the host for this URI or null if not present
     */
    public abstract String getHost();

    /**
     * Gets the decoded path.
     *
     * @return the decoded path, or null if this is not a hierarchical URI
     * (like "mailto:nobody@google.com") or the URI is invalid
     */
    public abstract String getPath();

    /**
     * Gets the decoded query component from this URI. The query comes after
     * the query separator ('?') and before the fragment separator ('#'). This
     * method would return "q=android" for
     * "http://www.google.com/search?q=android".
     *
     * @return the decoded query or null if there isn't one
     */
    public abstract String getQuery();


    /**
     * Creates a Uri which parses the given encoded URI string.
     *
     * @param uriString an RFC 2396-compliant, encoded URI
     * @throws NullPointerException if uriString is null
     * @return Uri for this given uri string
     */
    public static Uri parse(String uriString) throws MalformedURLException {
        return new StringUri(uriString);
    }

    private static class StringUri extends Uri {
        /**
         * URI string representation.
         */
        private final String uriString;

        protected String uri;
        protected String scheme;
        protected String host = null;
        protected int port = -1;
        protected boolean hasAuthority;
        protected String path;
        protected String query = null;

        private StringUri(String uriString) throws MalformedURLException {
            if (uriString == null) {
                throw new NullPointerException("uriString");
            }

            this.uriString = uriString;
            init(uriString);
        }

        @Override
        public String getScheme() {
            return scheme;
        }

        @Override
        public String getHost() {
            return host;
        }

        @Override
        public String getPath() {
            return path;
        }

        @Override
        public String getQuery() {
            return query;
        }

        private void init(String var1) throws MalformedURLException {
            int var2 = var1.indexOf(58);
            if (var2 < 0) {
                throw new MalformedURLException("Invalid URI: " + var1);
            } else {
                this.scheme = var1.substring(0, var2);
                ++var2;
                this.hasAuthority = var1.startsWith("//", var2);
                int var3;
                if (this.hasAuthority) {
                    var2 += 2;
                    var3 = var1.indexOf(47, var2);
                    if (var3 < 0) {
                        var3 = var1.length();
                    }

                    int var4;
                    if (var1.startsWith("[", var2)) {
                        var4 = var1.indexOf(93, var2 + 1);
                        if (var4 < 0 || var4 > var3) {
                            throw new MalformedURLException("Invalid URI: " + var1);
                        }

                        this.host = var1.substring(var2, var4 + 1);
                        var2 = var4 + 1;
                    } else {
                        var4 = var1.indexOf(58, var2);
                        int var5 = var4 >= 0 && var4 <= var3 ? var4 : var3;
                        if (var2 < var5) {
                            this.host = var1.substring(var2, var5);
                        }

                        var2 = var5;
                    }

                    if (var2 + 1 < var3 && var1.startsWith(":", var2)) {
                        ++var2;
                        this.port = Integer.parseInt(var1.substring(var2, var3));
                    }

                    var2 = var3;
                }

                var3 = var1.indexOf(63, var2);
                if (var3 < 0) {
                    this.path = var1.substring(var2);
                } else {
                    this.path = var1.substring(var2, var3);
                    this.query = var1.substring(var3);
                }

            }
        }
    }
}
