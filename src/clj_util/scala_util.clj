(ns clj-util.scala-util)


(defn sfn0 [f]
  (reify scala.Function0
    (apply [this] (f))))

(defn sfn1 [f]
  (reify scala.Function1
    (apply [this a] (f a))))

(defn sfn2 [f]
  (reify scala.Function2
    (apply [this a b] (f a b))))


(defn scala-seq [s]
  (.toList
   (reduce (fn [ab i] (.$plus$eq ab i))
           (scala.collection.mutable.ArrayBuffer.)
           s)))


(defn to-clj-seq [ss]
  (.foldLeft ss [] (sfn2 (fn [a b] (conj a b)))))
