(defproject oph/clj-test-utils "0.3.0-SNAPSHOT"
            :description "oph clojure testing utilities"
            :url "http://example.com/FIXME"
            :license {:name "EUPL"
                      :url "http://www.osor.eu/eupl/"}
            :plugins [[lein-modules "0.3.11"]]
            :dependencies [[oph/clj-s3 "0.2.5-SNAPSHOT"]
                           [oph/clj-log "0.3.1-SNAPSHOT"]
                           [com.amazonaws/aws-java-sdk-s3 "1.11.978"]
                           [io.findify/s3mock_2.12 "0.2.6"]
                           [base64-clj "0.1.1"]
                           [org.testcontainers/elasticsearch "1.15.3"]
                           [robert/hooke "1.3.0"]])
