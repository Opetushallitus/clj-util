(ns clj-log.error-log
    (:require [clojure.tools.logging.impl :as impl]
              [environ.core :refer [env]]
              [slingshot.slingshot :refer [try+]]))

(defmacro with-error-logging-value
          [value & body]
          `(try+
             (do ~@body)
             (catch [:status 500] {:keys [~'trace-redirects]}
               (.error (impl/get-logger (impl/find-factory) *ns*) "HTTP 500 from:" ~'trace-redirects))
             (catch Object ~'_
               (if (Boolean/valueOf (:test ~env))
                 (if (Boolean/valueOf (:verbose ~env))
                   (.error (impl/get-logger (impl/find-factory) *ns*) "Error during test:" (:throwable ~'&throw-context))
                   (.info (impl/get-logger (impl/find-factory) *ns*) "Error during test:" (:message ~'&throw-context)))
                 (.error (impl/get-logger (impl/find-factory) *ns*) "Error:" (:throwable ~'&throw-context)))
               ~value)))

(defmacro with-error-logging
          [& body]
          `(with-error-logging-value nil ~@body))