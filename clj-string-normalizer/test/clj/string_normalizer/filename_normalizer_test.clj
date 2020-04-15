(ns string-normalizer.filename-normalizer-test
  (:require [clojure.test :refer [deftest is]]
            [string-normalizer.filename-normalizer-fixtures :as fixtures]
            [string-normalizer.filename-normalizer :as normalizer]))

(deftest normalizes-string-in-clj
  (doseq [{expected :expected
           input    :input} fixtures/string-normalizer-fixtures]
    (let [actual (normalizer/normalize-filename input)]
      (is (= actual expected)
          (format "Did not normalize string \"%s\" properly, was: \"%s\", should have been: \"%s\"" input actual expected)))))
