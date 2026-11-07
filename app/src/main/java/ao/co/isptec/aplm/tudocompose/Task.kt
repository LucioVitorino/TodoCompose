package ao.co.isptec.aplm.tudocompose

import java.util.UUID

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val descricao: String,
    val concluida: Boolean = false
)