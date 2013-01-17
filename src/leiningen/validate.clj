(ns leiningen.validate
  (:require [leiningen.core.eval :as lein]))

(defn validate
  "I println 'Hi!'"
  [project & args]
  (println "Hi!"))

;;;;;;;;;;;;;;;;;;;;;;

(defn validate
  "Pretty-print the project"
  [project & args]
  (lein/eval-in-project
   project
   `(let [sw# (java.io.StringWriter.)
          _# (clojure.pprint/pprint '~project sw#)]
      (println (.toString sw#)))
   '(require 'clojure.pprint)))

;;;;;;;;;;;;;;;;;;;;;;

(defn validate
  "I find clojure test sources"
  [project & args]
  (lein/eval-in-project
   (-> project
       (update-in [:dependencies] conj ['org.clojure/tools.namespace "0.2.0"]))
   `(let [paths# (:test-paths '~project)]
      (println
       (mapcat #(-> %
                    clojure.java.io/file
                    clojure.tools.namespace.find/find-clojure-sources-in-dir)
               paths#)))
   '(require 'clojure.tools.namespace.find)))

;;;;;;;;;;;;;;;;;;;;;;

(defn validate
  "I find clojure test namespaces"
  [project & args]
  (lein/eval-in-project
   (-> project
       (update-in [:dependencies] conj ['org.clojure/tools.namespace "0.2.0"]))
   `(let [paths# (:test-paths '~project)
          files# (mapcat
                  #(-> %
                       clojure.java.io/file
                       clojure.tools.namespace.find/find-clojure-sources-in-dir)
                  paths#)
          ns-forms# (map clojure.tools.namespace.file/read-file-ns-decl files#)
          ns-names# (map second ns-forms#)]
      (doall (map #(println "Found:" %) ns-names#)))
   '(do (require 'clojure.tools.namespace.find)
        (require 'clojure.tools.namespace.file))))

;;;;;;;;;;;;;;;;;;;;;;

(defn validate
  "I test namespaces"
  [project & args]
  (lein/eval-in-project
   (-> project
       (update-in [:dependencies] conj ['org.clojure/tools.namespace "0.2.0"]))
   `(let [paths# (:test-paths '~project)
          files# (mapcat
                  #(-> %
                       clojure.java.io/file
                       clojure.tools.namespace.find/find-clojure-sources-in-dir)
                  paths#)
          ns-forms# (map clojure.tools.namespace.file/read-file-ns-decl files#)
          ns-names# (map second ns-forms#)]
      (doseq [n# ns-names#]
        (println "Found:" n#)
        (require n#))
      (apply clojure.test/run-tests ns-names#))
   '(do (require 'clojure.tools.namespace.find)
        (require 'clojure.tools.namespace.file)
        (require 'clojure.test))))

;;;;;;;;;;;;;;;;;;;;;;

(defn ^{:no-project-needed true} validate
  "I test namespaces, unless someone told me to abort"
  [project & args]
  (when (some #(= "abort" %) args)
    (println "Aborting!")
    (leiningen.core.main/abort))
  (lein/eval-in-project
   (-> project
       (update-in [:dependencies] conj ['org.clojure/tools.namespace "0.2.0"]))
   `(let [paths# (:test-paths '~project)
          files# (mapcat
                  #(-> %
                       clojure.java.io/file
                       clojure.tools.namespace.find/find-clojure-sources-in-dir)
                  paths#)
          ns-forms# (map clojure.tools.namespace.file/read-file-ns-decl files#)
          ns-names# (map second ns-forms#)]
      (doseq [n# ns-names#]
        (println "Found:" n#)
        (require n#))
      (apply clojure.test/run-tests ns-names#))
   '(do (require 'clojure.tools.namespace.find)
        (require 'clojure.tools.namespace.file)
        (require 'clojure.test))))
