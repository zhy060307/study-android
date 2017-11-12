package alvin.ui.list.list.domain.models

enum class FileType {
    DIRECTORY, FILE
}

data class FileItem
constructor(
        val id: Long,
        val type: FileType,
        val name: String)