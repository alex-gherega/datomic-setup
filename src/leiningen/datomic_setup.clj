(ns leiningen.datomic-setup
  (:use [leiningen.help :only (help-for subtask-help-for)]
        [leiningen.datomic-setup.transactor :only (transactor)]
        [leiningen.datomic-setup.database :only (database)]
        [leiningen.datomic-setup.peer-server :only (peer-server)])
  (:require [leiningen.core.eval :refer [eval-in-project]]))

(eval-in-project {:dependencies '[[org.clojure/clojure "1.8.0"]]}
                 '(println "Using this Clojure version: " *clojure-version*))

(defn datomic-setup
  "start a transactor; create a database; start a peer server"
  {:help-arglists '([transactor database peer-server])
   :subtasks [#'transactor #'database #'peer-server]}
  ([project]
   (prn (help-for project "datomic-setup") (help-for "datomic-setup")))
  ([project subtask & args]
   (case subtask
     "transactor" (future (apply transactor args))
     "database" (apply database args)
     "peer-server" (apply peer-server args))))
