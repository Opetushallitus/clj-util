(defproject oph/clj-parent "0.3.0-SNAPSHOT"
  :url "http://example.com/FIXME"
  :license {:name "EUPL"
            :url  "http://www.osor.eu/eupl/"}
  :plugins [[lein-modules "0.3.11"]]
  :modules {:inherited {
                        :repositories        [["releases" {:url "https://artifactory.opintopolku.fi/artifactory/oph-sade-release-local"
                                                           :username :env/artifactory_username
                                                           :password :env/artifactory_password
                                                           :sign-releases false
                                                           :snapshots false}]
                                              ["snapshots" {:url "https://artifactory.opintopolku.fi/artifactory/oph-sade-snapshot-local"
                                                            :username :env/artifactory_username
                                                            :password :env/artifactory_password
                                                            :snapshots true}]]}
            :subprocess "../lein"})
