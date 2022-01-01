;; Agregar :require a [sk.models.util :refer [xxx build-radiobuttons]
(build-radio-buttons
  "xxx"
  (list
    {:id "xxx_no"
     :name "xxx"
     :class "easyui-radiobutton"
     :value "N"
     :data-options "label:'No',checked:true"}
    {:id "xxx_si"
     :name "xxx"
     :class "easyui-radiobutton"
     :value "S"
     :data-options "label:'Si'"}))
