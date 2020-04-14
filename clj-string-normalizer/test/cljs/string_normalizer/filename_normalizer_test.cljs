(ns string-normalizer.filename-normalizer-test
  (:require [string-normalizer.filename-normalizer :as normalizer]
            [string-normalizer.filename-normalizer-fixtures :as fixtures]
            [goog.string :as gstring])
  (:require-macros [cljs.test :refer [deftest is]]))

(deftest normalizes-string-in-cljs
  (doseq [{expected :expected
           input    :input} fixtures/string-normalizer-fixtures]
    (let [actual (normalizer/normalize-filename input)]
      (is (= actual expected)
          (gstring/format "Did not normalize string \"%s\" properly, was: \"%s\", should have been: \"%s\"" input actual expected)))))
