(ns clj-test-utils.test-utils
    (:import (java.io IOException)
      (java.net Socket)))

(defn- is-free-local-port [port]
       (try
         (let [socket (new Socket "127.0.0.1" port)]
              (.close socket)
              false)
         (catch IOException ioe true)))

(defn find-free-local-port []
      (let [range (range 1024 60000)
            port (nth range (rand-int (count range)))]
           (if (is-free-local-port port)
             port
             (find-free-local-port))))

(defn init-test-logging []
      (intern 'clj-log.error-log 'test true)
      (intern 'clj-log.error-log 'verbose false))