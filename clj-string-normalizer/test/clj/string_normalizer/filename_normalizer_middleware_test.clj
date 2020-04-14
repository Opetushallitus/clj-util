(ns string-normalizer.filename-normalizer-middleware-test
  (:require [clojure.test :refer :all]
            [string-normalizer.filename-normalizer-fixtures :as fixtures]
            [string-normalizer.filename-normalizer-middleware :as normalizer]))

(deftest successfully-normalizes-filename-from-multipart-request
  (doseq [{expected :expected
           input    :input} fixtures/string-normalizer-fixtures]
    (let [request-before-mw {:multipart-params {"file-part" {:filename input :other-param "other-value"}}}
          request-after-mw  ((normalizer/wrap-multipart-filename-normalizer identity) request-before-mw)]
      (is (= {:multipart-params {"file-part" {:filename expected :other-param "other-value"}}}
             request-after-mw)
          (format "did not normalize filename from \"%s\" to \"%s\" in a multipart request" input expected)))))

(deftest wraps-around-request-without-file-part-inside-multipart-params
  (let [response-after-mw ((normalizer/wrap-multipart-filename-normalizer identity) {:body {:lol :bal}})]
    (is (= {:body {:lol :bal}}
           response-after-mw))))

(deftest successfully-normalises-filename-from-query-params
  (doseq [{expected :expected
           input    :input} fixtures/string-normalizer-fixtures]
    (let [request-before-mw {:query-params {"file-name" input "other-param" "other-value"}}
          request-after-mw  ((normalizer/wrap-query-params-filename-normalizer identity) request-before-mw)]
      (is (= {:query-params {"file-name" expected "other-param" "other-value"}}
             request-after-mw)
          (format "did not normalize filename \"%s\" to \"%s\" from a request with query params" input expected)))))

(deftest wraps-around-request-without-filename-in-query-params
  (let [response-after-mw ((normalizer/wrap-query-params-filename-normalizer identity) {:body         {:lol :bal}
                                                                                        :query-params {"foo" "bar"}})]
    (= {:body         {:lol :bal}
        :query-params {"foo" "bar"}}
       response-after-mw)))
