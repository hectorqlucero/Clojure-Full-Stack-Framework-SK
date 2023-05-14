(ns sk.handlers.menus.handler
  (:require [sk.handlers.menus.view :refer [build-open-menus-view
                                            build-private-admin-admin-menus-view
                                            build-private-admin-system-menus-view build-private-menus-view
                                            build-private-user-admin-menus-view]]))

(defn build-public-menus []
  (build-open-menus-view))

(defn build-private-menus []
  (build-private-menus-view))

(defn build-private-user-admin-menus []
  (build-private-user-admin-menus-view))

(defn build-private-admin-admin-menus []
  (build-private-admin-admin-menus-view))

(defn build-private-admin-system-menus []
  (build-private-admin-system-menus-view))

(comment
  (build-private-admin-system-menus)
  (build-private-admin-admin-menus)
  (build-private-user-admin-menus)
  (build-private-menus)
  (build-public-menus))
