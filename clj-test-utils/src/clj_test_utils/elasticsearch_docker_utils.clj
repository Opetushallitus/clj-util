(ns clj-test-utils.elasticsearch-docker-utils
  (:require
    [clj-test-utils.port-finder :refer [find-free-local-port]]
    [robert.hooke :refer [add-hook]]
    [clojure.java.shell :refer [sh]]))

(defn- stop-docker-elastic []
  (sh "docker" "kill" "kouta-elastic"))

(defn- elastic-has-started? [elastic-ip]
  (let [response (sh "curl" (str elastic-ip "/_cluster/health"))
        result-code (:exit response)]
    (= result-code 0)))

(defn- wait-elastic-to-start [elastic-ip]
  (loop [tries 30]
    (when (and (> tries 1)
               (not (elastic-has-started? elastic-ip)))
      (Thread/sleep 1000)
      (recur (- tries 1)))))

(defn- init-docker-elastic []
  (let [port (find-free-local-port)
        elastic-ip (str "http://127.0.0.1:" port)
        elastic-docker-ip (str "127.0.0.1:" port ":9200")]
    (intern 'clj-elasticsearch.elastic-utils 'elastic-host elastic-ip)
    (sh "docker" "run" "--rm" "-d" "--name" "kouta-elastic" "--env" "\"discovery.type=single-node\"" "-p" elastic-docker-ip "docker.elastic.co/elasticsearch/elasticsearch:6.8.13")
    (println "Starting elasticsearch container")
    (wait-elastic-to-start elastic-ip)))

(defn global-docker-elastic-fixture
  []
  (defn- run-tests-hook
    [f & nss]
    (let [embedded-elasticsearch? (find-ns 'clj-elasticsearch.elastic-utils)]
      (when embedded-elasticsearch? (init-docker-elastic))
      (let [result (apply f nss)]
        (when embedded-elasticsearch? (stop-docker-elastic))
        result)))
  (add-hook #'clojure.test/run-tests #'run-tests-hook))