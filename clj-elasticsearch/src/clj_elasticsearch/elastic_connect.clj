(ns clj-elasticsearch.elastic-connect
    (:require [clj-elasticsearch.elastic-utils :refer :all]
              [clj-http.client :as http]
              [cheshire.core :as json]))

(defn get-cluster-health [host]
      (elastic-get-as-json (str host "/_cluster/health")))

(defn check-elastic-status [host]
      (-> (get-cluster-health host)
          :status
          (= 200)))

(defn get-indices-info
      [host]
      (-> (elastic-get-as-json (str host "/_cat/indices?v&format=json"))
          :body))

(defn get-elastic-status
      []
      {:cluster_health (:body (get-cluster-health))
       :indices-info (get-indices-info)})

(defn create [host index mapping-type document]
      (elastic-post (elastic-url host index mapping-type) document))

(defn create-index [host index settings]            ;TODO mappings can be also created here
      (let [json {:settings settings}]
           (elastic-put (elastic-url host index) json)))

(defn search [host index mapping-type & query-params]
      (let [query-map (apply array-map query-params)]
           (elastic-post (elastic-url host index mapping-type "_search") query-map)))

(defn parse-search-result [res] (map :_source (get-in res [:hits :hits])))

(defn get-document [host index mapping-type id] ;TODO URL encoding
      (try
        (elastic-get (elastic-url host index mapping-type id))
        (catch Exception e
          (if (= 404 ((ex-data e) :status)) {:found false} (throw e)))))

(defn bulk [host index mapping-type data]
      (if (not (empty? data))
        (let [partitions (bulk-partitions data)]
             (doall (map #(elastic-post (elastic-url host index mapping-type "_bulk") %) partitions)))))

(defn index-exists [host index]
      (try
        (-> (http/head (elastic-url host index))
            (:status)
            (= 200))
        (catch Exception e
          (if (= 404 ((ex-data e) :status)) false (throw e)))))

(defn refresh-index
      [host index]
      (http/post (elastic-url host index "/_refresh") {:content-type :json}))

(defn delete-index ; TODO -connection timeouts
      [host index]
      (try (elastic-delete (elastic-url host index))
         (catch Exception e
           (if (not (= 404((ex-data e) :status))) (throw e)))))