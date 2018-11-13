(defproject oph/clj-s3 "0.1.0-SNAPSHOT"
            :description "oph clojure s3 utilities"
            :url "http://example.com/FIXME"
            :license {:name "EUPL"
                      :url "http://www.osor.eu/eupl/"}
            :plugins [[lein-modules "0.3.11"]]
            :dependencies [[com.amazonaws/aws-java-sdk-s3 "1.11.205"]]
            :profiles { :test { :dependencies [[oph/clj-test-utils "0.1.0-SNAPSHOT"]
                                               [base64-clj "0.1.1"]]}})