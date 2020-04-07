(ns clj-timbre-auditlog.audit-log-test
  (:require [clojure.test :refer :all]
            [clj-timbre-auditlog.audit-log :as audit-log])
  (:import (fi.vm.sade.auditlog ApplicationType Audit)))

(deftest audit-log-can-be-created
  (testing "Audit log creation"
    (let [^Audit audit-logger (audit-log/create-audit-logger "clj-log" "/tmp" ApplicationType/BACKEND)]
      (.logHeartbeat audit-logger))))
