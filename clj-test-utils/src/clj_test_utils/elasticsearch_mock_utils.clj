(ns clj-test-utils.elasticsearch-mock-utils
    (:require
      [clj-test-utils.port-finder :refer [find-free-local-port]]
      [robert.hooke :refer [add-hook]]
      [clojure.java.shell :refer [sh]])
  (:import
    (pl.allegro.tech.embeddedelasticsearch EmbeddedElastic PopularProperties)
    (java.util.concurrent TimeUnit)))

(def embedded-elastic (atom nil))

(defn start-embedded-elasticsearch
  ([port timeoutInMillis]
    (reset! embedded-elastic (-> (EmbeddedElastic/builder)
                                 (.withElasticVersion "6.8.4")
                                 (.withSetting PopularProperties/HTTP_PORT port)
                                 (.withSetting PopularProperties/CLUSTER_NAME "elasticsearch")
                                 (.withSetting "discovery.zen.ping.unicast.hosts" (java.util.ArrayList. [(str "127.0.0.1:" port)]))
                                 (.withSetting "action.auto_create_index" ".watches,.triggered_watches,.watcher-history-*")
                                 (.withStartTimeout timeoutInMillis TimeUnit/MILLISECONDS)
                                 (.build)))
    (.start @embedded-elastic))
  ([port]
    (start-embedded-elasticsearch port (* 60 1000))))


(defn stop-elastic-test []
      (.stop @embedded-elastic))

(defn init-elastic-test []
      (let [port (find-free-local-port)]
           (intern 'clj-elasticsearch.elastic-utils 'elastic-host (str "http://127.0.0.1:" port))
           (start-embedded-elasticsearch port)))

(defn mock-embedded-elasticsearch-fixture [test]
      (init-elastic-test)
      (test)
      (stop-elastic-test))

(defn global-elasticsearch-fixture
  []
  (defn- run-all-test-hook
    [f & nss]
    (let [embedded-elasticsearch? (find-ns 'clj-elasticsearch.elastic-utils)]
      (when embedded-elasticsearch? (init-elastic-test))
        (let [result (apply f nss)]
          (when embedded-elasticsearch? (stop-elastic-test))
          result)))
    (add-hook #'clojure.test/run-tests #'run-all-test-hook))

(defn stop-docker-elastic []
  (sh "docker" "kill" "kouta-elastic"))

(defn init-docker-elastic []
  (let [port (find-free-local-port)
        elastic-ip (str "http://127.0.0.1:" port)
        elastic-docker-ip (str "127.0.0.1:" port ":9200")]
    (intern 'clj-elasticsearch.elastic-utils 'elastic-host elastic-ip)
    ;(println (sh "curl" (str elastic-ip "/_cluster/health")))
    (println (sh "docker" "run" "--rm" "-d" "--name" "kouta-elastic" "--env" "\"discovery.type=single-node\"" "-p" elastic-docker-ip "docker.elastic.co/elasticsearch/elasticsearch:6.8.13"))
    (println "Starting elasticsearch container")
    (Thread/sleep 16000) ;TODO toteuta pollaus milloin elastic on käynnistynyt
    ;(println (sh "curl" (str elastic-ip "/_cluster/health")))
    ))

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
