package com.test.moviesapp.data.entities

enum class PRLevel (val prLevel : Int, val description : String){
    ML_18(8,"Mayores de 18 años"),
    ML_16(7,"Mayores de 16 años"),
    ML_15(6,"Mayores de 15 años"),
    ML_12(5,"Mayores de 12 años"),
    ML_7(4,"Mayores de 7 años"),
    ML_EMPTY(-1,"Pendiente de clasificar")
}