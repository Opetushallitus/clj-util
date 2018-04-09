(ns clj-log.access-log
    (:require [clojure.tools.logging.impl :as impl]
      [clj-log.access-headers :refer :all]
      [clj-time.core :as t]
      [cheshire.core :as c]
      [environ.core :as env]))

(def ^{:private true} logger (impl/get-logger (impl/find-factory) "ACCESS"))

(defn parse-access-headers [service start request response]
      (let [duration (- (System/currentTimeMillis) start)
            method (get-method-from-request request)
            path-info (request :uri)]
           {:user-agent    (user-agent-from-request request)
            :remote-addr   (remote-addr-from-request request)
            :timestamp     (t/now)
            :customer      "OPH"
            :service       service
            :responseCode  (response :status)
            :request       (str method " " path-info)
            :requestMethod method
            :responseTime  (str duration)}))

(defn log-access
      ([service start request response]
        (.info logger (c/generate-string (parse-access-headers service start request response))))
      ([start request response]
        (log-access (:service-name ~env) start request response)))

(defmacro with-access-logging [service request & operations]
          `(let [start# (System/currentTimeMillis)
                 response# ~(cons 'do operations)]
                (log-access ~service start# ~request response#)
                response#))

(defmacro with-access-logging-env [request & operations]
          `(with-access-logging (:service-name ~env) ~request ~@operations))