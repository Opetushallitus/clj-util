(defproject oph/clj-parent "0.1.0-SNAPSHOT"
  :url "http://example.com/FIXME"
  :license {:name "EUPL"
            :url  "http://www.osor.eu/eupl/"}
  :plugins [[lein-modules "0.3.11"]]
  :modules {:inherited {
                        :repositories        [["oph-releases" "https://artifactory.oph.ware.fi/artifactory/oph-sade-release-local"]
                                              ["oph-snapshots" "https://artifactory.oph.ware.fi/artifactory/oph-sade-snapshot-local"]]
                        :deploy-repositories {"snapshots" {:url "https://artifactory.oph.ware.fi/artifactory/oph-sade-snapshot-local"}
                                              "releases"  {:url "https://artifactory.oph.ware.fi/artifactory/oph-sade-release-local"}}}})
