package com.wzl.reversalchallenge.utils.common

import java.io.File

/**
 * Created by 24694
 * on 2020/2/9 15:00
 */
class FileUtil {

    companion object {

        // 删除文件
        public fun deleteFile(filePath: String): Boolean {
            val file = File(filePath)
            return file.delete()
        }

        // 删除文件夹
        public fun deleteFolder(folder: File?): Boolean {

            if (folder != null && folder.isDirectory) {
                val files: Array<String>? = folder.list()
                for (file in files!!) {
                    if (!deleteFolder(File(folder, file))) {
                        return false
                    }
                }
                return folder.delete()
            }
            if (folder != null) {
                return folder.delete()
            } else {
                return false
            }
        }

        // 格式化单位(B)
        public fun formatSize(size: Long): String {

            val b = size
            val kb = b / 1024
            if (kb < 1) {
                return "$b B"
            }
            val mb = kb / 1024
            if (mb < 1) {
                return "$kb KB"
            }
            val gb = mb / 1024
            if (gb < 1) {
                return "$mb MB"
            }
            val tb = gb / 1024
            if (tb < 1) {
                return "$gb GB"
            } else {
                return "0"
            }
        }

        // 获取文件夹大小
        public fun getFolderSize(folder: File?): Long {

            var size: Long = 0
            val files: Array<File>? = folder!!.listFiles()
            for (file in files!!) {
                size += if (file.isDirectory) {
                    getFolderSize(file)
                } else {
                    file.length()
                }
            }
            return size
        }
    }
}