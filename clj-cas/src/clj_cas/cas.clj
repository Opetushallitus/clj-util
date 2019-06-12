(ns clj-cas.cas
  (:refer-clojure :exclude [get])
  (:require [clj-cas.client :as cl])
  (:import [fi.vm.sade.utils.cas CasAuthenticatingClient CasClient CasParams]))

(defn cas-params [uri user passwd] (CasParams/apply uri user passwd))

(defn cas-client [^String uri] (CasClient. uri cl/client))

(defn cassableclient [^CasClient cc ^CasParams cp caller-id session-cookie-name]
  (CasAuthenticatingClient. cc cp cl/client caller-id session-cookie-name))

(def current-client (atom nil))

(defn set-cas [^String uri]
  (let [cc (cas-client uri)]
    (swap! current-client (constantly cc))))

(defn get [^CasParams cp ^String uri caller-id session-cookie-name & options]
  ((partial apply cl/get uri :cl (cassableclient @current-client cp caller-id session-cookie-name)) options))

(defn post [^CasParams cp ^String uri body caller-id session-cookie-name & options]
  ((partial apply cl/post uri body :cl (cassableclient @current-client cp caller-id session-cookie-name)) options))
