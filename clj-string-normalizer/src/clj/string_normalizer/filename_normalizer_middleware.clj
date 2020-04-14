(ns string-normalizer.filename-normalizer-middleware
  (:require [string-normalizer.filename-normalizer :as normalizer]))

(defn wrap-multipart-filename-normalizer [handler]
  (fn normalize-multipart-params-filename [request]
    (let [filename (-> request :multipart-params (get "file-part") :filename)
          request  (cond-> request
                           (some? filename)
                           (update-in [:multipart-params "file-part" :filename] normalizer/normalize-filename))]
      (handler request))))

(defn wrap-query-params-filename-normalizer [handler]
  (fn normalize-query-params-filename [request]
    (let [filename (-> request :query-params (get "file-name"))
          request  (cond-> request
                           (some? filename)
                           (update-in [:query-params "file-name"] normalizer/normalize-filename))]
      (handler request))))
