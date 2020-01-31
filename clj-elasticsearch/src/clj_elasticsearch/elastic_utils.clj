(ns clj-elasticsearch.elastic-utils
    (:require [clj-http.client :as http]
              [cheshire.core :as json]
              [clojure.string :refer [join]]))

(declare elastic-host)

(defonce timeout 120000)

(defn index-name
      [name is-test]
      (str name (when is-test "_test")))

(defn join-names
      [name-or-names]
      (clojure.string/join "," (flatten [name-or-names])))

(defn elastic-url
      ([index]
        (str elastic-host "/" index))
      ([index mapping-type]
        (str elastic-host "/" index "/" mapping-type))
      ([index mapping-type operation]
        (str elastic-host "/" index "/" mapping-type "/" operation )))

(defn json-request [body] {:body (if (instance? String body) body (json/encode body)) :content-type :json :socket-timeout timeout})

(defn elastic-post
      [url body]
      (-> (http/post url (json-request body))
          (:body)
          (json/decode true)))

(defn elastic-put
      [url body]
      (-> (http/put url (json-request body))
          (:body)
          (json/decode true)))

(defn elastic-get
      ([url]
        (-> (http/get url {:socket-timeout timeout})
            (:body)
            (json/decode true))))

(defn elastic-delete [url]
      (http/delete url {:content-type :json :body "{}" :socket-timeout timeout}))

(defn elastic-get-as-json
      ([url]
        (-> (http/get url {:socket-timeout timeout :as :json})
            (:body)
            (json/decode true))))

(defn elastic-get-response
      [url] (http/get url {:socket-timeout timeout :as :json}))

;MAX request payload size in AWS ElasticSearch
(defonce max-payload-size 10485760)

(defn bulk-partitions
  [data]
  (let [action? (fn [d] (or (contains? d :index) (contains? d :update) (contains? d :create) (contains? d :delete)))
        action-counter (atom 0)
        action-nr (fn [d] (if (action? d)
                            (swap! action-counter inc)
                            @action-counter))
        encode (fn [a] (join (map #(str (json/encode %) "\n") a)))
        cur-bytes (atom 0)
        partitioner (fn [e]
                      (let [bytes (count (.getBytes e))]
                        (if (> max-payload-size (+ @cur-bytes bytes))
                          (do (reset! cur-bytes (+ @cur-bytes bytes)) true)
                          (do (reset! cur-bytes 0) false))))]

    (->> data
         (partition-by action-nr)
         (map encode)
         (partition-by partitioner)
         (map #(clojure.string/join %)))))