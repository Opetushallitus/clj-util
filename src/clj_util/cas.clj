(ns clj-util.cas
  (:refer-clojure :exclude [get])
  (:require [clj-util.client :as cl])
  (:import [fi.vm.sade.utils.cas CasAbleClient CasClient CasParams]))

(defn cas-params [uri user passwd] (CasParams/apply uri user passwd))

(defn cas-client [uri] (CasClient. uri cl/client))

(defn cassableclient [cc cp] (CasAbleClient. cc cp cl/client))

(def current-client (atom nil))

(defn set-cas [uri]
  (let [cc (cas-client uri)]
    (swap! current-client (constantly cc))))

(defn get [cp uri]
  (cl/get uri :cl (cassableclient @current-client cp)))

(defn post [cp uri body]
  (cl/post uri body :cl (cassableclient @current-client cp)))
