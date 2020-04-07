(ns clj-timbre-auditlog.audit-log
    (:require [cheshire.core :as json]
              [taoensso.timbre :as timbre]
              [environ.core :refer [env]]
              [clojure.data :refer [diff]]
              [taoensso.timbre.appenders.core :refer [println-appender]]
              [taoensso.timbre.appenders.3rd-party.rolling :refer [rolling-appender]])
    (:import [fi.vm.sade.auditlog
              Logger
              Audit
              ApplicationType]
             java.util.TimeZone))

(defn create-audit-logger [service-name base-path ^ApplicationType application-type]
  (let [audit-log-config (assoc timbre/example-config
                                :appenders {:file-appender   (assoc (rolling-appender
                                                                     {:path    (str base-path
                                                                                    "/audit_" service-name
                                                                                    ;; Hostname will differentiate files in actual environments
                                                                                    (when (:hostname env) (str "_" (:hostname env))))
                                                                      :pattern :daily})
                                                                    :output-fn (fn [data] (force (:msg_ data))))
                                            :stdout-appender (assoc (println-appender
                                                                     {:stream :std-out})
                                                                    :output-fn (fn [data]
                                                                                 (json/generate-string
                                                                                  {:eventType "audit"
                                                                                   :timestamp (force (:timestamp_ data))
                                                                                   :event     (json/parse-string (force (:msg_ data)))})))}
                                :timestamp-opts {:pattern  "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
                                                 :timezone (TimeZone/getTimeZone "Europe/Helsinki")})
        logger           (proxy [Logger] [] (log [s]
                                              (timbre/log* audit-log-config :info s)))]
    (new Audit logger service-name application-type)))
