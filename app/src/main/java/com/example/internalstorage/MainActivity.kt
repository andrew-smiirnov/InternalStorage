package com.example.internalstorage

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.internalstorage.ui.theme.InternalStorageTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import androidx.compose.material3.Text

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InternalStorageTheme {
                val textState = remember {
                    mutableStateOf("")
                }
                val context = LocalContext.current
                val scope = rememberCoroutineScope()
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = textState.value)
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(onClick = {
                        scope.launch {
                            textState.value = readFile(context)
                        }
                    }) {
                        Text(text = "Read")
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(onClick = {
                        scope.launch {
                            save(context)
                        }
                    }) {
                        Text(text = "Save")
                    }
                }

            }
        }
    }
}

private suspend fun save(context: Context) {
    val text = "В небе свет предвечерних огней.\n" +
            "Чувства снова, как прежде, огнисты.\n" +
            "Небеса всё синей и синей,\n" +
            "Облачка, как барашки, волнисты."

    withContext(Dispatchers.IO) {
        context.openFileOutput("test.txt", Context.MODE_PRIVATE).use {
            it.write(text.toByteArray())
        }
    }
}

private suspend fun readFile(context: Context) = withContext(Dispatchers.IO) {
    try {
        context.openFileInput("test.txt").bufferedReader().useLines { lines ->
            lines.fold("") { acc, s ->
                "$acc\n$s"
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
        "Error: File not found!"
    }
}