(defproject oph/clj-test-utils "0.1.1-SNAPSHOT"
            :description "oph clojure testing utilities"
            :url "http://example.com/FIXME"
            :license {:name "EUPL"
                      :url "http://www.osor.eu/eupl/"}
            :plugins [[lein-modules "0.3.11"]]
            :dependencies [[oph/clj-s3 "0.1.0-SNAPSHOT"]
                           [com.amazonaws/aws-java-sdk-s3 "1.11.205"]
                           [io.findify/s3mock_2.11 "0.2.4"]
                           [base64-clj "0.1.1"]])