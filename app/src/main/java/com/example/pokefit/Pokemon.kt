package com.example.pokefit

class Pokemon
{
    var name:String?=null
    var des:String?=null //descriere
    var image:Int?=null
    var power:Double?=null
    var lat:Double?=null
    var long:Double?=null
    var isCatch:Boolean?=false

    constructor(image:Int, name:String, des:String, power:Double,lat:Double,long:Double)
    {
        this.name=name
        this.des=des
        this.image=image
        this.power=power
        this.lat=lat
        this.long=long
        this.isCatch=false
    }

}