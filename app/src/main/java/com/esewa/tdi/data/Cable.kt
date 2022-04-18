package com.esewa.tdi.data

data class Cable(var Name : String ?= null, var id : String?= null, var Department : String ?= null,
                 var Device : String?= null
){
    override fun toString(): String = Name?:""
}
