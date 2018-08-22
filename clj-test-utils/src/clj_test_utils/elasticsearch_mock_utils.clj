(ns clj-test-utils.elasticsearch-mock-utils
    (:require
      [clj-test-utils.test-utils :refer :all])
    (:import
      (pl.allegro.tech.embeddedelasticsearch EmbeddedElastic PopularProperties)))

(def embedded-elastic (atom nil))

(defn start-embedded-elasticsearch [port]
      (reset! embedded-elastic (-> (EmbeddedElastic/builder)
                                   (.withElasticVersion "6.0.0")
                                   (.withSetting PopularProperties/HTTP_PORT port)
                                   (.withSetting PopularProperties/CLUSTER_NAME "my_cluster")
                                   (.build)))
      (.start @embedded-elastic))

(defn stop-elastic-test []
      (.stop @embedded-elastic))

(defn init-elastic-test []
      (let [port (find-free-local-port)]
           (init-test-logging)
           (intern 'clj-elasticsearch.elastic-utils 'elastic-host (str  "http://127.0.0.1:" port))
           (start-embedded-elasticsearch port)))
