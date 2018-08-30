(ns clj-elasticsearch.elastic-connect
    (:require [clj-elasticsearch.elastic-utils :refer :all]
              [clj-http.client :as http]
              [cheshire.core :as json]))

(defn get-cluster-health []
      (elastic-get-response (str elastic-host "/_cluster/health")))

(defn check-elastic-status []
      (-> (get-cluster-health)
          :status
          (= 200)))

(defn get-indices-info
      []
      (-> (elastic-get-response (str elastic-host "/_cat/indices?v&format=json"))
          :body))

(defn get-elastic-status
      []
      {:cluster_health (:body (get-cluster-health))
       :indices-info (get-indices-info)})

(defn create [index mapping-type document]
      (elastic-post (elastic-url index mapping-type) document))

(defn create-index [index settings]            ;TODO mappings can be also created here
      (let [json {:settings settings}]
           (elastic-put (elastic-url index) json)))

(defn search [index mapping-type & query-params]
      (let [query-map (apply array-map query-params)]
           (elastic-post (elastic-url index mapping-type "_search") query-map)))

(defn simple-search
  ([index query pretty]
    (elastic-get (str (elastic-url index) "/_search?q=" query "&pretty=" pretty)))
  ([index query]
    (simple-search index query true)))

(defn count [index mapping-type & query-params]
      (let [query-map (apply array-map query-params)]
           (:count (elastic-post (elastic-url index mapping-type "_count") query-map))))

(defn parse-search-result [res] (map :_source (get-in res [:hits :hits])))

(defn get-document [index mapping-type id] ;TODO URL encoding
      (try
        (elastic-get (elastic-url index mapping-type id))
        (catch Exception e
          (if (= 404 ((ex-data e) :status)) {:found false} (throw e)))))

(defn bulk [index mapping-type data]
      (if (not (empty? data))
        (let [partitions (bulk-partitions data)]
             (doall (map #(elastic-post (elastic-url index mapping-type "_bulk") %) partitions)))))

(defn index-exists [index]
      (try
        (-> (http/head (elastic-url index))
            (:status)
            (= 200))
        (catch Exception e
          (if (= 404 ((ex-data e) :status)) false (throw e)))))

(defn refresh-index
      [index]
      (http/post (elastic-url index "_refresh") {:content-type :json}))

(defn delete-index ; TODO -connection timeouts
      [index]
      (try (elastic-delete (elastic-url index))
         (catch Exception e
           (if (not (= 404((ex-data e) :status))) (throw e)))))