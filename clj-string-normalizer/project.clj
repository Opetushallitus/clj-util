(defproject oph/clj-string-normalizer "0.1.1-SNAPSHOT"
  :description "Functionalities to normalize strings"

  :url "https://github.com/Opetushallitus/clj-util/tree/master/clj-string-normalizer"

  :license {:name "EUPL"
            :url  "http://www.osor.eu/eupl/"}

  :repositories [["releases" {:url           "https://artifactory.opintopolku.fi/artifactory/oph-sade-release-local"
                              :username      :env/artifactory_username
                              :password      :env/artifactory_password
                              :sign-releases false
                              :snapshots     false}]
                 ["snapshots" {:url           "https://artifactory.opintopolku.fi/artifactory/oph-sade-snapshot-local"
                               :username      :env/artifactory_username
                               :password      :env/artifactory_password
                               :sign-releases false
                               :snapshots     true}]]

  :source-paths ["src/clj" "src/cljc" "src/cljs"]

  :test-paths ["test/clj" "test/cljc" "test/cljs"]

  :dependencies [[org.clojure/clojure "1.10.3"]
                 [org.clojure/clojurescript "1.10.764"
                  :exclusions [com.google.javascript/closure-compiler-unshaded
                               org.clojure/google-closure-library
                               org.clojure/google-closure-library-third-party]]
                 [thheller/shadow-cljs "2.11.23"]
                 [prismatic/schema "1.1.12"]]

  :plugins [[lein-shell "0.5.0"]]

  :shell {:commands {"open" {:windows ["cmd" "/c" "start"]
                             :macosx  "open"
                             :linux   "xdg-open"}}}

  :aliases {"test:clj"  ["test"]
            "test:cljs" ["do"
                         ["run" "-m" "shadow.cljs.devtools.cli" "compile" "unit-test"]
                         ["shell" "npx" "karma" "start" "--single-run" "--reporters" "junit,dots"]]})
