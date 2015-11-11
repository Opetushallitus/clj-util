(defproject oph/clj-parent "0.1.0"
  :url "http://example.com/FIXME"
  :license {:name "EUPL"
            :url  "http://www.osor.eu/eupl/"}
  :plugins [[lein-modules "0.3.11"]]
  :modules {:inherited {
                        :repositories        [["releases" {:url "https://artifactory.oph.ware.fi/artifactory/oph-sade-release-local"
                                                               :sign-releases false
                                                               :snapshots false}]
                                              ["snapshots" "https://artifactory.oph.ware.fi/artifactory/oph-sade-snapshot-local"]]}
            :subprocess "../lein"})
