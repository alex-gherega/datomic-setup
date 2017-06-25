(ns leiningen.datomic-setup.database
  (:require [datomic.api :as d]))

(defn database [uri]
  (try
    (prn "[DATOMIC] Creating database...\n" uri
         "\n\tCreating database:" (d/create-database uri))
    (catch Exception e (database uri))))
