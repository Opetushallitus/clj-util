(ns clj-elasticsearch.elastic-utils
    (:require [clj-http.client :as http]
              [cheshire.core :as json]))

(defonce timeout 120000)

(defn index-name
      [name is-test]
      (str name (when is-test "_test")))

(defn join-names
      [name-or-names]
      (clojure.string/join "," (flatten [name-or-names])))

(defn elastic-url
      ([elastic-host index]
        (str elastic-host "/" index))
      ([elastic-host index mapping-type]
        (str elastic-host "/" index "/" mapping-type))
      ([elastic-host index mapping-type operation]
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

;MAX request payload size in AWS ElasticSearch
(defonce max-payload-size 10485760)

(defn bulk-partitions [data]
      (let [encoded-data (map #(str (json/encode %) "\n") data)
            action-and-data-entries (partition 2 encoded-data)
            bulk-entries (map (fn [e] (str (first e) (second e))) action-and-data-entries)
            cur-bytes (atom 0)
            partitioner (fn [e]
                            (let [bytes (count (.getBytes e))]
                                 (if (> max-payload-size (+ @cur-bytes bytes))
                                   (do (reset! cur-bytes (+ @cur-bytes bytes)) true)
                                   (do (reset! cur-bytes 0) false))))]
           (map #(clojure.string/join %) (partition-by partitioner bulk-entries))))