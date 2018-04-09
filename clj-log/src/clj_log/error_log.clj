(ns clj-log.error-log
    (:require [clojure.tools.logging.impl :as impl]
      [clj-log.access-headers :refer :all]
      [clj-time.core :as t]
      [cheshire.core :as c]
      [environ.core :as env]))

(def ^{:private true} logger (impl/get-logger (impl/find-factory) "error-log"))

(defmacro with-error-logging-value
          [value & body]
          `(try+
             (do ~@body)
             (catch [:status 500] {:keys [~'trace-redirects]}
               (logger/error "HTTP 500 from:" ~'trace-redirects))
             (catch Object ~'_
               (if (Boolean/valueOf (:test ~env))
                 (if (Boolean/valueOf (:verbose ~env)))
                   (logger/error (:throwable ~'&throw-context))
                   (logger/info "Error during test:" (:message ~'&throw-context))
                 (logger/error (:throwable ~'&throw-context)))
               ~value)))

(defmacro with-error-logging
          [& body]
          `(with-error-logging-value nil ~@body))