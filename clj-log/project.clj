(defproject oph/clj-log "0.2.2-SNAPSHOT"
            :description "oph clojure logging utilities"
            :url "http://example.com/FIXME"
            :license {:name "EUPL"
                      :url "http://www.osor.eu/eupl/"}
            :plugins [[lein-modules "0.3.11"]]
            :dependencies [[org.clojure/tools.logging "0.4.0"]
                           [clj-time "0.14.2"]
                           [cheshire "5.8.0"]
                           [slingshot "0.12.2"]])