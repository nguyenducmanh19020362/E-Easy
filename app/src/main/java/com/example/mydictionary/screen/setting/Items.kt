package com.example.mydictionary.screen.setting

class Item (
    var nameItem: String
)

object ListItems{
    var list = listOf(
        Item("Danh Sách Từ"),
        Item("Học"),
        Item("Kiểm Tra"),
        Item("Ôn Tập"),
        Item("Thêm Từ"),
        Item("Xóa Nhiều Từ"),
        Item("Trang Chủ")
    )
}