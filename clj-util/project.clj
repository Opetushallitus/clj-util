(defproject oph/clj-util "0.1.0-SNAPSHOT"
  :description "oph clojure utilities"
  :url "http://example.com/FIXME"
  :license {:name "EUPL"
            :url "http://www.osor.eu/eupl/"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [fi.vm.sade/scala-utils_2.11 "0.2.0-SNAPSHOT"]
                 [org.http4s/http4s-blaze-client_2.11 "0.10.0"]]
  :repositories [["oph-releases" "https://artifactory.oph.ware.fi/artifactory/oph-sade-release-local"]
                 ["oph-snapshots" "https://artifactory.oph.ware.fi/artifactory/oph-sade-snapshot-local"]]
  :deploy-repositories {"snapshots" {:url "https://artifactory.oph.ware.fi/artifactory/oph-sade-snapshot-local"}
                        "releases" {:url "https://artifactory.oph.ware.fi/artifactory/oph-sade-release-local"}}
  :plugins[[lein-modules "0.3.11"]]
  )
