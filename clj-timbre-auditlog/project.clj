(defproject oph/clj-timbre-auditlog "0.2.0-SNAPSHOT"
  :description "Clojure-kirjasto Opintopolun audit-login teekemiseksi Clojure-palveluista"
  :url "http://example.com/FIXME"
  :license {:name "EUPL, Version 1.1 or - as soon as they will be approved by the European Commission - subsequent versions of the EUPL (the \"Licence\")"
            :url "http://www.osor.eu/eupl/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [com.taoensso/timbre "6.2.2"]
                 [cheshire/cheshire "5.12.0"]
                 [fi.vm.sade/auditlogger "9.2.0-SNAPSHOT"]
                 [environ "1.2.0"]]
  :repl-options {:init-ns clj-timbre-auditlog.core}

  :plugins [[lein-modules "0.3.11"]
            [lein-ancient "0.7.0"]
            [lein-less "1.7.5"]
            [lein-shell "0.5.0"]]

  :min-lein-version "2.5.3")
