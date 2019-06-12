(ns clj-cas.client
  (:refer-clojure :exclude [get])
  (:use clj-cas.scala-util)
  (:import [org.http4s Header$ Headers HttpVersion]))

(def ^:dynamic client (.defaultClient org.http4s.client.blaze.package$/MODULE$))


(def utf8 (org.http4s.Charset/fromNioCharset
                         java.nio.charset.StandardCharsets/UTF_8))
(def utf-encoder
  (.stringEncoder org.http4s.EntityEncoder$/MODULE$ utf8))

(def utf-decoder
  (.text org.http4s.EntityDecoder$/MODULE$ utf8))


(defn parse-uri [us]
  (.getOrElse (org.http4s.Uri/fromString us) (sfn0 (fn [] (throw (IllegalArgumentException. "malformed uri"))))))


(defn header [k v]
  (.apply Header$/MODULE$ k v))




(defn headers [hm]
  (Headers/apply
   (scala-seq
    (map (partial apply header) hm))))


(defn attribute-entry [k v]
  (org.http4s.AttributeEntry. (org.http4s.AttributeKey.) v))

(defn attributes [am]
  (.apply org.http4s.AttributeMap$/MODULE$
          (scala-seq
           (map
            (partial apply attribute-entry)
            am))))

(defn resolve-method [mk]
  (case mk
    :get (org.http4s.Method/GET)
    :post (org.http4s.Method/POST)))




(defn req [{:keys [method url http-version-vec header-map body attribute-map] :or {method :get,
                                                                        url "/",
                                                                        http-version-vec [1 1],
                                                                        header-map {},
                                                                        body (.EmptyBody org.http4s.package$/MODULE$),
                                                                        attribute-map {}}} ]

  (let [mth (resolve-method method)
        uri (parse-uri url)
        [http-major http-minor] http-version-vec
        http-version (HttpVersion. http-major http-minor)
        sheaders (headers header-map)
        sattribs (attributes attribute-map)]
  (org.http4s.Request. mth uri http-version sheaders body sattribs)))




(defn parse-response [resp]
  {:status {:code (.code (.status resp)) :reason (.reason (.status resp))}
   :http-version [(.major (.httpVersion resp)) (.minor (.httpVersion resp))]
   :headers (map
             #(hash-map :name (.toString (.name %1)) :value (.value %1))
             (to-clj-seq (.headers resp)))
   :attributes (map
                #(hash-map :key (.name (.key %1)) :value (.value %1))
                (to-clj-seq (.entries (.attributes resp))))
   :body (scalaz.stream.io/toInputStream (.body resp))})




(defn run-request [request options]
  (let [{:keys [cl] :or {cl client}} (apply hash-map options)
        p (promise)]
    (.runAsync
     (.prepare cl request)
     (sfn1
      (fn [res]
        (let [ores (.toOption res)
              resp (if (.isDefined ores)
                    (parse-response (.get ores))
                    nil)]

        (deliver
         p
         resp)))))
    p))

(defn get [url & options]
  (run-request (req {:url url }) options))


(defn parse-content-type [mt st]
  (.apply (org.http4s.headers.Content$minusType$/MODULE$) (org.http4s.MediaType/fromKey (scala.Tuple2. mt st))))

(defn post [url body & options]
  (let [{:keys [content-type] :or {content-type ["text" "plain"] }} (apply hash-map options)]
    (run-request (.withBody (req {:method :post :url url}) body (.withContentType utf-encoder (apply parse-content-type content-type)) ) options)))
