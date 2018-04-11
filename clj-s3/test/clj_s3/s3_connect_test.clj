(ns clj-s3.s3-connect-test
    (:require [clojure.test :refer :all]
              [clj-s3.s3-connect :as s3]
              [clj-s3.free-port :refer [find-free-local-port]]
              [base64-clj.core :as b64])
    (:import (io.findify.s3mock S3Mock)
      (com.amazonaws.client.builder AwsClientBuilder$EndpointConfiguration)
      (com.amazonaws.services.s3 AmazonS3ClientBuilder)
      (com.amazonaws.auth AWSStaticCredentialsProvider AnonymousAWSCredentials)
      (com.amazonaws.services.s3.model CreateBucketRequest)
      (java.io IOException)
      (java.net Socket)))

(defonce koulutus-oid "1.2.3.4.567")

(intern 'clj-s3.s3-connect 's3-region "eu-west-1")
(intern 'clj-s3.s3-connect 's3-bucket "buketti")

(defn mock-s3-fixture [f]
      (let [port (find-free-local-port)
            mock (S3Mock/create port)
            s3-url (str "http://localhost:" port)]
           (.start mock)
           (let [endpoint-config (new AwsClientBuilder$EndpointConfiguration s3-url, s3/s3-region)
                 client (-> (AmazonS3ClientBuilder/standard)
                            (.withPathStyleAccessEnabled true)
                            (.withEndpointConfiguration endpoint-config)
                            (.withCredentials (new AWSStaticCredentialsProvider (new AnonymousAWSCredentials)))
                            (.build))]
                (reset! s3/s3-client client)
                (.createBucket @s3/s3-client (new CreateBucketRequest s3/s3-bucket s3/s3-region)))
           (f)
           (.stop mock)))

(use-fixtures :once mock-s3-fixture)

(deftest s3-connect-test
       (testing "return empty list"
             (is (= 0 (count (s3/list-keys "koulutus" koulutus-oid)))))

       (testing "upload documents"
             (s3/upload (.getBytes "moi") "text/plain" "moi.txt" "koulutus" koulutus-oid "kieli_fi")
             (s3/upload (.getBytes "hej") "text/plain" "moi.txt" "koulutus" koulutus-oid "kieli_sv")
             (s3/upload (.getBytes "terve") "text/plain" "terve.txt" "koulutus" koulutus-oid "kieli_fi")
             (is (= 3 (count (s3/list-keys "koulutus" koulutus-oid)))))

       (testing "list documents in specific language"
             (is (= 2 (count (s3/list-keys "koulutus" koulutus-oid "kieli_fi")))))

       (testing "download document by path parts"
             (let [doc (s3/download "moi.txt" "koulutus" koulutus-oid "kieli_fi")]
                (is (= "moi" (slurp (:stream doc))))
                (is (= "text/plain" (:content-type doc)))))

       (testing "download document by key"
            (let [doc (s3/download (str "koulutus/" koulutus-oid "/kieli_fi/terve.txt"))]
                 (is (= "terve" (slurp (:stream doc))))
                 (is (= "text/plain" (:content-type doc)))))

       (testing "delete documents"
             (let [resp (s3/list-keys "koulutus" koulutus-oid)]
                  (s3/delete resp))
             (is (= 0 (count (s3/list-keys "koulutus" koulutus-oid))))))