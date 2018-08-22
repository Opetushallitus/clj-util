(ns clj-test-utils.test-utils)

(defn init-test-logging []
      (intern 'clj-log.error-log 'test true)
      (intern 'clj-log.error-log 'verbose false))