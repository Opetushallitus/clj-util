(defproject oph/clj-test-utils "0.2.8-SNAPSHOT"
            :description "oph clojure testing utilities"
            :url "http://example.com/FIXME"
            :license {:name "EUPL"
                      :url "http://www.osor.eu/eupl/"}
            :plugins [[lein-modules "0.3.11"]]
            :dependencies [[oph/clj-s3 "0.2.2-SNAPSHOT"]
                           [oph/clj-log "0.2.2-SNAPSHOT"]
                           [com.amazonaws/aws-java-sdk-s3 "1.11.205"]
                           [io.findify/s3mock_2.12 "0.2.4"]
                           [base64-clj "0.1.1"]
                           [robert/hooke "1.3.0"]])
