package ao.co.isptec.aplm.tudocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import ao.co.isptec.aplm.tudocompose.ui.theme.TudoComposeTheme
import java.util.UUID


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TudoComposeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    TodoScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TodoScreen(modifier: Modifier = Modifier) {

    var textoTarefa by remember {
        mutableStateOf("")
    }

    var mensagemErro by remember {
        mutableStateOf("")
    }

    val tarefas = remember {
        mutableStateListOf<Task>()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Minhas tarefas",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Organiza as tuas actividades",
            style = MaterialTheme.typography.bodyMedium
        )

        OutlinedTextField(
            value = textoTarefa,
            onValueChange = {
                textoTarefa = it
                mensagemErro = ""
            },
            label = {
                Text("Nova tarefa")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        if (mensagemErro.isNotEmpty()) {
            Text(
                text = mensagemErro,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = {

                val descricao = textoTarefa.trim()

                when {
                    descricao.isEmpty() -> {
                        mensagemErro = "Introduz uma tarefa."
                    }

                    tarefas.any {
                        it.descricao.equals(
                            descricao,
                            ignoreCase = true
                        )
                    } -> {
                        mensagemErro = "Esta tarefa já existe."
                    }

                    else -> {
                        tarefas.add(
                            Task(descricao = descricao)
                        )

                        textoTarefa = ""
                        mensagemErro = ""
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Adicionar tarefa")
        }

        Text(
            text = "As minhas tarefas (${tarefas.size})",
            style = MaterialTheme.typography.titleMedium
        )

        if (tarefas.isEmpty()) {

            Text(
                text = "Ainda não existem tarefas.",
                style = MaterialTheme.typography.bodyMedium
            )

        } else {

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(
                    items = tarefas,
                    key = { it.id }
                ) { tarefa ->

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Checkbox(
                                checked = tarefa.concluida,
                                onCheckedChange = { marcada ->

                                    val indice = tarefas.indexOfFirst {
                                        it.id == tarefa.id
                                    }

                                    if (indice != -1) {
                                        tarefas[indice] =
                                            tarefas[indice].copy(
                                                concluida = marcada
                                            )
                                    }
                                }
                            )

                            Text(
                                text = tarefa.descricao,
                                modifier = Modifier.weight(1f),
                                textDecoration =
                                    if (tarefa.concluida) {
                                        TextDecoration.LineThrough
                                    } else {
                                        TextDecoration.None
                                    }
                            )

                            TextButton(
                                onClick = {
                                    tarefas.removeAll {
                                        it.id == tarefa.id
                                    }
                                }
                            ) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }
}