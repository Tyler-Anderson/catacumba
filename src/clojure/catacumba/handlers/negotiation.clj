;; Copyright (c) 2015 Andrey Antukh <niwi@niwi.nz>
;; All rights reserved.
;;
;; Redistribution and use in source and binary forms, with or without
;; modification, are permitted provided that the following conditions are met:
;;
;; * Redistributions of source code must retain the above copyright notice, this
;;   list of conditions and the following disclaimer.
;;
;; * Redistributions in binary form must reproduce the above copyright notice,
;;   this list of conditions and the following disclaimer in the documentation
;;   and/or other materials provided with the distribution.
;;
;; THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
;; AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
;; IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
;; DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
;; FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
;; DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
;; SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
;; CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
;; OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
;; OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

(ns catacumba.handlers.negotiation
  "Content negotiation.

  In the Hypertext Transfer Protocol (HTTP), content negotiation is the mechanism that is used,
  when facing the ability to serve several equivalent contents for a given URI, to provide the best
  suited one to the final user. The determination of the best suited content is made through one of
  three mechanisms:

  1. Specific HTTP headers by the client (server-driven negotiation)
  2. The 300 Multiple Choices or 406 Not Acceptable HTTP response codes by the server (agent-driven
  negotiation)
  3. Cache (transparent negotiation).

  This module implements the agent-driven negotiation."
  (:import ratpack.http.Request
           ratpack.http.TypedData
           ratpack.handling.Context))

;; (definterface IMediaType
;;   (accepts [

(deftype MediaType [type subtype params q]
  Object
  (equals [it other]
    (and (instance? MediaType other)
         (= (hash it)
            (hash other))))

  (toString [_]
    (with-out-str
      (print (str (name type) "/" (name subtype)))
      (run! (fn [[key value]]
              (print (str ":" key "=" value)))
            (into [] params))))

  clojure.lang.IHashEq
  (hasheq [it]
    (reduce #(hash-combine %1 %2) (hash type) [subtype params q]))

;; (defn content-negotiation
;;   ([]
;;    (content-negotiation serializer))
;;   ([serializer]
;;    {:pre [(instance? clojure.lang.MultiFn serializer)]}
;;    (let [content-types (set (keys (.getMethodTable foo)))]
;;      (fn [context]

