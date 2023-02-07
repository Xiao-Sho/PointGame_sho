package com.android.example.pointgame

import android.content.Context
import java.io.File

//ファイルの読み込み・書き出し処理をまとめたクラスです
class FileRW {

    //ファイルを読み込む関数
    //--------------------------------
    //第1引数:コンテキスト(context)
    //第2引数:ファイル名(string)※拡張子要
    //戻り値:ファイル内文章(string)※ファイルが存在しない場合は""(空文字)返却
    public fun FileRead(context:Context?,filename:String):String{
        try{
            var data:String=context?.filesDir.toString()+"\\"+filename
            data = File(context?.filesDir.toString()+"/"+filename).readText()
            return data
        }
        catch(e:Exception){
            return ""
        }

    }

    //サンプル
    //example.txtというテキストファイルを読み込みたいときの処理
    //var data=FileRW().FileRead(context,"example.txt")
    //↑これでdataという変数にテキストの中身を格納できる





    //ファイルへ書き込む関数
    //--------------------------------
    //第1引数:コンテキスト(context)
    //第2引数:ファイル名(string)※拡張子要
    //第3引数:書き込む文字列(string)
    //戻り値:成功(true)/失敗(false)(boolean)※ファイルが存在しない場合も失敗
    public fun FileWrite(context: Context?, filename:String, data:String):Boolean{
        return try{
            val file=File(context?.filesDir,filename)

            if(!file.exists())throw Exception()

            file.writer().use{
                it.write(data)
            }
            true
        } catch(e:Exception){
            false
        }
    }

    //サンプル
    //example.txtというファイルに"こんにちは"という文字を書き込みたい時
    //FileRW().FileWrite(context,"example.txt","こんにちは")



    //ファイルを作成する関数
    //--------------------------------
    //第1引数:コンテキスト(context)
    //第2引数:ファイル名(string)※拡張子要
    //第3引数:書き込む文字列(string)※省略可
    //戻り値:成功(true)/失敗(false)(boolean)
    public fun FileCreate(context:Context?,filename:String):Boolean{
        return FileWrite(context,filename,"")
    }
    public fun FileCreate(context:Context?,filename:String,data:String):Boolean{
        return FileWrite(context,filename,"")
    }

    //サンプル
    //example.txtというファイルを作成したいとき
    //FileRW().FileCreate(context,"example.txt")
    //example.txtというファイルを作成し"こんにちは"という文字を格納したいとき
    //FileRW().FileCreate(context,"example.txt","こんにちは")





    //ファイルが存在するか確認する関数
    //--------------------------------
    //第1引数:コンテキスト(context)
    //第2引数:ファイル名(string)※拡張子要
    //戻り値:存在する(true)/存在しない(false)(boolean)
    public fun FileExists(context:Context?,filename:String):Boolean{
        return File(context?.filesDir,filename).exists()
    }
}