(ns clj-ring-db-session.authentication.login
  (:require [clj-ring-db-session.authentication.cas-ticketstore :as cas-store]
            [ring.util.response :as resp]))

(defn- do-login [virkailija-finder henkilo-finder do-on-success success-redirect-url username ticket]
  (let [virkailija (virkailija-finder username)
        henkilo    (henkilo-finder (:oidHenkilo virkailija))]
    (-> (resp/redirect success-redirect-url)
        (assoc :session {:identity {:username   username
                                    :first-name (:kutsumanimi henkilo)
                                    :last-name  (:sukunimi henkilo)
                                    :oid        (:oidHenkilo henkilo)
                                    :lang       (or (some #{(-> henkilo :asiointiKieli :kieliKoodi)}
                                                          ["fi" "sv" "en"])
                                                    "fi")
                                    :ticket     ticket}})
        (do-on-success virkailija henkilo username ticket))))

(defn login
  "login-provider function returns username and valid service ticket when login was successful.
   virkailija-finder resolves virkailija user by username.
   henkilo-finder resolves personal data by person oid."
  [params]
  (let [{:keys [login-provider
                virkailija-finder
                henkilo-finder
                success-redirect-url
                do-on-success
                login-failed-handler
                datasource]} params]
    (try
      (if-let [[username ticket] (login-provider)]
        (do
          (cas-store/login ticket datasource)
          (do-login virkailija-finder henkilo-finder do-on-success success-redirect-url username ticket))
        (login-failed-handler))
      (catch Exception e
        (login-failed-handler e)))))

(defn logout [session logout-url datasource]
  (cas-store/logout (-> session :identity :ticket) datasource)
  (-> (resp/redirect logout-url)
      (assoc :session {:identity nil})))

(defn logged-in? [request datasource]
  (let [ticket (-> request :session :identity :ticket)]
    (cas-store/logged-in? ticket datasource)))