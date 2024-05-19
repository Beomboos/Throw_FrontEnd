package com.example.deamhome.domain.model

enum class Trash(val type: String) {
    ALL("전체"),
    GENERAL_TRASH("일반쓰레기"),
    GLASS_BOTTLE("병"),
    PLASTIC("플라스틱"),
    PAPER("종이"),
    ETC("캔"),
}